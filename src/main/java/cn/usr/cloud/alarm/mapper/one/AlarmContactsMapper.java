package cn.usr.cloud.alarm.mapper.one;

import cn.usr.cloud.alarm.dto.AlarmContactsDTO;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface AlarmContactsMapper {

    /**
     * 根据触发器id查询与之关联的报警联系人信息
     * @param triggerId
     * @return
     */
    @Select("(SELECT NAME,phoneNumber AS tel,email,openid_official_accounts " +
                "FROM iot_alarm_contacts c " +
                "RIGHT JOIN iot_alarm_contacts_u2g u2g ON c.contact_id=u2g.contact_group_id " +
                "RIGHT JOIN iot_alarm_contact_user u ON u.id=u2g.contact_user_id " +
                "LEFT JOIN iot_u2wx_bind wx ON wx.id=u.wx_id " +
                "WHERE c.trigger_id=#{triggerId} AND c.contact_type=1) " +
            "UNION " +
            "(SELECT NAME,phoneNumber AS tel,email,openid_official_accounts " +
                "FROM iot_alarm_contact_user u " +
                "LEFT JOIN iot_alarm_contacts c ON c.contact_id=u.id " +
                "LEFT JOIN iot_u2wx_bind wx ON wx.id=u.wx_id " +
                "WHERE c.trigger_id=#{triggerId} AND contact_type=0)")
    @Results({
            @Result(column = "openid_official_accounts", property = "openId")
    })
    List<AlarmContactsDTO> getAlarmContactsByTriggerId(Integer triggerId);

}
