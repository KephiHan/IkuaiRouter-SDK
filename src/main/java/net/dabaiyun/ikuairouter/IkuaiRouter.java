package net.dabaiyun.ikuairouter;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.dabaiyun.ikuairouter.Action.IkuaiResponseBase;
import net.dabaiyun.ikuairouter.Action.ResponseAdd;
import net.dabaiyun.ikuairouter.Action.ResponseShow;
import net.dabaiyun.ikuairouter.Entity.*;
import net.dabaiyun.ikuairouter.Entity.sysstat.SystemStatus;
import net.dabaiyun.ikuairouter.Exception.IkuaiRouterException;
import net.dabaiyun.ikuairouter.Util.IpAddrUtil;
import okhttp3.Cookie;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class IkuaiRouter {
    //Tools
    private static final ObjectMapper objectMapper = new ObjectMapper();

    //Agent
    private RouterAgent routerAgent;

    //Statu
    private boolean isLogin = false;

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

    public HashMap<String, List<Cookie>> getCookieStore() {
        return routerAgent.getCookieStore();
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
        //获取所有端口映射配置条目
        List<NetMapping> netMappingList = this.getNetMappingList();
        //遍历查找
        for (NetMapping netMapping : netMappingList) {
            //检查是否符合上行接口
            if (!netMapping.getInter_face().equals(inter_face)) {
                //不符合上行接口的配置直接忽略
//                    log("上行接口：" + netMapping.getInter_face() + " 不符合，跳过");
                continue;
            }
            //对范围端口进行处理
//            System.out.println("处理配置字符串：" + netMapping.getWan_port());
            //按逗号分隔多段配置
            String[] ports_str = netMapping.getWan_port().split(",");
            //处理每一段端口
            for (String s : ports_str) {
//                System.out.println("处理配置字符串：" + s);
                String[] portRange_str = s.split("-");
                //如果是单个端口
                if (portRange_str.length == 1) {
//                    System.out.println("单个端口：" + portRange_str[0]);
                    //判断是否和currentPort相同
                    if (Integer.parseInt(portRange_str[0]) == wanPort) {
//                        System.out.println("当前端口：" + wanPort + " 已使用");
                        return true;
                    }
                }
                //如果是端口范围
                if (portRange_str.length == 2) {
//                    System.out.println("范围端口：" + portRange_str[0] + " - " + portRange_str[1]);
                    //当前端口处于范围内
                    if ((wanPort >= Integer.parseInt(portRange_str[0]) && wanPort <= Integer.parseInt(portRange_str[1]))) {
//                        System.out.println("当前端口：" + wanPort + " 已使用");
                        return true;
                    }
                }
            }
        }
        return false;
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
        //端口合法性预处理
        if (portbegin <= 0) {
            portbegin = 1;
        }
        if (portend > 65535) {
            portend = 65535;
        }
        boolean has_port = false;
        int target_port = -1;
        //获取所有端口映射配置条目
        List<NetMapping> netMappingList = this.getNetMappingList();
//        log("获取到NetMapping记录共 " + netMappingList.size() + " 个");
        //遍历查找
        for (int currentPort = portbegin; currentPort <= portend; currentPort++) {
//            log("当前端口：" + currentPort);
            //当前端口i是否被使用
            boolean isInUse = false;
            //遍历所有已存在配置项
            for (NetMapping netMapping : netMappingList) {
                //检查是否符合上行接口
                if (!netMapping.getInter_face().equals(inter_face)) {
                    //不符合上行接口的配置直接忽略
//                    log("上行接口：" + netMapping.getInter_face() + " 不符合，跳过");
                    continue;
                }
                //对范围端口进行处理
//                log("处理配置字符串：" + netMapping.getWan_port());
                //按逗号分隔多段配置
                String[] ports_str = netMapping.getWan_port().split(",");
                //处理每一段端口
                for (String s : ports_str) {
//                    log("处理配置字符串：" + s);
                    String[] portRange_str = s.split("-");
                    //如果是单个端口
                    if (portRange_str.length == 1) {
//                        log("单个端口：" + portRange_str[0]);
                        //判断是否和currentPort相同
                        if (Integer.parseInt(portRange_str[0]) == currentPort) {
//                            log("当前端口：" + currentPort + " 已使用");
                            isInUse = true;
                            break;
                        }
                    }
                    //如果是端口范围
                    if (portRange_str.length == 2) {
//                        log("范围端口：" + portRange_str[0] + " - " + portRange_str[1]);
                        //当前端口处于范围内
                        if ((currentPort >= Integer.parseInt(portRange_str[0]) && currentPort <= Integer.parseInt(portRange_str[1]))) {
//                            log("当前端口：" + currentPort + " 已使用");
                            isInUse = true;
                            break;
                        }
                    }
                }
                if (isInUse) {
                    break;
                }
            }
            if (!isInUse) {
                target_port = currentPort;
                has_port = true;
                break;
            }
        }
        if (!has_port) {
            throw new IkuaiRouterException("No available port found.");
        }
        return target_port;
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

//    private Integer getIpNumberByIp(String ipaddr){
//        String[] strlist = ipaddr.split("\\.");
//        if(strlist.length < 4){
//            return 0;
//        }
//        return Integer.parseInt(strlist[3]);
//    }

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
        if (!IpAddrUtil.isIpVaild(gateway)) {
            throw new IkuaiRouterException("gateway " + gateway + " invaild");
        }
        if (!IpAddrUtil.isIpVaild(ip_begin)) {
            throw new IkuaiRouterException("ip_begin " + ip_begin + " invaild");
        }
        if (!IpAddrUtil.isIpVaild(ip_end)) {
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
        //获取当前系统已存在配置
        List<DHCPHost> dhcpHostList = this.getDHCPHostList();
//        System.out.println("Getted DHCPHostList Length: " + dhcpHostList.getList().size());
        List<DHCPStatic> dhcpStaticList = this.getDHCPStaticList();
//        System.out.println("Getted DHCPStaticList Length: " + dhcpStaticList.getList().size());
        //最终结果IP
        String result_ip = null;
        boolean hasResult = false;
//        //起始ip
//        String ip_begin = IpAddrUtil.getBeginIpStr(gateway, netmaskBit);
//        String ip_end = IpAddrUtil.getEndIpStr(gateway, netmaskBit);
        //把ip分成4段
        String[] ipfromd = ip_begin.split("\\.");
        String[] iptod = ip_end.split("\\.");
        //将字符串数组转换成int数组
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
                        //当前IP的字符串
                        String current_ip = A + "." + B + "." + C + "." + D;
//                        System.out.println("current_ip: " + current_ip);
                        //该ip是否已经被使用标记
                        boolean isUsed = false;
                        //检查是否有主机已使用该ip
                        for (DHCPHost dhcpHost : dhcpHostList) {
//                            System.out.println("current_dhcpHost: " + dhcpHost.getIp_addr());
                            //如果已经被DHCPHost使用，则标记为已使用，并直接跳出循环
                            if (dhcpHost.getIp_addr().equals(current_ip)) {
//                                System.out.println("current_dhcpHost: " + dhcpHost.getIp_addr() + " already in use.");
                                isUsed = true;
                                break;
                            }
                        }
                        //如果当前查找的ip已经被标记为已使用，直接进入下一个IP
                        if (isUsed) {
                            continue;
                        }
                        //检查是否已经有该ip的静态分配
                        for (DHCPStatic dhcpStatic : dhcpStaticList) {
//                            System.out.println("current_dhcpStatic: " + dhcpStatic.getIp_addr());
                            //如果已经被DHCPStatic使用，则标记为已使用，并直接跳出循环
                            if (dhcpStatic.getIp_addr().equals(current_ip)) {
//                                System.out.println("current_dhcpStatic: " + dhcpStatic.getIp_addr() + " already in use.");
                                isUsed = true;
                                break;
                            }
                        }
                        //如果当前查找的ip已经被标记为已使用，直接进入下一个IP
                        if (isUsed) {
                            continue;
                        }
                        //如果都没有被使用，则直接选取本IP
                        result_ip = current_ip;
                        hasResult = true;
                        break;
                    }
                    if (hasResult) {
                        break;
                    }
                }
                if (hasResult) {
                    break;
                }
            }
            if (hasResult) {
                break;
            }
        }

        if (hasResult) {
            return result_ip;
        } else {
            throw new IkuaiRouterException("No Available IpAddr");
        }
    }

    //================ Getter Functions ==========================

    /**
     * Get SystemStatus
     *
     * @return SystemStatus
     * @throws Exception e
     */
    public SystemStatus getSystemStatus() throws Exception {
        ResponseShow responseShow = routerAgent.getSystemStatus();
//        if (responseShow.isAuthFail()) {
//            throw new IkuaiRouterNoAuthException();
//        }
        JsonNode dataNode = objectMapper.readTree(responseShow.getData());
        if (!dataNode.has("sysstat")) {
            throw new IkuaiRouterException("sysstat");
        }
        String arrStr = dataNode.get("sysstat").toString();
        return objectMapper.readValue(
                arrStr,
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
        for (DHCPStatic dhcpStatic : this.getDHCPStaticList()) {
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
        for (DHCPStatic dhcpStatic : this.getDHCPStaticList()) {
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
        for (DHCPStatic dhcpStatic : this.getDHCPStaticList()) {
            if (dhcpStatic.getMac().equals(mac.toLowerCase())) {
                return dhcpStatic;
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
        for (QosLimit qosLimit : this.getQosLimitList()) {
            if (qosLimit.getIp_addr().equals(ip_addr)) {
                return qosLimit;
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
        for (QosLimit qosLimit : this.getQosLimitList()) {
            if (qosLimit.getId() == id) {
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
        for (NetMapping netMapping : this.getNetMappingList()) {
            if (netMapping.getId() == id) {
                return netMapping;
            }
        }
        return null;
    }

    /**
     * Get NetMapping By IP_Addr
     *
     * @param ipAddr ipaddress
     * @return NetMapping Object List
     * @throws Exception e
     */
    public List<NetMapping> getNetMappingByIpAddr(String ipAddr) throws Exception {
        List<NetMapping> netMappingList = new ArrayList<>();
        for (NetMapping netMapping : this.getNetMappingList()) {
            if (netMapping.getLan_addr().equals(ipAddr)) {
                netMappingList.add(netMapping);
            }
        }
        return netMappingList;
    }

    /**
     * Get NetMapping By wanport
     *
     * @param wanport Target wanport
     * @return NetMapping Object
     * @throws Exception e
     */
    public NetMapping getNetMappingByInterfaceAndWanPort(String inter_face, String wanport) throws Exception {
        for (NetMapping netMapping : this.getNetMappingList()) {
            if (netMapping.getWan_port().equals(wanport) && netMapping.getInter_face().equals(inter_face)) {
                return netMapping;
            }
        }
        return null;
    }

    /**
     * Get NetMappingList By IpAddr
     * More than one rules can be set to same lan_ip
     *
     * @param ip_addr IpAddr
     * @return Matching Object List
     * @throws Exception E
     */
    public List<NetMapping> getNetMappingListByIpAddr(String ip_addr) throws Exception {
        List<NetMapping> list = new ArrayList<>();
        for (NetMapping netMapping : this.getNetMappingList()) {
            if (netMapping.getLan_addr().equals(ip_addr)) {
                list.add(netMapping);
            }
        }
        return list;
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
        ResponseAdd responseAdd = routerAgent.addNetMapping(netMapping);
        if (responseAdd.isSuccess()) {
            netMapping.setId(responseAdd.getRowId());
            return responseAdd.getRowId();
        } else {
            throw new IkuaiRouterException(responseAdd.getResult() + " " + responseAdd.getErrMsg());
        }
    }


    //================ Editer Functions ==========================

    /**
     * Edit DHCPStatic by MAC
     *
     * @param mac           Target MAC
     * @param newDhcpStatic New DHCPStatic Object
     * @return Add seccess? throw ErrMsg
     * @throws Exception ErrMsg
     */
    public boolean editDHCPStaticByMac(String mac, DHCPStatic newDhcpStatic) throws Exception {
        //Find dhcpStatic Object by mac then set it id to new Object
        newDhcpStatic.setId(this.getDHCPStaticByMAC(mac).getId());
        IkuaiResponseBase response = routerAgent.editDHCPStatic(newDhcpStatic);
        return response.isSuccess();
    }

    /**
     * Edit DHCPStatic by ipv4
     *
     * @param ip_addr       Target ipv4
     * @param newDhcpStatic New DHCPStatic Object
     * @return Add seccess? throw ErrMsg
     * @throws Exception ErrMsg
     */
    public boolean editDHCPStaticByIpAddr(String ip_addr, DHCPStatic newDhcpStatic) throws Exception {
        newDhcpStatic.setId(this.getDHCPStaticByIpAddr(ip_addr).getId());
        IkuaiResponseBase response = routerAgent.editDHCPStatic(newDhcpStatic);
        return response.isSuccess();
    }

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
     * @param newDhcpStatic New DHCPStatic Object
     * @return Add seccess? throw ErrMsg
     * @throws Exception ErrMsg
     */
    public boolean editDHCPStatic(DHCPStatic newDhcpStatic) throws Exception {
        IkuaiResponseBase response = routerAgent.editDHCPStatic(newDhcpStatic);
        return response.isSuccess();
    }

    /**
     * Edit QosLimit by ip_addr
     *
     * @param ip_addr  Target ip_addr
     * @param qosLimit New QosLimit Object
     * @return Add seccess? throw ErrMsg
     * @throws Exception ErrMsg
     */
    public boolean editQosLimitByIpAddr(String ip_addr, QosLimit qosLimit) throws Exception {
        qosLimit.setId(this.getQosLimitByIpAddr(ip_addr).getId());
        IkuaiResponseBase response = routerAgent.editQosLimit(qosLimit);
        return response.isSuccess();
    }

//    /**
//     * Edit QosLimit by ID
//     *
//     * @param id       Target id
//     * @param qosLimit New QosLimit Object
//     * @return Add seccess? throw ErrMsg
//     * @throws Exception ErrMsg
//     */
//    public boolean editQosLimitById(int id, QosLimit qosLimit) throws Exception {
//        qosLimit.setId(id);
//        IkuaiResponseBase response = routerAgent.editQosLimit(qosLimit);
//        if (response.isSeccess()) {
//            return true;
//        } else {
//            throw new IkuaiRouterException(response.getResult() + " " + response.getErrMsg());
//        }
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

    /**
     * Edit NetMapping By Interface And Wanport
     *
     * @param inter_face Interface
     * @param wanport    Wanport
     * @param netMapping New NetMapping Object
     * @return Edit seccess? throw ErrMsg
     * @throws Exception e
     */
    public boolean editNetMappingByInterfaceAndWanport(String inter_face, String wanport, NetMapping netMapping) throws Exception {
        netMapping.setId(
                this.getNetMappingByInterfaceAndWanPort(
                        inter_face,
                        wanport).getId()
        );
        IkuaiResponseBase response = routerAgent.editNetMapping(netMapping);
        return response.isSuccess();
    }

//    /**
//     * Edit NetMapping by ID
//     *
//     * @param id         Target id
//     * @param netMapping New NetMapping Object
//     * @return Add seccess? throw ErrMsg
//     * @throws Exception ErrMsg
//     */
//    public boolean editNetMappingById(int id, NetMapping netMapping) throws Exception {
//        netMapping.setId(id);
//        IkuaiResponseBase response = routerAgent.editNetMapping(netMapping);
//        if (response.isSeccess()) {
//            return true;
//        } else {
//            throw new IkuaiRouterException(response.getResult() + " " + response.getErrMsg());
//        }
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
     * Down DHCPStatic By Object
     *
     * @param dhcpStatic Target Obj
     * @return Add seccess? throw ErrMsg
     * @throws Exception ErrMsg
     */
    public boolean downDHCPStatic(DHCPStatic dhcpStatic) throws Exception {
        IkuaiResponseBase response = routerAgent.downDHCPStatic(dhcpStatic.getId());
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
        for (NetMapping netMapping : this.getNetMappingListByIpAddr(lanip)) {
            IkuaiResponseBase response = routerAgent.downNetMapping(netMapping.getId());
            if (!response.isSuccess()) {
                throw new IkuaiRouterException(response.getResult() + " " + response.getErrMsg());
            }
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
        for (NetMapping netMapping : this.getNetMappingListByIpAddr(ip_addr)) {
            IkuaiResponseBase response =
                    routerAgent.delNetMapping(netMapping.getId());
            if (!response.isSuccess()) {
                throw new IkuaiRouterException(response.getResult() + " " + response.getErrMsg());
            }
        }
        return true;
    }

    //================ Private Functions ==========================

    /**
     * Get LanHostInfoList Object
     *
     * @return LanHostInfo List
     * @throws Exception ex
     */
    private List<LanHostInfo> getLanHostInfoList() throws Exception {
        ResponseShow responseShow = routerAgent.getLanHostStatus();
        JsonNode dataNode = objectMapper.readTree(responseShow.getData());
        if (!dataNode.has("data")) {
            throw new IkuaiRouterException("data");
        }
        String arrStr = dataNode.get("data").toString();
        return objectMapper.readValue(
                arrStr,
                new TypeReference<List<LanHostInfo>>() {
                }
        );
    }

    /**
     * Get InterfaceLanList Object
     *
     * @return InterfaceLan List
     * @throws Exception ex
     */
    private List<InterfaceLan> getInterfaceLanList() throws Exception {
        ResponseShow responseShow = routerAgent.getInterfaceSnapshoot();
        JsonNode dataNode = objectMapper.readTree(responseShow.getData());
        String arrStr = dataNode.get("snapshoot_lan").toString();
        return objectMapper.readValue(
                arrStr,
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
    private List<InterfaceWan> getInterfaceWanList() throws Exception {
        ResponseShow responseShow = routerAgent.getInterfaceSnapshoot();
        JsonNode dataNode = objectMapper.readTree(responseShow.getData());
        String arrStr = dataNode.get("snapshoot_wan").toString();
        return objectMapper.readValue(
                arrStr,
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
    private List<DHCPServer> getDHCPServerList() throws Exception {
        ResponseShow responseShow = routerAgent.getDHCPServers();
        JsonNode dataNode = objectMapper.readTree(responseShow.getData());
        String arrStr = dataNode.get("data").toString();
        return objectMapper.readValue(
                arrStr,
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
    private List<DHCPStatic> getDHCPStaticList() throws Exception {
        ResponseShow responseShow = routerAgent.getDHCPStatics();
        JsonNode dataNode = objectMapper.readTree(responseShow.getData());
        String arrStr = dataNode.get("static_data").toString();
        return objectMapper.readValue(
                arrStr,
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
    private List<DHCPHost> getDHCPHostList() throws Exception {
        ResponseShow responseShow = routerAgent.getDHCPHosts();
        JsonNode dataNode = objectMapper.readTree(responseShow.getData());
        String arrStr = dataNode.get("data").toString();
        return objectMapper.readValue(
                arrStr,
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
    private List<NetMapping> getNetMappingList() throws Exception {
        ResponseShow responseShow = routerAgent.getNetMapping();
        JsonNode dataNode = objectMapper.readTree(responseShow.getData());
        String arrStr = dataNode.get("data").toString();
        return objectMapper.readValue(
                arrStr,
                new TypeReference<List<NetMapping>>() {
                }
        );
    }

    /**
     * Get QosLimit Object
     *
     * @return QosLimit List
     * @throws Exception ex
     */
    private List<QosLimit> getQosLimitList() throws Exception {
        ResponseShow responseShow = routerAgent.getQosLimit();
        JsonNode dataNode = objectMapper.readTree(responseShow.getData());
        String arrStr = dataNode.get("data").toString();
        return objectMapper.readValue(
                arrStr,
                new TypeReference<List<QosLimit>>() {
                }
        );
    }
}
