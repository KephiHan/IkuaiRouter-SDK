package net.dabaiyun.ikuairouter.Entity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;

public class NetMappingList {
    //Data
    private List<NetMapping> list = new ArrayList<>();

    /**
     * Generate Fields By Json String
     * @param jsonStr
     * @throws JsonProcessingException
     */
    public void generateFieldsByJsonStr(String jsonStr) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        list = objectMapper.readValue(jsonStr, new TypeReference<List<NetMapping>>() {});
    }

    public int size(){
        return list.size();
    }

    public void add(NetMapping e){
        list.add(e);
    }

    public NetMapping get(int index){
        return list.get(index);
    }

    public NetMapping remove(int index){
        return list.remove(index);
    }

    public List<NetMapping> getList() {
        return list;
    }

    public void setList(List<NetMapping> list) {
        this.list = list;
    }

    public NetMappingList(String jsonStr) throws JsonProcessingException {
        generateFieldsByJsonStr(jsonStr);
    }

    public NetMappingList() {
    }
}
