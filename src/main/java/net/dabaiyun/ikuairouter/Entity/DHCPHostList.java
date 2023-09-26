package net.dabaiyun.ikuairouter.Entity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;

public class DHCPHostList {

    private List<DHCPHost> list = new ArrayList<>();

    /**
     * Generate Fields By Json String
     * @param jsonStr
     * @throws JsonProcessingException
     */
    public void generateFieldsByJsonStr(String jsonStr) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        list = objectMapper.readValue(jsonStr, new TypeReference<List<DHCPHost>>() {});
    }

    public int size(){
        return list.size();
    }

    public void add(DHCPHost e){
        list.add(e);
    }

    public DHCPHost get(int index){
        return list.get(index);
    }

    public DHCPHost remove(int index){
        return list.remove(index);
    }

    public List<DHCPHost> getList() {
        return list;
    }

    public void setList(List<DHCPHost> list) {
        this.list = list;
    }

    public DHCPHostList(String jsonStr) throws JsonProcessingException {
        generateFieldsByJsonStr(jsonStr);
    }

    public DHCPHostList() {
    }
}
