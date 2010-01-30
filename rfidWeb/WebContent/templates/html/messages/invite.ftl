${action.setPageType('safety_network_connections', 'invite')!}

<head>
	<@n4.includeStyle href="safetyNetwork" type="page"/>
	<@n4.includeStyle href="invite" type="page"/>
</head>
	
<@s.form action="sendInvite" cssClass="fullForm contentBlock" theme="fieldid">

<div id="messageInput">
	<h2 id="inbox_heading" class="clean"><@s.text name="label.invite_a_company"/></h2>

	<#include "../common/_formErrors.ftl"/>
	
	<div class="infoField">
		<label for="from" class="label inline"><@s.text name="label.from"/></label>
		<span class="fieldHolder" id="sender"><span id="senderOrg">${sessionUser.owner.name?html}</span></span> 
	</div>

	<div class="infoField">
		<label for="email" class="label inline"><@s.text name="label.to"/>: </label><span class="field_tip"><@s.text name="label.enter_single_addr"/></span>
		<@s.textfield name="email"/>
	</div>

	<div class="infoField">
		<label for="subject" class="label inline"><@s.text name="label.subject"/>: </label>
		<@s.textfield name="subject"/>
	</div>
	
	<div class="infoField">
		<label for="body" class="label inline"><@s.text name="label.body"/>: </label><span class="field_tip"><@s.text name="label.optional"/></span>
		<p id="body_desc_text"><@s.text name="label.body_desc"/></p>
		<@s.textarea name="body"/>
	</div>
	
	<div class="actions">
		<@s.submit key="label.sendinvitation" id="sendinvitation"/>
		<@s.text name="label.or"/>
		<a href="<@s.url action="messages"/>"><@s.text name="label.cancel"/></a> 
	</div>
		
</div>
</@s.form>
