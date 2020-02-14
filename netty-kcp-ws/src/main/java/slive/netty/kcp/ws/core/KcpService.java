package slive.netty.kcp.ws.core;

import slive.netty.kcp.ws.core.conf.KcpConf;
import slive.netty.kcp.ws.core.exception.KcpRuntimeException;

/**
 * 描述：
 *
 * @author slive
 * @date 2019年10月22日
 *
 */
public interface KcpService {

    void start(KcpConf conf)
        throws KcpRuntimeException;

    void stop();
}
