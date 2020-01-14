package slive.netty.kcp.ws.core.channel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import slive.netty.kcp.ws.core.frame.KcpCloseFrame;
import slive.netty.kcp.ws.core.frame.KcpFrame;
import slive.netty.kcp.ws.core.frame.KcpSignallingFrame;

import io.jpower.kcp.netty.UkcpChannel;
import io.netty.buffer.ByteBuf;

/**
 * 描述：
 *
 * @author slive
 * @date 2019年10月22日
 *
 */
public class BaseKcpChannelService implements KcpChannelService {

    private static final Logger logger = LoggerFactory.getLogger(BaseKcpChannelService.class);

    protected String channelId;

    protected UkcpChannel channel;

    public BaseKcpChannelService(String channelId, UkcpChannel kcpCh) {
        this.channelId = channelId;
        this.channel = kcpCh;
    }

    @Override
    public String getChannelId() {
        return channelId;
    }

    @Override
    public void close() {
        if (channel != null && channel.isOpen()) {
            KcpCloseFrame cf = new KcpCloseFrame();
            writeFrame(cf);
            channel.close();
            logger.info("kcpChannel close, channelId:" + channelId);
        }
    }

    public void writeFrame(KcpFrame kf) {
        byte[] kcpData = kf.getKcpData();
        ByteBuf byteBuf = channel.alloc().buffer(kcpData.length);
        byteBuf.writeBytes(kcpData);
        channel.writeAndFlush(byteBuf);
        logger.info("kcpChannel write, content: " + kf);
    }

    @Override
    public void writeAndFlush(String msg) {
        if (msg != null && isOpen()) {
            KcpSignallingFrame kf = new KcpSignallingFrame();
            kf.setPayloadStr(msg);
            writeFrame(kf);
        }
    }

    protected boolean isOpen() {
        if (channel != null && channel.isOpen()) {
            return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((channelId == null) ? 0 : channelId.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        BaseKcpChannelService other = (BaseKcpChannelService)obj;
        if (channelId == null) {
            if (other.channelId != null)
                return false;
        } else if (!channelId.equals(other.channelId))
            return false;
        return true;
    }

}
