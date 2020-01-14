package test.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;

import io.jpower.kcp.netty.UkcpChannel;
import slive.netty.kcp.ws.core.client.BaseKcpClientService;
import slive.netty.kcp.ws.core.frame.KcpPongFrame;
import slive.netty.kcp.ws.core.frame.KcpSignallingFrame;
import test.server.Request;
import test.server.Response;

/**
 * 描述：
 *
 * @author slive
 * @date 2019年10月22日
 */
public class KcpClientServiceImpl extends BaseKcpClientService {

    private static Logger logger = LoggerFactory.getLogger(KcpClientServiceImpl.class);

    @Override
    protected void onException(UkcpChannel kcpCh, Throwable cause) {
        logger.error("onException:" + cause);
    }

    @Override
    protected void onOpened(UkcpChannel kcpCh) {
        logger.info("onOpened");
    }

    @Override
    protected void onClosed(UkcpChannel kcpCh) {
        logger.warn("onClosed");
    }

    @Override
    public void sendPing() {
        super.sendPing();
        if (openWs != null) {
            Request request = new Request();
            request.setCommand("HEARTBEAT");
            request.setSessionId(openWs.getSessionId());
            request.setSequence(System.currentTimeMillis() + "");
            writeAndFlush(request.toString());
        }
    }

    @Override
    protected void onSignalling(UkcpChannel kcpCh, KcpSignallingFrame frame) {
        String payloadStr = frame.getPayloadStr();
        if (payloadStr != null && payloadStr.contains("OPENWS")) {
            openWs = JSON.parseObject(payloadStr, Response.class);
            logger.info("OPENWS:" + openWs.getSessionId());
        }
    }

    @Override
    protected void onPong(UkcpChannel kcpCh, KcpPongFrame frame) {
        super.onPong(kcpCh, frame);
        logger.info("rev pong:" + frame);
    }

    private Response openWs;

}
