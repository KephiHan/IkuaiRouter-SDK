/**
 * Copyright 2022 bejson.com
 */
package net.dabaiyun.ikuairouter.Entity;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Auto-generated: 2022-03-17 16:15:56
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class DHCPHost {

    private int id;
    private int status;
    private long ip_addr_int;
    private String hostname;
    private long start_time;
    private long end_time;
    private String mac;

    @JsonProperty("interface")
    private String inter_face;
    private String ip_addr;
    private String comment;
    private long timeout;

    public DHCPHost() {
    }

    public DHCPHost(int id, int status, long ip_addr_int, String hostname, long start_time, long end_time, String mac, String inter_face, String ip_addr, String comment, long timeout) {
        this.id = id;
        this.status = status;
        this.ip_addr_int = ip_addr_int;
        this.hostname = hostname;
        this.start_time = start_time;
        this.end_time = end_time;
        this.mac = mac;
        this.inter_face = inter_face;
        this.ip_addr = ip_addr;
        this.comment = comment;
        this.timeout = timeout;
    }

    public void setId(int id) {
        this.id = id;
    }
    public int getId() {
        return id;
    }

    public void setStatus(int status) {
        this.status = status;
    }
    public int getStatus() {
        return status;
    }

    public void setIp_addr_int(long ip_addr_int) {
        this.ip_addr_int = ip_addr_int;
    }
    public long getIp_addr_int() {
        return ip_addr_int;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }
    public String getHostname() {
        return hostname;
    }

    public void setStart_time(long start_time) {
        this.start_time = start_time;
    }
    public long getStart_time() {
        return start_time;
    }

    public void setEnd_time(long end_time) {
        this.end_time = end_time;
    }
    public long getEnd_time() {
        return end_time;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }
    public String getMac() {
        return mac;
    }

    public void setInter_face(String inter_face) {
        this.inter_face = inter_face;
    }
    public String getInter_face() {
        return inter_face;
    }

    public void setIp_addr(String ip_addr) {
        this.ip_addr = ip_addr;
    }
    public String getIp_addr() {
        return ip_addr;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
    public String getComment() {
        return comment;
    }

    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }
    public long getTimeout() {
        return timeout;
    }

    @Override
    public String toString() {
        return "DHCPHost{" +
                "id=" + id +
                ", status=" + status +
                ", ip_addr_int=" + ip_addr_int +
                ", hostname='" + hostname + '\'' +
                ", start_time=" + start_time +
                ", end_time=" + end_time +
                ", mac='" + mac + '\'' +
                ", inter_face='" + inter_face + '\'' +
                ", ip_addr='" + ip_addr + '\'' +
                ", comment='" + comment + '\'' +
                ", timeout=" + timeout +
                '}';
    }
}