package net.dabaiyun.ikuairouter.Entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PPPPackage {

    private int id;
    private String packname;
    private String packtime;
    private int price;
    private int up_speed;
    private int down_speed;
    private String comment;

    public PPPPackage() {
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getPackname() { return packname; }
    public void setPackname(String packname) { this.packname = packname; }

    public String getPacktime() { return packtime; }
    public void setPacktime(String packtime) { this.packtime = packtime; }

    public int getPrice() { return price; }
    public void setPrice(int price) { this.price = price; }

    public int getUp_speed() { return up_speed; }
    public void setUp_speed(int up_speed) { this.up_speed = up_speed; }

    public int getDown_speed() { return down_speed; }
    public void setDown_speed(int down_speed) { this.down_speed = down_speed; }

    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }

    @Override
    public String toString() {
        return "PPPPackage{" +
                "id=" + id +
                ", packname='" + packname + "'" +
                ", packtime='" + packtime + "'" +
                ", price=" + price +
                ", up_speed=" + up_speed +
                ", down_speed=" + down_speed +
                ", comment='" + comment + "'" +
                '}';
    }
}
