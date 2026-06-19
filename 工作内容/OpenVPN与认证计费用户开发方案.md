# 开发方案：OpenVPN + 认证计费用户 API 支持

> 日期: 2026-06-19
> 目标版本: 3.5.5.dev.01
> 数据来源: HAR 文件 192.168.77.1_Archive [26-06-19 16-21-14]_openvpn.har

---

## 一、HAR 分析结果

### 发现的 API 功能模块

| func_name | 说明 | SDK 当前支持 |
|-----------|------|-------------|
| `pppuser` | 认证计费用户管理（CRUD + paylog_add） | 不支持 |
| `ppp_online` | 认证在线用户查询 | 不支持 |
| `ppp_package` | 计费套餐管理 | 不支持 |
| `pppoe_server` | PPPoE 服务器配置查询 | 不支持 |
| `openvpn-server` | OpenVPN 服务端配置/状态/导出 | 不支持 |

---

## 二、pppuser（认证计费用户）— 核心功能

### 2.1 数据结构（从 shr 实体字段:
- id: int                 规则 ID
- username: String        用户名
- passwd: String          密码
- enabled: String         是否启用 (yes/no)
- ppptype: String         类型 (any/ovpn/pppoe)
- pppname: String         PPP 名称
- bind_ifname: String     绑定接口 (any/具体接口名)
- share: int              共享数
- auto_mac: int           自动 MAC (0/1)
- upload: int             上传限速 (Kbps)
- dowKbps)
- ip_type: int            IP 类型 (0=动态)
- ip_addr: String         固定分配 IP
- mac: String             绑定 MAC
- address: String         地址/住址
- name: String            姓名/昵称
- phone: String           电话
- cardid: String          身份证号
- comment: String         备注
- packages: int           套餐 ID (0=无套餐)
- duration: long          已用时长(秒)
- expires: long           到期时间戳 (0=永不过期)
- start_time: long        开始时间戳
- create_time: long       创建时间戳
- last_conntime: long     最后连接时间戳
- last_offtime: long      最后离线时间戳
- bind_vlanid: String     绑定 VLAN ID
- auto_vlanid: int        自动 VLAN (0/1)
- proxy_username: String  代理用户名
- pppoev6_wan: String     PPPoEv6 WAN
```

### 2.2 支持的操作 (action)

| action | 说明 | 参数 |
|--------|----|
| show | 查询用户列表 | TYPE:"total,data", limit, ORDER_BY, FINDS, KEYWORDS |
| show | 查询接口列表 | TYPE:"interface,wan_interface,addrgroup" |
| add | 新增用户 | 完整 PPPUser 字段 |
| edit | 编辑用户 | 完整 PPPUser 字段 (含 id) |
| down | 禁用用户 | id (字符串) |
| del | 删除用户 | id (整数) |
| paylog_add | 添加缴费记录 | name, username, packname, feemoney, action, comment |

---

## 三、ppp_online（认证在线用户）

### 3.1 数据结构

```
PPPOnline 实体字段:
- id: int                 ID
- username: String        用户名
- ppptype: String         类型 (ovpn/pppoe)
- pppdev: String          PPP 设备
- interface: String       接口
- ip_addr: String         分配的 IP
- ip_addr_int: String     IP 整型
- mac: String             来源 MAC/IP
- session: String         会话标识
- auth_time: long         认证时间戳
- upload: int             当前上传速率
- download: int           当前下载速率
- expires: long           到期时间
- packages: int           套餐 ID
- packname: String        套餐名
- phone: String           电话
- name: String            姓名
- comment: String         备注
- webid: int              Web ID
- uid: String             UID
- check_vlan_res: int     VLAN 检查结果
```

---

## 四、ppp_package（计费套餐）

### 4.1 数据结构

```
PPPPackage 实体字段:
- (HAR 中 data 为空数组，字段待后续补充)
- 已知查询参数: FINDS:"packname", KEYWORDS, TYPE:"data", limit:"0,100"
```

---

## 五、openvpn-server（OpenVPN 服务端）

### 5.1 数据结构（从 show TYPE:"data,status" 响应提取）

```
OpenVPNServer 实体字段:
- auth: String            认证方式
- comp_lzo: String        LZO 压缩 (0/1)
- dev_type: String        设备类型 (tun/tap)
- topology: String        拓扑 (subnet)
- method: int             方法
- enabled: String         是否启用 (yes/no)
- port: String            监听端口
- subnet: String          子网 (如 10.7.11.0)
- mask: String            子网掩码
- push_gateway: String    推送网关
- push_route: String      推送路由 (逗号分隔CIDR)
- push_route_comment: String  推送路由备注
- proto: String           协议 (udp/tcp)
- push_dns: String        推送 DNS
- extra_config: String    额外配置
- key: String             RSA 私钥
- (status 字段结构待补充)
```

### 5.2 支持的操作

| action | TYPE | 说明 |
|--------|------|------|
| show | data,status | 获取配置和状态 |
| show | export_client_config | 导出客户端配置文件 |

---

## 六、增实体类

| 新建文件 | 说明 |
|----------|------|
| Entity/PPPUser.java | 认证计费用户实体（30+ 字段） |
| Entity/PPPOnline.java | 认证在线用户实体 |
| Entity/PPPPackage.java | 计费套餐实体（字段待补充） |
| Entity/OpenVPNServer.java | OpenVPN 服务端配置实体 |- 方案A: 枚举值 `openvpn_server` + @JsonValue("openvpn-s## 阶段 C: RouterAgent 层新增方法

```java
// PPPUser CRUD
ResponseShow getPPPUsers() throws Exception
ResponseShow getPPPUsers(Requestows Exception
ResponseDown downPPPUser(int id) throws Exception
ResponseDel delPPPUser(int id) thponseShow getPPPOnlineUsers() throws Exception
ResponseShow getPPPOnlineUsers(RequestParamShow param) throws Exception

// PPP Package
ResponseShow getPPPPackages() throws Exception

// OpenVPN Server
ResponseShow getOpenVPNServerConfig() throws Exception
ResponseShow exportOpenVPNClientConfig(String outputFile) throws Exception

// PPPoE Server
ResponseShow getPPPoEServerInterfaces() throws Exception
```

### 阶段 D: IkuaiRouter 层高级方法

```java
// PPPUser
List<PPPUser> getPPPUserList() throws Exception
List<PPPUser> getAllPPPUserList(int pageSize) throws Exception
PPPUser getPPPUserById(int id) throws Exception
PPPUser getPPPUserByUsername(String username) throws Exception
Integer addPPPUser(PPPUser user) throws Exception
boolean editPPPUser(PPPUser user) throws Exception
boolean downPPPUserById(int id) throws Exception
boolean delPPPUserById(int id) throws Exception

// PPP Online
List<PPPOnline> getPPPOnlineList() throws Exception

// OpenVPN
OpenVPNServer getOpenVPNServerConfig() throws Exception
```

### 阶段 E: 验证 + 文档

- mvn compile 编译验证
- 更新 SPEC.md
- 更新适配指南
- git commit/push

---

## 八、风险评估

| 风险 | 说明 | 缓解措施 |
|------|------|----------|
| openvpn-server 横杠命名 | Java 枚举不支持横杠 | 使用 @JsonValue 注解或 String 重载 |
| PPPPackage 字段未知 | HAR 中 data 为空数组 | 先建空实体，后续抓包补充 |
| paylog_add 是非标准 action | 不在 ActionType 枚举中 | 需新增 ActionType 或使用 String 重载 |
| PPPUser 字段量大 | 30+ 字段 | @JsonIgnoreProperties 保护，逐步补充 |

---

## 九、预计工作量

| 阶段 | 预计时间 |
|------|----------|
| A. 实体类 | 中等（4 个类，字段映射） |
| B. 枚举扩展 | 小（需解决横杠命名问题） |
| C. RouterAgent 方法 | 中等（10+ 方法） |
| D. IkuaiRouter 高级方法 | 中等（10+ 方法） |
| E. 验证文档 | 小 |