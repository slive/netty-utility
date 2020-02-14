package slive.netty.kcp.ws.core.client;

import slive.netty.kcp.ws.core.KcpService;
import slive.netty.kcp.ws.core.channel.KcpChannelService;

/**
 * 描述：
 *
 * @author slive
 * @date 2019年10月22日
 *
 */
public interface KcpClientService extends KcpService, KcpChannelService {

    void sendPing();
}
