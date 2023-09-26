package net.dabaiyun.ikuairouter.Action;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.dabaiyun.ikuairouter.Exception.IkuaiRouterException;


public class ResponseShow extends IkuaiResponseBase {

    //    private int Result;
//    private String ErrMsg;
    @JsonProperty("Data")
    private String Data;

    /**
     * Generate Fields By JSON Data
     *
     * @param jsonStr JSON String
     * @throws Exception IkuaiPostException, JsonProcessingException
     */
    public void generateFieldsByJsonString(String jsonStr) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(jsonStr);
        //Result
        if (rootNode.has("Result")) {
            super.setResult(rootNode.get("Result").asInt());
        } else {
            throw new IkuaiRouterException("Result");
        }
        //ErrMsg
        if (rootNode.has("ErrMsg")) {
            super.setErrMsg(rootNode.get("ErrMsg").asText());
        } else {
            throw new IkuaiRouterException("ErrMsg");
        }
        //Data
        if (rootNode.has("Data")) {
            Data = rootNode.get("Data").toString();
        } else {
            throw new IkuaiRouterException("Data");
        }
    }

    public ResponseShow() {
    }

    public ResponseShow(String jsonStr) throws Exception {
        generateFieldsByJsonString(jsonStr);
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
