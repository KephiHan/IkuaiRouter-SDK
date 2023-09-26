package net.dabaiyun.ikuairouter.Entity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;

public class DHCPStaticList {

    //Data
    private List<DHCPStatic> list = new ArrayList<>();

    /**
     * Generate Fields By Json String
     * @param jsonStr
     * @throws JsonProcessingException
     */
    public void generateFieldsByJsonStr(String jsonStr) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        list = objectMapper.readValue(jsonStr, new TypeReference<List<DHCPStatic>>() {});
    }

    public int size(){
        return list.size();
    }

    public void add(DHCPStatic e){
        list.add(e);
    }

    public DHCPStatic get(int index){
        return list.get(index);
    }

    public DHCPStatic remove(int index){
        return list.remove(index);
    }

    public List<DHCPStatic> getList() {
        return list;
    }

    public void setList(List<DHCPStatic> list) {
        this.list = list;
    }

    public DHCPStaticList(String jsonStr) throws JsonProcessingException {
        generateFieldsByJsonStr(jsonStr);
    }

    public DHCPStaticList() {
    }
}
