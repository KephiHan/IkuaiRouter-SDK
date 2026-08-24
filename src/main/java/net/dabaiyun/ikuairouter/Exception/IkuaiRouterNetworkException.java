package net.dabaiyun.ikuairouter.Exception;

/**
 * 网络通信异常
 * 当 HTTP 连接失败、响应体为空、HTTP 状态码非 2xx 等网络层面问题时抛出
 */
public class IkuaiRouterNetworkException extends IkuaiRouterException {

    public IkuaiRouterNetworkException() {
    }

    public IkuaiRouterNetworkException(String message) {
        super(message);
    }

    public IkuaiRouterNetworkException(String message, Throwable cause) {
        super(message, cause);
    }

    public IkuaiRouterNetworkException(Throwable cause) {
        super(cause);
    }
}
