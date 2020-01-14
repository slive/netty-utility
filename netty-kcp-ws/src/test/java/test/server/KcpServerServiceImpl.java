package test.server;

import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;

import io.jpower.kcp.netty.UkcpChannel;
import slive.netty.kcp.ws.core.channel.BaseKcpChannelService;
import slive.netty.kcp.ws.core.frame.KcpCloseFrame;
import slive.netty.kcp.ws.core.frame.KcpSessionFrame;
import slive.netty.kcp.ws.core.frame.KcpSignallingFrame;
import slive.netty.kcp.ws.core.server.BaseKcpServerService;

/**
 * 描述：
 *
 * @author slive
 * @date 2019年10月22日
 *
 */
public class KcpServerServiceImpl extends BaseKcpServerService {

    private Map<String, BaseKcpChannelService> channelServiceLocalCache = new HashMap<String, BaseKcpChannelService>();

    private String channelHeadId;

    public KcpServerServiceImpl() {
        channelHeadId = this.hashCode() + "" + System.currentTimeMillis();
    }

    private String getChannelId(UkcpChannel kcpCh) {
        // 添加channel头，避免在缓存中出现重复
        return channelHeadId + "#" + kcpCh.id();
    }

    @Override
    protected void onException(UkcpChannel kcpCh, Throwable cause) {
        System.err.println("kcp handler exception:" + cause);
    }

    @Override
    protected void onOpened(UkcpChannel kcpCh) {
        String channelServiceId = getChannelId(kcpCh);
        System.out.println("onOpened, channelServiceId:" + channelServiceId);
        BaseKcpChannelService channelService = channelServiceLocalCache.get(channelServiceId);
        if (channelService != null) {
            channelService.close();
            // 更新channelService
            channelService = new BaseKcpChannelService(channelServiceId, kcpCh);
            channelServiceLocalCache.put(channelServiceId, channelService);
        }
    }

    @Override
    protected void onClosed(UkcpChannel kcpCh) {
        String channelServiceId = getChannelId(kcpCh);
        System.out.println("onClosed, channelServiceId:" + channelServiceId);
        BaseKcpChannelService channelService = channelServiceLocalCache.get(channelServiceId);
        if (channelService != null) {
            channelService.close();
        }
    }

    @Override
    protected void onSignalling(UkcpChannel kcpCh, KcpSignallingFrame parseFrame) {
        String channelServiceId = getChannelId(kcpCh);
        String payloadStr = parseFrame.getPayloadStr();
        System.out.println("onSignalling, channelServiceId:" + channelServiceId + ", payload:" + payloadStr);
        try {
            Request req = JSON.parseObject(payloadStr, Request.class);
            if (req != null) {
                BaseKcpChannelService channelService = channelServiceLocalCache.get(channelServiceId);
                if (channelService != null) {
                    Response resp = Response.copyRequest(req);
                    channelService.writeAndFlush(resp.toString());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            BaseKcpChannelService channelService = channelServiceLocalCache.get(channelServiceId);
            if (channelService != null) {
                channelService.writeAndFlush(payloadStr);
            }
        }

    }

    @SuppressWarnings("unchecked")
    @Override
    protected void onOpenSession(UkcpChannel kcpCh, KcpSessionFrame parseFrame) {
        String channelServiceId = getChannelId(kcpCh);
        Map<String, Object> attrMap = JSON.parseObject(parseFrame.getPayloadStr(), Map.class);
        List<String> sourceIps = new ArrayList<String>();
        SocketAddress remoteAddress = kcpCh.remoteAddress();
        sourceIps.add(remoteAddress.toString());
        String payloadStr = parseFrame.getPayloadStr();
        System.out.println("onOpenSession, channelServiceId:" + channelServiceId + ", payload:" + payloadStr);
        BaseKcpChannelService channelService = channelServiceLocalCache.get(channelServiceId);
        if (channelService != null) {
            Response resp = new Response();
            resp.setCommand("OPENWS");
            resp.setData(attrMap);
            channelService.writeAndFlush(resp.toString());
        }
    }

    @Override
    protected void onCloseSession(UkcpChannel kcpCh, KcpCloseFrame parseFrame) {
        String channelServiceId = getChannelId(kcpCh);
        System.out.println("onCloseSession, channelServiceId:" + channelServiceId);
    }

}
