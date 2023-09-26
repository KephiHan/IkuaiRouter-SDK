package net.dabaiyun.ikuairouter.Exception;

public class IkuaiRouterException extends Exception{

    public IkuaiRouterException() {
    }

    public IkuaiRouterException(String message) {
        super(message);
    }

    public IkuaiRouterException(String message, Throwable cause) {
        super(message, cause);
    }

    public IkuaiRouterException(Throwable cause) {
        super(cause);
    }

    public IkuaiRouterException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
