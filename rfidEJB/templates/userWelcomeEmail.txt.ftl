++++++++++++++++++++++++++++++++++++++++++
Your Field ID Account
++++++++++++++++++++++++++++++++++++++++++
${notification.tenantName} has created a Field ID Account for you. Your account details are:

User name: ${notification.userName}
Password: If you do not know or remember your password you may reset it by going to ${notification.forgotPasswordUrl} and following the instructions.

<#if notification.personalized>
${notification.personalMessage}
</#if>

You can sign in securely to your new account at ${notification.signInUrl}

<#include "_helpfulFieldIDResources.txt.ftl"/>