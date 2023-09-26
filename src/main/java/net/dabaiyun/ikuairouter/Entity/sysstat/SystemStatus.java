
package net.dabaiyun.ikuairouter.Entity.sysstat;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SystemStatus {

    private List<String> cpu;
    private List<Integer> cputemp;
    private String gwid;
    private String hostname;
    private int link_status;
    private Memory memory;
    private Online_user online_user;
    private Stream stream;
    private long uptime;
    private Verinfo verinfo;

    public void setCpu(List<String> cpu) {
        this.cpu = cpu;
    }

    public List<String> getCpu() {
        return cpu;
    }

    public void setCputemp(List<Integer> cputemp) {
        this.cputemp = cputemp;
    }

    public List<Integer> getCputemp() {
        return cputemp;
    }

    public void setGwid(String gwid) {
        this.gwid = gwid;
    }

    public String getGwid() {
        return gwid;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public String getHostname() {
        return hostname;
    }

    public void setLink_status(int link_status) {
        this.link_status = link_status;
    }

    public int getLink_status() {
        return link_status;
    }

    public void setMemory(Memory memory) {
        this.memory = memory;
    }

    public Memory getMemory() {
        return memory;
    }

    public void setOnline_user(Online_user online_user) {
        this.online_user = online_user;
    }

    public Online_user getOnline_user() {
        return online_user;
    }

    public void setStream(Stream stream) {
        this.stream = stream;
    }

    public Stream getStream() {
        return stream;
    }

    public void setUptime(long uptime) {
        this.uptime = uptime;
    }

    public long getUptime() {
        return uptime;
    }

    public void setVerinfo(Verinfo verinfo) {
        this.verinfo = verinfo;
    }

    public Verinfo getVerinfo() {
        return verinfo;
    }

    @Override
    public String toString() {
        return "SystemStatus{" +
                "cpu=" + cpu +
                ", cputemp=" + cputemp +
                ", gwid='" + gwid + '\'' +
                ", hostname='" + hostname + '\'' +
                ", link_status=" + link_status +
                ", memory=" + memory +
                ", online_user=" + online_user +
                ", stream=" + stream +
                ", uptime=" + uptime +
                ", verinfo=" + verinfo +
                '}';
    }
}