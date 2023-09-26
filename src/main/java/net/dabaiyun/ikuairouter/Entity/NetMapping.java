package net.dabaiyun.ikuairouter.Entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class NetMapping {

    private int    id;
    private String wan_port;
    private String protocol;
    private String lan_addr;
    private String lan_port;

    @JsonProperty("interface")
    private String inter_face;

    private String enabled;
    private long   lan_addr_int;
    private String comment;

    //Static Values

    public static class EnabledType {
        public static final String YES = "yes";
        public static final String NO  = "no";
    }

    public static class ProtocolType {
        public static final String TCP      = "tcp";
        public static final String UDP      = "udp";
        public static final String TCP_UDP  = "tcp+udp";
//        public static final String ANY      = "any";
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getWan_port() {
        return wan_port;
    }

    public void setWan_port(String wan_port) {
        this.wan_port = wan_port;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getLan_addr() {
        return lan_addr;
    }

    public void setLan_addr(String lan_addr) {
        this.lan_addr = lan_addr;
    }

    public String getLan_port() {
        return lan_port;
    }

    public void setLan_port(String lan_port) {
        this.lan_port = lan_port;
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

    public long getLan_addr_int() {
        return lan_addr_int;
    }

    public void setLan_addr_int(long lan_addr_int) {
        this.lan_addr_int = lan_addr_int;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public String toString() {
        return "NetMapping{" +
                "id=" + id +
                ", wan_port='" + wan_port + '\'' +
                ", protocol='" + protocol + '\'' +
                ", lan_addr='" + lan_addr + '\'' +
                ", lan_port='" + lan_port + '\'' +
                ", inter_face='" + inter_face + '\'' +
                ", enabled='" + enabled + '\'' +
                ", lan_addr_int=" + lan_addr_int +
                ", comment='" + comment + '\'' +
                '}';
    }
}