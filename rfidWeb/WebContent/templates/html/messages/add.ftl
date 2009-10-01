${action.setPageType('inbox', 'add')!}

<div>
	<@s.form action="messageCreate" theme="fieldid" cssClass="minForm">
		<@s.hidden name="messageId"/>
		<#include "/templates/html/common/_formErrors.ftl"/>
		<div class="infoField">
			<label for="from" class="label"><@s.text name="label.from"/></label>
			<span class="fieldHolder">${sessionUser.name?html} ${sessionUser.owner.name?html}</span> 
		</div>
	
		<div class="infoField">
			<label for="receiver" class="label"><@s.text name="label.to"/></label>
			<@s.textfield name="message.receiver"/>
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
			<a href="<@s.url action="messages"/>" id="cancel"><@s.text name="label.cancel"/></a>
		</div>
	</@s.form>
</div>