<#if alarmState >
    <#assign alarm >
        触发报警
    </#assign>
<#else>
    <#assign alarm >
        恢复正常
    </#assign>
</#if>
${deviceName}设备下的${slaveIndex}号从机, 数据点[${dataPointName!"未知数据点"}]${alarm?trim}