<title><@s.text name="title.forgotpassword"/></title>
<head>
	<link rel="stylesheet" type="text/css" href="<@s.url value="/style/public.css"/>"/>
</head>
<@s.form action="sendPassword" theme="fieldid" cssClass="easyForm">
	<p class="instructions">
		<@s.text name="instructions.forgotpassword"/>
	</p>
	<#include "/templates/html/common/_formErrors.ftl"/>
	<label class='label'><@s.text name="label.username"/></label>
	<@s.textfield name="userName" id="userName"/>
	<div class="formAction">
		<@s.submit key="hbutton.send"/>
	</div>
	<p>
		<a href="<@s.url action="login"/>"><@s.text name="label.login"/></a>
	</p>
</@s.form>



<script type="text/javascript">
	$('userName').select();
</script> 