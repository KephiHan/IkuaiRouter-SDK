package net.dabaiyun.ikuairouter.Entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class InterfaceCheck {

    private int id;

    @JsonProperty("interface")
    private String inter_face;

    private String parent_interface;
    private String ip_addr;
    private String gateway;
    private String internet;
    private String updatetime;
    private String auto_switch;
    private String result;
    private String errmsg;
    private String comment;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getInter_face() {
        return inter_face;
    }

    public void setInter_face(String inter_face) {
        this.inter_face = inter_face;
    }

    public String getParent_interface() {
        return parent_interface;
    }

    public void setParent_interface(String parent_interface) {
        this.parent_interface = parent_interface;
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

    public String getInternet() {
        return internet;
    }

    public void setInternet(String internet) {
        this.internet = internet;
    }

    public String getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(String updatetime) {
        this.updatetime = updatetime;
    }

    public String getAuto_switch() {
        return auto_switch;
    }

    public void setAuto_switch(String auto_switch) {
        this.auto_switch = auto_switch;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getErrmsg() {
        return errmsg;
    }

    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
