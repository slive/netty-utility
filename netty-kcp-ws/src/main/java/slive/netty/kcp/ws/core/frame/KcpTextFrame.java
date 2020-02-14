package slive.netty.kcp.ws.core.frame;

import java.nio.charset.Charset;

import slive.netty.kcp.ws.core.exception.KcpRuntimeException;

/**
 * 描述：文本帧：统一utf-8编解码
 *
 * @author slive
 * @date 2019年10月17日
 *
 */
public class KcpTextFrame extends KcpFrame {

    public static final Charset UTF_8 = Charset.forName("UTF-8");

    private String payloadStr;

    public KcpTextFrame(byte[] kcpData) {
        super(kcpData);
    }

    public KcpTextFrame() {
        super();
    }

    public KcpTextFrame(short opcode) {
        super(opcode);
    }

    @Override
    protected void init(byte[] kcpData) {
        super.init(kcpData);
        if (payloadLen > 0) {
            payloadStr = new String(payload, UTF_8);
        }
    }

    public String getPayloadStr() {
        return payloadStr;
    }

    public void setPayloadStr(String payloadStr) {
        if (read) {
            throw new KcpRuntimeException("unsupported setPayloadStr.");
        }
        this.payloadStr = payloadStr;
        if (payloadStr != null && !"".equals(payloadStr)) {
            setPayload(payloadStr.getBytes(UTF_8));
        }
    }
}
