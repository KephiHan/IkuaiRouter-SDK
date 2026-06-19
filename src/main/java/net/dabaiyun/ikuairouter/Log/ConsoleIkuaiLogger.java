package net.dabaiyun.ikuairouter.Log;

/**
 * SDK 默认日志实现（零外部依赖，stdout/stderr 输出）
 * 生产环境建议通过 IkuaiRouter.setLogger() 注入 SLF4J 桥接实现
 */
public class ConsoleIkuaiLogger implements IkuaiLogger {

    @Override
    public void debug(String msg) {
        System.out.println("[IkuaiSDK-DEBUG] " + msg);
    }

    @Override
    public void info(String msg) {
        System.out.println("[IkuaiSDK-INFO] " + msg);
    }

    @Override
    public void warn(String msg) {
        System.out.println("[IkuaiSDK-WARN] " + msg);
    }

    @Override
    public void error(String msg) {
        System.err.println("[IkuaiSDK-ERROR] " + msg);
    }

    @Override
    public void error(String msg, Throwable t) {
        System.err.println("[IkuaiSDK-ERROR] " + msg);
        if (t != null) {
            t.printStackTrace(System.err);
        }
    }
}
