
package net.dabaiyun.ikuairouter.Entity.sysstat;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Online_user {

    private int count;
    private int count_2g;
    private int count_5g;
    private int count_wired;
    private int count_wireless;

    public void setCount(int count) {
        this.count = count;
    }

    public int getCount() {
        return count;
    }

    public void setCount_2g(int count_2g) {
        this.count_2g = count_2g;
    }

    public int getCount_2g() {
        return count_2g;
    }

    public void setCount_5g(int count_5g) {
        this.count_5g = count_5g;
    }

    public int getCount_5g() {
        return count_5g;
    }

    public void setCount_wired(int count_wired) {
        this.count_wired = count_wired;
    }

    public int getCount_wired() {
        return count_wired;
    }

    public void setCount_wireless(int count_wireless) {
        this.count_wireless = count_wireless;
    }

    public int getCount_wireless() {
        return count_wireless;
    }

    @Override
    public String toString() {
        return "Online_user{" +
                "count=" + count +
                ", count_2g=" + count_2g +
                ", count_5g=" + count_5g +
                ", count_wired=" + count_wired +
                ", count_wireless=" + count_wireless +
                '}';
    }
}