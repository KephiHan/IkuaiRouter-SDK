package net.dabaiyun.ikuairouter.Exception;

/**
 * API 业务逻辑异常
 * 当 iKuai API 返回 Result 非成功码（非 30000/10000）且非认证失败时抛出
 * 包含 API 返回的 ErrMsg 信息
 */
public class IkuaiRouterApiException extends IkuaiRouterException {

    private int resultCode;

    public IkuaiRouterApiException() {
    }

    public IkuaiRouterApiException(String message) {
        super(message);
    }

    public IkuaiRouterApiException(String message, int resultCode) {
        super(message);
        this.resultCode = resultCode;
    }

    public IkuaiRouterApiException(String message, Throwable cause) {
        super(message, cause);
    }

    public IkuaiRouterApiException(Throwable cause) {
        super(cause);
    }

    public int getResultCode() {
        return resultCode;
    }
}
