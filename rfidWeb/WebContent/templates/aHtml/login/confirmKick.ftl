<div class="titleBlock">
	<h1>${(securityGuard.primaryOrg.displayName?html)!} <@s.text name="title.sign_in_confirm"/></h1>
</div>

<@s.form action="confirmKick" theme="fieldid" cssClass="minForm" id="kickSessionConfirm">
	<#include "/templates/html/common/_formErrors.ftl" />
	<div>
		<@s.text name="instruction.someone_else_is_currently_signed_in_with_this_account"/>
	</div>	
	
	
	<div class="actions" > 
		<@s.submit key="label.yes_kick_other_user" id="kickOtherUser"/> <@s.text name="label.or"/> <a href="#" onclick="Lightview.hide()" ><@s.text name="label.cancel_and_sign_in"/></a>
	</div>
			
</@s.form>
<@n4.includeScript>
	$('kickOtherUser').observe('click', kickOtherUserSubmit);
</@n4.includeScript>
