<title><@s.text name="title.forgot_password"/></title>

<div id="mainContent">
	
	<div class="titleBlock">	
		<h1><@s.text name="title.forgot_password"/></h1>
		<p class="titleSummary">
			<@s.text name="instruction.reset_your_password"/>
		</p>
	</div>
	
	
	<@s.form action="sendPassword" theme="fieldid" cssClass="minForm">
		<#include "/templates/html/common/_formErrors.ftl"/>
		<p class="actionInstructions">
			<@s.text name="instructions.forgotpassword"/>
		</p>
		
		<div class="infoSet">
			<label class='label' for="userName"><@s.text name="label.username"/></label>
			<@s.textfield name="userName" id="userName"/>
		</div>
		<div class="actions">
			<@s.submit key="label.reset_password"/> <@s.text name="label.or"/> <a href="<@s.url action="login"/>"/><@s.text name="label.return_to_sign_in"/></a>
		</div>
		
	</@s.form>
</div>
<div id="secondaryContent">
	<#include "../public/_requestAccount.ftl"/>
</div>

<script type="text/javascript">
	$('userName').select();
</script> 