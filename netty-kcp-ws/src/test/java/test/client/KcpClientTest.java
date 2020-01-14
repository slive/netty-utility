package test.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import slive.netty.kcp.ws.core.client.KcpClientService;
import slive.netty.kcp.ws.core.conf.KcpConf;
import slive.netty.kcp.ws.core.frame.KcpFrame;
import slive.netty.kcp.ws.core.frame.KcpFrameFactory;

/**
 * 描述：
 *
 * @author slive
 * @date 2019年10月22日
 *
 */
public class KcpClientTest {

    /**
     * @param args
     * @throws IOException
     * @throws InterruptedException
     */
    public static void main(String[] args)
        throws IOException, InterruptedException {
        // TODO Auto-generated method stub
        final KcpClientService kcs = new KcpClientServiceImpl();
        KcpConf conf = new KcpConf();
        conf.setHost("101.89.93.106");
        conf.setPort(9080);

        //conf.setHost("121.12.84.140");
        //conf.setPort(9080);
        //
        //conf.setHost("pfmc-ctl-mo-stg1.pingan.com.cn");
        //conf.setPort(9081);

        //conf.setHost("202.69.25.144");
        //conf.setHost("121.12.84.140");
        conf.setHost("101.89.103.12");
        conf.setPort(9080);
        //conf.setHost("pfmc-ctl-stg2.pingan.com.cn");
        //conf.setPort(9182);
        kcs.start(conf);

        Thread.sleep(2000);
        // 登录会话
        Map<String, Object> msg = new HashMap<>();
        msg.put("u", "354833333333");
        msg.put("sequence", System.currentTimeMillis() + "");
        msg.put("token", "nXZl1JvmJ+iYaoXSOMPOqo4EyhWX2gQnAvlDzZw5bS9DUvdTrTAPVFeSLlpsGrrm");
        msg.put("appId", "153137");
        KcpFrame pf = KcpFrameFactory.createSessionFrame(msg);
        kcs.writeFrame(pf);

        ScheduledExecutorService threadPool = Executors.newScheduledThreadPool(1);
        threadPool.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                kcs.sendPing();
            }
        },3,5, TimeUnit.SECONDS);

        // 收发信令
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        String readLine = null;
        System.out.println("please input:");
        long currentTimeMillis = System.currentTimeMillis();
        while ((readLine = in.readLine()) != null) {
            System.out.println(readLine);
            kcs.writeAndFlush(readLine);
        }
    }

}
