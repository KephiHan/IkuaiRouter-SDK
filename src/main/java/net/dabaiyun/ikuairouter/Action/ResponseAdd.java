package net.dabaiyun.ikuairouter.Action;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ResponseAdd extends IkuaiResponseBase{

    @JsonProperty("RowId")
    private Integer RowId;
    @JsonProperty("ErrKeyUnique")
    private String ErrKeyUnique;

    public ResponseAdd() {
    }

    public ResponseAdd(int result, String errMsg, Integer rowId) {
        super(result, errMsg);
        RowId = rowId;
    }

    public Integer getRowId() {
        return RowId;
    }

    public void setRowId(Integer rowId) {
        RowId = rowId;
    }

    public String getErrKeyUnique() {
        return ErrKeyUnique;
    }

    public void setErrKeyUnique(String errKeyUnique) {
        ErrKeyUnique = errKeyUnique;
    }
}
