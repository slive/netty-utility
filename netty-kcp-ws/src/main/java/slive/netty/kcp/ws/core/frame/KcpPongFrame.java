package slive.netty.kcp.ws.core.frame;

import slive.netty.kcp.ws.core.exception.KcpRuntimeException;

/**
 * 描述：Ping帧：0x09 发送ping等待对端回复pong，一般是客户端发起
 *
 * @author slive
 * @date 2019年10月18日
 *
 */
public class KcpPongFrame extends KcpFrame {

    public KcpPongFrame(byte[] kcpData) {
        super(kcpData);
        if (this.opcode != OPCODE_PONG) {
            throw new KcpRuntimeException("init wrong Pong opcode.");
        }
    }

    public KcpPongFrame() {
        super(OPCODE_PONG);
    }

    @Override
    protected void setOpcode(short opcode) {
        if (opcode != OPCODE_PONG) {
            throw new KcpRuntimeException("wrong Pong opcode.");
        }
        super.setOpcode(opcode);
    }

}
