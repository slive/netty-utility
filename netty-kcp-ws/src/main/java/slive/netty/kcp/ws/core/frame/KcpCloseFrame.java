package slive.netty.kcp.ws.core.frame;

import slive.netty.kcp.ws.core.exception.KcpRuntimeException;

/**
 * 描述：主动关闭帧：0x08，正常关闭
 *
 * @author slive
 * @date 2019年10月18日
 *
 */
public class KcpCloseFrame extends KcpFrame {

    public KcpCloseFrame(byte[] kcpData) {
        super(kcpData);
        if (this.opcode != OPCODE_CLOSE) {
            throw new KcpRuntimeException("init wrong Close opcode.");
        }
    }

    public KcpCloseFrame() {
        super(OPCODE_CLOSE);
    }

    @Override
    protected void setOpcode(short opcode) {
        if (opcode != OPCODE_CLOSE) {
            throw new KcpRuntimeException("wrong Close opcode.");
        }
        super.setOpcode(opcode);
    }

}
