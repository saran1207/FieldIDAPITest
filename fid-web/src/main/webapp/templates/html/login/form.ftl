<title>${(securityGuard.primaryOrg.displayName?html)!} <@s.text name="title.sign_in"/></title>
<head>
	<style type="text/css">
		#signInForm {
			margin: 10px 0px;
		}
	</style>
</head>
<div id="mainContent">
	<div class="titleBlock">
		<h1>${(securityGuard.primaryOrg.displayName?html)!} <@s.text name="title.sign_in"/></h1>
		<p class="titleSummary"><@s.text name="instruction.already_have_a_field_id_account"/></p>
	</div>
	
	<@s.form action="logIntoSystem" theme="fieldid" cssClass="minForm" id="signInForm">
		<#include "/templates/html/common/_formErrors.ftl" />
		<@s.hidden name="signIn.normalLogin" id="normalLogin"/>
		<div id="normal_container">
			<label class="label"><@s.text name="label.username"/></label>
			<@s.textfield name="signIn.userName" id="userName"/>
			
			<label class="label"><@s.text name="label.password"/></label>
			<@s.password name="signIn.password" id="password"/>
		</div>
		
		<div class="oneLine">
			<span class="fieldHolder"><@s.checkbox name="signIn.rememberMe" theme="fieldidSimple" /><@s.text name="label.rememberme"/></span>
		</div>	
		
		<div class="actions" id="normalActions_container"> 
			<@s.submit key="label.sign_in" id="signInButton"/>
		</div>
						
	</@s.form>
	
	<ul id="otherActions">
  		<li><label class="label"><@s.text name="label.help"/>:</label> <span><a href="<@s.url action="forgotPassword"/>"><@s.text name="link.emailpassword"/></a></span></li>
	</ul>

</div>

<#include "../common/_secondary_content.ftl"/>
