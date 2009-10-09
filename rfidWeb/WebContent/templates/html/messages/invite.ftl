${action.setPageType('inbox', 'invite')!}

<head>
	<@n4.includeStyle href="invite" type="page"/>
</head>
	
<@s.form action="sendInvite" cssClass="fullForm contentBlock" theme="fieldid">

<div id="messageInput">
	<h2 class="clean"><@s.text name="label.invite_a_company"/></h2>

	<#include "../common/_formErrors.ftl"/>
	
	<div class="infoField">
		<label for="from" class="label"><@s.text name="label.from"/></label>
		<span class="fieldHolder" id="sender"><span id="senderOrg">${sessionUser.owner.name?html}</span></span> 
	</div>

	<div class="infoField">
		<label for="email" class="label"><@s.text name="label.to"/></label>
		<@s.textfield name="email"/>
	</div>

	<div class="infoField">
		<label for="subject" class="label"><@s.text name="label.subject"/></label>
		<@s.textfield name="subject"/>
	</div>
	
	<div class="infoField">
		<label for="body" class="label"><@s.text name="label.body"/></label>
		<@s.textarea name="body"/>
	</div>

	
	<div class="actions">
		<@s.submit key="label.sendinvitation" id="sendinvitation"/>
		<@s.text name="label.or"/>
		<a href="<@s.url action="messages"/>"><@s.text name="label.cancel"/></a> 
	</div>
		
</div>
</@s.form>
