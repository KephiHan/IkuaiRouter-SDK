package net.dabaiyun.ikuairouter;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.dabaiyun.ikuairouter.Action.*;
import net.dabaiyun.ikuairouter.Entity.*;
import net.dabaiyun.ikuairouter.Entity.sysstat.SystemStatus;
import net.dabaiyun.ikuairouter.Exception.IkuaiRouterException;
import net.dabaiyun.ikuairouter.Log.ConsoleIkuaiLogger;
import net.dabaiyun.ikuairouter.Log.IkuaiLogger;
import net.dabaiyun.ikuairouter.Util.IpAddrUtil;
import okhttp3.Cookie;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class IkuaiRouter {
    //Tools
    private static final ObjectMapper objectMapper = new ObjectMapper();

    //Agent
    private RouterAgent routerAgent;

    //Status
    private boolean isLogin = false;
    private boolean debug = false;

    //Concurrency
    private final Object portLock = new Object();

    //Logger
    private IkuaiLogger logger = new ConsoleIkuaiLogger();

    //Construcs

    public IkuaiRouter(String address, int port, boolean https, String username, String pwd) throws Exception {
        this.routerAgent = new RouterAgent(address, port, https, username, pwd);
        this.login();
    }

//    public IkuaiRouter(String address, int port, boolean https, String username, String pwd, HashMap<String, List<Cookie>> cookieStore) throws Exception {
//        this.routerAgent = new RouterAgent(address, port, https, username, pwd, cookieStore);
//        this.login();
//    }

    public IkuaiRouter(String address, int port, boolean https, String username, String pwd, boolean doLogin) throws Exception {
        this.routerAgent = new RouterAgent(address, port, https, username, pwd);
        if (doLogin) {
            this.login();
        }
    }

    public IkuaiRouter(String address, int port, boolean https, String username, String pwd, String sess_key) {
        this.routerAgent = new RouterAgent(address, port, https, username, pwd, sess_key);
    }

    public RouterAgent getRouterAgent() {
        return routerAgent;
    }

    public void setRouterAgent(RouterAgent routerAgent) {
        this.routerAgent = routerAgent;
    }

    public boolean isLogin() {
        return isLogin;
    }

    public void setLogin(boolean login) {
        isLogin = login;
    }

    public Map<String, List<Cookie>> getCookieStore() {
        return routerAgent.getCookieStore();
    }

    public boolean isDebug() {
        return debug;
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    /**
     * 设置自定义日志实现
     * 默认使用 ConsoleIkuaiLogger（stdout 输出）
     * 生产环境建议注入 SLF4J 桥接实现
     *
     * @param logger 日志实现
     */
    public void setLogger(IkuaiLogger logger) {
        this.logger = logger;
    }

    public IkuaiLogger getLogger() {
        return logger;
    }

    /**
     * 主动清除内存中的密码，释放敏感数据
     * 调用后此实例不可再次 login
     */
    public void destroy() {
        routerAgent.destroy();
    }

//================ Other Functions ==========================


    public String getPlugins() throws Exception {
        ResponseShow responseShow = routerAgent.getPlugins();
        return responseShow.getData();
    }

    public boolean checkSessKeyValid() {
        try {
            routerAgent.getPlugins();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    //=========== Base Functions ====================

    /**
     * Make Agent Login
     *
     * @return is login seccess?
     * @throws Exception ex
     */
    public boolean login() throws Exception {
        this.isLogin = routerAgent.login().isSuccess();
        return this.isLogin;
    }

    public String getSessKey() {
        return routerAgent.getSessKey();
    }

    //Custom Functions

    /**
     * 查询公网端口是否被使用
     *
     * @param inter_face WAN接口
     * @param wanPort    端口
     * @return 是否被使用
     * @throws Exception ex
     */
    public boolean isWanPortInUse(String inter_face, int wanPort) throws Exception {
        List<NetMapping> netMappingList = this.getNetMappingList();
        Set<Integer> usedPorts = parseUsedPorts(netMappingList, Arrays.asList(inter_face));
        return usedPorts.contains(wanPort);
    }

    /**
     * 查询公网端口是否被使用(多接口)
     *
     * @param interfaceList WAN接口
     * @param wanPort       端口
     * @return 是否被使用
     * @throws Exception ex
     */
    public boolean isWanPortInUseMultiInterface(List<String> interfaceList, int wanPort) throws Exception {
        List<NetMapping> netMappingList = this.getNetMappingList();
        Set<Integer> usedPorts = parseUsedPorts(netMappingList, interfaceList);
        return usedPorts.contains(wanPort);
    }

    /**
     * 查找可用公网端口
     *
     * @param inter_face 上行接口
     * @param portbegin  起始端口
     * @param portend    结束端口
     * @return int          找到的端口
     * @throws Exception 找不到可用端口
     */
    public int findAvailableNetMappingWanPort(String inter_face, int portbegin, int portend) throws Exception {
        synchronized (portLock) {
            if (portbegin <= 0) portbegin = 1;
            if (portend > 65535) portend = 65535;
            List<NetMapping> netMappingList = this.getNetMappingList();
            Set<Integer> usedPorts = parseUsedPorts(netMappingList, Arrays.asList(inter_face));
            for (int port = portbegin; port <= portend; port++) {
                if (!usedPorts.contains(port)) {
                    return port;
                }
            }
            throw new IkuaiRouterException("No available port found.");
        }
    }

    /**
     * 查找可用公网端口(多接口)
     *
     * @param toUseInterfaceList 上行接口列表
     * @param portbegin          起始端口
     * @param portend            结束端口
     * @return int          找到的端口
     * @throws Exception 找不到可用端口
     */
    public int findAvailableNetMappingWanPortMultiInterface(List<String> toUseInterfaceList, int portbegin, int portend) throws Exception {
        synchronized (portLock) {
            if (portbegin <= 0) portbegin = 1;
            if (portend > 65535) portend = 65535;
            List<NetMapping> netMappingList = this.getNetMappingList();
            Set<Integer> usedPorts = parseUsedPorts(netMappingList, toUseInterfaceList);
            for (int port = portbegin; port <= portend; port++) {
                if (!usedPorts.contains(port)) {
                    return port;
                }
            }
            throw new IkuaiRouterException("No available port found.");
        }
    }

    /**
     * 查找可用端口区间
     *
     * @return port regon begin
     */
    public int findAvailableNetMappingPortRegionBegin(int portbegin, int portend, int protnumeachvps) throws Exception {
        //获取所有端口映射表
        List<NetMapping> netMappingList = this.getNetMappingList();
        //便利所有可用区间的起始点
        for (
                int newportbegin = portbegin;
                newportbegin < portend;
                newportbegin += protnumeachvps
        ) {
            boolean available = true;
            //遍历所有已存在的端口映射表
            for (NetMapping netMapping : netMappingList) {
//                System.out.println("正在检查端口映射：" + netMapping.toString());
                //分割端口区段文本
                String[] ports = netMapping.getWan_port().split("-");
//                System.out.println("分割后的文本串： " + String.valueOf(ports));
                //首先NATMapping记录里的port要大于等于30000
                if (Integer.parseInt(ports[0]) >= portbegin) {
                    //如果当前端口已存在记录，则直接跳到下一个端口
                    if (ports[0].equals(String.valueOf(newportbegin))) {
//                        System.out.println("端口:" + newportbegin + "已存在记录，跳过");
                        available = false;
                        break;
                    }
                }
//                else{
//                    System.out.println("端口映射记录不符合区间要求，跳过： " + ports[0]);
//                }
            }
            if (available) {
                return newportbegin;
            }
        }
        throw new IkuaiRouterException("Can not find Available natmapping port");
    }


    /**
     * 需按照一个没有呗使用的ip
     *
     * @param gateway             网关，用于确定ip地址段
     * @param netmaskBit          掩码，用于确定ip范围
     * @param reserveIpStartCount 首部预留ip数量
     * @param reserveIpEndCount   尾部预留ip数量
     * @return 可用的ip
     */
    public String findAvailableIpAddr(String gateway, int netmaskBit, int reserveIpStartCount, int reserveIpEndCount) throws Exception {
        String ip_range_begin = IpAddrUtil.getBeginIpStr(gateway, netmaskBit);
        String ip_range_end = IpAddrUtil.getEndIpStr(gateway, netmaskBit);
        String ip_find_start = IpAddrUtil.getNextNIp(ip_range_begin, reserveIpStartCount);
        String ip_find_end = IpAddrUtil.getPreviousNIp(ip_range_end, reserveIpEndCount);
        return this.findAvailableIpAddr(gateway, netmaskBit, ip_find_start, ip_find_end);
    }

    /**
     * 需按照一个没有呗使用的ip
     *
     * @param gateway    网关，用于确定ip地址段
     * @param netmaskBit 掩码，用于确定ip范围
     * @param ip_begin   查找起始ip
     * @param ip_end     查找结束ip
     * @return 可用的ip
     * @throws Exception e
     */
    public String findAvailableIpAddr(String gateway, int netmaskBit, String ip_begin, String ip_end) throws Exception {
        if (!IpAddrUtil.isIpValid(gateway)) {
            throw new IkuaiRouterException("gateway " + gateway + " invaild");
        }
        if (!IpAddrUtil.isIpValid(ip_begin)) {
            throw new IkuaiRouterException("ip_begin " + ip_begin + " invaild");
        }
        if (!IpAddrUtil.isIpValid(ip_end)) {
            throw new IkuaiRouterException("ip_end " + ip_end + " invaild");
        }
        if (!IpAddrUtil.isMaskBitVaild(netmaskBit)) {
            throw new IkuaiRouterException("netmaskBit " + netmaskBit + " invaild");
        }
        if (!IpAddrUtil.isIpInRange(ip_begin, gateway + "/" + netmaskBit)) {
            throw new IkuaiRouterException("ip_begin " + ip_begin + " not in CIDR:  " + gateway + "/" + netmaskBit);
        }
        if (!IpAddrUtil.isIpInRange(ip_end, gateway + "/" + netmaskBit)) {
            throw new IkuaiRouterException("ip_end " + ip_end + " not in CIDR:  " + gateway + "/" + netmaskBit);
        }
        //获取当前系统已存在配置，预建已用IP集合
        List<DHCPHost> dhcpHostList = this.getDHCPHostList();
        List<DHCPStatic> dhcpStaticList = this.getDHCPStaticList();
        Set<String> usedIps = new HashSet<>();
        for (DHCPHost dhcpHost : dhcpHostList) {
            usedIps.add(dhcpHost.getIp_addr());
        }
        for (DHCPStatic dhcpStatic : dhcpStaticList) {
            usedIps.add(dhcpStatic.getIp_addr());
        }
        //把ip分成4段
        String[] ipfromd = ip_begin.split("\\.");
        String[] iptod = ip_end.split("\\.");
        int[] int_ipf = new int[4];
        int[] int_ipt = new int[4];
        for (int i = 0; i < 4; i++) {
            int_ipf[i] = Integer.parseInt(ipfromd[i]);
            int_ipt[i] = Integer.parseInt(iptod[i]);
        }
        //循环遍历每个ip
        for (int A = int_ipf[0]; A <= int_ipt[0]; A++) {
            for (int B = (A == int_ipf[0] ? int_ipf[1] : 0); B <= (A == int_ipt[0] ? int_ipt[1] : 255); B++) {
                for (int C = (B == int_ipf[1] ? int_ipf[2] : 0); C <= (B == int_ipt[1] ? int_ipt[2] : 255); C++) {
                    for (int D = (C == int_ipf[2] ? int_ipf[3] : 0); D <= (C == int_ipt[2] ? int_ipt[3] : 255); D++) {
                        String current_ip = A + "." + B + "." + C + "." + D;
                        if (!usedIps.contains(current_ip)) {
                            return current_ip;
                        }
                    }
                }
            }
        }
        throw new IkuaiRouterException("No Available IpAddr");
    }

    //================ Getter Functions ==========================

    /**
     * Get SystemStatus
     *
     * @return SystemStatus
     * @throws Exception e
     */
    public SystemStatus getSystemStatus() throws Exception {
        return parseData(
                routerAgent.getSystemStatus(),
                "sysstat",
                new TypeReference<SystemStatus>() {
                }
        );
    }

    /**
     * Get LanHostInfo Object By ipv4 String
     *
     * @param ip_addr 192.168.xxx.xxx
     * @return Macthing Object
     * @throws Exception e
     */
    public LanHostInfo getLanHostInfoByIpAddr(String ip_addr) throws Exception {
        for (LanHostInfo o : this.getLanHostInfoList()) {
            if (o.getIp_addr().equals(ip_addr)) {
                return o;
            }
        }
        return null;
    }

    /**
     * Get LanHostInfo Object By MAC Address String
     *
     * @param mac MAC Address (hex)
     * @return Macthing Object
     */
    public LanHostInfo getLanHostInfoByMAC(String mac) throws Exception {
        for (LanHostInfo o : this.getLanHostInfoList()) {
            if (o.getMac().equals(mac.toLowerCase())) {
                return o;
            }
        }
        return null;
    }

    /**
     * Get DHCPServer Object By Interface String
     *
     * @param inter_face Interface String
     * @return Macthing Object
     */
    public DHCPServer getDHCPServerByInterface(String inter_face) throws Exception {
        for (DHCPServer dhcpServer : this.getDHCPServerList()) {
            if (dhcpServer.getInter_face().equals(inter_face)) {
                return dhcpServer;
            }
        }
        return null;
    }

    /**
     * Get DHCPStatic Object By Id
     *
     * @param id Interface String
     * @return Macthing Object
     */
    public DHCPStatic getDHCPStaticById(int id) throws Exception {
        List<DHCPStatic> dhcpStaticList = parseData(
                routerAgent.getDHCPStaticsById(String.valueOf(id)),
                "static_data",
                new TypeReference<List<DHCPStatic>>() {
                }
        );
        for (DHCPStatic dhcpStatic : dhcpStaticList) {
            if (dhcpStatic.getId() == id) {
                return dhcpStatic;
            }
        }
        return null;
    }

    /**
     * Get DHCPStatic Object By ip_addr
     *
     * @param ip_addr ipv4 String
     * @return Macthing Object
     */
    public DHCPStatic getDHCPStaticByIpAddr(String ip_addr) throws Exception {
        List<DHCPStatic> dhcpStaticList = parseData(
                routerAgent.getDHCPStaticsByIpAddr(ip_addr),
                "static_data",
                new TypeReference<List<DHCPStatic>>() {
                }
        );
        for (DHCPStatic dhcpStatic : dhcpStaticList) {
            if (dhcpStatic.getIp_addr().equals(ip_addr)) {
                return dhcpStatic;
            }
        }
        return null;
    }

    /**
     * Get DHCPStatic Object By MAC
     *
     * @param mac Mac String
     * @return Macthing Object
     */
    public DHCPStatic getDHCPStaticByMAC(String mac) throws Exception {
        List<DHCPStatic> dhcpStaticList = parseData(
                routerAgent.getDHCPStaticsByMac(mac),
                "static_data",
                new TypeReference<List<DHCPStatic>>() {
                }
        );
        ;
        for (DHCPStatic dhcpStatic : dhcpStaticList) {
            if (dhcpStatic.getMac().equals(mac)) {
                return dhcpStatic;
            }
        }
        return null;
    }

    /**
     * Get QosLimit Object By ID
     *
     * @param id Target ID
     * @return Macthing Object
     */
    public QosLimit getQosLimitById(int id) throws Exception {
        List<QosLimit> qosLimitList = parseData(
                routerAgent.getQosLimitById(String.valueOf(id)),
                "data",
                new TypeReference<List<QosLimit>>() {
                }
        );
        for (QosLimit qosLimit : qosLimitList) {
            if (qosLimit.getId() == id) {
                return qosLimit;
            }
        }
        return null;
    }

    /**
     * Get QosLimit Object By ip
     *
     * @param ip_addr ipv4 String
     * @return Macthing Object
     */
    public QosLimit getQosLimitByIpAddr(String ip_addr) throws Exception {
        List<QosLimit> qosLimitList = parseData(
                routerAgent.getQosLimitByIpAddr(ip_addr),
                "data",
                new TypeReference<List<QosLimit>>() {
                }
        );
        for (QosLimit qosLimit : qosLimitList) {
            if (qosLimit.getIp_addr().equals(ip_addr)) {
                return qosLimit;
            }
        }
        return null;
    }

    /**
     * Get NetMapping By ID
     *
     * @param id Target Id
     * @return NetMapping Object
     * @throws Exception e
     */
    public NetMapping getNetMappingById(int id) throws Exception {
        List<NetMapping> netMappingList = parseData(
                routerAgent.getNetMappingById(String.valueOf(id)),
                "data",
                new TypeReference<List<NetMapping>>() {
                }
        );
        for (NetMapping netMapping : netMappingList) {
            if (netMapping.getId() == id) {
                return netMapping;
            }
        }
        return null;
    }

    /**
     * Get NetMappingList By IpAddr
     *
     * @param ip_addr IpAddr
     * @return Matching Object List
     * @throws Exception E
     */
    public List<NetMapping> getNetMappingListByIpAddr(String ip_addr) throws Exception {
        List<NetMapping> netMappingList = parseData(
                routerAgent.getNetMappingByIpAddr(ip_addr),
                "data",
                new TypeReference<List<NetMapping>>() {
                }
        );
        List<NetMapping> matchList = new ArrayList<>();
        for (NetMapping netMapping : netMappingList) {
            if (netMapping.getLan_addr().equals(ip_addr)) {
                matchList.add(netMapping);
            }
        }
        return matchList;
    }

    /**
     * Get NetMapping By wanport
     *
     * @param wanport Target wanport
     * @return NetMapping Object
     * @throws Exception e
     */
    public NetMapping getNetMappingByInterfaceAndWanPort(String inter_face, String wanport) throws Exception {
        List<NetMapping> netMappingList = parseData(
                routerAgent.getNetMappingByWanPort(wanport),
                "data",
                new TypeReference<List<NetMapping>>() {
                }
        );
        for (NetMapping netMapping : netMappingList) {
            if (netMapping.getWan_port().equals(wanport) && netMapping.getInter_face().equals(inter_face)) {
                return netMapping;
            }
        }
        return null;
    }

    /**
     * Get LanHostInfoList Object
     *
     * @return LanHostInfo List
     * @throws Exception ex
     */
    public List<LanHostInfo> getLanHostInfoList() throws Exception {
        return parseData(
                routerAgent.getLanHostStatus(),
                "data",
                new TypeReference<List<LanHostInfo>>() {
                }
        );
    }

    /**
     * 分页获取全量 LanHostInfo 列表
     * 通过 TYPE="data,total" 获取总数，循环翻页直到取完
     *
     * @param pageSize 每页数量
     * @return 全量 LanHostInfo List
     * @throws Exception ex
     */
    public List<LanHostInfo> getAllLanHostInfoList(int pageSize) throws Exception {
        List<LanHostInfo> allData = new ArrayList<>();
        int offset = 0;
        int total;
        do {
            RequestParamShow param = new RequestParamShow("data,total", offset + "," + pageSize);
            ResponseShow responseShow = routerAgent.getLanHostStatus(param);
            total = getTotal(responseShow);
            List<LanHostInfo> page = parseData(responseShow, "data", new TypeReference<List<LanHostInfo>>() {});
            allData.addAll(page);
            offset += pageSize;
        } while (offset < total);
        return allData;
    }

    /**
     * Get InterfaceLanList Object
     *
     * @return InterfaceLan List
     * @throws Exception ex
     */
    public List<InterfaceLan> getInterfaceLanList() throws Exception {
        return parseData(
                routerAgent.getInterfaceSnapshoot(),
                "snapshoot_lan",
                new TypeReference<List<InterfaceLan>>() {
                }
        );
    }

    /**
     * Get InterfaceWanList Object
     *
     * @return InterfaceWan List
     * @throws Exception ex
     */
    public List<InterfaceWan> getInterfaceWanList() throws Exception {
        return parseData(
                routerAgent.getInterfaceSnapshoot(),
                "snapshoot_wan",
                new TypeReference<List<InterfaceWan>>() {
                }
        );
    }

    /**
     * Get DHCPServerList Object
     *
     * @return DHCPServer List
     * @throws Exception ex
     */
    public List<DHCPServer> getDHCPServerList() throws Exception {
        return parseData(
                routerAgent.getDHCPServers(),
                "data",
                new TypeReference<List<DHCPServer>>() {
                }
        );
    }

    /**
     * Get DHCPStaticList Object
     *
     * @return DHCPStatic List
     * @throws Exception ex
     */
    public List<DHCPStatic> getDHCPStaticList() throws Exception {
        return parseData(
                routerAgent.getDHCPStatics(),
                "static_data",
                new TypeReference<List<DHCPStatic>>() {
                }
        );
    }

    /**
     * Get getDHCPHostList
     *
     * @return DHCPHost List
     * @throws Exception ex
     */
    public List<DHCPHost> getDHCPHostList() throws Exception {
        return parseData(
                routerAgent.getDHCPHosts(),
                "data",
                new TypeReference<List<DHCPHost>>() {
                }
        );
    }

    /**
     * Get NetMappingList
     *
     * @return NetMapping List
     * @throws Exception ex
     */
    public List<NetMapping> getNetMappingList() throws Exception {
        return parseData(
                routerAgent.getNetMapping(),
                "data",
                new TypeReference<List<NetMapping>>() {
                }
        );
    }

    /**
     * Get InterfaceCheckList
     *
     * @return InterfaceCheckList
     * @throws Exception e
     */
    public List<InterfaceCheck> getInterfaceCheckList() throws Exception {
        return parseData(
                routerAgent.getInterfaceCheckList(),
                "iface_check",
                new TypeReference<List<InterfaceCheck>>() {
                }
        );
    }

    /**
     * Get InterfaceSteamList
     *
     * @return InterfaceSteamList
     * @throws Exception e
     */
    public List<InterfaceStream> getInterfaceStreamList() throws Exception {
        return parseData(
                routerAgent.getInterfaceStreamList(),
                "iface_stream",
                new TypeReference<List<InterfaceStream>>() {
                }
        );
    }

    //================ Adder Functions ==========================

    /**
     * Add DHCPStatic by DHCPStatic Object
     *
     * @param dhcpStatic DHCPStatic Object
     * @return NewRowId
     * @throws Exception ErrMsg
     */
    public Integer addDHCPStatic(DHCPStatic dhcpStatic) throws Exception {
        ResponseAdd responseAdd = routerAgent.addDHCPStatic(dhcpStatic);
        if (responseAdd.isSuccess()) {
            dhcpStatic.setId(responseAdd.getRowId());
            return responseAdd.getRowId();
        } else {
            throw new IkuaiRouterException(responseAdd.getResult() + " " + responseAdd.getErrMsg());
        }
    }

    /**
     * Add QosLimit
     *
     * @param qosLimit QosLimit Object
     * @return NewRowId
     * @throws Exception ErrMsg
     */
    public Integer addQosLimit(QosLimit qosLimit) throws Exception {
        ResponseAdd responseAdd = routerAgent.addQosLimit(qosLimit);
        if (responseAdd.isSuccess()) {
            qosLimit.setId(responseAdd.getRowId());
            return responseAdd.getRowId();
        } else {
            throw new IkuaiRouterException(responseAdd.getResult() + " " + responseAdd.getErrMsg());
        }
    }

    /**
     * Add NetMapping
     *
     * @param netMapping NetMapping Object
     * @return NewRowId
     * @throws Exception ErrMsg
     */
    public Integer addNetMapping(NetMapping netMapping) throws Exception {
        synchronized (portLock) {
            ResponseAdd responseAdd = routerAgent.addNetMapping(netMapping);
            if (responseAdd.isSuccess()) {
                netMapping.setId(responseAdd.getRowId());
                return responseAdd.getRowId();
            } else {
                throw new IkuaiRouterException(responseAdd.getResult() + " " + responseAdd.getErrMsg());
            }
        }
    }


    /**
     * 查找可用端口并立即创建映射（原子操作）
     * 注意：此同步仅保证单 JVM 实例内的原子性，多 JVM 场景需上层分布式锁
     *
     * @param inter_face 上行接口
     * @param portbegin  起始端口
     * @param portend    结束端口
     * @param template   NetMapping 模板（wan_port 将被自动设置）
     * @return 新行 ID
     * @throws Exception 找不到可用端口或添加失败
     */
    public Integer findAndAddNetMapping(String inter_face, int portbegin, int portend, NetMapping template) throws Exception {
        synchronized (portLock) {
            int port = findAvailableNetMappingWanPort(inter_face, portbegin, portend);
            template.setWan_port(String.valueOf(port));
            template.setInter_face(inter_face);
            ResponseAdd responseAdd = routerAgent.addNetMapping(template);
            if (responseAdd.isSuccess()) {
                template.setId(responseAdd.getRowId());
                return responseAdd.getRowId();
            } else {
                throw new IkuaiRouterException(responseAdd.getResult() + " " + responseAdd.getErrMsg());
            }
        }
    }

    /**
     * 查找可用端口并立即创建映射 - 多接口版本（原子操作）
     * 在多个接口上查找未被占用的端口，找到后立即创建映射
     * 注意：此同步仅保证单 JVM 实例内的原子性，多 JVM 场景需上层分布式锁
     *
     * @param interfaceList 上行接口列表
     * @param portbegin     起始端口
     * @param portend       结束端口
     * @param template      NetMapping 模板（wan_port 和 inter_face 将被自动设置）
     turn 新行 ID
     * @throws Exception 找不到可用端口或添加失败
     */
    public Integer findAndAddNetMappingMultiInterface(List<String> interfaceList, int portbegin, int portend, NetMapping template) throws Exception {
        synchronized (portLock) {
            int port = findAvailableNetMappingWanPortMultiInterface(interfaceList, portbegin, portend);
            template.setWan_port(String.valueOf(port));
            template.setInter_face(String.join(",", interfaceList));
            ResponseAdd responseAdd = routerAgent.addNetMapping(template);
            if (responseAdd.isSuccess()) {
                template.setId(responseAdd.getRowId());
                return responseAdd.getRowId();
            } else {
                throw new IkuaiRouterException(responseAdd.getResult() + " " + responseAdd.getErrMsg());
            }
        }
    }


    //================ Editer Functions ==========================

//    /**
//     * Edit DHCPStatic by MAC
//     *
//     * @param mac           Target MAC
//     * @param newDhcpStatic New DHCPStatic Object
//     * @return Add seccess? throw ErrMsg
//     * @throws Exception ErrMsg
//     */
//    public boolean editDHCPStaticByMac(String mac, DHCPStatic newDhcpStatic) throws Exception {
//        //Find dhcpStatic Object by mac then set it id to new Object
//        newDhcpStatic.setId(this.getDHCPStaticByMAC(mac).getId());
//        IkuaiResponseBase response = routerAgent.editDHCPStatic(newDhcpStatic);
//        return response.isSuccess();
//    }

//    /**
//     * Edit DHCPStatic by ipv4
//     *
//     * @param ip_addr       Target ipv4
//     * @param newDhcpStatic New DHCPStatic Object
//     * @return Add seccess? throw ErrMsg
//     * @throws Exception ErrMsg
//     */
//    public boolean editDHCPStaticByIpAddr(String ip_addr, DHCPStatic newDhcpStatic) throws Exception {
//        newDhcpStatic.setId(this.getDHCPStaticByIpAddr(ip_addr).getId());
//        IkuaiResponseBase response = routerAgent.editDHCPStatic(newDhcpStatic);
//        return response.isSuccess();
//    }

//    /**
//     * Edit DHCPStatic by ID
//     *
//     * @param id            Target id
//     * @param newDhcpStatic New DHCPStatic Object
//     * @return Add seccess? throw ErrMsg
//     * @throws Exception ErrMsg
//     */
//    public boolean editDHCPStaticById(int id, DHCPStatic newDhcpStatic) throws Exception {
//        newDhcpStatic.setId(id);
//        IkuaiResponseBase response = routerAgent.editDHCPStatic(newDhcpStatic);
//        if (response.isSeccess()) {
//            return true;
//        } else {
//            throw new IkuaiRouterException(response.getResult() + " " + response.getErrMsg());
//        }
//    }

    /**
     * Edit DHCPStatic
     *
     * @param dhcpStatic New DHCPStatic Object
     * @return Add seccess? throw ErrMsg
     * @throws Exception ErrMsg
     */
    public boolean editDHCPStatic(DHCPStatic dhcpStatic) throws Exception {
        IkuaiResponseBase response = routerAgent.editDHCPStatic(dhcpStatic);
        return response.isSuccess();
    }

//    /**
//     * Edit QosLimit by ip_addr
//     *
//     * @param ip_addr  Target ip_addr
//     * @param qosLimit New QosLimit Object
//     * @return Add seccess? throw ErrMsg
//     * @throws Exception ErrMsg
//     */
//    public boolean editQosLimitByIpAddr(String ip_addr, QosLimit qosLimit) throws Exception {
//        qosLimit.setId(this.getQosLimitByIpAddr(ip_addr).getId());
//        IkuaiResponseBase response = routerAgent.editQosLimit(qosLimit);
//        return response.isSuccess();
//    }

    /**
     * Edit QosLimit
     *
     * @param qosLimit New QosLimit Object
     * @return Add seccess? throw ErrMsg
     * @throws Exception ErrMsg
     */
    public boolean editQosLimit(QosLimit qosLimit) throws Exception {
        IkuaiResponseBase response = routerAgent.editQosLimit(qosLimit);
        return response.isSuccess();
    }

//    /**
//     * Edit NetMapping By Interface And Wanport
//     *
//     * @param inter_face Interface
//     * @param wanport    Wanport
//     * @param netMapping New NetMapping Object
//     * @return Edit seccess? throw ErrMsg
//     * @throws Exception e
//     */
//    public boolean editNetMappingByInterfaceAndWanport(String inter_face, String wanport, NetMapping netMapping) throws Exception {
//        netMapping.setId(
//                this.getNetMappingByInterfaceAndWanPort(
//                        inter_face,
//                        wanport).getId()
//        );
//        IkuaiResponseBase response = routerAgent.editNetMapping(netMapping);
//        return response.isSuccess();
//    }

    /**
     * Edit NetMapping
     *
     * @param netMapping New NetMapping Object
     * @return Add seccess? throw ErrMsg
     * @throws Exception ErrMsg
     */
    public boolean editNetMapping(NetMapping netMapping) throws Exception {
        IkuaiResponseBase response = routerAgent.editNetMapping(netMapping);
        return response.isSuccess();
    }

    //================ Downer Functions ==========================

    /**
     * Down DHCPStatic By MAC
     *
     * @param mac Target MAC
     * @return Add seccess? throw ErrMsg
     * @throws Exception ErrMsg
     */
    public boolean downDHCPStaticByMac(String mac) throws Exception {
        IkuaiResponseBase response =
                routerAgent.downDHCPStatic(
                        this.getDHCPStaticByMAC(mac).getId()
                );
        return response.isSuccess();
    }

    /**
     * Down DHCPStatic By ipv4
     *
     * @param ip_addr Target Ip
     * @return Add seccess? throw ErrMsg
     * @throws Exception ErrMsg
     */
    public boolean downDHCPStaticByIpAddr(String ip_addr) throws Exception {
        IkuaiResponseBase response =
                routerAgent.downDHCPStatic(
                        this.getDHCPStaticByIpAddr(ip_addr).getId()
                );
        return response.isSuccess();
    }

    /**
     * Down DHCPStatic By id
     *
     * @param id Target id
     * @return Add seccess? throw ErrMsg
     * @throws Exception ErrMsg
     */
    public boolean downDHCPStaticById(int id) throws Exception {
        IkuaiResponseBase response = routerAgent.downDHCPStatic(id);
        return response.isSuccess();
    }

    /**
     * Down QosLimit By id
     *
     * @param id Target id
     * @return Down seccess? throw ErrMsg
     * @throws Exception ErrMsg
     */
    public boolean downQosLimitById(int id) throws Exception {
        IkuaiResponseBase response = routerAgent.downQosLimit(id);
        return response.isSuccess();
    }

    /**
     * Down QosLimit by target ip_addr
     *
     * @param ip_addr ip_addr
     * @return Down seccess? throw ErrMsg
     * @throws Exception e
     */
    public boolean downQosLimitByIpAddr(String ip_addr) throws Exception {
        IkuaiResponseBase response =
                routerAgent.downQosLimit(
                        this.getQosLimitByIpAddr(ip_addr).getId()
                );
        return response.isSuccess();
    }

    /**
     * Down NetMapping By Id
     *
     * @param id ID
     * @return Down seccess? throw ErrMsg
     * @throws Exception e
     */
    public boolean downNetMappingById(int id) throws Exception {
        IkuaiResponseBase response = routerAgent.downNetMapping(id);
        return response.isSuccess();
    }

    /**
     * Down NetMapping By Interface And Wanport
     *
     * @param inter_face Interface
     * @param wanport    Wanport
     * @return Down seccess? throw ErrMsg
     * @throws Exception e
     */
    public boolean downNetMappingByInterfaceAndWanport(String inter_face, String wanport) throws Exception {
        IkuaiResponseBase response = routerAgent.downNetMapping(
                this.getNetMappingByInterfaceAndWanPort(inter_face, wanport).getId()
        );
        return response.isSuccess();
    }

    /**
     * Down NetMapping By LanIp
     * More than one rules can be set to same lan_ip
     *
     * @param lanip LanIp
     * @return Down seccess? throw ErrMsg
     * @throws Exception
     */
    public boolean downNetMappingByLanIp(String lanip) throws Exception {
        List<String> errors = new ArrayList<>();
        for (NetMapping netMapping : this.getNetMappingListByIpAddr(lanip)) {
            try {
                IkuaiResponseBase response = routerAgent.downNetMapping(netMapping.getId());
                if (!response.isSuccess()) {
                    errors.add("id=" + netMapping.getId() + ": " + response.getResult() + " " + response.getErrMsg());
                }
            } catch (Exception e) {
                errors.add("id=" + netMapping.getId() + ": " + e.getMessage());
            }
        }
        if (!errors.isEmpty()) {
            throw new IkuaiRouterException("Partial failure in downNetMappingByLanIp: " + String.join("; ", errors));
        }
        return true;
    }

    //================ Deleter Functions ==========================

    /**
     * Delete DHCPStatic By id
     *
     * @param id Target id
     * @return Delete seccess? throw ErrMsg
     * @throws Exception ErrMsg
     */
    public boolean delDHCPStaticById(int id) throws Exception {
        IkuaiResponseBase response =
                routerAgent.delDHCPStatic(id);
        return response.isSuccess();
    }

    /**
     * Delete DHCPStatic By ip_addr
     *
     * @param ip_addr Target ip_addr
     * @return Delete seccess? throw ErrMsg
     * @throws Exception ErrMsg
     */
    public boolean delDHCPStaticByIpAddr(String ip_addr) throws Exception {
        DHCPStatic dhcpStatic = this.getDHCPStaticByIpAddr(ip_addr);
        if (dhcpStatic == null) {
            return true;
        }
        IkuaiResponseBase response =
                routerAgent.delDHCPStatic(
                        dhcpStatic.getId()
                );
        return response.isSuccess();
    }

    /**
     * Delete DHCPStatic By mac
     *
     * @param mac Target mac
     * @return Delete seccess? throw ErrMsg
     * @throws Exception ErrMsg
     */
    public boolean delDHCPStaticByMac(String mac) throws Exception {
        DHCPStatic dhcpStatic = this.getDHCPStaticByMAC(mac);
        if (dhcpStatic == null) {
            return true;
        }
        IkuaiResponseBase response =
                routerAgent.delDHCPStatic(
                        dhcpStatic.getId()
                );
        return response.isSuccess();
    }

    /**
     * Delete QosLimit By id
     *
     * @param id Target id
     * @return Delete seccess? throw ErrMsg
     * @throws Exception ErrMsg
     */
    public boolean delQosLimitById(int id) throws Exception {
        IkuaiResponseBase response = routerAgent.delQosLimit(id);
        return response.isSuccess();
    }

    /**
     * Delete QosLimit By IpAddr
     *
     * @param ip_addr Target IpAddr
     * @return Delete seccess? throw ErrMsg
     * @throws Exception ErrMsg
     */
    public boolean delQosLimitByIpAddr(String ip_addr) throws Exception {
        QosLimit qosLimit = this.getQosLimitByIpAddr(ip_addr);
        if (qosLimit == null) {
            return true;
        }
        IkuaiResponseBase response =
                routerAgent.delQosLimit(qosLimit.getId());
        return response.isSuccess();
    }

    /**
     * delNetMappingById
     *
     * @param id ID
     * @return Delete seccess? throw ErrMsg
     * @throws Exception e
     */
    public boolean delNetMappingById(int id) throws Exception {
        IkuaiResponseBase response = routerAgent.delNetMapping(id);
        return response.isSuccess();
    }

    /**
     * Delete NetMapping By Interface And Wanport
     *
     * @param inter_face Interface
     * @param wanport    Wanport
     * @return Delete seccess? throw ErrMsg
     * @throws Exception e
     */
    public boolean delNetMappingByInterfaceAndWanport(String inter_face, String wanport) throws Exception {
        NetMapping netMapping = this.getNetMappingByInterfaceAndWanPort(inter_face, wanport);
        if (netMapping == null) {
            return true;
        }
        IkuaiResponseBase response = routerAgent.delNetMapping(
                netMapping.getId()
        );
        return response.isSuccess();
    }

    /**
     * Delete NetMapping By IpAddr
     * More than one rules can be set to same lan_ip
     *
     * @param ip_addr IpAddr
     * @return Delete seccess? throw ErrMsg
     * @throws Exception e
     */
    public boolean delNetMappingByIpAddr(String ip_addr) throws Exception {
        List<String> errors = new ArrayList<>();
        for (NetMapping netMapping : this.getNetMappingListByIpAddr(ip_addr)) {
            try {
                IkuaiResponseBase response =
                        routerAgent.delNetMapping(netMapping.getId());
                if (!response.isSuccess()) {
                    errors.add("id=" + netMapping.getId() + ": " + response.getResult() + " " + response.getErrMsg());
                }
            } catch (Exception e) {
                errors.add("id=" + netMapping.getId() + ": " + e.getMessage());
            }
        }
        if (!errors.isEmpty()) {
            throw new IkuaiRouterException("Partial failure in delNetMappingByIpAddr: " + String.join("; ", errors));
        }
        return true;
    }

    //================ Private Functions ==========================

    /**
     * 从 ResponseShow 中解析 total 字段
     *
     * @param response ResponseShow 响应对象
     * @return total 值，如果不存在返回 -1
     * @throws Exception e
     */
    private int getTotal(ResponseShow response) throws Exception {
        JsonNode dataNode = objectMapper.readTree(response.getData());
        JsonNode totalNode = dataNode.get("total");
        return totalNode != null ? totalNode.asInt() : -1;
    }

    /**
     * 统一解析 ResponseShow 中的 JSON 数据
     *
     * @param response ResponseShow 响应对象
     * @param dataKey  JSON 数据中的 key（如 "data", "static_data", "sysstat" 等）
     * @param type     目标类型的 TypeReference
     * @param <T>      返回类型
     * @return 反序列化后的对象
     * @throws Exception e
     */
    private <T> T parseData(ResponseShow response, String dataKey, TypeReference<T> type) throws Exception {
        JsonNode dataNode = objectMapper.readTree(response.getData());
        JsonNode targetNode = dataNode.get(dataKey);
        if (targetNode == null) {
            throw new IkuaiRouterException("Response missing expected key: " + dataKey);
        }
        return objectMapper.readValue(targetNode.toString(), type);
    }

    /**
     * 解析端口映射列表中，匹配指定接口的所有已占用端口
     *
     * @param netMappingList   端口映射规则列表
     * @param targetInterfaces 目标接口列表（有交集即匹配）
     * @return 已占用端口集合
     */
    private Set<Integer> parseUsedPorts(List<NetMapping> netMappingList, List<String> targetInterfaces) {
        Set<Integer> usedPorts = new HashSet<>();
        for (NetMapping netMapping : netMappingList) {
            // 检查接口是否有交集
            List<String> mappingInterfaces = Arrays.asList(netMapping.getInter_face().split(","));
            boolean interfaceMatch = false;
            for (String target : targetInterfaces) {
                if (mappingInterfaces.contains(target)) {
                    interfaceMatch = true;
                    break;
                }
            }
            if (!interfaceMatch) {
                continue;
            }
            // 解析 wan_port 字段，加入 usedPorts
            parseWanPortInto(usedPorts, netMapping.getWan_port());
        }
        return usedPorts;
    }

    /**
     * 解析 wan_port 字段（支持 "8080"、"8000-8010"、"8000,9000-9010"）
     *
     * @param usedPorts   已占用端口集合（结果写入此集合）
     * @param wanPortSpec wan_port 字段值
     */
    private void parseWanPortInto(Set<Integer> usedPorts, String wanPortSpec) {
        String[] segments = wanPortSpec.split(",");
        for (String segment : segments) {
            String[] range = segment.split("-");
            if (range.length == 1) {
                usedPorts.add(Integer.parseInt(range[0]));
            } else if (range.length == 2) {
                int start = Integer.parseInt(range[0]);
                int end = Integer.parseInt(range[1]);
                for (int p = start; p <= end; p++) {
                    usedPorts.add(p);
                }
            }
        }
    }

    /**
     * 日志打印
     *
     * @param msg 日志内容
     */
    private void log(String msg) {
        if (debug) {
            logger.debug(msg);
        }
    }


}
