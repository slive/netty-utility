package slive.netty.kcp.ws.core.frame;

import slive.netty.kcp.ws.core.exception.KcpRuntimeException;

/**
 * 描述：会话文本帧：0x01，指示payload内容为文本格式-建立会话用，统一utf-8编解码
 *
 * @author slive
 * @date 2019年10月18日
 *
 */
public class KcpSessionFrame extends KcpTextFrame {

    public KcpSessionFrame(byte[] kcpData) {
        super(kcpData);
        if (this.opcode != OPCODE_TEXT_SESSION) {
            throw new KcpRuntimeException("init wrong Session opcode.");
        }
    }

    public KcpSessionFrame() {
        super(OPCODE_TEXT_SESSION);
    }

    @Override
    protected void setOpcode(short opcode) {
        if (opcode != OPCODE_TEXT_SESSION) {
            throw new KcpRuntimeException("wrong Session opcode.");
        }
        super.setOpcode(opcode);
    }

}
