package net.dabaiyun.ikuairouter.Action;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Use as param when 'show' action. And find by some options.
 * Transform to jsonString before http post
 */
public class RequestParamFind extends RequestParamShow{

    public static final String FINDS_ID = "id";
    public static final String FINDS_IP_ADDR = "ip_addr";
    public static final String FINDS_MAC = "mac";
    public static final String FINDS_LAN_ADDR = "lan_addr";
    public static final String FINDS_LAN_PORT = "lan_port";
    public static final String FINDS_WAN_PORT = "wan_port";
    public static final String FINDS_INTERFACE = "interface";
    public static final String FINDS_COMMENT = "comment";

    @JsonProperty("FINDS")
    private String finds;
    @JsonProperty("KEYWORDS")
    private String keywords;

    public RequestParamFind(String type) {
        super(type);
    }

    public String getFinds() {
        return finds;
    }

    public void setFinds(String... finds) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < finds.length; i++) {
            if(i != 0){
                stringBuilder.append(",");
            }
            stringBuilder.append(finds[i]);
        }
        this.finds = stringBuilder.toString();
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }
}
