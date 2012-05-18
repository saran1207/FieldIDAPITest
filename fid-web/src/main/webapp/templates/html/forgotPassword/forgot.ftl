<title><@s.text name="title.forgot_password"/></title>

<head>
    <@n4.includeStyle href="pageStyles/login" />
    <@n4.includeScript src="jquery/watermark/jquery.watermark.js"/>
</head>
<div id="mainContent" class="login-page">

	<div class="titleBlock">
		<@s.text name="title.forgot_password"/>
	</div>

	<@s.form action="sendPassword" theme="fieldid" cssClass="minForm forgot-login-form">
		<#include "/templates/html/common/_formErrors.ftl"/>
		<p class="actionInstructions">
			<@s.text name="instructions.forgotpassword"/>
		</p>

		<div class="infoSet">
			<@s.textfield name="userName" id="userName"/>
		</div>
		<div class="actions">
			<@s.submit cssClass="blue-submit" key="label.reset_password"/> <span style="color:#999;"><@s.text name="label.or"/></span> <a href="<@s.url action="login"/>"/><@s.text name="label.return_to_sign_in"/></a>
		</div>

	</@s.form>
</div>

<script type="text/javascript">
    jQuery('#userName').watermark('username');
</script>
















