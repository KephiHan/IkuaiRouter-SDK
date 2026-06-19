# IkuaiRouter-SDK 变更适配指南

> 供上层业务平台（FreeMyKvm）AI 开发代理参考
> SDK 版本: 3.5.3T02 -> 3.5.4.dev.06
> 分支: refactor/stability-and-quality
> 日期: 2026-06-19

---

## 一、Breaking Changes（必须适配）

### 1.1 getCookieStore() 返回类型变更

```java
// 旧
public HashMap<String, List<Cookie>> getCookieStore()

// 新
public Map<String, List<Cookie>> getCookieStore()
```

**适配**: 上层代码如果用 HashMap 类型接收，改为 Map 即可。

---

### 1.2 Online_user -> OnlineUser 类重命名

```java
// 旧
import net.dabaiyun.ikuairouter.Entity.sysstat.Online_user;
systemStatus.getOnline_user()
systemStatus.setOnline_user(...)

// 新
import net.dabaiyun.ikuairouter.Entity.sysstat.OnlineUser;
systemStatus.getOnlineUser()
systemStatus.setOnlineUser(...)
```

**适配**: 替换 import + getter/setter 调用名。JSON 序列化不受影响（内部有 @JsonProperty("online_user")）。

---

### 1.3 批量操作行为变更

downNetMappingByLanIp(String lanip) 和 delNetMappingByIpAddr(String ip_addr):

- 旧行为: 循环中某条失败立即抛异常，后续规则不执行
- 新行为: 所有规则都会尝试执行，全部完成后如有失败则汇总抛出

异常信息格式变更:
- 旧: "30001 端口映射不存在"
- 新: "Partial failure in downNetMappingByLanIp: id=5: 30001 端口映射不存在; id=8: ..."

**适配**: 如果上层解析异常 message 内容，需适配新格式。如果只是 catch 异常做重试/告警，无需改动。

---

## 二、新增异常类（可选适配，推荐）

SDK 内部 RouterAgent 层现在精确抛出子类异常。所有方法签名仍为 throws Exception，上层无编译错误，但推荐通过 instanceof 精确处理：

```
异常层次:
IkuaiRouterException            // 基类（业务逻辑错误）
+-- IkuaiRouterAuthException    // Result=10014，认证失败/会话过期
+-- IkuaiRouterNetworkException // HTTP 通信失败、响应体为空
+-- IkuaiRouterApiException     // API 返回非成功 Result（含 getResultCode()）
```

**推荐适配（IkuaiRouterFactory）**:

```java
try {
    // 使用 ikuaiRouter 调用
} catch (IkuaiRouterAuthException e) {
    // 会话过期 -> 清除 Redis sesskey -> 重新获取实例
    log.warn("Session expired for router {}, re-login", routerId);
    redisAgent.deleteRouterSessKey(routerId);
    // 重试逻辑...
} catch (IkuaiRouterNetworkException e) {
    // 网络问题 -> 可重试
    log.error("Network error: {}", e.getMessage());
} catch (IkuaiRouterApiException e) {
    // 业务错误 -> 记录 resultCode
    log.ror("API error [{}]: {}", e.getResultCode(), e.getMessage());
} catch (Exception e) {
    // 其他（如 JSON 解析异常）
    log.error("Unexpected: {}", e.getMessage());
}
```

---

## 三、新增 API（无需适配，按需使用）

### 3.1 destroy() — 密码清除

```java
// 主动清除内存中nfo(String msg)  { log.info(msg); }
    public void warn(String msg)  { log.warn(msg); }
    public void error(String msg) { log.error(msg); }
    public void error(String msg, Throwable t) { log.error(msg, t); }
});
```

使用场景: 在 IkuaiRouterFactory.getRouterInstance() 中创建实例后注入，SDK 内部日志将走主项目的 Log4j2。

IkuaiLogger 接口定义:
- void debug(String msg)
- void info(String msg)
- void warn(String msg)
- void error(String msg)
- void error(String msg, Throwable t)

---

### 3.3 findAndAddNetMapping() — 原子查找+添加

```java
pddNetMapping(
    String inter_face,  // 上行接口
    int portbegin,      // 起始端口
    int portend,        // 结束端口
    NetMapping template // 模板（wan_port 和 inter_face 将被自动设置）
) throws Exception
```

使用场景: 替代原来的 findAvailableNetMappingWanPort() + addNetMapping() 两步调用，避免并发下端口冲突。
注意: 仅保证单 JVM 实例内原子性，多 JVM 需上层分布式锁。

---

### 3.4 getAllLanHostInfoList() —lose，异常时可能泄漏 | try-with-resources 确保关闭 |
| response.body() null | NPE | 抛出 IkuaiRouterNetworkException |
| SSL 初始化失败 | printStackTrace + NPE | 抛出 RuntimeException 快速失败 |
| CookieStore | HashMap | ConcurrentHashMap（线程安全） |
| 密码存储 | String 字段 | char[] 字段 + destroy() 可清除 |
| 端口查找算法 | O(P*N) 遍历 | HashSet O(1) 查询 |
| IP 查找算法 | O(IP*列表) 线性扫描 | HashSet O(1) 查询 |
| JSON 解析 | 18 处裸调用无 null 检查 | parseData() 统一封装 + null 检查 |
| 端口方法并发 | 无保护 | synchronized(portLock) |

---

## 五、构造函数（未变更）

```java
// 自动登录
new IkuaiRouter(String address, int port, boolean https, String username, String pwd) throws Exception

// 可选登录
new IkuaiRouter(String address, int port, boolean https, String username, String pwd, boolean doLogin) throut 实现 |
| IkuaiRouterAuthException | net.dabaiyun.ikuairouter.Exception | 认证失败异常 |
| IkuaiRouterNetworkException | net.dabaiyun.ikuairouter.Exception | 网络通信异常 |
| IkuaiRouterApiException | net.dabaiyun.ikuairouter.Exception | API 业务异常（含 getResultCode()） |
| OnlineUser | net.dabaiyun.ikuairouter.Entity.sysstat | 替代 Online_user |

### 删除的类

| 类 | 说明 |
|----|------|
| Online_user | 被 OnlineUser 替代 |

---

## 七、新增公开方法汇总

| 方法签名 | 说明 |
|----------|------|
| void setLogger(IkuaiLogger logger) | 注入自定义日志实现 |
| IkuaiLogger getLogger() | 获取当前日志实现 |
| void destroy() | 清除密码，释放敏感数据 |
| Integer findAndAddNetMapping(String, int, int, NetMapping) | 原子查找端口+添加映射 |
| List<LanHostInfo> getAllLanHostInfoList(int pageSize) | 分页全量获取 LAN 主机 |

---

## 八、上层适配检查清单

- [ ] getCookieStore() 接收类型从 HashMap 改为 Map
- [ ] Online_user 引用改为 OnlineUser
- [ ] getOnline_user() / setOnline_user() 改为 getOnlineUser() / setOnlineUser()
- [ ] （推荐）IkuaiRouterFactory 中创建实例后调用 setLogger() 注入 SLF4J
- [ ] （推荐）异常处理增加 IkuaiRouterAuthException 分支用于精确 sesskey 失效处理
- [ ] （可选）批量删除/禁用端口映射的异常 message 解析逻辑适配
- [ ] （可选）端口分配场景改用 findAndAddNetMapping() 原子方法