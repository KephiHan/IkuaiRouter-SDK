package net.dabaiyun.ikuairouter.Entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class InterfaceWan {

    private int id;
    private String comment;

    @JsonProperty("interface")
    private String inter_face;

    private String mac;
    private List<String> member;
    private int default_route;
    private int internet;
    private String ip_addr;
    private String netmask;
    private String gateway;
    private String dns1;
    private String dns2;
    private int count_static;
    private int count_dhcp;
    private int count_pppoe;
    private int count_check_fail;
    private int updatetime;
    private int check_res;
    private String errmsg;

    public InterfaceWan() {
    }

    public InterfaceWan(int id, String comment, String inter_face, String mac, List<String> member, int default_route, int internet, String ip_addr, String netmask, String gateway, String dns1, String dns2, int count_static, int count_dhcp, int count_pppoe, int count_check_fail, int updatetime, int check_res, String errmsg) {
        this.id = id;
        this.comment = comment;
        this.inter_face = inter_face;
        this.mac = mac;
        this.member = member;
        this.default_route = default_route;
        this.internet = internet;
        this.ip_addr = ip_addr;
        this.netmask = netmask;
        this.gateway = gateway;
        this.dns1 = dns1;
        this.dns2 = dns2;
        this.count_static = count_static;
        this.count_dhcp = count_dhcp;
        this.count_pppoe = count_pppoe;
        this.count_check_fail = count_check_fail;
        this.updatetime = updatetime;
        this.check_res = check_res;
        this.errmsg = errmsg;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getComment() {
        return comment;
    }

    public void setInter_face(String inter_face) {
        this.inter_face = inter_face;
    }

    public String getInter_face() {
        return inter_face;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getMac() {
        return mac;
    }

    public void setMember(List<String> member) {
        this.member = member;
    }

    public List<String> getMember() {
        return member;
    }

    public void setDefault_route(int default_route) {
        this.default_route = default_route;
    }

    public int getDefault_route() {
        return default_route;
    }

    public void setInternet(int internet) {
        this.internet = internet;
    }

    public int getInternet() {
        return internet;
    }

    public void setIp_addr(String ip_addr) {
        this.ip_addr = ip_addr;
    }

    public String getIp_addr() {
        return ip_addr;
    }

    public void setNetmask(String netmask) {
        this.netmask = netmask;
    }

    public String getNetmask() {
        return netmask;
    }

    public void setGateway(String gateway) {
        this.gateway = gateway;
    }

    public String getGateway() {
        return gateway;
    }

    public void setDns1(String dns1) {
        this.dns1 = dns1;
    }

    public String getDns1() {
        return dns1;
    }

    public void setDns2(String dns2) {
        this.dns2 = dns2;
    }

    public String getDns2() {
        return dns2;
    }

    public void setCount_static(int count_static) {
        this.count_static = count_static;
    }

    public int getCount_static() {
        return count_static;
    }

    public void setCount_dhcp(int count_dhcp) {
        this.count_dhcp = count_dhcp;
    }

    public int getCount_dhcp() {
        return count_dhcp;
    }

    public void setCount_pppoe(int count_pppoe) {
        this.count_pppoe = count_pppoe;
    }

    public int getCount_pppoe() {
        return count_pppoe;
    }

    public void setCount_check_fail(int count_check_fail) {
        this.count_check_fail = count_check_fail;
    }

    public int getCount_check_fail() {
        return count_check_fail;
    }

    public void setUpdatetime(int updatetime) {
        this.updatetime = updatetime;
    }

    public int getUpdatetime() {
        return updatetime;
    }

    public void setCheck_res(int check_res) {
        this.check_res = check_res;
    }

    public int getCheck_res() {
        return check_res;
    }

    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }

    public String getErrmsg() {
        return errmsg;
    }

    @Override
    public String toString() {
        return "InterfaceWan{" +
                "id=" + id +
                ", comment='" + comment + '\'' +
                ", inter_face='" + inter_face + '\'' +
                ", mac='" + mac + '\'' +
                ", member=" + member +
                ", default_route=" + default_route +
                ", internet=" + internet +
                ", ip_addr='" + ip_addr + '\'' +
                ", netmask='" + netmask + '\'' +
                ", gateway='" + gateway + '\'' +
                ", dns1='" + dns1 + '\'' +
                ", dns2='" + dns2 + '\'' +
                ", count_static=" + count_static +
                ", count_dhcp=" + count_dhcp +
                ", count_pppoe=" + count_pppoe +
                ", count_check_fail=" + count_check_fail +
                ", updatetime=" + updatetime +
                ", check_res=" + check_res +
                ", errmsg='" + errmsg + '\'' +
                '}';
    }
}