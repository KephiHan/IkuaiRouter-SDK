
package net.dabaiyun.ikuairouter.Entity.sysstat;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Memory {

    private long total;
    private long available;
    private long free;
    private long cached;
    private long buffers;
    private String used;

    public void setTotal(long total) {
        this.total = total;
    }

    public long getTotal() {
        return total;
    }

    public void setAvailable(long available) {
        this.available = available;
    }

    public long getAvailable() {
        return available;
    }

    public void setFree(long free) {
        this.free = free;
    }

    public long getFree() {
        return free;
    }

    public void setCached(long cached) {
        this.cached = cached;
    }

    public long getCached() {
        return cached;
    }

    public void setBuffers(long buffers) {
        this.buffers = buffers;
    }

    public long getBuffers() {
        return buffers;
    }

    public void setUsed(String used) {
        this.used = used;
    }

    public String getUsed() {
        return used;
    }

    @Override
    public String toString() {
        return "Memory{" +
                "total=" + total +
                ", available=" + available +
                ", free=" + free +
                ", cached=" + cached +
                ", buffers=" + buffers +
                ", used='" + used + '\'' +
                '}';
    }
}