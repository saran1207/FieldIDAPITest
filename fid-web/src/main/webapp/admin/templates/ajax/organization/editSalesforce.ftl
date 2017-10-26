<#escape x as x?j_string >
    <#assign html>
        <#include "../../html/organization/_salesforceForm.ftl" >
    </#assign>
$('salesforceId').update("${html}");
</#escape>