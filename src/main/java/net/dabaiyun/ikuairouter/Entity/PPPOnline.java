package net.dabaiyun.ikuairouter.Entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PPPOnline {

    private int id;
    private String username;
    private String ppptype;
    private String pppdev;
    @JsonProperty("interface")
    private String inter_face;
    private String ip_addr;
    private String ip_addr_int;
    private String mac;
    private String session;
    private long auth_time;
    private int upload;
    private int download;
    private long expires;
    private int packages;
    private String packname;
    private String phone;
    private String name;
    private String comment;
    private int webid;
    private String uid;
    private int check_vlan_res;

    public PPPOnline() {
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPpptype() { return ppptype; }
    public void setPpptype(String ppptype) { this.ppptype = ppptype; }

    public String getPppdev() { return pppdev; }
    public void setPppdev(String pppdev) { this.pppdev = pppdev; }

    public String getInter_face() { return inter_face; }
    public void setInter_face(String inter_face) { this.inter_face = inter_face; }

    public String getIp_addr() { return ip_addr; }
    public void setIp_addr(String ip_addr) { this.ip_addr = ip_addr; }

    public String getIp_addr_int() { return ip_addr_int; }
    public void setIp_addr_int(String ip_addr_int) { this.ip_addr_int = ip_addr_int; }

    public String getMac() { return mac; }
    public void setMac(String mac) { this.mac = mac; }

    public String getSession() { return session; }
    public void setSession(String session) { this.session = session; }

    public long getAuth_time() { return auth_time; }
    public void setAuth_time(long auth_time) { this.auth_time = auth_time; }

    public int getUpload() { return upload; }
    public void setUpload(int upload) { this.upload = upload; }

    public int getDownload() { return download; }
    public void setDownload(int download) { this.download = download; }

    public long getExpires() { return expires; }
    public void setExpires(long expires) { this.expires = expires; }

    public int getPackages() { return packages; }
    public void setPackages(int packages) { this.packages = packages; }

    public String getPackname() { return packname; }
    public void setPackname(String packname) { this.packname = packname; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }

    public int getWebid() { return webid; }
    public void setWebid(int webid) { this.webid = webid; }

    public String getUid() { return uid; }
    public void setUid(String uid) { this.uid = uid; }

    public int getCheck_vlan_res() { return check_vlan_res; }
    public void setCheck_vlan_res(int check_vlan_res) { this.check_vlan_res = check_vlan_res; }

    @Override
    public String toString() {
        return "PPPOnline{" +
                "id=" + id +
                ", username='" + username + "'" +
                ", ppptype='" + ppptype + "'" +
                ", pppdev='" + pppdev + "'" +
                ", inter_face='" + inter_face + "'" +
                ", ip_addr='" + ip_addr + "'" +
                ", ip_addr_int='" + ip_addr_int + "'" +
                ", mac='" + mac + "'" +
                ", session='" + session + "'" +
                ", auth_time=" + auth_time +
                ", upload=" + upload +
                ", download=" + download +
                ", expires=" + expires +
                ", packages=" + packages +
                ", packname='" + packname + "'" +
                ", phone='" + phone + "'" +
                ", name='" + name + "'" +
                ", comment='" + comment + "'" +
                ", webid=" + webid +
                ", uid='" + uid + "'" +
                ", check_vlan_res=" + check_vlan_res +
                '}';
    }
}
