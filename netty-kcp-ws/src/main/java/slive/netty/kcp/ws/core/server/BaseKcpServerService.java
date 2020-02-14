package slive.netty.kcp.ws.core.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import slive.netty.kcp.ws.core.BaseKcpService;
import slive.netty.kcp.ws.core.conf.KcpConf;
import slive.netty.kcp.ws.core.exception.KcpRuntimeException;

import io.jpower.kcp.netty.ChannelOptionHelper;
import io.jpower.kcp.netty.UkcpChannel;
import io.jpower.kcp.netty.UkcpChannelOption;
import io.jpower.kcp.netty.UkcpServerChannel;
import io.netty.bootstrap.UkcpServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;

/**
 * 描述：
 *
 * @author slive
 * @date 2019年10月21日
 *
 */
public abstract class BaseKcpServerService extends BaseKcpService implements KcpServerService {

    private static Logger log = LoggerFactory.getLogger(BaseKcpServerService.class);

    @Override
    public void start(KcpConf conf)
        throws KcpRuntimeException {
        long currentTimeMillis = System.currentTimeMillis();
        log.info("start kcp server, conf[{}].", conf);
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            UkcpServerBootstrap serverBootstrap = new UkcpServerBootstrap();
            serverBootstrap.group(group)
                .channel(UkcpServerChannel.class)
                .childHandler(new ChannelInitializer<UkcpChannel>() {
                    @Override
                    public void initChannel(UkcpChannel ch)
                        throws Exception {
                        ChannelPipeline p = ch.pipeline();
                        p.addLast(new InnerHandler());
                    }
                });
            ChannelOptionHelper
                .nodelay(serverBootstrap, conf.isNodelay(), conf.getInterval(), conf.getFastResend(), conf.isNocwnd())
                .childOption(UkcpChannelOption.UKCP_MTU, conf.getMtu())
                .childOption(UkcpChannelOption.UKCP_AUTO_SET_CONV, true)
                .childOption(UkcpChannelOption.UKCP_DEAD_LINK, conf.getDeadLink());

            // Start the server.
            ChannelFuture f = serverBootstrap.bind(conf.getPort()).sync();
            // Wait until the server socket is closed.
            channel = f.channel();
        } catch (Exception ex) {
            throw new KcpRuntimeException("start kcp server error.", ex);
        } finally {
            log.info("start kcp server, spendTime:{}, open:{}",
                (System.currentTimeMillis() - currentTimeMillis),
                (channel != null ? channel.isOpen() : false));
        }

    }

}
