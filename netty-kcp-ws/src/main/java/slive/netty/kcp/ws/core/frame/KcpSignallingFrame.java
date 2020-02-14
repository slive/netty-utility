package slive.netty.kcp.ws.core.frame;

import slive.netty.kcp.ws.core.exception.KcpRuntimeException;

/**
 * 描述：信令文本帧：0x02，指示payload内容为文本格式-发送信令用 ，统一utf-8编解码
 *
 * @author slive
 * @date 2019年10月18日
 *
 */
public class KcpSignallingFrame extends KcpTextFrame {

    public KcpSignallingFrame(byte[] kcpData) {
        super(kcpData);
        if (this.opcode != OPCODE_TEXT_SIGNALLING) {
            throw new KcpRuntimeException("init wrong Signalling opcode.");
        }
    }

    public KcpSignallingFrame() {
        super(OPCODE_TEXT_SIGNALLING);
    }

    @Override
    protected void setOpcode(short opcode) {
        if (opcode != OPCODE_TEXT_SIGNALLING) {
            throw new KcpRuntimeException("wrong Signalling opcode.");
        }
        super.setOpcode(opcode);
    }

}
