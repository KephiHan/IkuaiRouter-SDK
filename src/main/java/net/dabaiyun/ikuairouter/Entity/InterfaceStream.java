package net.dabaiyun.ikuairouter.Entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class InterfaceStream {

    @JsonProperty("interface")
    private String inter_face;

    private String comment;
    private String ip_addr;
    private String connect_num;
    private long upload;
    private long download;
    private long total_up;
    private long total_down;
    private long updropped;
    private long downdropped;
    private long uppacked;
    private long downpacked;

    public String getInter_face() {
        return inter_face;
    }

    public void setInter_face(String inter_face) {
        this.inter_face = inter_face;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getIp_addr() {
        return ip_addr;
    }

    public void setIp_addr(String ip_addr) {
        this.ip_addr = ip_addr;
    }

    public String getConnect_num() {
        return connect_num;
    }

    public void setConnect_num(String connect_num) {
        this.connect_num = connect_num;
    }

    public long getUpload() {
        return upload;
    }

    public void setUpload(long upload) {
        this.upload = upload;
    }

    public long getDownload() {
        return download;
    }

    public void setDownload(long download) {
        this.download = download;
    }

    public long getTotal_up() {
        return total_up;
    }

    public void setTotal_up(long total_up) {
        this.total_up = total_up;
    }

    public long getTotal_down() {
        return total_down;
    }

    public void setTotal_down(long total_down) {
        this.total_down = total_down;
    }

    public long getUpdropped() {
        return updropped;
    }

    public void setUpdropped(long updropped) {
        this.updropped = updropped;
    }

    public long getDowndropped() {
        return downdropped;
    }

    public void setDowndropped(long downdropped) {
        this.downdropped = downdropped;
    }

    public long getUppacked() {
        return uppacked;
    }

    public void setUppacked(long uppacked) {
        this.uppacked = uppacked;
    }

    public long getDownpacked() {
        return downpacked;
    }

    public void setDownpacked(long downpacked) {
        this.downpacked = downpacked;
    }
}
