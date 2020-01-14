package slive.netty.kcp.ws.core.conf;

import com.alibaba.fastjson.JSON;

/**
 * 描述：
 *
 * @author slive
 * @date 2019年10月21日
 *
 */
public class KcpConf {

    public static final String KCP_DEFAULT_HOST = "localhost";

    public static final int KCP_DEFAULT_PORT = 9080;

    public static final int KCP_MTU = 1400;

    public static final int KCP_DEAD_LINK = 10;

    public static final int KCP_INTERVAL = 20;

    public static final boolean KCP_NODELAY = true;

    public static final boolean KCP_NOCWND = true;

    public static final int KCP_FAST_RESEND = 2;

    private String host = KCP_DEFAULT_HOST;

    private int port = KCP_DEFAULT_PORT;

    // 最大传输单元，默认数据为1400，最小为50
    private int mtu = KCP_MTU;

    // 最大重传次数，被认为连接中断
    private int deadLink = KCP_DEAD_LINK;

    // 触发快速重传的重复ACK个数；
    private int fastResend = KCP_FAST_RESEND;

    // 是否启动无延迟模式。无延迟模式rtomin将设置为0，拥塞控制不启动
    private boolean nodelay = KCP_NODELAY;

    // 是否取消拥塞控制
    private boolean nocwnd = KCP_NOCWND;

    // 内部flush刷新间隔，对系统循环效率有非常重要影响；
    private int interval = KCP_INTERVAL;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getMtu() {
        return mtu;
    }

    public void setMtu(int mtu) {
        this.mtu = mtu;
    }

    public int getDeadLink() {
        return deadLink;
    }

    public void setDeadLink(int deadLink) {
        this.deadLink = deadLink;
    }

    public int getFastResend() {
        return fastResend;
    }

    public void setFastResend(int fastResend) {
        this.fastResend = fastResend;
    }

    public boolean isNodelay() {
        return nodelay;
    }

    public void setNodelay(boolean nodelay) {
        this.nodelay = nodelay;
    }

    public boolean isNocwnd() {
        return nocwnd;
    }

    public void setNocwnd(boolean nocwnd) {
        this.nocwnd = nocwnd;
    }

    public int getInterval() {
        return interval;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
