package slive.netty.kcp.ws.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import slive.netty.kcp.ws.core.frame.KcpCloseFrame;
import slive.netty.kcp.ws.core.frame.KcpFrame;
import slive.netty.kcp.ws.core.frame.KcpFrameFactory;
import slive.netty.kcp.ws.core.frame.KcpPingFrame;
import slive.netty.kcp.ws.core.frame.KcpPongFrame;
import slive.netty.kcp.ws.core.frame.KcpSessionFrame;
import slive.netty.kcp.ws.core.frame.KcpSignallingFrame;

import io.jpower.kcp.netty.UkcpChannel;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;

/**
 * 描述：
 *
 * @author slive
 * @date 2019年10月21日
 *
 */
public abstract class BaseKcpService implements KcpService {

    private static Logger logger = LoggerFactory.getLogger(BaseKcpService.class);

    protected Channel channel;

    @Override
    public void stop() {
        if (channel != null) {
            try {
                channel.closeFuture().sync();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public class InnerHandler extends ChannelInboundHandlerAdapter {
        @Override
        public void channelActive(ChannelHandlerContext ctx)
            throws Exception {
            UkcpChannel kcpCh = (UkcpChannel)ctx.channel();
            logger.info("active conv: {}, channelId: {}", kcpCh.conv(), kcpCh.id());
        }

        @Override
        public void channelInactive(ChannelHandlerContext ctx)
            throws Exception {
            UkcpChannel kcpCh = (UkcpChannel)ctx.channel();
            logger.info("inactive conv: {}, channelId: {}", kcpCh.conv(), kcpCh.id());
            onClosed(kcpCh);
            super.channelInactive(ctx);
        }

        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) {
            UkcpChannel kcpCh = (UkcpChannel)ctx.channel();
            logger.info("rev conv: {}, channelId: {}", kcpCh.conv(), kcpCh.id());
            ByteBuf byteBuf = (ByteBuf)msg;
            try {
                int readLen = byteBuf.readableBytes();
                byte[] kcpData = new byte[readLen];
                byteBuf.readBytes(kcpData);
                KcpFrame frame = KcpFrameFactory.parseFrame(kcpData);
                logger.info("revFrame:" + frame);
                if (frame != null) {
                    // TODO fork和多线程?
                    if (frame instanceof KcpSignallingFrame) {
                        onSignalling(kcpCh, (KcpSignallingFrame)frame);
                    } else if (frame instanceof KcpPingFrame) {
                        onPing(kcpCh, (KcpPingFrame)frame);
                    } else if (frame instanceof KcpSessionFrame) {
                        onOpenSession(kcpCh, (KcpSessionFrame)frame);
                    } else if (frame instanceof KcpCloseFrame) {
                        onCloseSession(kcpCh, (KcpCloseFrame)frame);
                    } else if (frame instanceof KcpPongFrame) {
                        onPong(kcpCh, (KcpPongFrame)frame);
                    } else {
                        onOtherMessage(kcpCh);
                    }
                }
            } catch (Exception e) {
                logger.error("receive kcp error.", e);
            } finally {
                ReferenceCountUtil.release(byteBuf);
            }
        }

        @Override
        public void channelReadComplete(ChannelHandlerContext ctx) {
            ctx.flush();
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
            logger.info("server error:" + cause);
            UkcpChannel kcpCh = (UkcpChannel)ctx.channel();
            onException(kcpCh, cause);
            ctx.close();
        }
    }

    /**
     * 处理信令消息
     * 
     * @param frame 信令帧
     */
    protected abstract void onSignalling(UkcpChannel kcpCh, KcpSignallingFrame frame);

    /**
     * 处理业务开启会话
     * 
     * @param frame 会话帧
     */
    protected void onOpenSession(UkcpChannel kcpCh, KcpSessionFrame frame) {
        // TODO
        if (logger.isDebugEnabled()) {
            logger.debug("rev will open session.");
        }
    }

    /**
     * 处理业务关闭会话
     * 
     * @param ctx
     * @param frame
     */
    protected void onCloseSession(UkcpChannel kcpCh, KcpCloseFrame frame) {
        // TODO
        if (logger.isDebugEnabled()) {
            logger.debug("rev will close.");
        }
    }

    /**
     * 处理ping，默认回复pong
     */
    protected void onPing(UkcpChannel kcpCh, KcpPingFrame frame) {
        // 回复pong
        KcpPongFrame pf = new KcpPongFrame();
        writeFrame(pf);
    }

    /**
     * 处理pong，默认不处理
     */
    protected void onPong(UkcpChannel kcpCh, KcpPongFrame frame) {
        // TODO
        if (logger.isDebugEnabled()) {
            logger.debug("rev ping.");
        }
    }

    /**
     * 其他消息
     */
    protected void onOtherMessage(UkcpChannel kcpCh) {
        // TODO
        if (logger.isDebugEnabled()) {
            logger.debug("rev other message.");
        }
    }

    private void writeFrame(KcpFrame pf) {
        byte[] kcpData = pf.getKcpData();
        ByteBuf byteBuf = channel.alloc().buffer(kcpData.length);
        byteBuf.writeBytes(kcpData);
        channel.writeAndFlush(byteBuf);
        logger.info("write frame:" + pf);
    }

    /**
     * 异常处理
     * 
     * @param ctx
     * @param cause
     */
    protected abstract void onException(UkcpChannel kcpCh, Throwable cause);

    /**
     * 激活连接
     * 
     * @param ctx
     */
    protected abstract void onOpened(UkcpChannel kcpCh);

    /**
     * 被关闭后的通知
     */
    protected void onClosed(UkcpChannel kcpCh) {
        {
            // TODO
            if (logger.isDebugEnabled()) {
                logger.debug("rev had closed.");
            }
        }
    }

}
