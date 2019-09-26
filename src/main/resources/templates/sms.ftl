<#assign deviceName=deviceName!'' >
<#if deviceName?length <= 38>
    <#assign device=deviceName>
<#else>
    <#assign device >
        ${deviceName?substring(0,38)}...
    </#assign>
</#if>
<#assign dataPointName=dataPointName!'' >
<#if dataPointName?length <= 28>
    <#assign dataPoint=dataPointName>
<#else>
    <#assign dataPoint >
        ${dataPointName?substring(0,28)}...
    </#assign>
</#if>
<#if alarmState >
    <#assign alarm >
        触发报警
    </#assign>
<#else>
    <#assign alarm >
        恢复正常
    </#assign>
</#if>
{
    "DEVNAME": "${device?trim}",
    "DATAPOINTNAME": "${dataPoint?trim}",
    "TRIGGERCONDITION": "${alarm?trim}",
    "NOWVALUE": "${trigger.getInputValue()?string("0.##")}"
}