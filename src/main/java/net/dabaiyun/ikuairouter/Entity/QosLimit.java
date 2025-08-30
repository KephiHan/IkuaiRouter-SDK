/**
 * Copyright 2022 bejson.com
 */
package net.dabaiyun.ikuairouter.Entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class QosLimit {

    /**
     * Vaild when use 'edit' Action
     */
    private int attr;

    private int    id;
    private String src_port;
    private String protocol;
    private String dst_port;
    private String time;

    @JsonProperty("interface")
    private String inter_face;

    private int    download;
    private int    upload;
    private String week;
    private int    type;
    private String enabled;
    private String ip_addr;
    private String comment;

    //Static Values

    public class EnabledType {
        public static final String YES = "yes";
        public static final String NO  = "no";
    }

    public static class WeekType {
        public static final String ALL = "1234567";
        public static final String WORK_DAY = "12345";
        public static final String WEEK_END = "67";
    }

    public static class TimeType {
        public static final String ALL_DAY = "00:00-23:59";
    }

    public static class LimitType {
        public static final Integer INDEPENDENT = 0;
        public static final Integer SHARE       = 1;
    }

    public static class ProtocolType {
        public static final String TCP      = "tcp";
        public static final String UDP      = "udp";
        public static final String TCP_UDP  = "tcp+udp";
        public static final String ANY      = "any";
    }

    public QosLimit() {
        this.src_port = "";
        this.dst_port = "";
        this.attr = 0;
    }

    public int getAttr() {
        return attr;
    }

    public void setAttr(int attr) {
        this.attr = attr;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSrc_port() {
        return src_port;
    }

    public void setSrc_port(String src_port) {
        this.src_port = src_port;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getDst_port() {
        return dst_port;
    }

    public void setDst_port(String dst_port) {
        this.dst_port = dst_port;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getInter_face() {
        return inter_face;
    }

    public void setInter_face(String inter_face) {
        this.inter_face = inter_face;
    }

    public int getDownload() {
        return download;
    }

    public void setDownload(int download) {
        this.download = download;
    }

    public int getUpload() {
        return upload;
    }

    public void setUpload(int upload) {
        this.upload = upload;
    }

    public String getWeek() {
        return week;
    }

    public void setWeek(String week) {
        this.week = week;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
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

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public String toString() {
        return "QosLimit{" +
                "attr=" + attr +
                ", id=" + id +
                ", src_port='" + src_port + '\'' +
                ", protocol='" + protocol + '\'' +
                ", dst_port='" + dst_port + '\'' +
                ", time='" + time + '\'' +
                ", inter_face='" + inter_face + '\'' +
                ", download=" + download +
                ", upload=" + upload +
                ", week='" + week + '\'' +
                ", type=" + type +
                ", enabled='" + enabled + '\'' +
                ", ip_addr='" + ip_addr + '\'' +
                ", comment='" + comment + '\'' +
                '}';
    }
}