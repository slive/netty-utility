package slive.netty.kcp.ws.core.frame;

import slive.netty.kcp.ws.core.exception.KcpRuntimeException;

/**
 * 描述：Ping帧：0x09 发送ping等待对端回复pong，一般是客户端发起
 *
 * @author slive
 * @date 2019年10月18日
 *
 */
public class KcpPingFrame extends KcpFrame {

    public KcpPingFrame(byte[] kcpData) {
        super(kcpData);
        if (this.opcode != OPCODE_PING) {
            throw new KcpRuntimeException("init wrong Ping opcode.");
        }
    }

    public KcpPingFrame() {
        super(OPCODE_PING);
    }

    @Override
    protected void setOpcode(short opcode) {
        if (opcode != OPCODE_PING) {
            throw new KcpRuntimeException("wrong Ping opcode.");
        }
        super.setOpcode(opcode);
    }

}
