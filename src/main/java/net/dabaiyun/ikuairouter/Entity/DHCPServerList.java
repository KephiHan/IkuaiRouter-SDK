package net.dabaiyun.ikuairouter.Entity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;

public class DHCPServerList {

    //Data
    private List<DHCPServer> list = new ArrayList<>();

    /**
     * Generate Fields By Json String
     * @param jsonStr
     * @throws JsonProcessingException
     */
    public void generateFieldsByJsonStr(String jsonStr) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        list = objectMapper.readValue(jsonStr, new TypeReference<List<DHCPServer>>() {});
    }

    public int size(){
        return list.size();
    }

    public void add(DHCPServer e){
        list.add(e);
    }

    public DHCPServer get(int index){
        return list.get(index);
    }

    public DHCPServer remove(int index){
        return list.remove(index);
    }

    public List<DHCPServer> getList() {
        return list;
    }

    public void setList(List<DHCPServer> list) {
        this.list = list;
    }

    public DHCPServerList(String jsonStr) throws JsonProcessingException {
        generateFieldsByJsonStr(jsonStr);
    }

    public DHCPServerList() {
    }
}
