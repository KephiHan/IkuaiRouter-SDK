package net.dabaiyun.ikuairouter.Log;

/**
 * SDK 日志抽象接口
 * 上层业务可通过 IkuaiRouter.setLogger() 注入自定义实现（如 SLF4J 桥接）
 * SDK 默认使用 ConsoleIkuaiLogger（stdout 输出）
 */
public interface IkuaiLogger {

    void debug(String msg);

    void info(String msg);

    void warn(String msg);

    void error(String msg);

    void error(String msg, Throwable t);
}
