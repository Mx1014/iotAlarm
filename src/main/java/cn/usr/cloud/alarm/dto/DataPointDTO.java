package cn.usr.cloud.alarm.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.ReadContext;
import lombok.Data;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by pi on 2018/10/10.
 */
@Data
public class DataPointDTO {

    @JsonProperty("dataPointTemplateId")
    Long id = 0L;
    @JsonIgnore
    Long templateId = 0L;
    @JsonIgnore
    Long ownerId = 0L;
    @JsonIgnore
    String data;

    @JsonIgnore
    String formula="";

    @JsonIgnore
    String deviceId = "";

    String slaveIndex = "0";
    @JsonProperty("itemId")
    String item = "";
    String value = "";

    @JsonProperty("time")
    String t = "";

    @JsonIgnore
    public Long getTime() {
        try {
            return Long.parseLong(t) * 1000L;
        } catch (NumberFormatException e) {
            Date date = new Date();
            return date.getTime();
        }
    }

    public String getT(){
        if(!t.equals("")){
            return t;
        }
        Date date = new Date();
        Long time = date.getTime()/1000L;
        return time.toString();
    }

    static Set<String> availableCmds = new HashSet<>(3);
    static {
        availableCmds.add("u");
        availableCmds.add("qa");
        availableCmds.add("sa");
    }

    public static String parseCmd(String payload){
        String json= payload.substring(2);

        ReadContext ctx = JsonPath.parse(json);
        String f = ctx.read("f");
        return f;
    }

    public static List<DataPointDTO> parsePayload(String payload){
        List<DataPointDTO> res = new ArrayList<>(0);

        ReadContext ctx = JsonPath.parse(payload);
        String f = ctx.read("f");
        if(!availableCmds.contains(f)){
            return res;
        }
        List<Map> dataPayloads = ctx.read("$.d");
        return dataPayloads.stream()
                .map(mapItem->{
                    DataPointDTO tmp = new DataPointDTO();
                    tmp.setId(Long.parseLong(mapItem.get("k").toString()));
                    tmp.setData(mapItem.get("v").toString());
                    tmp.setT(mapItem.get("t").toString());
                    return tmp;
                }).collect(Collectors.toList());
    }
}