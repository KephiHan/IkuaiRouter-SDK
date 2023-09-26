package net.dabaiyun.ikuairouter.Entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DHCPStatic {

    private int    id;
    private long   ip_addr_int;
    private String hostname;
    private String mac;
    private String comment;

    @JsonProperty("interface")
    private String inter_face;

    private String enabled;
    private String ip_addr;
    private String gateway;

    //Static Values

    public static class EnabledType {
        public static final String YES = "yes";
        public static final String NO  = "no";
    }

    public DHCPStatic() {
        this.gateway = "";
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getIp_addr_int() {
        return ip_addr_int;
    }

    public void setIp_addr_int(long ip_addr_int) {
        this.ip_addr_int = ip_addr_int;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getInter_face() {
        return inter_face;
    }

    public void setInter_face(String inter_face) {
        this.inter_face = inter_face;
    }

    public String getEnabled() {
        return enabled;
    }

    public void setEnabled(String enabled) {
        this.enabled = enabled;
    }

    public String getIp_addr() {
        return ip_addr;
    }

    public void setIp_addr(String ip_addr) {
        this.ip_addr = ip_addr;
    }

    public String getGateway() {
        return gateway;
    }

    public void setGateway(String gateway) {
        this.gateway = gateway;
    }

    @Override
    public String toString() {
        return "DHCPStatic{" +
                "id=" + id +
                ", ip_addr_int=" + ip_addr_int +
                ", hostname='" + hostname + '\'' +
                ", mac='" + mac + '\'' +
                ", comment='" + comment + '\'' +
                ", inter_face='" + inter_face + '\'' +
                ", enabled='" + enabled + '\'' +
                ", ip_addr='" + ip_addr + '\'' +
                ", gateway='" + gateway + '\'' +
                '}';
    }
}