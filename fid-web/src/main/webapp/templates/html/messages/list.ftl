<head>
	<@n4.includeStyle href="messages" type="page"/>
</head>

<#include '../safetyNetwork/_safetyNetworkLayout.ftl'>

<div id="mainContent">
<#if  page.hasResults() && page.validPage() >
	<h1 id="inbox_heading" class="clean"><@s.text name="label.inbox"/></h1>
	<#assign currentAction="messages" />
	<#include '../common/_pagination.ftl' />
	<table class="list">
		<tr>
			<th><@s.text name="label.from" /></th>
			<th><@s.text name="label.to" /></th>
			<th><@s.text name="label.subject"/></th>
			<th><@s.text name="label.date"/></th>
		</tr>
		<#list page.getList() as message > 
			<tr id="message_${message.id}" <#if message.unread>class="unread"</#if>>
				<td>${message.sender?html}</td>
				<td>${message.receiver?html}</td>
				<td><a href="<@s.url action="message" uniqueID="${message.id}"/>">${helper.trimString(message.subject, 150)?html}</a></td>
				<td>${action.formatDateTime(message.sentTime)}</td>
			</tr>
		</#list>
	</table>
	
	<#include '../common/_pagination.ftl' />
<#elseif !page.hasResults() >
	<div class="emptyList" >
		<h2><@s.text name="label.noresults" /></h2>
		<p>
			<@s.text name="label.emptyinbox" />
		</p>
	</div>
<#else>
	<div class="emptyList" >
		<h2><@s.text name="label.invalidpage" /></h2>
		<p>
			<@s.text name="message.invalidpage" />
			<a href="<@s.url  action="messages" currentPage="1" includeParams="get"/>" ><@s.text name="message.backtopageone"/></a>
		</p>
	</div>
</#if>
</div>
	