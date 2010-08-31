

<div style="width:750px; float:left">
	<#include "../userCrud/_userForm.ftl">
</div>
<div class="extraActions" >
	<h3><a href="#sendWelcomeEmail" id="sendWelcomeEmail"><@s.text name="label.send_welcome_email"/></a></h3>
	<p><@s.text name="description.send_welcome_email"><@s.param>${user.fullName}</@s.param></@s.text></p>
</div>
<head>
	<@n4.includeScript>
		sendingWelcomeEmailText = '<@s.text name="label.sending_welcome_email"/>';
		sendWelcomeEmailUrl = '<@s.url action="sendWelcomeEmail" namespace="/ajax" uniqueID="${uniqueID}"/>';
	</@n4.includeScript>
</head>