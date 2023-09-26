package net.dabaiyun.ikuairouter.Action;

public class LoginPostInfo {

    private String pass;
    private String passwd;
    private String remember_password;
    private String username;

    public LoginPostInfo() {
    }

    public LoginPostInfo(String pass, String passwd, String remember_password, String username) {
        this.pass = pass;
        this.passwd = passwd;
        this.remember_password = remember_password;
        this.username = username;
    }

    public LoginPostInfo(String pass, String passwd, String username) {
        this.pass = pass;
        this.passwd = passwd;
        this.remember_password = "";
        this.username = username;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

    public String getRemember_password() {
        return remember_password;
    }

    public void setRemember_password(String remember_password) {
        this.remember_password = remember_password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
