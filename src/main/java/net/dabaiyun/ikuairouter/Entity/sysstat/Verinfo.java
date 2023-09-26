
package net.dabaiyun.ikuairouter.Entity.sysstat;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Verinfo {

    private String modelname;
    private String verstring;
    private String version;
    private long build_date;
    private String arch;
    private String sysbit;
    private String verflags;
    private int is_enterprise;
    private int support_i18n;
    private int support_lcd;

    public void setModelname(String modelname) {
        this.modelname = modelname;
    }

    public String getModelname() {
        return modelname;
    }

    public void setVerstring(String verstring) {
        this.verstring = verstring;
    }

    public String getVerstring() {
        return verstring;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getVersion() {
        return version;
    }

    public void setBuild_date(long build_date) {
        this.build_date = build_date;
    }

    public long getBuild_date() {
        return build_date;
    }

    public void setArch(String arch) {
        this.arch = arch;
    }

    public String getArch() {
        return arch;
    }

    public void setSysbit(String sysbit) {
        this.sysbit = sysbit;
    }

    public String getSysbit() {
        return sysbit;
    }

    public void setVerflags(String verflags) {
        this.verflags = verflags;
    }

    public String getVerflags() {
        return verflags;
    }

    public void setIs_enterprise(int is_enterprise) {
        this.is_enterprise = is_enterprise;
    }

    public int getIs_enterprise() {
        return is_enterprise;
    }

    public void setSupport_i18n(int support_i18n) {
        this.support_i18n = support_i18n;
    }

    public int getSupport_i18n() {
        return support_i18n;
    }

    public void setSupport_lcd(int support_lcd) {
        this.support_lcd = support_lcd;
    }

    public int getSupport_lcd() {
        return support_lcd;
    }

    @Override
    public String toString() {
        return "Verinfo{" +
                "modelname='" + modelname + '\'' +
                ", verstring='" + verstring + '\'' +
                ", version='" + version + '\'' +
                ", build_date=" + build_date +
                ", arch='" + arch + '\'' +
                ", sysbit='" + sysbit + '\'' +
                ", verflags='" + verflags + '\'' +
                ", is_enterprise=" + is_enterprise +
                ", support_i18n=" + support_i18n +
                ", support_lcd=" + support_lcd +
                '}';
    }
}