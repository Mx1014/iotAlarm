package cn.usr.cloud.alarm.entity;

import cn.usr.cloud.alarm.dto.TargetDataPointDTO;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class Linkage implements Serializable {
    Integer id;
    Integer uid;
    Long triggerId;
    Integer type = -1;
    Integer controlTargetType; //联动控制对象类型
    String deviceId;
    String slaveIndex;
    Integer templateId;
    Integer dataPointTemplateId;
    Integer dataPointId;
    String sendData;

    List<TargetDataPointDTO> targetDataPointList;

    public Boolean isQuery() {
        return type.equals(0);
    }

}
