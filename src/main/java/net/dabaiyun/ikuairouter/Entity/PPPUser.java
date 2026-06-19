package net.dabaiyun.ikuairouter.Entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PPPUser {

    private int id;
    private String username;
    private String passwd;
    private String enabled;
    private String ppptype;
    private String pppname;
    private String bind_ifname;
    private int share;
    private int auto_mac;
    private int upload;
    private int download;
    private int ip_type;
    private String ip_addr;
    private String mac;
    private String address;
    private String name;
    private String phone;
    private String cardid;
    private String comment;
    private int packages;
    private long duration;
    private long expires;
    private long start_time;
    private long create_time;
    private long last_conntime;
    private long last_offtime;
    private String bind_vlanid;
    private int auto_vlanid;
    private String proxy_username;
    private String pppoev6_wan;

    public static class EnabledType {
        public static final String YES = "yes";
        public static final String NO = "no";
    }

    public static class PPPType {
        public static final String ANY = "any";
        public static final String OVPN = "ovpn";
        public static final String PPPOE = "pppoe";
    }

    public PPPUser() {
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPasswd() { return passwd; }
    public void setPasswd(String passwd) { this.passwd = passwd; }

    public String getEnabled() { return enabled; }
    public void setEnabled(String enabled) { this.enabled = enabled; }

    public String getPpptype() { return ppptype; }
    public void setPpptype(String ppptype) { this.ppptype = ppptype; }

    public String getPppname() { return pppname; }
    public void setPppname(String pppname) { this.pppname = pppname; }

    public String getBind_ifname() { return bind_ifname; }
    public void setBind_ifname(String bind_ifname) { this.bind_ifname = bind_ifname; }

    public int getShare() { return share; }
    public void setShare(int share) { this.share = share; }

    public int getAuto_mac() { return auto_mac; }
    public void setAuto_mac(int auto_mac) { this.auto_mac = auto_mac; }

    public int getUpload() { return upload; }
    public void setUpload(int upload) { this.upload = upload; }

    public int getDownload() { return download; }
    public void setDownload(int download) { this.download = download; }

    public int getIp_type() { return ip_type; }
    public void setIp_type(int ip_type) { this.ip_type = ip_type; }

    public String getIp_addr() { return ip_addr; }
    public void setIp_addr(String ip_addr) { this.ip_addr = ip_addr; }

    public String getMac() { return mac; }
    public void setMac(String mac) { this.mac = mac; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getCardid() { return cardid; }
    public void setCardid(String cardid) { this.cardid = cardid; }

    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }

    public int getPackages() { return packages; }
    public void setPackages(int packages) { this.packages = packages; }

    public long getDuration() { return duration; }
    public void setDuration(long duration) { this.duration = duration; }

    public long getExpires() { return expires; }
    public void setExpires(long expires) { this.expires = expires; }

    public long getStart_time() { return start_time; }
    public void setStart_time(long start_time) { this.start_time = start_time; }

    public long getCreate_time() { return create_time; }
    public void setCreate_time(long create_time) { this.create_time = create_time; }

    public long getLast_conntime() { return last_conntime; }
    public void setLast_conntime(long last_conntime) { this.last_conntime = last_conntime; }

    public long getLast_offtime() { return last_offtime; }
    public void setLast_offtime(long last_offtime) { this.last_offtime = last_offtime; }

    public String getBind_vlanid() { return bind_vlanid; }
    public void setBind_vlanid(String bind_vlanid) { this.bind_vlanid = bind_vlanid; }

    public int getAuto_vlanid() { return auto_vlanid; }
    public void setAuto_vlanid(int auto_vlanid) { this.auto_vlanid = auto_vlanid; }

    public String getProxy_username() { return proxy_username; }
    public void setProxy_username(String proxy_username) { this.proxy_username = proxy_username; }

    public String getPppoev6_wan() { return pppoev6_wan; }
    public void setPppoev6_wan(String pppoev6_wan) { this.pppoev6_wan = pppoev6_wan; }

    @Override
    public String toString() {
        return "PPPUser{" +
                "id=" + id +
                ", username='" + username + "'" +
                ", enabled='" + enabled + "'" +
                ", ppptype='" + ppptype + "'" +
                ", ip_addr='" + ip_addr + "'" +
                ", name='" + name + "'" +
                ", comment='" + comment + "'" +
                '}';
    }
}
