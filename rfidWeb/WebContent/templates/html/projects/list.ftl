<head>
	<style type="text/css">
		#filter {
			margin-top:5px;
		}
	</style>
</head>
${action.setPageType('job', 'list')!}
<div id="filter">
	<h3 class="inline"><@s.text name="label.filter"/>:</h3>
	<ul class="listOfLinks inline">
		<li class="first <#if !justAssignedOn?exists || !justAssignedOn >selected</#if>">
			<#if justAssignedOn?exists && justAssignedOn >
				<a href="<@s.url action="jobs"/>"><@s.text name="label.all_jobs"/></a>
			<#else>
				<@s.text name="label.all_jobs"/>
			</#if>
		</li>
		<li <#if justAssignedOn?exists && justAssignedOn >class="selected"</#if>>
			<#if !justAssignedOn?exists || !justAssignedOn >
				<a href="<@s.url action="jobs" justAssignedOn="true"/>"><@s.text name="label.only_jobs_im_assigned_to"/></a>
			<#else>
				<@s.text name="label.only_jobs_im_assigned_to"/>
			</#if>
		</li>
	</ul>
</div>
<#if  page.hasResults() && page.validPage() >
	<#assign currentAction="jobs"/>
	<#include '../common/_pagination.ftl' />
	<table class="list">
		<tr>
			<th><@s.text name="label.type"/></th>
			<th><@s.text name="label.projectid"/></th>
			<th><@s.text name="label.title" /></th>
			<#if securityGuard.jobSitesEnabled>
				<th><@s.text name="label.jobsite" /></th>
			<#else>
				<th><@s.text name="label.customer" /></th>
				<th><@s.text name="label.division" /></th>
			</#if>
			<th><@s.text name="label.datestarted" /></th>
			<th><@s.text name="label.estimatedcompletion" /></th>
			<th><@s.text name="label.actualcompletion" /></th>
			<th><@s.text name="label.status" /></th>
			<th>&nbsp;</th>
			<#if sessionUser.hasAccess("managejobs") >
				<th></th>
			</#if>
		</tr>
		<#list page.getList() as project > 
			<tr id="project_${project.id}" >
				<td>${project.eventJob?string(action.getText("label.eventjob"), action.getText("label.assetjob"))}</td>
				<td>${project.projectID?html}</td>
				<td><a href="<@s.url action="job" uniqueID="${project.id}" />" >${project.name?html}</td>
				<#if securityGuard.jobSitesEnabled>
					<td>${(project.jobSite.name?html)!}</td>
				<#else>
					<td>${(project.owner.customerOrg.name?html)!}</td>
					<td>${(project.owner.divisionOrg.name?html)!}</td>
				</#if>
				<td>${action.formatDateTime(project.started)}</td>
				<td>${action.formatDateTime(project.estimatedCompletion)}</td>
				<td>${action.formatDateTime(project.actualCompletion)}</td>
				<td>${project.status?html}</td>
				<td><#if project.open><@s.text name="label.open" /><#else><@s.text name="label.closed" /></#if></td>
				<#if sessionUser.hasAccess("managejobs") >
					<td>
						<a href="<@s.url action="jobDelete" uniqueID="${project.id}" justAssignedOn="${justAssignedOn?string}"/>" onclick="if( !confirm( '${action.getText( 'warning.deleteproject' )}' ) ) { return false; } "><@s.text name="label.delete"/></a>
					</td>
				</#if>
			</tr>	
		</#list>
	</table>
	
	<#include '../common/_pagination.ftl' />
<#elseif !page.hasResults() >
	<div class="emptyList" >
		<h2><@s.text name="label.noresults" /></h2>
		<p>
			<@s.text name="label.emptyprojectlist" />
			<#if sessionUser.hasAccess("managejobs") >
				 <@s.text name="label.emptyprojectlistinstruction" />
			</#if>
		</p>
	</div>
<#else>
	<div class="emptyList" >
		<h2><@s.text name="label.invalidpage" /></h2>
		<p>
			<@s.text name="message.invalidpage" />
			<a href="<@s.url  action="jobs" currentPage="1" includeParams="get"/>" ><@s.text name="message.backtopageone"/></a>
		</p>
	</div>
</#if>



