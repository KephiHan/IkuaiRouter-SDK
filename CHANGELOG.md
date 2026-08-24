# Changelog

本文件记录 IkuaiRouter-SDK 的版本变更。

格式遵循 [Keep a Changelog](https://keepachangelog.com/zh-CN/1.1.0/)，
版本号遵循 [语义化版本](https://semver.org/lang/zh-CN/)。

> 本文件自 3.5.5 起开始维护，更早版本请查阅 git log。

---

## [3.5.5] - 2026-06-19

本版本包含两部分工作：一是对 SDK 进行全面的稳定性、性能与代码质量重构（原 3.5.4.dev.01 ~ dev.06 六个阶段）；二是新增 OpenVPN 与认证计费用户 API 支持。

### Added

- **认证计费用户管理（pppuser）**
  - 新增 `PPPUser` 实体类（30 字段），含 `EnabledType`、`PPPType` 常量
  - `IkuaiRouter` 新增 8 个方法：`getPPPUserList`、`getAllPPPUserList(pageSize)`、`getPPPUserById`、`getPPPUserByUsername`、`addPPPUser`、`editPPPUser`、`downPPPUserById`、`delPPPUserById`
- **认证在线用户查询（ppp_online）**
  - 新增 `PPPOnline` 实体类
  - `IkuaiRouter` 新增 `getPPPOnlineList()`
- **计费套餐管理（ppp_package）**
  - 新增 `PPPPackage` 实体类
  - `IkuaiRouter` 新增 4 个方法：`getPPPPackageList`、`addPPPPackage`、`editPPPPackage`、`delPPPPackageById`
- **OpenVPN 服务端（openvpn-server）**
  - 新增 `OpenVPNServer` 实体类（`status` 字段：0=关闭，1=开启）
  - `IkuaiRouter` 新增 `getOpenVPNServerConfig()`
- **日志抽象层**
  - 新增 `Log/IkuaiLogger` 接口与 `Log/ConsoleIkuaiLogger` 默认实现（零外部依赖）
  - `IkuaiRouter` 新增 `setLogger(IkuaiLogger)` / `getLogger()`，支持上层注入 SLF4J 桥接
- **异常分层体系**
  - 新增 `IkuaiRouterAuthException`（Result=10014 会话过期）
  - 新增 `IkuaiRouterNetworkException`（HTTP 通信失败、响应体为空）
  - 新增 `IkuaiRouterApiException`（API 业务失败，含 `getResultCode()`）
- **并发保护**
  - `IkuaiRouter` 新增 `portLock` 同步锁，保护端口查找与添加操作
  - 新增原子组合方法 `findAndAddNetMapping`（单接口）与 `findAndAddNetMappingMultiInterface`（多接口）
- **分页机制**
  - `RequestParamShow` 新增 `(type, limit)` 双参数构造函数，支持 `TYPE:"data,total"` 获取记录总数
  - `IkuaiRouter` 新增 `getAllLanHostInfoList(pageSize)` 自动翻页方法
- **安全增强**
  - `IkuaiRouter` / `RouterAgent` 新增 `destroy()` 方法，主动清除内存中的密码
- **枚举扩展**
  - `FuncName` 新增 `wan`、`pppuser`、`ppp_online`、`ppp_package`、`pppoe_server`、`openvpn_server`
  - `FuncName` 引入 `@JsonValue` 机制，解决 `openvpn-server` 含横杠无法作为 Java 枚举名的问题
  - `ActionType` 新增 `paylog_add`
- `NetMapping` 新增 `src_addr` 字段（源地址限制），此前该字段被静默丢弃，导致无法创建带源地址限制的端口映射规则

### Changed

- **HTTP 连接管理**：`OkHttpClient` 从每次请求新建改为每个 `RouterAgent` 实例持有一个，复用连接池与线程池
- **资源释放**：`login()` 与 `executeAction()` 改用 try-with-resources，确保 `Response` 在任何路径下都被关闭
- **线程安全**：`cookieStore` 从 `HashMap` 改为 `ConcurrentHashMap`
- **密码存储**：`RouterAgent.pwd` 从 `String` 改为 `char[]`
- **JSON 解析**：18 处重复的 `readTree → get → toString → readValue` 模式统一收敛为 `parseData()` 泛型方法，并内置 key 缺失检查
- **端口解析**：4 处重复的端口解析逻辑（约 30 行 × 4）抽取为 `parseUsedPorts()` 与 `parseWanPortInto()`
- **批量操作语义**：`downNetMappingByLanIp` 与 `delNetMappingByIpAddr` 由「遇错立即中断」改为「全部尝试执行后汇总错误」
- **命名规范**：`Online_user` 类重命名为 `OnlineUser`，`SystemStatus` 中对应 getter/setter 改为 `getOnlineUser` / `setOnlineUser`（通过 `@JsonProperty("online_user")` 保持 JSON 兼容）
- `pom.xml` 新增 `project.build.sourceEncoding=UTF-8`，解决 Windows 环境 Maven 默认 GBK 编码导致的中文注释编译失败

### Fixed

- 修复 `response.body()` 可能返回 null 导致的 NPE，改为显式判空并抛出 `IkuaiRouterNetworkException`
- 修复 SSL 上下文初始化失败时 `printStackTrace()` 吞掉异常、后续 NPE 无法定位根因的问题，改为抛出 `RuntimeException` 快速失败
- 启用此前被注释掉的认证失败检测逻辑（`Result == 10014`），现抛出 `IkuaiRouterAuthException`

### Performance

- **端口查找**：`isWanPortInUse`、`isWanPortInUseMultiInterface`、`findAvailableNetMappingWanPort`、`findAvailableNetMappingWanPortMultiInterface` 由 O(P×N) 双重遍历改为预建 `HashSet` + O(1) 查询
- **IP 查找**：`findAvailableIpAddr` 内层对 DHCPHost / DHCPStatic 列表的线性扫描改为预建 `HashSet<String>` + `contains()`。以 /24 子网、200 条记录为例，比较次数由约 50000 次降至 254 次

### Security

- 在 `login()` 的 MD5 处理代码处添加风险注释，说明 MD5 为 iKuai API 协议约束而非安全推荐
- `OpenVPNServer.toString()` 排除 `key` 字段（RSA 私钥），避免日志泄漏

### Notes

- **未实施项**：会话自动续期（`executeAction` 检测 10014 后自动 relogin）经评估不实施 —— 上层业务平台已通过 Factory + Redis 缓存 sesskey 形成闭环，SDK 内部自动 relogin 会导致新 sesskey 与 Redis 不一致
- **未实施项**：Entity 中 `inter_face` 字段重命名为 `interfaceName` 经评估暂缓 —— 涉及 8 个 Entity 共 76 处引用，且构成上层业务平台的 breaking change，现有 `@JsonProperty("interface")` 已保证序列化正确
- 上层业务平台适配说明见 `工作内容/SDK变更适配指南.md`