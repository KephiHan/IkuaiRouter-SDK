package net.dabaiyun.ikuairouter.Entity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;

public class InterfaceWanList {

    //Data
    private List<InterfaceWan> list = new ArrayList<>();

    /**
     * Generate Fields By Json String
     * @param jsonStr
     * @throws JsonProcessingException
     */
    public void generateFieldsByJsonStr(String jsonStr) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        list = objectMapper.readValue(jsonStr, new TypeReference<List<InterfaceWan>>() {});
    }

    public int size(){
        return list.size();
    }

    public void add(InterfaceWan e){
        list.add(e);
    }

    public InterfaceWan get(int index){
        return list.get(index);
    }

    public InterfaceWan remove(int index){
        return list.remove(index);
    }

    public List<InterfaceWan> getList() {
        return list;
    }

    public void setList(List<InterfaceWan> list) {
        this.list = list;
    }

    public InterfaceWanList(String jsonStr) throws JsonProcessingException {
        generateFieldsByJsonStr(jsonStr);
    }

    public InterfaceWanList() {
    }
}
