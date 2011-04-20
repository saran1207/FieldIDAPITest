<head>
	<link type="text/css" rel="stylesheet" href="<@s.url value="/style/pageStyles/projectEvents.css"/>"/>
    <@n4.includeScript src="eventSchedule"/>
</head>
${action.setPageType('job','events')!}
<#assign secondaryNavAction="list"/>
<#include "_secondaryNav.ftl"/>


<@s.form action="jobEvents" theme="fieldid" cssClass="smallForm" method="get">
	<@s.hidden name="projectId"/>
	
	<label for="status"><@s.text name="label.schedulestatus"/></label>
	<@s.select name="searchStatuses" list="scheduleStatuses" listKey="name" listValue="%{getText(label)}"/>
	
	<@s.submit key="label.filter"/>
</@s.form>

<#if page.hasResults() && page.validPage() >
	<#assign currentAction="jobEvents" >
	<#include '../common/_pagination.ftl' >
	<div class="pageSection">
		<h2 class="decoratedHeader"><@s.text name="label.eventsonproject"/></h2>
		<div class="sectionContent" >
			<#list page.list as event > 
				<#include "../projects/_attachedEvents.ftl"/>
			</#list>
		</div>
	</div>
	<#include '../common/_pagination.ftl' >
<#elseif  page.totalResults == 0  >
	<div class="emptyList" >
		<h2><@s.text name="label.noresults" /></h2>
		<p>
			<@s.text name="label.emptyprojecteventslist"/> <#if sessionUser.hasAccess("createevent")><@s.text name="label.emptyprojecteventlistinstruction"/></#if></label>
		</p>
	</div>
<#else>
	<div class="emptyList" >
		<h2 class="decoratedHeader"><@s.text name="label.invalidpage" /></h2>
		<p>
			<@s.text name="message.invalidpage" />
			<a href="<@s.url  action="jobEvents" projectId="${projectId}"/>" ><@s.text name="message.backtopageone"/></a>
		</p>
	</div>
</#if>


