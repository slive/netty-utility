package slive.netty.kcp.ws.core.client;

import com.alibaba.fastjson.JSON;
import slive.netty.kcp.ws.core.BaseKcpService;
import slive.netty.kcp.ws.core.channel.BaseKcpChannelService;
import slive.netty.kcp.ws.core.channel.KcpChannelService;
import slive.netty.kcp.ws.core.conf.KcpConf;
import slive.netty.kcp.ws.core.exception.KcpRuntimeException;
import slive.netty.kcp.ws.core.frame.KcpFrame;
import slive.netty.kcp.ws.core.frame.KcpPingFrame;
import io.jpower.kcp.netty.ChannelOptionHelper;
import io.jpower.kcp.netty.UkcpChannel;
import io.jpower.kcp.netty.UkcpChannelOption;
import io.jpower.kcp.netty.UkcpClientChannel;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 描述：
 *
 * @author slive
 * @date 2019年10月22日
 */
public abstract class BaseKcpClientService extends BaseKcpService implements KcpClientService {

    private static Logger logger = LoggerFactory.getLogger(BaseKcpClientService.class);

    private KcpChannelService kcpChannelService;

    @Override
    public void start(KcpConf conf)
            throws KcpRuntimeException {
        long currentTimeMillis = System.currentTimeMillis();
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(group).channel(UkcpClientChannel.class).handler(new ChannelInitializer<UkcpChannel>() {
                @Override
                public void initChannel(UkcpChannel ch)
                        throws Exception {
                    ChannelPipeline p = ch.pipeline();
                    p.addLast(new InnerHandler());
                }
            });
            ChannelOptionHelper.nodelay(b, conf.isNodelay(), conf.getInterval(), conf.getFastResend(), conf.isNocwnd())
                    .option(UkcpChannelOption.UKCP_MTU, conf.getMtu());

            // Start the client.
            ChannelFuture f = b.connect(conf.getHost(), conf.getPort()).sync();
            this.channel = f.channel();
            if (this.channel.isOpen()) {
                UkcpChannel ukcphannel = (UkcpChannel) this.channel;
                ukcphannel.conv((int) (System.currentTimeMillis() & 0x1fffffff));
                kcpChannelService = new BaseKcpChannelService((hashCode() + "#" + this.channel.id()),
                        ukcphannel);
            }
        } catch (Exception ex) {
            throw new KcpRuntimeException("start kcp client error.", ex);
        } finally {
            logger.info("start kcp client, spendTime:{}, open:{}",
                    (System.currentTimeMillis() - currentTimeMillis),
                    (channel != null ? channel.isOpen() : false));
        }
    }

    @Override
    public String getChannelId() {
        if (kcpChannelService != null) {
            kcpChannelService.getChannelId();
        }
        return null;
    }

    @Override
    public void close() {
        if (kcpChannelService != null) {
            kcpChannelService.close();
        }
    }

    @Override
    public void writeFrame(KcpFrame kf) {
        if (kcpChannelService != null) {
            kcpChannelService.writeFrame(kf);
        }
    }

    @Override
    public void writeAndFlush(String msg) {
        if (kcpChannelService != null) {
            kcpChannelService.writeAndFlush(msg);
        }
    }

    @Override
    public void sendPing() {
        writeFrame(new KcpPingFrame());
    }

}
