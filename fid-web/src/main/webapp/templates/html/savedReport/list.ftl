${action.setPageType('saved_reports', 'saved_reports')!}
<#assign secondaryNavAction="list"/>
<#include "_secondaryNav.ftl"/>


<#if page.hasResults() && page.validPage() >
	
	<div class="headerWithFootnote">
		<div class="tipContainer">
			<img src="<@s.url value="/images/tip-icon.png" />"/>
			<p class="footnoteText">
				
				<@s.text name="label.saved_report_explanation"/>
				<a href="<@s.url action="userList" namespace="/" />">
					<@s.text name="label.add_new_user"/>
				</a>
			</p>
		</div>
	</div>

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
		     		<a href="<@s.url action="savedReportLoad" uniqueID="${report.id}" namespace="/" />"><@s.text name="label.Run"/></a> |
					<a id="shareReport_${report.id}" class="savedReportShareLink" href="<@s.url action="savedReportShare" uniqueID="${report.id}" />"><@s.text name="label.share"/></a> |
					<a id="deleteReport_${report.id}" class="savedReportDeleteLink" href="<@s.url action="savedReportDelete" uniqueID="${report.id}" />"><@s.text name="label.delete"/></a> 
				</td>
			</tr>	
		</#list>
	</table>
	
	<#include '../common/_pagination.ftl' />
<#elseif !page.hasResults() >
	<@s.url id="reportUrl" action="report"/>

	<div class="initialMessage">
		<div class="textContainer">
			<h1><@s.text name="label.create_first_report" /></h1>
			<p><@s.text name="label.create_first_report_description" /></p>
		</div>
		<input type="submit" value="<@s.text name="label.run_a_new_report_now"/>"onclick="return redirect('${reportUrl}');"/>
	</div>
<#else>
	<div class="emptyList" >
		<h2><@s.text name="label.invalidpage" /></h2>
		<p><@s.text name="message.invalidpage" /></p>
	</div>
</#if>