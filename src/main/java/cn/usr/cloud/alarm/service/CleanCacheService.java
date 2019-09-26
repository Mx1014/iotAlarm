package cn.usr.cloud.alarm.service;

/**
 * @Auther: Andy(李永强)
 * @Date: 2019/6/19 上午10:03
 * @Describe:
 */
public interface CleanCacheService {
    /**
     * 根据userId清除用户换缓存
     * @param userId
     */
    void cleanUser(String userId);

    /**
     * 根据dataPointId清除触发器缓存
     * @param dataPointId
     */
    void cleanTrigger(Integer dataPointId);

    /**
     * 根据dataPointId清除数据点缓存
     * @param dataPointId
     */
    void cleanDataPoint(Integer dataPointId);

    /**
     * 根据deviceNo清除触发器缓存
     * @param deviceNo
     */
    void cleanDevice(String deviceNo);

    /**
     * 根据deviceTemplateId清除从机缓存
     * @param deviceTemplateId
     */
    void cleanSlave(Integer deviceTemplateId);

    /**
     * 根据triggerId清除报警联系人缓存
     * @param triggerId
     */
    void cleanContacts(Integer triggerId);

    /**
     * 根据triggerId清除报警联动缓存
     * @param triggerId
     */
    void cleanLinkage(Integer triggerId);

    /**
     * 根据dataPointTemplateId清除数据点关联缓存
     * @param dataPointTemplateId
     */
    void cleanDataPointRel(Integer dataPointTemplateId);

}
