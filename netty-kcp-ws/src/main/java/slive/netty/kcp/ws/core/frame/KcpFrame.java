package slive.netty.kcp.ws.core.frame;

import java.util.Arrays;

import com.alibaba.fastjson.JSON;
import slive.netty.kcp.ws.core.exception.KcpRuntimeException;

/**
 * 描述：kcp基本帧
 *
 * @author slive
 * @date 2019年10月17日
 *
 */
public class KcpFrame {

    public static final byte VERSION_1 = 1;

    public static final short OPCODE_TEXT_SESSION = 0X01;

    public static final short OPCODE_TEXT_SIGNALLING = 0X02;

    public static final short OPCODE_CLOSE = 0x08;

    public static final short OPCODE_PING = 0x09;

    public static final short OPCODE_PONG = 0x0A;

    protected short version;

    protected short opcode;

    protected int payloadLen;

    protected byte[] payload;

    protected boolean read = false;

    private byte[] kcpData;

    public KcpFrame(byte[] kcpData) {
        init(kcpData);
    }

    public KcpFrame(short opcode) {
        setOpcode(opcode);
    }

    public KcpFrame() {

    }

    protected void init(byte[] kcpData) {
        this.kcpData = kcpData;
        this.read = true;
        this.version = (short)(((kcpData[0] << 8)) | (kcpData[1] & 0xff));
        this.opcode = fetchOpcode(kcpData);
        this.payloadLen = ((kcpData[4] << 24) | (kcpData[5] << 16) | (kcpData[6] << 8) | (kcpData[7] & 0xff));
        if (payloadLen > 0) {
            this.payload = Arrays.copyOfRange(kcpData, 8, (8 + payloadLen));
        }
    }

    public static short fetchOpcode(byte[] kcpData) {
        return (short)(((kcpData[2] << 8)) | (kcpData[3] & 0xff));
    }

    public short getVersion() {
        return version;
    }

    void setVersion(byte version) {
        if (read) {
            throw new KcpRuntimeException("unsupported setVersion.");
        }
        this.version = version;
    }

    public short getOpcode() {
        return opcode;
    }

    protected void setOpcode(short opcode) {
        if (read) {
            throw new KcpRuntimeException("unsupported setOpcode.");
        }

        this.opcode = opcode;
    }

    public int getPayloadLen() {
        return payloadLen;
    }

    public byte[] getPayload() {
        return payload;
    }

    public void setPayload(byte[] payload) {
        if (read) {
            throw new KcpRuntimeException("unsupported setPayload.");
        }

        this.payload = payload;
        if (payload != null) {
            this.payloadLen = payload.length;
        }
    }

    public byte[] getKcpData() {
        if (read) {
            return this.kcpData;
        }
        byte[] rKcpData = new byte[8 + payloadLen];
        rKcpData[0] = (byte)((this.version >> 8) & 0xff);
        rKcpData[1] = (byte)(this.version & 0xff);

        rKcpData[2] = (byte)((this.opcode >> 8) & 0xff);
        rKcpData[3] = (byte)(this.opcode & 0xff);

        if (this.payloadLen > 0) {
            rKcpData[4] = (byte)((this.payloadLen >> 24) & 0xff);
            rKcpData[5] = (byte)((this.payloadLen >> 16) & 0xff);
            rKcpData[6] = (byte)((this.payloadLen >> 8) & 0xff);
            rKcpData[7] = (byte)(this.payloadLen & 0xff);
            for (int index = 0; index < this.payloadLen; index++) {
                rKcpData[8 + index] = this.payload[index];
            }
        }
        return rKcpData;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
    
    public static void main(String[] args) {
        System.out.println((short)((0 << 8) | (1 & 0xff)));
        System.out.println((((0&0xff) << 8)));
    }
}
