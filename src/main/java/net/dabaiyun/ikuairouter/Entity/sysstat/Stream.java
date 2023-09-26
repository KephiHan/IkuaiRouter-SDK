
package net.dabaiyun.ikuairouter.Entity.sysstat;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Stream {

    private int connect_num;
    private long upload;
    private long download;
    private long total_up;
    private long total_down;

    public void setConnect_num(int connect_num) {
        this.connect_num = connect_num;
    }

    public int getConnect_num() {
        return connect_num;
    }

    public void setUpload(long upload) {
        this.upload = upload;
    }

    public long getUpload() {
        return upload;
    }

    public void setDownload(long download) {
        this.download = download;
    }

    public long getDownload() {
        return download;
    }

    public void setTotal_up(long total_up) {
        this.total_up = total_up;
    }

    public long getTotal_up() {
        return total_up;
    }

    public void setTotal_down(long total_down) {
        this.total_down = total_down;
    }

    public long getTotal_down() {
        return total_down;
    }

    @Override
    public String toString() {
        return "Stream{" +
                "connect_num=" + connect_num +
                ", upload=" + upload +
                ", download=" + download +
                ", total_up=" + total_up +
                ", total_down=" + total_down +
                '}';
    }
}