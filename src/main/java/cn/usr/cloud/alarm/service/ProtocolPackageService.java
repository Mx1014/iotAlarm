package cn.usr.cloud.alarm.service;


import cn.usr.cloud.alarm.dto.AlarmInfoDTO;
import cn.usr.cloud.alarm.dto.DataPointDTO;
import cn.usr.cloud.alarm.entity.DataPointInfo;
import cn.usr.cloud.alarm.entity.Linkage;
import cn.usr.cloud.alarm.entity.Trigger;

/**
 * Created by pi on 2019-03-13.
 */
public interface ProtocolPackageService {


    /**
     * 文档地址：https://www.yuque.com/sw_fw/plcnet_develop/cloud3_protocol_for_app#867b2729
     * @param alarmInfoDTO
     * @return
     */
    String frontenfAlarmPackage(AlarmInfoDTO alarmInfoDTO);

    /**
     * 向其他模块发送控制指令
     * @param linkages
     * @return 实例如下
     * {
     *   "dataPoint": [
     *     {
     *       "operation": "control",
     *       "deviceId": "123",
     *       "slaveIndex": "1",
     *       "dataPointId": "1",
     *       "value": "12.11"
     *     }]
     *  }
     */
    String linkageDevicePackage(Linkage... linkages);
}
