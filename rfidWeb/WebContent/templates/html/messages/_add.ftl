
<head>
	<@n4.includeStyle href="messages" type="page"/>
</head>
	
<div id="messageInput">
	<h2><@s.text name="label.send_message"/></h2>
	<div class="infoField">
		<label for="from" class="label"><@s.text name="label.from"/></label>
		<span class="fieldHolder" id="sender">${sessionUser.name?html} ${sessionUser.owner.name?html}</span> 
	</div>

	<div class="infoField">
		<label for="receiver" class="label"><@s.text name="label.to"/></label>
		<span class="fieldHolder" id="receiver"></span>
	</div>
	<div class="infoField">
		<label for="subject" class="label"><@s.text name="label.subject"/></label>
		<@s.textfield name="message.subject"/>
	</div>
	<div class="infoField">
		<label for="body" class="label"><@s.text name="label.body"/></label>
		<@s.textarea name="message.body"/>
	</div>
	<div class="actions">
		<@s.submit key="lable.send" id="send"/>
		<@s.text name="label.or"/>
		<a href="${cancelUrl}" id="messageCancel"><@s.text name="label.cancel"/></a>
	</div>
</div>