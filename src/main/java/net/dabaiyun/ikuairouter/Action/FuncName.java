package net.dabaiyun.ikuairouter.Action;

public enum FuncName {
    //Main_Page
    homepage,
    //Statu Montior
    monitor_iface,
    monitor_lanip,
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
    //ERROR
    Error;



//    private String str;
//
//    IkuaiFuncName(String str){
//        this.str = str;
//    }
//
//    /**
//     * 根据文本返回状态枚举
//     * @param s
//     * @return
//     */
//    public static IkuaiFuncName getFuncNameByString(String s){
//        switch (s){
//            case "homepage":{
//                return HOMEPAGE;
//            }
//            case "monitor_iface":{
//                return MONITOR_IFACE;
//            }
//            case "monitor_lanip":{
//                return MONITOR_LANIP;
//            }
//            case "dhcp_server":{
//                return DHCP_SERVER;
//            }
//            case "dhcp_lease":{
//                return DHCP_LEASE;
//            }
//            case "dnat":{
//                return DNAT;
//            }
//            default:{
//                return ERROR;
//            }
//        }
//    }
}
