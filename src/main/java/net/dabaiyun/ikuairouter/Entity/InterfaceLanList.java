package net.dabaiyun.ikuairouter.Entity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;

public class InterfaceLanList {

    //Data
    private List<InterfaceLan> list = new ArrayList<>();

    /**
     * Generate Fields By Json String
     * @param jsonStr
     * @throws JsonProcessingException
     */
    public void generateFieldsByJsonStr(String jsonStr) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        list = objectMapper.readValue(jsonStr, new TypeReference<List<InterfaceLan>>() {});
    }

    public int size(){
        return list.size();
    }

    public void add(InterfaceLan e){
        list.add(e);
    }

    public InterfaceLan get(int index){
        return list.get(index);
    }

    public InterfaceLan remove(int index){
        return list.remove(index);
    }

    public List<InterfaceLan> getList() {
        return list;
    }

    public void setList(List<InterfaceLan> list) {
        this.list = list;
    }

    public InterfaceLanList(String jsonStr) throws JsonProcessingException {
        generateFieldsByJsonStr(jsonStr);
    }

    public InterfaceLanList() {
    }
}
