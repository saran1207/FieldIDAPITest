
<#include "../userCrud/_userForm.ftl">

<head>
	<@n4.includeScript>
		sendingWelcomeEmailText = '<@s.text name="label.sending_welcome_email"/>';
		sendWelcomeEmailUrl = '<@s.url action="sendWelcomeEmail" namespace="/ajax" uniqueID="${uniqueID}"/>';
	</@n4.includeScript>
</head>