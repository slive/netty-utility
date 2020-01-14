package test.server;

import java.io.IOException;

import slive.netty.kcp.ws.core.conf.KcpConf;
import slive.netty.kcp.ws.core.server.KcpServerService;

/**
 * 描述：
 *
 * @author slive
 * @date 2019年10月22日
 *
 */
public class KcpServerTest {

    /**
     * @param args
     * @throws IOException
     * @throws InterruptedException
     */
    public static void main(String[] args)
        throws IOException, InterruptedException {
        // TODO Auto-generated method stub
        KcpServerService kcs = new KcpServerServiceImpl();
        KcpConf conf = new KcpConf();
        conf.setPort(5000);
        kcs.start(conf);
        while (true) {
            Thread.sleep(2000);
        }
    }

}
