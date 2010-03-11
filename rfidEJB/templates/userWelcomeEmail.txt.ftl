++++++++++++++++++++++++++++++++++++++++++
Your Field ID Account
++++++++++++++++++++++++++++++++++++++++++
${notification.tenantName} has created a Field ID Account for you. Your account details are:

User name: ${notification.userName}

<#if notification.personalized>
${notification.personalMessage}
</#if>

<#if notification.resetPasswordSet>
<<reset password url>>
<#else> 
You can login securely to your new account at <<signin url>>
</#if>


<#include "_helpfulFieldIDResources.txt.ftl"/>