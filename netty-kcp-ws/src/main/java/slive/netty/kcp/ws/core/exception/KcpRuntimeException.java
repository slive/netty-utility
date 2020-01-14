package slive.netty.kcp.ws.core.exception;

/**
 * 描述：
 *
 * @author slive
 * @date 2019年10月18日
 *
 */
public class KcpRuntimeException extends RuntimeException {

    private static final long serialVersionUID = -4503997774828055300L;

    public KcpRuntimeException() {
        super();
    }

    public KcpRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public KcpRuntimeException(String message) {
        super(message);
    }

    public KcpRuntimeException(Throwable cause) {
        super(cause);
    }

}
