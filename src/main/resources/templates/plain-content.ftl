<#if trigger.isSufficientValue()==false>恢复正常状态
<#else>
    <#switch trigger.condition>
    <#case 0>当前值:${trigger.inputValue},不符合设置的关闭状态<#break>
    <#case 1>当前值:${trigger.inputValue},不符合设置的开启状态<#break>
    <#case 2>当前值:${trigger.inputValue},已低于设置的${trigger.min}<#break>
    <#case 3>当前值:${trigger.inputValue},已高于设置的${trigger.max}<#break>
    <#case 4>当前值:${trigger.inputValue},处于设置的${trigger.min}和${trigger.max}之间<#break>
    <#case 5>当前值:${trigger.inputValue},低于设置的${trigger.min}或高于${trigger.max}<#break>
    <#case 6>当前值:${trigger.inputValue},等于设置的${trigger.min}<#break>
    <#default>未知情况
    </#switch>
</#if>0