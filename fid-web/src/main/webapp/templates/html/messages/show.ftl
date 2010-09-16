<head>
	<@n4.includeStyle href="messages" type="page"/>
</head>
	
<#include '../safetyNetwork/_safetyNetworkLayout.ftl'>
<div id="mainContent">
<div class="fullForm" >

	<h1><@s.text name="label.messagetitle"/></h1>
	
	<div id="messageHeader">
		
		<div class="viewSection smallViewSection">
			<p id="subject">${message.subject?html}</p>
		</div>
		<div class="viewSection smallViewSection">
			<p>
				<label for="from" class="label"><@s.text name="label.from"/></label>
				<span class="fieldHolder">${message.sender?html}</span>
			</p> 
		</div>
	
		<div class="viewSection smallViewSection">
			<p>
				<label for="receiver" class="label"><@s.text name="label.to"/></label>
				<span class="fieldHolder">${message.receiver?html}</span>
			</p>
		</div>
		
		<div class="viewSection smallViewSection">
			<p>
				<label for="sentDate" class="label"><@s.text name="label.date"/></label>
				<span class="fieldHolder">${action.formatDateTime(message.sentTime)}</span>
			</p>
		</div>
	
	</div>
	<div id="messageBody">
		<div class="viewSection smallViewSection">
			<p class="fieldHolder">${action.replaceCR(message.body?html)}</p>
		</div>
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
</div>