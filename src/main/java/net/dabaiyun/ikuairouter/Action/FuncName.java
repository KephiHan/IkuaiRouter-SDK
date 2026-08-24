package net.dabaiyun.ikuairouter.Action;

import com.fasterxml.jackson.annotation.JsonValue;

public enum FuncName {
    //Main_Page
    homepage,
    //Statu Montior
    monitor_iface,
    monitor_lanip,
    //LAN
    lan,
    //WAN
    wan,
    //DHCP Settings
    dhcp_server,
    //DHCP_Static_Allocate
    dhcp_lease,
    //Port Mapping
    dnat,
    //QosLimit
    simple_qos,
    //Plugins
    plugins,
    //PPP User (认证计费用户)
    pppuser,
    //PPP Online (认证在线用户)
    ppp_online,
    //PPP Package (计费套餐)
    ppp_package,
    //PPPoE Server
    pppoe_server,
    //OpenVPN Server (含横杠，需 @JsonValue)
    openvpn_server("openvpn-server"),
    //ERROR
    Error;

    private String value;

    FuncName() {
        this.value = null;
    }

    FuncName(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value != null ? value : name();
    }
}
