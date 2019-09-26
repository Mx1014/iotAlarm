package cn.usr.cloud.alarm.service.impl;

import cn.usr.cloud.alarm.constant.ConstanControlTargetType;
import cn.usr.cloud.alarm.entity.UserInfo;
import cn.usr.cloud.alarm.mapper.one.DeviceMapper;
import cn.usr.cloud.alarm.mapper.one.UserMapper;
import cn.usr.cloud.alarm.service.ProtocolTopicService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by pi on 2019-03-13.
 */
@Slf4j
@Service
public class ProtocolTopicServiceImpl implements ProtocolTopicService {

    @Autowired
    UserMapper userDao;
    @Autowired
    DeviceMapper deviceDao;



    @Override
    public String frontendTopicForDeviceStatus(UserInfo userInfo) {
        return String.format("/app/status/%s/u", userInfo.getAccount());
    }

    @Override
    public String eTopicToDevice(String deviceId) {
        return "/e/{deviceId}/d".replace("{deviceId}",deviceId);
    }

    @Override
    public String topicSendToModule(Integer controlTargetType,String moduleName) {
        //controlTargetType
        return String.format("AlarmTo%s", moduleName);
       /* if(ConstanControlTargetType.CONTROL_SELF.equals(controlTargetType)){
            //云朵内部
            return String.format("/ALARM/%s/d", moduleName.toLowerCase());
        }else{
            return String.format("AlarmTo%s", moduleName);
        }*/

    }

}
