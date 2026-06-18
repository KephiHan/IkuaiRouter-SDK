# IkuaiRouter-SDK 软件工程规格说明书 (SPEC)

> 版本: 3.5.3T02 | 语言: Java 11 | 构建: Maven

---

## 目录

1. [项目概述](#1-项目概述)
2. [技术栈](#2-技术栈)
3. [系统架构](#3-系统架构)
4. [核心 API 协议](#4-核心-api-协议)
5. [功能模块详细说明](#5-功能模块详细说明)
6. [高级业务逻辑](#6-高级业务逻辑)
7. [工具类](#7-工具类-ipaddrutil)
8. [错误处理](#8-错误处理)
9. [安全考量](#9-安全考量)
10. [使用示例](#10-使用示例)
11. [项目约束与限制](#11-项目约束与限制)
12. [版本演进记录](#12-版本演进记录)
13. [构建与运行](#13-构建与运行)

---

## 1. 项目概述

| 元数据 | 值 |
|--------|-----|
| **项目名称** | IkuaiRouter-SDK |
| **版本** | 3.5.3T02 |
| **GroupId** | net.dabaiyun |
| **ArtifactId** | IkuaiRouter-SDK |
| **开发语言** | Java 11 |
| **构建工具** | Maven |
| **项目性质** | 爱快路由器 (iKuai Router) Web API 的 Java SDK 封装库 |
| **仓库分支** | master |

### 1.1 项目定位

本项目是一个面向爱快路由器管理 Web API 的 Java SDK。它通过封装 iKuai 路由器的 HTTP JSON API，为 Java 应用程序提供对路由器管理功能的编程式访问能力，包括：

- DHCP 服务器管理（动态分配 + 静态绑定）
- 端口映射 (DNAT) 规则管理
- QoS 限速规则管理
- 系统状态监控（CPU、内存、流量、在线用户）
- 网络接口管理（WAN/LAN 接口、线路检测、流量统计）
- LAN 在线主机查询

### 1.2 目标用户

- 需要通过程序自动化管理爱快路由器的 Java 开发者
- 需要批量操作路由器配置的运维自动化场景
- 需要集成路由器管理功能到自有系统的开发团队

### 1.3 项目地址

```
https://github.com/dabaiyun/IkuaiRouter-SDK
```

---

## 2. 技术栈

| 组件 | 技术选型 | 版本 | 用途 |
|------|----------|------|------|
| 语言 | Java | 11 | 开发语言 |
| 构建 | Maven | - | 项目构建与依赖管理 |
| HTTP 客户端 | OkHttp3 | 4.10.0 | HTTP/HTTPS 通信 |
| JSON 序列化 | Jackson Databind | 2.15.1 | JSON 反/序列化 |
| JSON 注解 | Jackson Annotations | 2.15.1 | JSON 字段映射注解 |
| 测试框架 | JUnit | 4.13.2 | 单元测试 (scope=test) |

### 依赖坐标 (pom.xml)

```xml
<dependency>
    <groupId>com.fasterxml.jackson.core</groupId>
    <artifactId>jackson-databind</artifactId>
    <version>2.15.1</version>
</dependency>
<dependency>
    <groupId>com.fasterxml.jackson.core</groupId>
    <artifactId>jackson-annotations</artifactId>
    <version>2.15.1</version>
</dependency>
<dependency>
    <groupId>com.squareup.okhttp3</groupId>
    <artifactId>okhttp</artifactId>
    <version>4.10.0</version>
</dependency>
<dependency>
    <groupId>junit</groupId>
    <artifactId>junit</artifactId>
    <version>4.13.2</version>
    <scope>test</scope>
</dependency>
```

---

## 3. 系统架构

### 3.1 分层架构

```
┌───────────────────────────────────────────────────────────┐
│                      用户代码层                             │
│              (使用 IkuaiRouter 类进行操作)                  │
├───────────────────────────────────────────────────────────┤
│              IkuaiRouter (高级业务逻辑层)                    │
│  - CRUD 操作封装                                          │
│  - 业务逻辑方法（端口查找、IP 查找等）                       │
│  - JSON 反序列化为 Entity 对象                             │
├───────────────────────────────────────────────────────────┤
│              RouterAgent (HTTP 通信代理层)                  │
│  - 认证/登录管理                                           │
│  - API 请求构建 (ActionType + FuncName + Param)            │
│  - HTTP POST 执行                                         │
│  - 响应预检查 (Result 码校验)                              │
├───────────────────────────────────────────────────────────┤
│         TrustAllCertOkHttpClient (HTTP 传输层)             │
│  - HTTPS 信任所有证书 (兼容自签名证书)                       │
│  - Cookie 自动管理 (包含 sess_key 会话维持)                │
├───────────────────────────────────────────────────────────┤
│               iKuai Router HTTP API                        │
│  - POST /Action/login   (登录接口)                         │
│  - POST /Action/call    (所有业务操作统一接口)              │
└───────────────────────────────────────────────────────────┘
```

### 3.2 包结构

```
net.dabaiyun.ikuairouter
├── IkuaiRouter.java                # 主入口类，高级 API 封装
├── RouterAgent.java                # HTTP 通信代理层
│
├── Action/                         # API 请求/响应模型
│   ├── ActionType.java             # 操作类型枚举
│   ├── FuncName.java               # 功能名称枚举
│   ├── RequestInfo.java            # API 请求体封装
│   ├── RequestParamShow.java       # show 操作参数
│   ├── RequestParamFind.java       # 条件查询参数
│   ├── RequestParamDel.java        # 删除操作参数
│   ├── RequestParamDown.java       # 禁用操作参数
│   ├── LoginPostInfo.java          # 登录请求体
│   ├── LoginResult.java            # 登录响应
│   ├── IkuaiResponseBase.java      # 响应基类
│   ├── ResponseShow.java           # show 响应
│   ├── ResponseAdd.java            # add 响应
│   ├── ResponseEdit.java           # edit 响应
│   ├── ResponseDel.java            # del 响应
│   └── ResponseDown.java           # down 响应
│
├── Entity/                         # 数据实体模型
│   ├── DHCPHost.java               # DHCP 动态分配主机
│   ├── DHCPStatic.java             # DHCP 静态绑定
│   ├── DHCPServer.java             # DHCP 服务器配置
│   ├── NetMapping.java             # 端口映射 (DNAT) 规则
│   ├── QosLimit.java               # QoS 限速规则
│   ├── LanHostInfo.java            # LAN 在线主机信息
│   ├── InterfaceWan.java           # WAN 接口信息
│   ├── InterfaceLan.java           # LAN 接口信息
│   ├── InterfaceCheck.java         # 接口线路检测
│   ├── InterfaceStream.java        # 接口流量统计
│   ├── EtherInfo.java              # 以太网口信息
│   └── sysstat/                    # 系统状态子包
│       ├── SystemStatus.java       # 系统状态汇总
│       ├── Memory.java             # 内存信息
│       ├── Stream.java             # 流量信息
│       ├── Online_user.java        # 在线用户统计
│       └── Verinfo.java            # 版本信息
│
├── Exception/
│   └── IkuaiRouterException.java   # 统一自定义异常
│
├── HttpApi/
│   └── TrustAllCertOkHttpClient.java  # 免验证 HTTP 客户端工厂
│
└── Util/
    └── IpAddrUtil.java             # IP 地址计算实用工具类
```

### 3.3 核心类职责

| 类名 | 行数 | 职责描述 |
|------|------|----------|
| `IkuaiRouter` | 1420 | 对外主 API 门面，封装所有业务操作和高级逻辑 |
| `RouterAgent` | 787 | HTTP 通信代理，请求构建与响应预处理 |
| `TrustAllCertOkHttpClient` | 67 | OkHttpClient 工厂，信任所有证书 + CookieJar 管理 + SSL 初始化失败快速失败 |
| `IpAddrUtil` | 381 | IP 地址计算、校验、转换工具 |
| `IkuaiRouterException` | 23 | 项目唯一自定义异常 |

---

## 4. 核心 API 协议

### 4.1 iKuai HTTP API 通信协议

所有业务操作通过统一端点 `POST /Action/call` 完成，请求体结构为：

```json
{
  "action": "show | add | edit | del | down",
  "func_name": "homepage | monitor_iface | monitor_lanip | dhcp_server | dhcp_lease | dnat | simple_qos | plugins",
  "param": {
    ...  // 各操作对应的参数对象
  }
}
```

**统一响应体格式**：

```json
{
  "Result": 30000,
  "ErrMsg": "",
  "Data": {
    ...  // show 操作返回的实际数据
  }
}
```

登录接口为独立的 `POST /Action/login`，请求体：

```json
{
  "pass": "c2FsdF8xMTthZG1pbg==",   // Base64("salt_11" + password)
  "password": "5f4dcc3b5aa765d61d8327deb882cf99", // MD5(password)
  "username": "admin"
}
```

登录成功 Result 码为 **10000**，一般业务操作成功 Result 码为 **30000**。

### 4.2 认证与会话管理

1. **密码处理**:
   - `pass` 字段: `Base64("salt_11" + 原始密码)` — 加盐 Base64 编码
   - `password` 字段: `MD5(原始密码).toHex()` — MD5 哈希

2. **会话维持**: 登录成功后，服务端返回 `sess_key` 通过 Cookie 传递，`TrustAllCertOkHttpClient` 的 `CookieJar` 自动维护

3. **会话复用**: 可通过构造函数直接传入已有 `sess_key` 构建实例：
   ```java
   new IkuaiRouter(address, port, https, username, pwd, sess_key);
   ```
   内部构造对应 Cookie 存入 `cookieStore`

4. **会话校验**: `checkSessKeyValid()` 通过调用 `getPlugins()` 检测 session 是否有效

### 4.3 操作类型枚举 (ActionType)

| 枚举值 | HTTP 语义 | 说明 |
|--------|-----------|------|
| `show` | 查询 | 获取数据列表或详情，可带条件筛选 |
| `add` | 新增 | 创建新的配置条目 |
| `edit` | 编辑 | 修改已有配置 |
| `del` | 删除 | 永久删除配置条目 |
| `down` | 禁用 | 临时禁用配置（保留配置，不删除） |

### 4.4 功能模块枚举 (FuncName)

| 枚举值 | API 映射 | 说明 |
|--------|----------|------|
| `homepage` | 首页仪表盘 | 系统运行状态、CPU、内存、流量 |
| `monitor_iface` | 接口监控 | WAN/LAN 接口快照、线路检测、流量统计 |
| `monitor_lanip` | LAN IP 监控 | 在线终端主机列表 |
| `dhcp_server` | DHCP 服务 | DHCP 服务器参数配置 |
| `dhcp_lease` | DHCP 租约 | 动态租约列表 + 静态绑定管理 |
| `dnat` | 端口映射 | DNAT 目的地址转换规则 |
| `simple_qos` | 简单限速 | IP 级上行/下行带宽限制 |
| `plugins` | 插件管理 | 系统已安装插件信息 |

### 4.5 查询参数模式

**普通查询** (RequestParamShow):
```json
{
  "limit": "0,1000",
  "ORDER": "",
  "ORDER_BY": "",
  "TYPE": "data | snapshoot | sysstat | ..."
}
```

**条件查询** (RequestParamFind, 继承 RequestParamShow):
```json
{
  "limit": "0,1000",
  "TYPE": "data",
  "FINDS": "ip_addr,mac",
  "KEYWORDS": "192.168.1.100"
}
```

支持的可筛选字段常量 (通过 `RequestParamFind` 定义):
- `FINDS_ID` = `"id"`
- `FINDS_IP_ADDR` = `"ip_addr"`
- `FINDS_MAC` = `"mac"`
- `FINDS_LAN_ADDR` = `"lan_addr"`
- `FINDS_LAN_PORT` = `"lan_port"`
- `FINDS_WAN_PORT` = `"wan_port"`
- `FINDS_INTERFACE` = `"interface"`
- `FINDS_COMMENT` = `"comment"`

---

## 5. 功能模块详细说明

### 5.1 HTTP 传输层 — TrustAllCertOkHttpClient

**文件**: `src/main/java/net/dabaiyun/ikuairouter/HttpApi/TrustAllCertOkHttpClient.java`

核心功能：

1. **信任所有 SSL 证书** — 实现空的 `X509TrustManager`，`checkClientTrusted` / `checkServerTrusted` 均为空实现
2. **忽略主机名验证** — `hostnameVerifier` 对所有主机名返回 `true`
3. **自动 Cookie 管理** — 实现 `CookieJar` 接口，以 `cookieStore` (ConcurrentHashMap) 为后端存储，跨请求自动携带 Cookie
4. **实例复用**: `OkHttpClient` 在 `RouterAgent` 构造时创建一次，整个实例生命周期内复用同一连接池

```java
public static OkHttpClient getTrustAllCertOkHttpClient(Map<String, List<Cookie>> cookieStore)
```

### 5.2 HTTP 通信代理层 — RouterAgent

**文件**: `src/main/java/net/dabaiyun/ikuairouter/RouterAgent.java`

负责所有底层 HTTP 通信，提供以下功能群组：

#### 5.2.1 登录

```java
LoginResult login() throws Exception
```

执行完整登录流程：构造加盐密码 → 构造请求体 → POST → 解析登录结果。

#### 5.2.2 通用查询 (show)

| 方法 | API 功能模块 | 说明 |
|------|-------------|------|
| `getPlugins()` | `plugins` | 获取已安装插件列表 |
| `getInterfaceSnapshoot()` | `monitor_iface` | 获取 WAN/LAN 接口快照 |
| `getSystemStatus()` | `homepage` | 获取系统状态 (sysstat) |
| `getLanHostStatus()` | `monitor_lanip` | 获取 LAN 主机列表 |
| `getDHCPServers()` | `dhcp_server` | 获取 DHCP 服务器配置 |
| `getDHCPHosts()` | `dhcp_lease` | 获取 DHCP 动态租约 |
| `getDHCPStatics()` | `dhcp_lease` | 获取 DHCP 静态绑定列表 |
| `getDHCPStaticsById()` | `dhcp_lease` | 按 ID 条件查询静态绑定 |
| `getDHCPStaticsByIpAddr()` | `dhcp_lease` | 按 IP 条件查询静态绑定 |
| `getDHCPStaticsByMac()` | `dhcp_lease` | 按 MAC 条件查询静态绑定 |
| `getNetMapping()` | `dnat` | 获取所有端口映射规则 |
| `getNetMappingById()` | `dnat` | 按 ID 查询端口映射 |
| `getNetMappingByIpAddr()` | `dnat` | 按 LAN IP 查询端口映射 |
| `getNetMappingByWanPort()` | `dnat` | 按 WAN 端口查询端口映射 |
| `getNetMappingByInterface()` | `dnat` | 按接口查询端口映射 |
| `getQosLimitById()` | `simple_qos` | 按 ID 查询限速规则 |
| `getQosLimitByIpAddr()` | `simple_qos` | 按 IP 查询限速规则 |
| `getInterfaceCheckList()` | `monitor_iface` | 获取接口线路检测结果 |
| `getInterfaceStreamList()` | `monitor_iface` | 获取接口流量统计 |

#### 5.2.3 新增 (add)

| 方法 | API 功能模块 |
|------|-------------|
| `addDHCPStatic(DHCPStatic)` | `dhcp_lease` |
| `addQosLimit(QosLimit)` | `simple_qos` |
| `addNetMapping(NetMapping)` | `dnat` |

#### 5.2.4 编辑 (edit)

| 方法 | API 功能模块 |
|------|-------------|
| `editDHCPStatic(DHCPStatic)` | `dhcp_lease` |
| `editQosLimit(QosLimit)` | `simple_qos` |
| `editNetMapping(NetMapping)` | `dnat` |

#### 5.2.5 禁用 (down)

| 方法 | API 功能模块 |
|------|-------------|
| `downDHCPStatic(int id)` | `dhcp_lease` |
| `downQosLimit(int id)` | `simple_qos` |
| `downNetMapping(int id)` | `dnat` |

#### 5.2.6 删除 (del)

| 方法 | API 功能模块 |
|------|-------------|
| `delDHCPStatic(int id)` | `dhcp_lease` |
| `delQosLimit(int id)` | `simple_qos` |
| `delNetMapping(int id)` | `dnat` |

#### 5.2.7 核心私有方法

```java
private String executeAction(ActionType actionType, FuncName funcName, Object param) throws Exception
```

这是 `RouterAgent` 的核心调用方法：
1. 构造请求 URL: `{protocol}://{address}:{port}/Action/call`
2. 构建 `RequestInfo` 对象 (含 action, func_name, param)
3. 序列化为 JSON → 创建 OkHttp POST 请求
4. 通过实例持有的 `OkHttpClient` 发起调用（try-with-resources 确保 Response 关闭）
5. 校验 HTTP 状态码 + response body 非空
6. 预检查 `Result` 码，非 30000 时抛出 `IkuaiRouterException`
7. 成功返回原始 JSON 字符串

### 5.3 高级业务逻辑层 — IkuaiRouter

**文件**: `src/main/java/net/dabaiyun/ikuairouter/IkuaiRouter.java`

这是对外的主要 API 门面类，封装了完整的功能集合。

#### 5.3.1 构造与生命周期

| 构造器 | 说明 |
|--------|------|
| `IkuaiRouter(address, port, https, username, pwd)` | 完整参数，自动登录 |
| `IkuaiRouter(address, port, https, username, pwd, doLogin)` | 控制是否自动登录 |
| `IkuaiRouter(address, port, https, username, pwd, sess_key)` | 传入已有 session key 复用会话 |

**获取 Session Key**:
```java
String sessKey = ikuaiRouter.getSessKey();  // 从 CookieStore 中提取
```

#### 5.3.2 DHCP 静态绑定管理

**查询**:
```java
DHCPStatic getDHCPStaticById(int id)
DHCPStatic getDHCPStaticByIpAddr(String ip_addr)
DHCPStatic getDHCPStaticByMAC(String mac)
List<DHCPStatic> getDHCPStaticList()
```

**新增**:
```java
Integer addDHCPStatic(DHCPStatic dhcpStatic)  // 返回新行 ID
```

**编辑**:
```java
boolean editDHCPStatic(DHCPStatic dhcpStatic)
```

**禁用（不下线，仅停用规则）**:
```java
boolean downDHCPStaticByMac(String mac)
boolean downDHCPStaticByIpAddr(String ip_addr)
boolean downDHCPStaticById(int id)
```

**删除**:
```java
boolean delDHCPStaticById(int id)
boolean delDHCPStaticByIpAddr(String ip_addr)
boolean delDHCPStaticByMac(String mac)
```

#### 5.3.3 DHCP 服务器与动态租约

```java
List<DHCPServer> getDHCPServerList()
DHCPServer getDHCPServerByInterface(String inter_face)
List<DHCPHost> getDHCPHostList()
```

#### 5.3.4 端口映射管理 (DNAT)

**查询**:
```java
NetMapping getNetMappingById(int id)
NetMapping getNetMappingByInterfaceAndWanPort(String inter_face, String wanport)
List<NetMapping> getNetMappingList()
List<NetMapping> getNetMappingListByIpAddr(String ip_addr)
```

**新增**:
```java
Integer addNetMapping(NetMapping netMapping)  // 返回新行 ID
```

**编辑**:
```java
boolean editNetMapping(NetMapping netMapping)
```

**禁用**:
```java
boolean downNetMappingById(int id)
boolean downNetMappingByInterfaceAndWanport(String inter_face, String wanport)
boolean downNetMappingByLanIp(String lanip)  // 批量禁用同一内网 IP 的所有规则
```

**删除**:
```java
boolean delNetMappingById(int id)
boolean delNetMappingByInterfaceAndWanport(String inter_face, String wanport)
boolean delNetMappingByIpAddr(String ip_addr)  // 批量删除同一内网 IP 的所有规则
```

#### 5.3.5 QoS 限速管理

**查询**:
```java
QosLimit getQosLimitById(int id)
QosLimit getQosLimitByIpAddr(String ip_addr)
```

**新增**:
```java
Integer addQosLimit(QosLimit qosLimit)  // 返回新行 ID
```

**编辑**:
```java
boolean editQosLimit(QosLimit qosLimit)
```

**禁用**:
```java
boolean downQosLimitById(int id)
boolean downQosLimitByIpAddr(String ip_addr)
```

**删除**:
```java
boolean delQosLimitById(int id)
boolean delQosLimitByIpAddr(String ip_addr)
```

#### 5.3.6 网络接口监控

```java
List<InterfaceWan> getInterfaceWanList()        // WAN 接口状态
List<InterfaceLan> getInterfaceLanList()         // LAN 接口状态
List<InterfaceCheck> getInterfaceCheckList()     // 线路检测结果
List<InterfaceStream> getInterfaceStreamList()   // 接口流量统计
```

#### 5.3.7 LAN 主机查询

```java
List<LanHostInfo> getLanHostInfoList()             // 所有在线主机
LanHostInfo getLanHostInfoByIpAddr(String ip)      // 按 IP 查
LanHostInfo getLanHostInfoByMAC(String mac)        // 按 MAC 查
```

#### 5.3.8 系统状态

```java
SystemStatus getSystemStatus()    // CPU、内存、流量、在线用户、版本
```

---

## 6. 高级业务逻辑

### 6.1 WAN 端口占用检测

```java
/**
 * 判断某个 WAN 接口上的特定端口是否已被端口映射规则使用
 * 
 * @param inter_face WAN 接口名称（如 "wan1", "adsl1"）
 * @param wanPort    待检测端口号
 * @return true=已被占用
 */
boolean isWanPortInUse(String inter_face, int wanPort)

/**
 * 多接口版本 - 目标接口列表中有任一匹配即视为占用
 * 
 * @param interfaceList 待检测的接口名称列表
 * @param wanPort       待检测端口号
 */
boolean isWanPortInUseMultiInterface(List<String> interfaceList, int wanPort)
```

**算法逻辑**:
1. 获取全部端口映射规则
2. 过滤出与目标接口匹配的规则（接口名逗号分隔，只要有交集就匹配）
3. 解析每条规则的 `wan_port` 字段（支持三种格式）
4. 判断目标端口是否落入已占用的端口集合

**wan_port 字段支持的格式**:
- 单端口: `"8080"`
- 端口范围: `"8000-8010"`
- 逗号分隔组合: `"8000,8005,9000-9010"`

### 6.2 可用 WAN 端口查找

```java
/**
 * 在指定端口范围内顺序查找第一个未被占用的端口
 * 
 * @param inter_face  上行接口
 * @param portbegin   起始端口（最小 1）
 * @param portend     结束端口（最大 65535）
 * @return 找到的可用端口
 * @throws IkuaiRouterException 找不到可用端口
 */
int findAvailableNetMappingWanPort(String inter_face, int portbegin, int portend)

/**
 * 多接口版本
 */
int findAvailableNetMappingWanPortMultiInterface(List<String> interfaceList, int portbegin, int portend)
```

### 6.3 可用端口区间查找

```java
/**
 * 按固定步长查找未被使用的连续端口区间起始点
 * 适用于为多个 VPS 批量分配连续端口段
 * 
 * @param portbegin         起始端口
 * @param portend           结束端口
 * @param portnumEachVPS    每个 VPS 需要分配的端口数（步长）
 * @return 可用区间的起始端口
 */
int findAvailableNetMappingPortRegionBegin(int portbegin, int portend, int portnumEachVPS)
```

**算法**: 在 `[portbegin, portend)` 范围内，以 `portnumEachVPS` 为步长遍历，检查每个步长起始点是否已有端口映射配置，返回第一个未被使用的起始点。

### 6.4 可用 IP 地址查找

```java
/**
 * 自动计算 IP 范围
 * 
 * @param gateway            网关（用于确定网段）
 * @param netmaskBit         掩码位数
 * @param reserveIpStartCount 网段首部预留 IP 数量
 * @param reserveIpEndCount   网段尾部预留 IP 数量
 * @return 可用的 IP 地址
 */
String findAvailableIpAddr(String gateway, int netmaskBit, int reserveIpStartCount, int reserveIpEndCount)

/**
 * 自定义 IP 搜索范围
 * 
 * @param gateway    网关
 * @param netmaskBit 掩码位数
 * @param ip_begin   搜索起始 IP
 * @param ip_end     搜索结束 IP
 * @return 可用的 IP 地址
 */
String findAvailableIpAddr(String gateway, int netmaskBit, String ip_begin, String ip_end)
```

**参数校验**: 对 gateway、ip_begin、ip_end、netmaskBit 逐一进行合法性校验，包括 IP 格式、掩码范围（1-32）、IP 是否在指定 CIDR 内。

**算法逻辑**:
1. 获取当前系统所有 DHCP 动态租约和静态绑定
2. 在指定范围内从小到大遍历每个 IP
3. 检查是否已被 DHCPHost 或 DHCPStatic 占用
4. 返回第一个未被占用的 IP

---

## 7. 工具类 — IpAddrUtil

**文件**: `src/main/java/net/dabaiyun/ikuairouter/Util/IpAddrUtil.java`

### 7.1 IP 校验

| 方法 | 说明 |
|------|------|
| `isIpVaild(String str)` | 正则校验 IPv4 地址格式 |
| `isMaskBitVaild(int maskBit)` | 校验掩码位数是否在 1-32 范围 |
| `isIpInRange(String ip, String cidr)` | 判断 IP 是否在指定 CIDR 网段内 |

### 7.2 IP 地址计算

| 方法 | 说明 |
|------|------|
| `getBeginIpStr(String ip, int maskBit)` | 计算网段起始 IP（字符串） |
| `getBeginIpLong(String ip, int maskBit)` | 计算网段起始 IP（长整型） |
| `getEndIpStr(String ip, int maskBit)` | 计算网段结束 IP（字符串） |
| `getEndIpLong(String ip, int maskBit)` | 计算网段结束 IP（长整型） |
| `getIpRangeAllIp(String ipfrom, String ipto)` | 枚举两个 IP 之间的所有 IP |
| `getIpRangeByIpAndMask(String ip, int mask)` | 枚举网段内所有可用 IP（排除网络号和广播地址） |
| `getNextNIp(String ip, int N)` | 获取当前 IP 向后第 N 个 IP（支持跨段进位） |
| `getPreviousNIp(String ip, int N)` | 获取当前 IP 向前第 N 个 IP（支持跨段借位） |

### 7.3 IP 格式转换

| 方法 | 说明 |
|------|------|
| `parseIpFromLongToString(Long ip)` | 长整型 → 点分十进制 |
| `parseIpFromStringToLong(String ip)` | 点分十进制 → 长整型 |
| `ipToDouble(String ip)` | IP 转浮点数（用于排序/比较） |
| `getIpCountByMask(String mask)` | 根据掩码计算 IP 总数 |

### 7.4 掩码转换

| 方法 | 说明 |
|------|------|
| `parseMaskBitToMask(int maskBit)` | 掩码位数 → 子网掩码（如 24 → "255.255.255.0"） |
| `parseMaskBitToMask(String maskBit)` | 字符串版 |
| `parseMaskToMaskBit(String maskStr)` | 子网掩码 → 掩码位数（如 "255.255.255.0" → 24） |

---

## 8. 数据实体模型

### 8.1 DHCPHost (DHCP 动态租约)

| 字段 | 类型 | 说明 |
|------|------|------|
| `id` | `int` | 记录 ID |
| `status` | `int` | 租约状态 |
| `ip_addr_int` | `long` | IP 地址整型表示 |
| `hostname` | `String` | 主机名 |
| `mac` | `String` | MAC 地址 |
| `inter_face` | `String` | 绑定接口（@JsonProperty "interface"） |
| `ip_addr` | `String` | IP 地址 |
| `start_time` | `long` | 租约起始时间戳 |
| `end_time` | `long` | 租约到期时间戳 |
| `timeout` | `long` | 租约超时时间 |
| `comment` | `String` | 备注 |

**来源**: `bejson.com` 自动生成

### 8.2 DHCPStatic (DHCP 静态绑定)

| 字段 | 类型 | 说明 |
|------|------|------|
| `id` | `int` | 记录 ID |
| `ip_addr_int` | `long` | IP 地址整型表示 |
| `hostname` | `String` | 主机名 |
| `mac` | `String` | MAC 地址 |
| `inter_face` | `String` | 绑定接口 |
| `ip_addr` | `String` | 静态分配的 IP |
| `gateway` | `String` | 网关（默认空字符串） |
| `enabled` | `String` | 是否启用 ("yes"/"no") |
| `comment` | `String` | 备注 |

**常量**:
```java
DHCPStatic.EnabledType.YES = "yes";
DHCPStatic.EnabledType.NO = "no";
```

### 8.3 DHCPServer (DHCP 服务器配置)

| 字段 | 类型 | 说明 |
|------|------|------|
| `id` | `int` | 配置 ID |
| `inter_face` | `String` | 关联接口 |
| `addr_pool` | `String` | 地址池范围 |
| `gateway` | `String` | 网关 |
| `netmask` | `String` | 子网掩码 |
| `dns1` / `dns2` | `String` | DNS 服务器 |
| `wins1` / `wins2` | `String` | WINS 服务器 |
| `domain` | `String` | 域名 |
| `lease` | `int` | 租约时长 |
| `status` | `int` | 运行状态 |
| `enabled` | `String` | 是否启用 |
| `available` | `int` | 是否可用 |
| `check_addr_valid` | `int` | 地址有效性检查 |
| `check_relay_only` | `int` | 仅中继模式 |
| `next_server` | `String` | 下一服务器 (PXE) |
| `opt43/60/66/67/80/119/125/128/138` | `String` | DHCP 选项 |

### 8.4 NetMapping (端口映射/DNAT 规则)

| 字段 | 类型 | 说明 |
|------|------|------|
| `id` | `int` | 规则 ID |
| `wan_port` | `String` | 公网端口（支持单端口/范围/逗号组合） |
| `protocol` | `String` | 协议 (tcp/udp/tcp+udp) |
| `lan_addr` | `String` | 内网 IP |
| `lan_port` | `String` | 内网端口 |
| `inter_face` | `String` | 上行接口 |
| `lan_addr_int` | `long` | 内网 IP 整型表示 |
| `enabled` | `String` | 是否启用 ("yes"/"no") |
| `comment` | `String` | 备注 |

**常量**:
```java
NetMapping.ProtocolType.TCP = "tcp";
NetMapping.ProtocolType.UDP = "udp";
NetMapping.ProtocolType.TCP_UDP = "tcp+udp";
NetMapping.EnabledType.YES = "yes";
NetMapping.EnabledType.NO = "no";
```

### 8.5 QosLimit (QoS 限速规则)

| 字段 | 类型 | 说明 |
|------|------|------|
| `id` | `int` | 规则 ID |
| `attr` | `int` | 编辑时有效（预留） |
| `ip_addr` | `String` | 目标 IP |
| `download` | `int` | 下行限速 (Kbps) |
| `upload` | `int` | 上行限速 (Kbps) |
| `protocol` | `String` | 协议 (tcp/udp/tcp+udp/any) |
| `src_port` | `String` | 源端口 |
| `dst_port` | `String` | 目的端口 |
| `inter_face` | `String` | 关联接口 |
| `type` | `int` | 限速模式（0=独立, 1=共享） |
| `week` | `String` | 生效星期 ("1234567"=每天) |
| `time` | `String` | 生效时段 ("00:00-23:59"=全天) |
| `enabled` | `String` | 是否启用 |
| `comment` | `String` | 备注 |

**预定义常量**:
```java
// 启用状态
QosLimit.EnabledType.YES = "yes";
QosLimit.EnabledType.NO = "no";

// 星期
QosLimit.WeekType.ALL = "1234567";       // 每天
QosLimit.WeekType.WORK_DAY = "12345";    // 工作日
QosLimit.WeekType.WEEK_END = "67";       // 周末

// 时段
QosLimit.TimeType.ALL_DAY = "00:00-23:59"; // 全天

// 限速模式
QosLimit.LimitType.INDEPENDENT = 0;  // 独立限速
QosLimit.LimitType.SHARE = 1;        // 共享限速

// 协议
QosLimit.ProtocolType.TCP = "tcp";
QosLimit.ProtocolType.UDP = "udp";
QosLimit.ProtocolType.TCP_UDP = "tcp+udp";
QosLimit.ProtocolType.ANY = "any";
```

### 8.6 LanHostInfo (LAN 在线主机)

| 字段 | 类型 | 说明 |
|------|------|------|
| `id` | `int` | 记录 ID |
| `ip_addr` / `ip_addr_int` | `String` / `long` | IP 地址 |
| `mac` | `String` | MAC 地址 |
| `hostname` | `String` | 主机名 |
| `username` | `String` | 认证用户名 |
| `ssid` | `String` | 连接的 SSID |
| `bssid` | `String` | BSSID |
| `apname` | `String` | AP 名称 |
| `apmac` | `String` | AP MAC |
| `frequencies` | `String` | 频段 (2.4G/5G) |
| `signal` | `String` | 信号强度 |
| `client_type` | `String` | 客户端类型 |
| `client_device` | `String` | 客户端设备型号 |
| `connect_num` | `int` | 连接数 |
| `download` / `upload` | `int` | 当前速率 |
| `downrate` / `uprate` | `String` | 速率文本表示 |
| `total_down` / `total_up` | `long` | 累计流量 |
| `uptime` | `String` | 在线时长 |
| `timestamp` | `long` | 更新时间戳 |
| `ppptype` | `String` | PPPoE 类型 |
| `auth_type` | `int` | 认证类型 |
| `ac_gid` | `int` | AC 组 ID |
| `webid` | `int` | Web 认证 ID |
| `reject` | `int` | 拒绝状态 |
| `dtalk_name` | `String` | Dtalk 名称 |
| `comment` | `String` | 备注 |

### 8.7 InterfaceWan (WAN 接口)

| 字段 | 类型 | 说明 |
|------|------|------|
| `id` | `int` | 接口 ID |
| `inter_face` | `String` | 接口名称 |
| `mac` | `String` | MAC 地址 |
| `member` | `List<String>` | 成员接口（多拨） |
| `ip_addr` | `String` | IP 地址 |
| `netmask` | `String` | 子网掩码 |
| `gateway` | `String` | 网关 |
| `dns1` / `dns2` | `String` | DNS |
| `default_route` | `int` | 默认路由优先级 |
| `internet` | `int` | 互联网连接状态 |
| `count_static` / `count_dhcp` / `count_pppoe` | `int` | 各类型连接数 |
| `count_check_fail` | `int` | 检测失败次数 |
| `check_res` | `int` | 检测结果 |
| `errmsg` | `String` | 错误信息 |
| `updatetime` | `int` | 更新时间 |
| `comment` | `String` | 备注 |

### 8.8 InterfaceStream (接口流量统计)

| 字段 | 类型 | 说明 |
|------|------|------|
| `inter_face` | `String` | 接口名称 |
| `ip_addr` | `String` | IP 地址 |
| `upload` / `download` | `long` | 当前速率 (bytes/s) |
| `total_up` / `total_down` | `long` | 累计流量 (bytes) |
| `uppacked` / `downpacked` | `long` | 累计数据包数 |
| `updropped` / `downdropped` | `long` | 丢包数 |
| `connect_num` | `String` | 连接数 |
| `comment` | `String` | 备注 |

### 8.9 InterfaceCheck (接口线路检测)

| 字段 | 类型 | 说明 |
|------|------|------|
| `id` | `int` | 记录 ID |
| `inter_face` | `String` | 接口名称 |
| `parent_interface` | `String` | 父接口 |
| `ip_addr` | `String` | IP 地址 |
| `gateway` | `String` | 检测网关 |
| `internet` | `String` | 互联网通断 |
| `result` | `String` | 检测结果 |
| `errmsg` | `String` | 错误信息 |
| `auto_switch` | `String` | 自动切换状态 |
| `updatetime` | `String` | 更新时间 |
| `comment` | `String` | 备注 |

### 8.10 SystemStatus (系统状态)

| 字段 | 类型 | 说明 |
|------|------|------|
| `cpu` | `List<String>` | CPU 使用率列表 |
| `cputemp` | `List<Integer>` | CPU 温度列表 |
| `memory` | `Memory` | 内存信息 |
| `stream` | `Stream` | 总流量信息 |
| `online_user` | `Online_user` | 在线用户统计 |
| `uptime` | `long` | 系统运行时间 (秒) |
| `hostname` | `String` | 路由器主机名 |
| `gwid` | `String` | 网关 ID |
| `link_status` | `int` | 链路状态 |
| `verinfo` | `Verinfo` | 固件版本信息 |

---

## 9. 错误处理

### 9.1 异常体系

项目使用分层异常体系：

```
IkuaiRouterException (extends java.lang.Exception) — 基类
├── IkuaiRouterAuthException       — 认证失败/会话过期 (Result=10014)
├── IkuaiRouterNetworkException    — HTTP 通信失败、响应体为空、序列化失败
└── IkuaiRouterApiException        — API 业务失败 (Result 非成功码且非认证失败)
                                     含 resultCode 字段，可获取原始 Result 码
```

异常分层应用于 `RouterAgent` 的 `login()` 和 `executeAction()` 方法内部。所有 public 方法签名保持 `throws Exception`，调用方可通过 stanceof` 精确判断异常类型。

### 9.2 API 响应码对照表

| Result 码 | 含义 | 触发场景 |
|-----------|------|----------|
| `10000` | 登录成功 | `LoginResult.isSuccess()` 返回 true |
| `30000` | 操作成功 | `IkuaiResponseBase.isSuccess()` 返回 true |
| `10014` | 认证失败 | Session 过期/无效，抛出 `IkuaiRouterAuthException` |
| 其他 | 操作失败 | 抛出 `IkuaiRouterApiException(errMsg, resultCode)` |

### 9.3 错误处理流程

```
executeAction()
    ↓
HTTP POST → 检查 response.isSuccessful()
    ↓ (失败) → throw IkuaiRouterNetworkException("HTTP status: " + code)
    ↓ (成功)
检查 response body 非空
    ↓ (null) → throw IkuaiRouterNetworkException("Response body is null")
    ↓ (非空)
解析 JSON → 预检查 Result 码
    ↓ (10014) → throw IkuaiRouterAuthException(errMsg)
    ↓ (非 30000) → throw IkuaiRouterApiException(errMsg, resultCode)
    ↓ (30000)
返回原始 JSON 字符串
```

### 9.4 会话过期处理

当 `executeAction()` 检测到 Result=10014 时，抛出 `IkuaiRouterAuthException`。调用方可捕获此异常后调用 `login()` 重新认证，或由上层业务平台的 Redis 缓存机制处理 sesskey 续期。

---

## 10. 安全考量

### 10.1 当前实现

| 安全特性 | 实现方式 | 风险等级 |
|----------|----------|----------|
| SSL/TLS | 信任所有证书 + 忽略主机名 | ⚠️ 中等 |
| 密码传输 | MD5 哈希 + Base64 加盐编码 | ⚠️ 中等（MD5 已不推荐） |
| 会话管理 | Cookie (`sess_key`) | ✅ 标准做法 |
| 密码存储 | 运行时内存持有明文密码 | ⚠️ 低（主机本地） |

### 10.2 TrustAllCert 说明

```java
X509TrustManager trustManager = new X509TrustManager() {
    public void checkClientTrusted(...) { }   // 空实现
    public void checkServerTrusted(...) { }   // 空实现
    public X509Certificate[] getAcceptedIssuers() { return new X509Certificate[0]; }
};
```

所有证书均被无条件信任，同时 `hostnameVerifier` 也接收所有主机名。这是为了兼容爱快路由器的自签名证书，但在生产环境中使用时应确保通信链路安全（如仅在内网使用）。

### 10.3 安全建议

> ⚠️ 以下为安全建议，非当前实现

1. **内网隔离**: 建议仅在可信任的内网环境中使用本 SDK
2. **凭据管理**: 不应硬编码凭据；推荐通过环境变量或加密配置注入
3. **权限最小化**: 使用专用的管理账号，避免高权限账号暴露

---

## 11. 项目约束与限制

### 11.1 技术限制

| 限制项 | 说明 |
|--------|------|
| **分页限制** | 默认 `limit=0,1000`，一次最多返回 1000 条记录 |
| **同步调用** | 所有 HTTP 调用为同步阻塞，无异步/响应式支持 |
| **连接管理** | 每个 `RouterAgent` 实例持有独立的 `OkHttpClient`，同一节点复用连接池 |
| **重试机制** | 网络错误或 API 失败直接抛出异常，无自动重试 |
| **日志系统** | 无日志框架，调试信息通过 `System.out.println` 输出 |
| **线程安全** | `cookieStore` 使用 `ConcurrentHashMap`，支持多线程环境 |
| **iKuai 版本** | API 字段依赖特定 iKuai 固件版本，未做版本协商/兼容性检测 |

### 11.2 遗留代码

项目代码中存在多处注释掉的代码，包括：
- 备用构造函数（`cookieStore` 参数版本）
- `IkuaiRouterNoAuthException` 引用
- `editDHCPStaticByMac/ByIpAddr/ById` 等批量操作方法
- 旧的枚举 `toString` 实现（枚举值直接用作字符串）

### 11.3 代码质量信号

- `@JsonIgnoreProperties(ignoreUnknown = true)` 在所有 Entity 上使用 — 良好的前向兼容设计
- 无泛型类型信息丢失（使用 Jackson `TypeReference`）
- IP 工具类实现完整，但部分算法时间复杂度较高（如 IP 遍历的四层嵌套循环）

---

## 12. 测试覆盖

### 12.1 测试文件

**路径**: `src/test/java/RouterTest.java` (462 行)

**测试框架**: JUnit 4.13.2

### 12.2 测试用例清单

| 测试方法 | 测试内容 |
|----------|----------|
| `getInterfaceCheckListTest` | 接口线路检测列表查询 |
| `getInterfaceStreamListTest` | 接口流量统计列表查询 |
| `getInterfaceWanListTest` | WAN 接口列表查询 |
| `isWanPortInUseMultiInterface` | 多接口端口占用检测 |
| `findAvailableNetMappingWanPortMultiInterfaceTest` | 多接口可用端口查找 |
| `wanPortInUseTest` | 单接口端口占用检测 |
| `getSystemStatusTest` | 系统状态查询 |
| `paramFindTest` | 条件查询参数测试 |
| `portInUseTest` | 端口占用检测 |
| `cookieStoreTest` | Cookie 会话复用测试 |
| `qosLimitTest` | QoS 限速查询 |
| `delTest` | DHCP 静态绑定删除 |
| `findWanPortTest` | 可用 WAN 端口查找（含耗时统计） |
| `getNextNIp` | IP 工具类 — 后 N 个 IP |
| `getPreviousNIp` | IP 工具类 — 前 N 个 IP |
| `findAvailableIp` | 可用 IP 查找（含耗时统计） |
| `logintest` | 登录测试 |
| `getDHCPStaticTest` | DHCP 静态绑定查询 |
| `getLanHostInfo` | LAN 主机信息查询 |
| `getSystemStatu` | 系统状态查询 |
| `cookieTest` | Cookie 会话循环测试 |
| `idTest` | 批量创建端口映射测试 |

### 12.3 测试资源

```
src/test/file/
├── EtherInfoRespone.json    # 以太网信息响应 Mock
└── IfaceResponse.json       # 接口信息响应 Mock
```

### 12.4 注意事项

测试代码中包含硬编码的路由器凭据（如 `"admin"/"Admin@123"`、`"BayMax"/"gs65stealth9se"`），这些仅用于开发测试环境，不应直接用于生产。

---

## 13. 构建与运行

### 13.1 构建命令

```bash
# 编译项目
mvn compile

# 运行测试（需要连接到实际路由器）
mvn test

# 打包为 JAR
mvn package

# 跳过测试打包
mvn package -DskipTests

# 安装到本地 Maven 仓库
mvn install
```

### 13.2 运行依赖

- Java 11+
- Maven 3.6+
- 网络可到达的爱快路由器

### 13.3 预期构建产物

```
target/IkuaiRouter-SDK-3.5.3T02.jar
```

### 13.4 外部依赖

不需要额外中间件、数据库或容器，SDK 可直接集成到任何 Java 11+ 应用中。

---

## 14. 版本演进记录

| 时间/顺序 | 提交 | 内容 |
|-----------|------|------|
| Init | `1359e76` / `58c2501` | 基础框架：登录认证、DHCP 静态绑定、端口映射基本 CRUD |
| 改进 | `5332574` | ResponseShow 反序列化重构，部分操作不再抛出异常 |
| 改进 | `39d5395` / `8148bf2` | checkSessKey 改为不抛异常，返回 false |
| 功能 | `09a99ac` | 新增 WAN 端口占用检测方法 |
| 优化 | `2867ab2` | 查询方法改用 iKuai 条件查询 (FINDS/KEYWORDS)，降低传输量 |
| 功能 | `f80ddd3` | 端口占用检测支持多接口场景 |
| 功能 | `399d6da` | 可用端口查找新增多接口匹配 |
| 功能 | `456c457` | WAN 端口占用检测新增多接口匹配 |
| 功能 | `ec876be` | 新增 WAN/LAN 接口状态读取 |
| 功能 | `d52808a` | 限速实体新增共享限速模式 |
| 维护 | `fcfbc01` | 删除 src/test 目录 |
| 维护 | `a660ede` | 修改限速常量名 |
| 合并 | `024169c` | Merge remote-tracking branch 'origin/master' |

---

## 附录 A: 请求/响应示例

### A.1 登录请求

```
POST /Action/login
```

```json
{
  "username": "admin",
  "pass": "c2FsdF8xMTthZG1pbg==",
  "password": "5f4dcc3b5aa765d61d8327deb882cf99"
}
```

### A.2 登录响应

```json
{
  "Result": 10000,
  "ErrMsg": ""
}
```

### A.3 查询 DHCP 静态绑定

```
POST /Action/call
```

请求体：
```json
{
  "action": "show",
  "func_name": "dhcp_lease",
  "param": {
    "limit": "0,1000",
    "ORDER": "",
    "ORDER_BY": "",
    "TYPE": "static_data"
  }
}
```

### A.4 新增端口映射

```
POST /Action/call
```

请求体：
```json
{
  "action": "add",
  "func_name": "dnat",
  "param": {
    "wan_port": "8080",
    "protocol": "tcp+udp",
    "lan_addr": "192.168.1.100",
    "lan_port": "80",
    "interface": "wan1",
    "enabled": "yes",
    "comment": "Web Server"
  }
}
```

### A.5 条件查询

查找 LAN 地址为 `192.168.1.100` 的端口映射规则：

```json
{
  "action": "show",
  "func_name": "dnat",
  "param": {
    "limit": "0,1000",
    "TYPE": "data",
    "FINDS": "lan_addr",
    "KEYWORDS": "192.168.1.100"
  }
}
```

---

## 附录 B: 快速参考

### B.1 核心 API 速查表

```java
// === 初始化 ===
IkuaiRouter router = new IkuaiRouter("192.168.1.1", 80, false, "admin", "admin");

// === DHCP 管理 ===
router.getDHCPHostList();             // 所有动态租约
router.getDHCPStaticList();           // 所有静态绑定
router.addDHCPStatic(new DHCPStatic(...));           // 新增
router.editDHCPStatic(dhcpStatic);                   // 编辑
router.delDHCPStaticById(id);                        // 删除
router.delDHCPStaticByIpAddr("192.168.1.100");       // 按IP删除
router.delDHCPStaticByMac("aa:bb:cc:dd:ee:ff");     // 按MAC删除

// === 端口映射 ===
router.getNetMappingList();                           // 所有规则
router.addNetMapping(new NetMapping(...));             // 新增
router.editNetMapping(netMapping);                    // 编辑
router.delNetMappingById(id);                         // 删除单条
router.delNetMappingByIpAddr("192.168.1.100");        // 批量删除
router.isWanPortInUse("wan1", 8080);                  // 端口占用检测
router.findAvailableNetMappingWanPort("wan1", 30000, 31000);  // 查找可用端口

// === QoS 限速 ===
router.getQosLimitByIpAddr("192.168.1.100");          // 查询
router.addQosLimit(new QosLimit(...));                 // 新增
router.editQosLimit(qosLimit);                        // 编辑
router.delQosLimitByIpAddr("192.168.1.100");          // 删除

// === 系统信息 ===
router.getSystemStatus();      // 系统状态
router.getInterfaceWanList();  // WAN 接口
router.getInterfaceLanList();  // LAN 接口
router.getInterfaceCheckList(); // 线路检测
router.getInterfaceStreamList(); // 流量统计
router.getLanHostInfoList();   // 在线主机
router.getLanHostInfoByIpAddr("192.168.1.100"); // 按IP查主机
router.getLanHostInfoByMAC("aa:bb:cc:dd:ee:ff"); // 按MAC查主机
```

### B.2 IP 工具速查

```java
IpAddrUtil.isIpVaild("192.168.1.1");               // true
IpAddrUtil.parseIpFromStringToLong("192.168.1.1"); // 3232235777L
IpAddrUtil.parseIpFromLongToString(3232235777L);   // "192.168.1.1"
IpAddrUtil.isIpInRange("192.168.1.100", "192.168.1.0/24");  // true
IpAddrUtil.getBeginIpStr("192.168.1.100", 24);     // "192.168.1.0"
IpAddrUtil.getEndIpStr("192.168.1.100", 24);       // "192.168.1.255"
IpAddrUtil.parseMaskBitToMask(24);                  // "255.255.255.0"
IpAddrUtil.getNextNIp("192.168.1.255", 1);          // "192.168.2.0" (跨段进位)
```
