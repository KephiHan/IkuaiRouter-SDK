package net.dabaiyun.ikuairouter.Entity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;

public class LanHostInfoList {

    //Data
    private List<LanHostInfo> list = new ArrayList<>();

    /**
     * Generate Fields By Json String
     * @param jsonStr
     * @throws JsonProcessingException
     */
    public void generateFieldsByJsonStr(String jsonStr) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        list = objectMapper.readValue(jsonStr, new TypeReference<List<LanHostInfo>>() {});
    }

    public int size(){
        return list.size();
    }

    public void add(LanHostInfo e){
        list.add(e);
    }

    public LanHostInfo get(int index){
        return list.get(index);
    }

    public LanHostInfo remove(int index){
        return list.remove(index);
    }

    public List<LanHostInfo> getList() {
        return list;
    }

    public void setList(List<LanHostInfo> list) {
        this.list = list;
    }

    public LanHostInfoList(String jsonStr) throws JsonProcessingException {
        generateFieldsByJsonStr(jsonStr);
    }

    public LanHostInfoList() {
    }
}
