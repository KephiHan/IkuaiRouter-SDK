package net.dabaiyun.ikuairouter.Entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class InterfaceLan {

    private int id;
    private String comment;

    @JsonProperty("interface")
    private String inter_face;

    private int bandmode;
    private int linkmode;
    private String mac;
    private List<String> member;
    private String ip_addr;
    private String netmask;

    public InterfaceLan() {
    }

    public InterfaceLan(int id, String comment, String inter_face, int bandmode, int linkmode, String mac, List<String> member, String ip_addr, String netmask) {
        this.id = id;
        this.comment = comment;
        this.inter_face = inter_face;
        this.bandmode = bandmode;
        this.linkmode = linkmode;
        this.mac = mac;
        this.member = member;
        this.ip_addr = ip_addr;
        this.netmask = netmask;
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

    public void setBandmode(int bandmode) {
        this.bandmode = bandmode;
    }

    public int getBandmode() {
        return bandmode;
    }

    public void setLinkmode(int linkmode) {
        this.linkmode = linkmode;
    }

    public int getLinkmode() {
        return linkmode;
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

    @Override
    public String toString() {
        return "InterfaceLan{" +
                "id=" + id +
                ", comment='" + comment + '\'' +
                ", inter_face='" + inter_face + '\'' +
                ", bandmode=" + bandmode +
                ", linkmode=" + linkmode +
                ", mac='" + mac + '\'' +
                ", member=" + member +
                ", ip_addr='" + ip_addr + '\'' +
                ", netmask='" + netmask + '\'' +
                '}';
    }
}