package net.dabaiyun.ikuairouter.Entity;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DHCPServer {

    private String domain;
    private int id;
    private String opt67;
    private String opt119;
    private String wins2;
    private String opt80;
    private String opt66;
    private String addr_pool;
    private String wins1;
    private String opt60;
    private String opt43;
    private String next_server;
    private String opt138;
    private int check_addr_valid;
    private int status;
    private int check_relay_only;
    private int available;
    private String opt128;
    private String netmask;
    private String opt125;
    private int lease;
    private String dns2;

    @JsonProperty("interface")
    private String inter_face;

    private String enabled;
    private String dns1;
    private String gateway;
    public void setDomain(String domain) {
        this.domain = domain;
    }
    public String getDomain() {
        return domain;
    }

    public void setId(int id) {
        this.id = id;
    }
    public int getId() {
        return id;
    }

    public void setOpt67(String opt67) {
        this.opt67 = opt67;
    }
    public String getOpt67() {
        return opt67;
    }

    public void setOpt119(String opt119) {
        this.opt119 = opt119;
    }
    public String getOpt119() {
        return opt119;
    }

    public void setWins2(String wins2) {
        this.wins2 = wins2;
    }
    public String getWins2() {
        return wins2;
    }

    public void setOpt80(String opt80) {
        this.opt80 = opt80;
    }
    public String getOpt80() {
        return opt80;
    }

    public void setOpt66(String opt66) {
        this.opt66 = opt66;
    }
    public String getOpt66() {
        return opt66;
    }

    public void setAddr_pool(String addr_pool) {
        this.addr_pool = addr_pool;
    }
    public String getAddr_pool() {
        return addr_pool;
    }

    public void setWins1(String wins1) {
        this.wins1 = wins1;
    }
    public String getWins1() {
        return wins1;
    }

    public void setOpt60(String opt60) {
        this.opt60 = opt60;
    }
    public String getOpt60() {
        return opt60;
    }

    public void setOpt43(String opt43) {
        this.opt43 = opt43;
    }
    public String getOpt43() {
        return opt43;
    }

    public void setNext_server(String next_server) {
        this.next_server = next_server;
    }
    public String getNext_server() {
        return next_server;
    }

    public void setOpt138(String opt138) {
        this.opt138 = opt138;
    }
    public String getOpt138() {
        return opt138;
    }

    public void setCheck_addr_valid(int check_addr_valid) {
        this.check_addr_valid = check_addr_valid;
    }
    public int getCheck_addr_valid() {
        return check_addr_valid;
    }

    public void setStatus(int status) {
        this.status = status;
    }
    public int getStatus() {
        return status;
    }

    public void setCheck_relay_only(int check_relay_only) {
        this.check_relay_only = check_relay_only;
    }
    public int getCheck_relay_only() {
        return check_relay_only;
    }

    public void setAvailable(int available) {
        this.available = available;
    }
    public int getAvailable() {
        return available;
    }

    public void setOpt128(String opt128) {
        this.opt128 = opt128;
    }
    public String getOpt128() {
        return opt128;
    }

    public void setNetmask(String netmask) {
        this.netmask = netmask;
    }
    public String getNetmask() {
        return netmask;
    }

    public void setOpt125(String opt125) {
        this.opt125 = opt125;
    }
    public String getOpt125() {
        return opt125;
    }

    public void setLease(int lease) {
        this.lease = lease;
    }
    public int getLease() {
        return lease;
    }

    public void setDns2(String dns2) {
        this.dns2 = dns2;
    }
    public String getDns2() {
        return dns2;
    }

    public void setInter_face(String inter_face) {
        this.inter_face = inter_face;
    }
    public String getInter_face() {
        return inter_face;
    }

    public void setEnabled(String enabled) {
        this.enabled = enabled;
    }
    public String getEnabled() {
        return enabled;
    }

    public void setDns1(String dns1) {
        this.dns1 = dns1;
    }
    public String getDns1() {
        return dns1;
    }

    public void setGateway(String gateway) {
        this.gateway = gateway;
    }
    public String getGateway() {
        return gateway;
    }

    @Override
    public String toString() {
        return "DHCPServer{" +
                "domain='" + domain + '\'' +
                ", id=" + id +
                ", opt67='" + opt67 + '\'' +
                ", opt119='" + opt119 + '\'' +
                ", wins2='" + wins2 + '\'' +
                ", opt80='" + opt80 + '\'' +
                ", opt66='" + opt66 + '\'' +
                ", addr_pool='" + addr_pool + '\'' +
                ", wins1='" + wins1 + '\'' +
                ", opt60='" + opt60 + '\'' +
                ", opt43='" + opt43 + '\'' +
                ", next_server='" + next_server + '\'' +
                ", opt138='" + opt138 + '\'' +
                ", check_addr_valid=" + check_addr_valid +
                ", status=" + status +
                ", check_relay_only=" + check_relay_only +
                ", available=" + available +
                ", opt128='" + opt128 + '\'' +
                ", netmask='" + netmask + '\'' +
                ", opt125='" + opt125 + '\'' +
                ", lease=" + lease +
                ", dns2='" + dns2 + '\'' +
                ", inter_face='" + inter_face + '\'' +
                ", enabled='" + enabled + '\'' +
                ", dns1='" + dns1 + '\'' +
                ", gateway='" + gateway + '\'' +
                '}';
    }
}