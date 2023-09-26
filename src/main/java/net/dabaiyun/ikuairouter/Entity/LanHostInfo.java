package net.dabaiyun.ikuairouter.Entity;

public class LanHostInfo {

    private String apname;
    private int id;
    private String bssid;
    private long ip_addr_int;
    private String downrate;
    private String ppptype;
    private String client_device;
    private String uprate;
    private String mac;
    private int reject;
    private int webid;
    private long total_up;
    private String signal;
    private int ac_gid;
    private String apmac;
    private int connect_num;
    private String ssid;
    private String username;
    private String hostname;
    private String client_type;
    private int upload;
    private String uptime;
    private long total_down;
    private int auth_type;
    private String dtalk_name;
    private String comment;
    private long timestamp;
    private String ip_addr;
    private int download;
    private String frequencies;

    public LanHostInfo() {
    }

    public LanHostInfo(String apname, int id, String bssid, long ip_addr_int, String downrate, String ppptype, String client_device, String uprate, String mac, int reject, int webid, long total_up, String signal, int ac_gid, String apmac, int connect_num, String ssid, String username, String hostname, String client_type, int upload, String uptime, long total_down, int auth_type, String dtalk_name, String comment, long timestamp, String ip_addr, int download, String frequencies) {
        this.apname = apname;
        this.id = id;
        this.bssid = bssid;
        this.ip_addr_int = ip_addr_int;
        this.downrate = downrate;
        this.ppptype = ppptype;
        this.client_device = client_device;
        this.uprate = uprate;
        this.mac = mac;
        this.reject = reject;
        this.webid = webid;
        this.total_up = total_up;
        this.signal = signal;
        this.ac_gid = ac_gid;
        this.apmac = apmac;
        this.connect_num = connect_num;
        this.ssid = ssid;
        this.username = username;
        this.hostname = hostname;
        this.client_type = client_type;
        this.upload = upload;
        this.uptime = uptime;
        this.total_down = total_down;
        this.auth_type = auth_type;
        this.dtalk_name = dtalk_name;
        this.comment = comment;
        this.timestamp = timestamp;
        this.ip_addr = ip_addr;
        this.download = download;
        this.frequencies = frequencies;
    }

    public void setApname(String apname) {
        this.apname = apname;
    }

    public String getApname() {
        return apname;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setBssid(String bssid) {
        this.bssid = bssid;
    }

    public String getBssid() {
        return bssid;
    }

    public void setIp_addr_int(long ip_addr_int) {
        this.ip_addr_int = ip_addr_int;
    }

    public long getIp_addr_int() {
        return ip_addr_int;
    }

    public void setDownrate(String downrate) {
        this.downrate = downrate;
    }

    public String getDownrate() {
        return downrate;
    }

    public void setPpptype(String ppptype) {
        this.ppptype = ppptype;
    }

    public String getPpptype() {
        return ppptype;
    }

    public void setClient_device(String client_device) {
        this.client_device = client_device;
    }

    public String getClient_device() {
        return client_device;
    }

    public void setUprate(String uprate) {
        this.uprate = uprate;
    }

    public String getUprate() {
        return uprate;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getMac() {
        return mac;
    }

    public void setReject(int reject) {
        this.reject = reject;
    }

    public int getReject() {
        return reject;
    }

    public void setWebid(int webid) {
        this.webid = webid;
    }

    public int getWebid() {
        return webid;
    }

    public void setTotal_up(long total_up) {
        this.total_up = total_up;
    }

    public long getTotal_up() {
        return total_up;
    }

    public void setSignal(String signal) {
        this.signal = signal;
    }

    public String getSignal() {
        return signal;
    }

    public void setAc_gid(int ac_gid) {
        this.ac_gid = ac_gid;
    }

    public int getAc_gid() {
        return ac_gid;
    }

    public void setApmac(String apmac) {
        this.apmac = apmac;
    }

    public String getApmac() {
        return apmac;
    }

    public void setConnect_num(int connect_num) {
        this.connect_num = connect_num;
    }

    public int getConnect_num() {
        return connect_num;
    }

    public void setSsid(String ssid) {
        this.ssid = ssid;
    }

    public String getSsid() {
        return ssid;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public String getHostname() {
        return hostname;
    }

    public void setClient_type(String client_type) {
        this.client_type = client_type;
    }

    public String getClient_type() {
        return client_type;
    }

    public void setUpload(int upload) {
        this.upload = upload;
    }

    public int getUpload() {
        return upload;
    }

    public void setUptime(String uptime) {
        this.uptime = uptime;
    }

    public String getUptime() {
        return uptime;
    }

    public void setTotal_down(long total_down) {
        this.total_down = total_down;
    }

    public long getTotal_down() {
        return total_down;
    }

    public void setAuth_type(int auth_type) {
        this.auth_type = auth_type;
    }

    public int getAuth_type() {
        return auth_type;
    }

    public void setDtalk_name(String dtalk_name) {
        this.dtalk_name = dtalk_name;
    }

    public String getDtalk_name() {
        return dtalk_name;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getComment() {
        return comment;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setIp_addr(String ip_addr) {
        this.ip_addr = ip_addr;
    }

    public String getIp_addr() {
        return ip_addr;
    }

    public void setDownload(int download) {
        this.download = download;
    }

    public int getDownload() {
        return download;
    }

    public void setFrequencies(String frequencies) {
        this.frequencies = frequencies;
    }

    public String getFrequencies() {
        return frequencies;
    }

    @Override
    public String toString() {
        return "LanHostInfo{" +
                "apname='" + apname + '\'' +
                ", id=" + id +
                ", bssid='" + bssid + '\'' +
                ", ip_addr_int=" + ip_addr_int +
                ", downrate='" + downrate + '\'' +
                ", ppptype='" + ppptype + '\'' +
                ", client_device='" + client_device + '\'' +
                ", uprate='" + uprate + '\'' +
                ", mac='" + mac + '\'' +
                ", reject=" + reject +
                ", webid=" + webid +
                ", total_up=" + total_up +
                ", signal='" + signal + '\'' +
                ", ac_gid=" + ac_gid +
                ", apmac='" + apmac + '\'' +
                ", connect_num=" + connect_num +
                ", ssid='" + ssid + '\'' +
                ", username='" + username + '\'' +
                ", hostname='" + hostname + '\'' +
                ", client_type='" + client_type + '\'' +
                ", upload=" + upload +
                ", uptime='" + uptime + '\'' +
                ", total_down=" + total_down +
                ", auth_type=" + auth_type +
                ", dtalk_name='" + dtalk_name + '\'' +
                ", comment='" + comment + '\'' +
                ", timestamp=" + timestamp +
                ", ip_addr='" + ip_addr + '\'' +
                ", download=" + download +
                ", frequencies='" + frequencies + '\'' +
                '}';
    }
}