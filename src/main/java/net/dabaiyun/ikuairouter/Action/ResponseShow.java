package net.dabaiyun.ikuairouter.Action;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.dabaiyun.ikuairouter.Exception.IkuaiRouterException;


public class ResponseShow extends IkuaiResponseBase {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @JsonProperty("Data")
    private String Data;

    public ResponseShow() {
    }

    public ResponseShow(String jsonStr) throws Exception {
        JsonNode rootNode = objectMapper.readTree(jsonStr);
        //Result
        super.setResult(rootNode.get("Result").asInt());
        //ErrMsg
        super.setErrMsg(rootNode.get("ErrMsg").asText());
        //Data
        this.Data = rootNode.get("Data").toString();
    }

    public ResponseShow(int result, String errMsg, String data) {
        super(result, errMsg);
        Data = data;
    }

    public String getData() {
        return Data;
    }

    public void setData(String data) {
        Data = data;
    }

    @Override
    public String toString() {
        return
                "Result : " + super.getResult() + "," + " ErrMsg : " + super.getErrMsg() + "," + " Data : " + Data + ";";
    }
}
