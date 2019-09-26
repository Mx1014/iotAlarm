package cn.usr.cloud.alarm.service;

import cn.usr.cloud.alarm.entity.UserInfo;

/**
 * Created by pi on 2019-03-13.
 */
public interface ProtocolTopicService {


    /**
     * 客户端接收设备上下线通知、报警信息的topic
     * @return /app/status/{username}/u
     */
    String frontendTopicForDeviceStatus(UserInfo userInfo);

    /**
     * 生成e主题下行topic
     * @param deviceId
     * @return /e/{deviceId}/d
     */
    String eTopicToDevice(String deviceId);


    /**
     *
     * @param moduleName
     * @return /ALARM/{Module}/d
     */
    String topicSendToModule(Integer controlTargetType, String moduleName);
}
