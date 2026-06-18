package net.dabaiyun.ikuairouter.Action;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class IkuaiResponseBase {

    @JsonProperty("Result")
    private int result;
    @JsonProperty("ErrMsg")
    private String errMsg;

    /**
     * 登入是否成功，Result码 30000成功 其他失败
     * @return 成功与否
     */
    public boolean isSuccess(){
        return result == 30000;
    }

    public boolean isAuthFail(){
        return result == 10014;
    }

    public IkuaiResponseBase() {
    }

    public IkuaiResponseBase(int result, String errMsg) {
        this.result = result;
        this.errMsg = errMsg;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }
}
