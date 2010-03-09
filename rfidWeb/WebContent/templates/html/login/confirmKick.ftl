<title>${(securityGuard.primaryOrg.displayName?html)!} <@s.text name="title.sign_in"/></title>
<head>
	<style type="text/css">
		#signInConfirm {
			margin: 10px 0px;
		}
	</style>
</head>
<div id="mainContent">
	<div class="titleBlock">
		<h1>${(securityGuard.primaryOrg.displayName?html)!} <@s.text name="title.sign_in_confirm"/></h1>
	</div>
	
	<@s.form action="confirmKick" theme="fieldid" cssClass="minForm" id="signInConfirm">
		<#include "/templates/html/common/_formErrors.ftl" />
		<div class="instructions">
			<@s.text name="instruction.someone_else_is_currently_signed_in_with_this_account"/>
		</div>	
		
		<div class="actions" > 
			<@s.submit key="label.yes_kick_other_user" id="signInWithSecurityButton"/> <@s.text name="label.or"/> <a href="<@s.url action="login"/>" ><@s.text name="label.cancel_and_sign_in"/></a>
		</div>
	</@s.form>
	

</div>

