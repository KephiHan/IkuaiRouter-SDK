package net.dabaiyun.ikuairouter.Action;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Use as param when 'show' action.
 * Transform to jsonString before http post
 */
public class RequestParamShow {

    @JsonProperty("limit")
    private String limit;
    @JsonProperty("ORDER")
    private String ORDER;
    @JsonProperty("ORDER_BY")
    private String ORDER_BY;
    @JsonProperty("TYPE")
    private String TYPE;

    public RequestParamShow() {
    }

    public RequestParamShow(String TYPE) {
        this.limit = "0,1000";
        this.ORDER = "";
        this.ORDER_BY = "";
        this.TYPE = TYPE;
    }

    public RequestParamShow(boolean isDefaultParams) {
        if(isDefaultParams){
            this.limit = "0,100";
            this.ORDER = "";
            this.ORDER_BY = "";
            this.TYPE = "data";
        }else{
            this.limit = "";
            this.ORDER = "";
            this.ORDER_BY = "";
            this.TYPE = "";
        }
    }

    public RequestParamShow(String limit, String ORDER, String ORDER_BY, String TYPE) {
        this.limit = limit;
        this.ORDER = ORDER;
        this.ORDER_BY = ORDER_BY;
        this.TYPE = TYPE;
    }

    public String getLimit() {
        return limit;
    }

    public void setLimit(String limit) {
        this.limit = limit;
    }

    public String getORDER() {
        return ORDER;
    }

    public void setORDER(String ORDER) {
        this.ORDER = ORDER;
    }

    public String getORDER_BY() {
        return ORDER_BY;
    }

    public void setORDER_BY(String ORDER_BY) {
        this.ORDER_BY = ORDER_BY;
    }

    public String getTYPE() {
        return TYPE;
    }

    public void setTYPE(String TYPE) {
        this.TYPE = TYPE;
    }
}
