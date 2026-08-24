package net.dabaiyun.ikuairouter.Exception;

/**
 * 认证/会话相关异常
 * 当 iKuai API 返回 Result=10014（认证失败/会话过期）时抛出
 */
public class IkuaiRouterAuthException extends IkuaiRouterException {

    public IkuaiRouterAuthException() {
    }

    public IkuaiRouterAuthException(String message) {
        super(message);
    }

    public IkuaiRouterAuthException(String message, Throwable cause) {
        super(message, cause);
    }

    public IkuaiRouterAuthException(Throwable cause) {
        super(cause);
    }
}
