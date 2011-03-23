${action.setPageType('setup','dataLog')!}
<#include "_form.ftl"/>
<#if page?exists >
	<#if page.hasResults() && page.validPage() >
		<#assign currentAction="dataLogSearch" />
		
		<#include '../common/_pagination.ftl' />
		<table class="list">
			<tr>
				<th><@s.text name="label.time"/></th>
				<th><@s.text name="label.message"/></th>
				<th><@s.text name="label.type" /></th>
				<th><@s.text name="label.status" /></th>
				
			</tr>
			<#list page.getList() as dataLogEntry > 
				<tr id="dataLogEntry_${dataLogEntry.uniqueID}" >
					<td>${action.formatDateTime(dataLogEntry.timeLogged)}</td>
					<td>${dataLogEntry.logMessage?html}</td>
					<td>${(dataLogEntry.logType.name())!}</td>
					<td>${dataLogEntry.logStatus.name()?html}</td>
				</tr>	
			</#list>
		</table>
		
		<#include '../common/_pagination.ftl' />
	<#elseif !page.hasResults() >
		<div class="emptyList" >
			<h2><@s.text name="label.noresults" /></h2>
			<p>
				<@s.text name="label.emptydatalogentrylist" />
				
			</p>
		</div>
	<#else >
		<div class="emptyList" >
			<h2><@s.text name="label.invalidpage" /></h2>
			<p>
				<@s.text name="message.invalidpage" />
				<a href="<@s.url  action="dataLogSearch" currentPage="1" includeParams="get"/>" ><@s.text name="message.backtopageone"/></a>
			</p>
		</div>
	</#if>
</#if>