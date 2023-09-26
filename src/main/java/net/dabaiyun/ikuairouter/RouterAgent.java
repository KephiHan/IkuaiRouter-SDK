package net.dabaiyun.ikuairouter;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.dabaiyun.ikuairouter.Action.*;
import net.dabaiyun.ikuairouter.Entity.DHCPStatic;
import net.dabaiyun.ikuairouter.Entity.NetMapping;
import net.dabaiyun.ikuairouter.Entity.QosLimit;
import net.dabaiyun.ikuairouter.Exception.IkuaiRouterException;
import net.dabaiyun.ikuairouter.HttpApi.TrustAllCertOkHttpClient;
import okhttp3.*;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;

public class RouterAgent {
    //Tools
    private final ObjectMapper objectMapper = new ObjectMapper();

    //Field

    //Router base infomation
    private String address;
    private int port;
    private boolean https;
    private String username;
    private String pwd;
    //CookieStore
    private HashMap<String, List<Cookie>> cookieStore;

    //Default constructor
    public RouterAgent() {
        cookieStore = new HashMap<>();
    }

    //Constructor with base info
    public RouterAgent(String address, int port, boolean https, String username, String pwd) {
        this.address = address;
        this.port = port;
        this.https = https;
        this.username = username;
        this.pwd = pwd;
        this.cookieStore = new HashMap<>();
    }

    public RouterAgent(String address, int port, boolean https, String username, String pwd, HashMap<String, List<Cookie>> cookieStore) {
        this.address = address;
        this.port = port;
        this.https = https;
        this.username = username;
        this.pwd = pwd;
        this.cookieStore = cookieStore;
    }

    public RouterAgent(String address, int port, boolean https, String username, String pwd, String sess_key) {
        this.address = address;
        this.port = port;
        this.https = https;
        this.username = username;
        this.pwd = pwd;
        //生成cookie存入store
        Cookie cookie = new Cookie.Builder()
                .domain(address)
                .name("sess_key")
                .value(sess_key)
                .path("/")
                .expiresAt(System.currentTimeMillis() + 1000L * 3600 * 24 * 365)
                .build();
        List<Cookie> cookieList = new ArrayList<>();
        cookieList.add(cookie);
        cookieStore = new HashMap<>();
        cookieStore.put(address, cookieList);
    }

    //Getter && Setter

    public String getSessKey(){
        if (!cookieStore.containsKey(address)) {
            return "";
        }
        for (Cookie cookie : cookieStore.get(address)) {
            if (cookie.name().equals("sess_key")) {
                return cookie.value();
            }
        }
        return "";
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public boolean isHttps() {
        return https;
    }

    public void setHttps(boolean https) {
        this.https = https;
    }

    public HashMap<String, List<Cookie>> getCookieStore() {
        return cookieStore;
    }

    public void setCookieStore(HashMap<String, List<Cookie>> cookieStore) {
        this.cookieStore = cookieStore;
    }

    //Custon Function

    /**
     * 登入到ikaui路由并获取sess_key及相关cookie
     * Login to Ikuai and get Cookies
     *
     * @return 登入成功与否 Is login seccess?
     */
    public LoginResult login() throws Exception {
        //Check info are not null
        if (!(address != null && port != 0 && username != null && pwd != null)) {
            return new LoginResult(0,"缺失认证表单数据");
        }

        //Generate post infomation
        String password;//md5hash Encoded string
        String pass;//Base64 Encoded string
        //getPass
        String str = "salt_11" + pwd;
        pass = Base64.getEncoder().encodeToString(str.getBytes(StandardCharsets.UTF_8));
        //getPasswd
        // MessageDigest instance for MD5
        MessageDigest md = MessageDigest.getInstance("MD5");
        // Update MessageDigest with input text in bytes
        md.update(pwd.getBytes(StandardCharsets.UTF_8));
        // Get the hashbytes
        byte[] hashBytes = md.digest();
        // Convert hash bytes to hex format
        StringBuilder sb = new StringBuilder();
        for (byte b : hashBytes) {
            sb.append(String.format("%02x", b));
        }
        //Result
        password = sb.toString();
        //LoginInfoObject
        LoginPostInfo ikuaiLoginPostInfo
                = new LoginPostInfo(
                pass,
                password,
                username
        );

        //Initializ HTTPUtil
        String url_login =
                (https ? "https" : "http") +
                        "://" + address +
                        ":" + port +
                        "/Action/login";

        OkHttpClient okHttpClient = TrustAllCertOkHttpClient.getTrustAllCertOkHttpClient(cookieStore);

        MediaType JSONType = MediaType.parse("application/json; charset=utf-8");

        RequestBody requestBody = RequestBody.create(
                objectMapper.writeValueAsString(ikuaiLoginPostInfo),
                JSONType
        );

        Request request = new Request.Builder()
                .url(url_login)
                .post(requestBody)
                .build();

        Call call = okHttpClient.newCall(request);

        Response response = call.execute();

        String response_string = response.body().string();

        LoginResult loginResult = objectMapper.readValue(
                response_string,
                new TypeReference<LoginResult>() {}
        );

        return loginResult;
    }


    //================ Other Functions ==========================

    /**
     * Get System Plugins
     * @return
     * @throws Exception
     */
    public ResponseShow getPlugins() throws Exception {
        RequestParamShow param = new RequestParamShow("data");
        return new ResponseShow(executeAction(ActionType.show, FuncName.plugins, param));
    }

    //================ Show Functions ==========================

    /**
     * Get Interface Snapshoot JsonString
     *
     * @return Interface Snapshoot JsonString
     * @throws Exception ex
     */
    public ResponseShow getInterfaceSnapshoot() throws Exception {
        //Params Object
        RequestParamShow param = new RequestParamShow("snapshoot");
        //do post
        return new ResponseShow(executeAction(ActionType.show, FuncName.monitor_iface, param));
    }

    /**
     * GetSystemStatus
     *
     * @return SystemStatus JsonString
     * @throws Exception ex
     */
    public ResponseShow getSystemStatus() throws Exception {
        //Params Object
        RequestParamShow param = new RequestParamShow("sysstat");
        //do post
        String resp = executeAction(ActionType.show, FuncName.homepage, param);
//        System.out.println(resp);
        return new ResponseShow(resp);
    }

    /**
     * Get LanHost Status
     *
     * @return LanHostStatu jsonString
     * @throws Exception ex
     */
    public ResponseShow getLanHostStatus() throws Exception {
        //Params Object
        RequestParamShow param = new RequestParamShow("data");
        //do post
        return new ResponseShow(executeAction(ActionType.show, FuncName.monitor_lanip, param));
    }

    /**
     * Get DHCPServers
     *
     * @return DHCPServers jsonString
     * @throws Exception ex
     */
    public ResponseShow getDHCPServers() throws Exception {
        //Params Object
        RequestParamShow param = new RequestParamShow("data");
        //do post
        return new ResponseShow(executeAction(ActionType.show, FuncName.dhcp_server, param));
    }

    /**
     * Get DHCPStaticList
     *
     * @return DHCPStaticList jsonString
     * @throws Exception ex
     */
    public ResponseShow getDHCPStatics() throws Exception {
        //Params Object
        RequestParamShow param = new RequestParamShow("static_data");
        //do post
        return new ResponseShow(executeAction(ActionType.show, FuncName.dhcp_lease, param));
    }

    /**
     * Get DHCPhosts list
     * @return jsonString
     * @throws Exception
     */
    public ResponseShow getDHCPHosts() throws Exception{
        //Params Object
        RequestParamShow param = new RequestParamShow("data");
        //do post
        return new ResponseShow(executeAction(ActionType.show, FuncName.dhcp_lease, param));
    }

    /**
     * Get NetMappingList
     *
     * @return NetMappingList jsonString
     * @throws Exception ex
     */
    public ResponseShow getNetMapping() throws Exception {
        //Params Object
        RequestParamShow param = new RequestParamShow("data");
        //do post
        return new ResponseShow(executeAction(ActionType.show, FuncName.dnat, param));
    }

    /**
     * Get QosLimitList
     *
     * @return QosLimitList jsonString
     * @throws Exception ex
     */
    public ResponseShow getQosLimit() throws Exception {
        RequestParamShow param = new RequestParamShow("data");
        return new ResponseShow(executeAction(ActionType.show,FuncName.simple_qos,param));
    }


    //================ Add Functions ==========================


    /**
     * Add DHCPStatic by DHCPStatic Object
     * @param dhcpStatic DHCPStatic Object
     * @return Response Object
     * @throws Exception e
     */
    public ResponseAdd addDHCPStatic(DHCPStatic dhcpStatic) throws Exception {
        return objectMapper.readValue(
                executeAction(
                        ActionType.add,
                        FuncName.dhcp_lease,
                        dhcpStatic
                ),
                ResponseAdd.class
        );
    }

    /**
     * Add QosLimit by QosLimit Object
     * @param qosLimit QosLimit Object
     * @return Response Object
     * @throws Exception e
     */
    public ResponseAdd addQosLimit(QosLimit qosLimit) throws Exception{
        return objectMapper.readValue(
                executeAction(
                        ActionType.add,
                        FuncName.simple_qos,
                        qosLimit
                ),
                ResponseAdd.class
        );
    }

    /**
     * Add NetMapping by NetMapping Object
     * @param netMapping NetMapping Object
     * @return Response Object
     * @throws Exception e
     */
    public ResponseAdd addNetMapping(NetMapping netMapping) throws Exception{
        return objectMapper.readValue(
                executeAction(
                        ActionType.add,
                        FuncName.dnat,
                        netMapping
                ),
                ResponseAdd.class
        );
    }


    //================ Edit Functions ==========================


    /**
     * Edit DHCPStatic
     * @param newDhcpStatic  New DHCPStatic Object
     * @return Response Object
     * @throws Exception e
     */
    public ResponseEdit editDHCPStatic(DHCPStatic newDhcpStatic) throws Exception {
        return objectMapper.readValue(
                executeAction(
                        ActionType.edit,
                        FuncName.dhcp_lease,
                        newDhcpStatic
                ),
                ResponseEdit.class
        );
    }

    /**
     * Edit QosLimit
     * @param qosLimit  New QosLimit Object
     * @return Response Object
     * @throws Exception e
     */
    public ResponseEdit editQosLimit(QosLimit qosLimit) throws Exception{
        return objectMapper.readValue(
                executeAction(
                        ActionType.edit,
                        FuncName.simple_qos,
                        qosLimit
                ),
                ResponseEdit.class
        );
    }

    /**
     * Edit NetMapping
     * @param netMapping  New NetMapping Object
     * @return Response Object
     * @throws Exception e
     */
    public ResponseEdit editNetMapping(NetMapping netMapping) throws Exception{
        return objectMapper.readValue(
                executeAction(
                        ActionType.edit,
                        FuncName.dnat,
                        netMapping
                ),
                ResponseEdit.class
        );
    }


    //================ Down Functions ==========================

    /**
     * Down DHCPStatic By id
     * @param id Target ID
     * @return Response Object
     * @throws Exception e
     */
    public ResponseDown downDHCPStatic(int id)throws Exception{
        return objectMapper.readValue(
                executeAction(
                        ActionType.down,
                        FuncName.dhcp_lease,
                        new RequestParamDown(id)
                ),
                ResponseDown.class
        );
    }

    /**
     * Down QosLimit
     * @param id QosLimit_id
     * @return Response Object
     * @throws Exception e
     */
    public ResponseDown downQosLimit(int id) throws Exception{
        return objectMapper.readValue(
                executeAction(
                        ActionType.down,
                        FuncName.simple_qos,
                        new RequestParamDown(id)
                ),
                ResponseDown.class
        );
    }

    /**
     * Down NetMapping
     * @param id NetMapping_id
     * @return Response Object
     * @throws Exception e
     */
    public ResponseDown downNetMapping(int id) throws Exception{
        return objectMapper.readValue(
                executeAction(
                        ActionType.down,
                        FuncName.dnat,
                        new RequestParamDown(id)
                ),
                ResponseDown.class
        );
    }


    //================ Delete Functions ==========================


    /**
     * Delete DHCPStatic By id
     * @param id Target ID
     * @return Response Object
     * @throws Exception e
     */
    public ResponseDel delDHCPStatic(int id) throws Exception{
        return objectMapper.readValue(
                executeAction(
                        ActionType.del,
                        FuncName.dhcp_lease,
                        new RequestParamDel(id)
                ),
                ResponseDel.class
        );
    }

    /**
     * Delete QosLimit By id
     * @param id Target ID
     * @return Response Object
     * @throws Exception e
     */
    public ResponseDel delQosLimit(int id) throws Exception{
        return objectMapper.readValue(
                executeAction(
                        ActionType.del,
                        FuncName.simple_qos,
                        new RequestParamDel(id)
                ),
                ResponseDel.class
        );
    }

    /**
     * Delete NetMapping by id
     * @param id Id
     * @return Response Object
     * @throws Exception e
     */
    public ResponseDel delNetMapping(int id) throws Exception{
        return objectMapper.readValue(
                executeAction(
                        ActionType.del,
                        FuncName.dnat,
                        new RequestParamDel(id)
                ),
                ResponseDel.class
        );
    }


    //================ Private Functions ==========================

    /**
     * Post Proxy for url /Action/call. Designed to simplify the code.
     * @param actionType
     * @param funcName
     * @param param
     * @return
     * @throws Exception
     */
    private String executeAction(ActionType actionType,
                                 FuncName funcName,
                                 Object param
    ) throws Exception {
        //Generate url
        String url =
                (https ? "https" : "http") +
                        "://" + address +
                        ":" + port +
                        "/Action/call";

        OkHttpClient okHttpClient = TrustAllCertOkHttpClient.getTrustAllCertOkHttpClient(cookieStore);

        MediaType JSONType = MediaType.parse("application/json; charset=utf-8");

        RequestInfo requestInfo = new RequestInfo(actionType,funcName);
        requestInfo.setParam(param);

        RequestBody requestBody = RequestBody.create(
                objectMapper.writeValueAsString(requestInfo),
                JSONType
        );

        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();

        Call call = okHttpClient.newCall(request);

        Response response = call.execute();

        if(!response.isSuccessful()){
            throw new IkuaiRouterException("Error orrcued when executeAction");
        }

        String resp = response.body().string();

        response.close();

        //预检查相应
        IkuaiResponseBase responseBase = objectMapper.readValue(
                resp,
                new TypeReference<IkuaiResponseBase>() {}
        );
//        //相应身份过期
//        if (responseBase.isAuthFail()) {
//            throw new IkuaiRouterNoAuthException(responseBase.getErrMsg());
//        }
        //响应不成功
        if (!responseBase.isSuccess()) {
            throw new IkuaiRouterException(responseBase.getErrMsg());
        }
        return resp;
    }
}
