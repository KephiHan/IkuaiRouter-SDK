package net.dabaiyun.ikuairouter.Action;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class IkuaiResponseBase {

    @JsonProperty("Result")
    private int Result;
    @JsonProperty("ErrMsg")
    private String ErrMsg;

    /**
     * 登入是否成功，Result码 30000成功 其他失败
     * @return 成功与否
     */
    public boolean isSuccess(){
        return Result == 30000;
    }

    public boolean isAuthFail(){
        return Result == 10014;
    }

    public IkuaiResponseBase() {
    }

    public IkuaiResponseBase(int result, String errMsg) {
        Result = result;
        ErrMsg = errMsg;
    }

    public int getResult() {
        return Result;
    }

    public void setResult(int result) {
        Result = result;
    }

    public String getErrMsg() {
        return ErrMsg;
    }

    public void setErrMsg(String errMsg) {
        ErrMsg = errMsg;
    }
}
