package slive.netty.kcp.ws.core.frame;

import com.alibaba.fastjson.JSON;

/**
 * 描述：
 *
 * @author slive
 * @date 2019年10月21日
 */
public class KcpFrameFactory {

    public static KcpFrame parseFrame(byte[] kcpData) {
        short opcode = KcpFrame.fetchOpcode(kcpData);
        if (opcode == KcpFrame.OPCODE_TEXT_SESSION) {
            return new KcpSessionFrame(kcpData);
        } else if (opcode == KcpFrame.OPCODE_TEXT_SIGNALLING) {
            return new KcpSignallingFrame(kcpData);
        } else if (opcode == KcpFrame.OPCODE_CLOSE) {
            return new KcpCloseFrame(kcpData);
        } else if (opcode == KcpFrame.OPCODE_PING) {
            return new KcpPingFrame(kcpData);
        } else if (opcode == KcpFrame.OPCODE_PONG) {
            return new KcpPongFrame(kcpData);
        }
        return null;
    }

    public static KcpFrame createSessionFrame(Object msg) {
        KcpSessionFrame kf = new KcpSessionFrame();
        if (msg != null) {
            if (msg instanceof String) {
                kf.setPayloadStr((String) msg);
            } else {
                kf.setPayloadStr(JSON.toJSONString(msg));
            }
        }
        return kf;
    }
}
