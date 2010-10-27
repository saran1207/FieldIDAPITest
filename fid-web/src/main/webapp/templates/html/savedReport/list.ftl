${action.setPageType('saved_reports', 'saved_reports')!}
<#assign secondaryNavAction="list"/>
<#include "_secondaryNav.ftl"/>

<#if  page.hasResults() && page.validPage() >
	<#assign currentAction="savedReports.action" />
	<#include '../common/_pagination.ftl' />
	<table class="list">
		<tr>
			<th><@s.text name="label.name" /></th>
			<th><@s.text name="label.sharedby" /></th>
			<th><@s.text name="label.datemodified" /></th>
			<th></th>
		</tr>
		<#list page.getList() as report > 
			<tr id="report_${report.id}" >
				<td><a href="<@s.url action="savedReportLoad" uniqueID="${report.id}" namespace="/" />">${report.name?html}</a></td>
				<td>${report.sharedByName!}</td>
				<td>${action.formatDateTime(report.modified)}</td>
				<td>
					<a id="shareReport_${report.id}" class="savedReportShareLink" href="<@s.url action="savedReportShare" uniqueID="${report.id}" />"><@s.text name="label.share"/></a>
					<a id="deleteReport_${report.id}" class="savedReportDeleteLink" href="<@s.url action="savedReportDelete" uniqueID="${report.id}" />"><@s.text name="label.delete"/></a>
				</td>
			</tr>	
		</#list>
	</table>
	
	<#include '../common/_pagination.ftl' />
<#elseif !page.hasResults() >
	<div class="emptyList" >
		<h2><@s.text name="label.noresults" /></h2>
		<p><@s.text name="label.emptysavedreportslist" /> <a href="<@s.url action="report" />"><@s.text name="label.click_here_to_go_to_reporting"/></a></p>
	</div>
<#else>
	<div class="emptyList" >
		<h2><@s.text name="label.invalidpage" /></h2>
		<p><@s.text name="message.invalidpage" /></p>
	</div>
</#if>