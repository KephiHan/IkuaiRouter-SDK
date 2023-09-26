package net.dabaiyun.ikuairouter.Action;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RequestInfo {

        //Field

    @JsonProperty("action")
    private ActionType action = ActionType.show;//Default setting : SHOW
    @JsonProperty("func_name")
    private FuncName func_name = FuncName.homepage;//Default setting : HOMEPAGE
    @JsonProperty("param")
    private Object param = new Object();

        //Custon Function

//    public void addParam(String key, String value){
//        this.param.put(key, value);
//    }
//
//    public String getParam(String key){
//        return this.param.get(key);
//    }
//
//    public String removeParam(String key){
//        return this.param.remove(key);
//    }

        //Constructor

    public RequestInfo() {
    }

    public RequestInfo(ActionType action, FuncName func_name) {
        this.action = action;
        this.func_name = func_name;
    }

        //Getter and Setter

    public ActionType getAction() {
        return action;
    }

    public void setAction(ActionType action) {
        this.action = action;
    }

    public FuncName getFunc_name() {
        return func_name;
    }

    public void setFunc_name(FuncName func_name) {
        this.func_name = func_name;
    }

    public Object getParam() {
        return param;
    }

    public void setParam(Object param) {
        this.param = param;
    }
}
