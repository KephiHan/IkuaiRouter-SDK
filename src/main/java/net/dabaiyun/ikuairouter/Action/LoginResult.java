package net.dabaiyun.ikuairouter.Action;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class LoginResult extends IkuaiResponseBase{

    /**
     * 登入是否成功，Result码 10000成功 其他失败
     * @return 成功与否
     */
    @Override
    public boolean isSuccess(){
        return super.getResult() == 10000;
    }

    public LoginResult() {
    }

    public LoginResult(int result, String errMsg) {
        super(result, errMsg);
    }

    @Override
    public String toString(){
        return
                "Result : " + super.getResult() + "," + " ErrMsg : " + super.getErrMsg() + ";";
    }
}
