package net.dabaiyun.ikuairouter.Entity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;

public class QosLimitList {

    //Data
    private List<QosLimit> list = new ArrayList<>();

    /**
     * Generate Fields By Json String
     * @param jsonStr
     * @throws JsonProcessingException
     */
    public void generateFieldsByJsonStr(String jsonStr) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        list = objectMapper.readValue(jsonStr, new TypeReference<List<QosLimit>>() {});
    }

    public int size(){
        return list.size();
    }

    public void add(QosLimit e){
        list.add(e);
    }

    public QosLimit get(int index){
        return list.get(index);
    }

    public QosLimit remove(int index){
        return list.remove(index);
    }

    public List<QosLimit> getList() {
        return list;
    }

    public void setList(List<QosLimit> list) {
        this.list = list;
    }

    public QosLimitList(String jsonStr) throws JsonProcessingException {
        generateFieldsByJsonStr(jsonStr);
    }

    public QosLimitList() {
    }
}
