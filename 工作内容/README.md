# 工作内容索引

本目录按开发阶段分类存放需求文档、开发方案与开发日志。

---

## 01_代码质量重构

**版本**: 3.5.4.dev.01 ~ 3.5.4.dev.06（未单独发版，随 v3.5.5 一并发布）

对 SDK 进行全面的稳定性、性能与代码质量重构，共六个阶段。

| 文件 | 说明 |
|------|------|
| `code_review_advice.txt` | 项目负责人对代码审查报告的逐条意见 |
| `代码优化工作计划.md` | 整合审查报告与负责人意见后的完整工作计划（阶段一~五 + 待定事项 A/B/C） |
| `代码优化工作计划待定事项的建议（已完成）.txt` | 负责人对待定事项（日志抽象层/会话续期/分页机制）的决策意见 |
| `开发日志/` | 各阶段开发日志（7 份） |

**阶段划分**

| 阶段 | 内容 | 日志 |
|------|------|------|
| 一 | 稳定性/可靠性修复（OkHttpClient 复用、Response 关闭、NPE 防护、SSL 快速失败、ConcurrentHashMap） | `2026-06-18_阶段一_稳定性修复.md` |
| 二 | 异常体系重构（Auth/Network/Api 三层异常类） | `2026-06-18_阶段二_异常体系重构.md` |
| 三 | 性能优化（端口查找与 IP 查找 HashSet O(1) 化） | `2026-06-18_阶段三_性能优化.md` |
| 四 | 代码质量优化（parseData 统一 JSON 解析、批量操作结果收集） | `2026-06-19_阶段四_代码质量优化.md` |
| 五 | 安全与命名（密码 char[]、OnlineUser 重命名、并发保护） | `2026-06-19_阶段五_安全与命名.md` |
| 六 | 日志抽象层 + 分页机制 | `2026-06-19_阶段六_日志与分页.md` |
| 补丁 | HAR 抓包验证修复（NetMapping.src_addr、FuncName.wan） | `2026-06-19_HAR验证修复.md` |

**未实施决策**

- 会话自动续期：上层业务平台已通过 Factory + Redis 缓存 sesskey 形成闭环，SDK 内部自动 relogin 会导致新 sesskey 与 Redis 不一致
- `inter_face` 字段重命名为 `interfaceName`：涉及 8 个 Entity 共 76 处引用，且构成上层 breaking change，现有 `@JsonProperty("interface")` 已保证序列化正确

---

## 02_OpenVPN与认证计费用户

**版本**: 3.5.5

基于 HAR 抓包分析，新增 OpenVPN 与认证计费用户相关 API 支持。

| 文件 | 说明 |
|------|------|
| `OpenVPN与认证计费用户开发方案.md` | HAR 分析结果 + 实体字段定义 + 五阶段实施计划 |
| `开发日志/` | 开发日志（1 份） |

**新增 API 模块**

| func_name | 功能 | 支持的操作 |
|-----------|------|-----------|
| `pppuser` | 认证计费用户 | show / add / edit / down / del / paylog_add |
| `ppp_online` | 认证在线用户 | show |
| `ppp_package` | 计费套餐 | show / add / edit / del |
| `pppoe_server` | PPPoE 服务器 | show（接口列表） |
| `openvpn-server` | OpenVPN 服务端 | show（配置 + 状态 + 导出客户端配置） |

**技术要点**

- `openvpn-server` 含横杠无法直接作为 Java 枚举名，通过 `FuncName` 引入 `@JsonValue` 机制解决
- `paylog_add` 是非标准 action，已扩展至 `ActionType` 枚举

---

## 03_上层平台适配

面向上层业务平台（FreeMyKvm）的适配交付物。

| 文件 | 说明 |
|------|------|
| `SDK变更适配指南.md` | 供上层 AI 开发代理参考的完整适配指南，含 breaking changes、新增 API、适配检查清单 |

**Breaking Changes 摘要**

1. `getCookieStore()` 返回类型 `HashMap` → `Map`
2. `Online_user` → `OnlineUser`，`getOnline_user()` → `getOnlineUser()`
3. 批量操作（`downNetMappingByLanIp` / `delNetMappingByIpAddr`）异常 message 格式变更

---

## 数据来源

HAR 抓包文件位于 `src/test/file/`：

| 文件 | 覆盖内容 |
|------|----------|
| `192.168.77.1_Archive [26-06-19 12-36-20].har` | 全界面 show 操作 |
| `192.168.77.1_Archive [26-06-19 16-15-18]_interface_and_netmapping.har` | 接口与端口映射 CRUD |
| `192.168.77.1_Archive [26-06-19 16-21-14]_openvpn.har` | OpenVPN 与认证计费用户 CRUD |
| `192.168.77.1_Archive [26-06-19 21-27-58]_ppp_package.har` | 计费套餐 CRUD |