${action.setPageType('inbox', 'show')!}

<head>
	<@n4.includeStyle href="messages" type="page"/>
</head>
	

<div class="fullForm" >
	
	<div class="infoField">
		<label for="from" class="label"><@s.text name="label.from"/></label>
		<span class="fieldHolder">${message.sender?html}</span> 
	</div>

	<div class="infoField">
		<label for="receiver" class="label"><@s.text name="label.to"/></label>
		<span class="fieldHolder">${message.receiver?html}</span>
	</div>
	
	<div class="infoField">
		<label for="sentDate" class="label"><@s.text name="label.date"/></label>
		<span class="fieldHolder">${action.formatDateTime(message.sentTime)}</span>
	</div>
	<div class="infoField">
		<label for="subject" class="label"><@s.text name="label.subject"/></label>
		<span class="fieldHolder">${message.subject?html}</span>
	</div>
	<div class="infoField">
		<label for="body" class="label"><@s.text name="label.body"/></label>
		<p class="fieldHolder">${action.replaceCR(message.body?html)}</p>
	</div>
	<div class="actions">
		<#if !message.command.processed>
			<@s.form action="messageUpdate" theme="fieldid" >
				<@s.hidden name="uniqueID"/>
				<@s.submit key="label.accept" id="accept"/>
			</@s.form>
		</#if>
		<@s.form action="messageDelete" theme="fieldid">
			<@s.hidden name="uniqueID"/>
			<@s.submit key="label.delete" id="delete"/>
			<@s.text name="label.or"/>
			<a href="<@s.url action="messages"/>"><@s.text name="label.cancel"/></a> 
		</@s.form> 
	</div>
		
</div>