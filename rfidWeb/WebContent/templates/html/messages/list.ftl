${action.setPageType('inbox', 'list')!}

<#if  page.hasResults() && page.validPage() >
	<#assign currentAction="messages" />
	<#include '../common/_pagination.ftl' />
	<table class="list">
		<tr>
			<th><@s.text name="label.from" /></th>
			<th><@s.text name="label.subject"/></th>
			<th><@s.text name="label.date"/></th>
		</tr>
		<#list page.getList() as message > 
			<tr id="message_${message.id}" <#if message.unread>class="unread"</#if>>
				<td>${message.sender?html}</td>
				<td><a href="<@s.url action="message" uniquID="${message.id}"/>">${message.sender?html}</a></td>
				<td>${action.formatDateTime(message.sentDate)}</td>
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
	