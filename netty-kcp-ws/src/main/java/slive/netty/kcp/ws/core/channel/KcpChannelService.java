package slive.netty.kcp.ws.core.channel;

import slive.netty.kcp.ws.core.frame.KcpFrame;

public interface KcpChannelService {

    String getChannelId();

    void close();

    /**
     * 写信令信息
     */
    void writeAndFlush(String msg);

    void writeFrame(KcpFrame pf);

}
