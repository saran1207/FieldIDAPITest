<title>${(securityGuard.primaryOrg.displayName?html)!} <@s.text name="title.sign_in"/></title>
<head>
    <@n4.includeStyle href="pageStyles/login" />
</head>
<div id="mainContent" class="login-page">
	<div class="titleBlock">
		${(securityGuard.primaryOrg.displayName?html)!} <@s.text name="title.sign_in"/>
	</div>
	
	<@s.form action="logIntoSystem" theme="fieldid" cssClass="minForm login-form" id="signInForm">
		<#include "/templates/html/common/_formErrors.ftl" />
		<@s.hidden name="signIn.normalLogin" id="normalLogin"/>
		<div id="normal_container login-input">
			<@s.textfield name="signIn.userName" id="userName"/>
			<@s.password name="signIn.password" id="password"/>
		</div>
		
		<div class="oneLine remember-me">
			<span class="fieldHolder"><@s.checkbox name="signIn.rememberMe" theme="fieldidSimple" /><@s.text name="label.rememberme"/></span>
		</div>	
		
		<div class="actions" id="normalActions_container"> 
			<@s.submit cssClass="submit" key="label.sign_in" id="signInButton"/>
		</div>
						
	</@s.form>

</div>

<script>
    document.observe("dom:loaded", function() { $('userName').focus(); });
</script>

