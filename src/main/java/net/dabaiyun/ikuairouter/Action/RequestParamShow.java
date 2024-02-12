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
    private String order;
    @JsonProperty("ORDER_BY")
    private String orderBy;
    @JsonProperty("TYPE")
    private String type;

    public RequestParamShow() {
    }

    public RequestParamShow(String type) {
        this.limit = "0,1000";
        this.order = "";
        this.orderBy = "";
        this.type = type;
    }

//    public RequestParamShow(boolean isDefaultParams) {
//        if(isDefaultParams){
//            this.limit = "0,100";
//            this.order = "";
//            this.orderBy = "";
//            this.type = "data";
//        }else{
//            this.limit = "";
//            this.order = "";
//            this.orderBy = "";
//            this.type = "";
//        }
//    }

    public RequestParamShow(String limit, String order, String orderBy, String type) {
        this.limit = limit;
        this.order = order;
        this.orderBy = orderBy;
        this.type = type;
    }

    public String getLimit() {
        return limit;
    }

    public void setLimit(String limit) {
        this.limit = limit;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
