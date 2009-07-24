<head>
	<style>
		#page {	width:460px; }
	</style>
</head>
<@s.form action="logIntoSystem" theme="fieldid" namespace="/" cssClass="easyForm" target="_parent">
	<label class="label"><@s.text name="label.username"/></label>
	<@s.textfield name="userName" id="userName"/>
	
	<label class="label"><@s.text name="label.password"/></label>
	<@s.password name="password" id="password"/>
	
	<div class="oneLine">
		<span class="fieldHolder"><@s.checkbox name="rememberMe" theme="fieldidSimple"/><@s.text name="label.rememberme"/></span>
	</div>
	
	<div class="formAction"> 
		<@s.submit key="hbutton.login" id="loginButton"/>
	</div>
	
	<p>
		<a href="<@s.url namespace="/" action="forgotPassword"/>" target="_parent"><@s.text name="link.emailpassword"/></a>
	</p>
	<#if securityGuard.partnerCenterEnabled>
		<p>
			<a href="<@s.url namespace="/" action="registerUser"/>" target="_parent"><@s.text name="label.requestanaccount"/></a>
		</p>
	</#if>
		
		
</@s.form>