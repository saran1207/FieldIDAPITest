<head>
	<style type="text/css">
		#filter {
			float: left;
			margin-top: 5px;
		}
		
		#filter .listOfLinks{
			margin-left: 15px;
		}
		
		.filterHeading{
			display: inline;
			float: left;
		}
		
		.textbox, .searchContainer label {
			display: block;
			float: left;
		}
		
		.searchContainer label {
			margin: 6px 8px 0 0;
			font-size: 13px;
			font-weight: bold;
			
		}
		
		.fieldHolder .textbox{
			font-size:1.2em;
			border:1px solid #666666;
			padding: 3px;
			margin-right: 7px;
		}
		
		#searchJob{
			padding: 0 1px 0 1px;
		}
		
		#searchJob, .textbox{
			vertical-align: middle;
			*vertical-align: text-bottom;
		}
		
		.searchContainer{
			margin-left: 50px;
			width: 450px;
			float: left;
		}
		
		.searchContainer .fieldHolder{
			display: inline;
		}
		
		.headerContents{
			background-color: #E8E8E8;
			padding: 5px;
		}
		
	</style>
</head>
${action.setPageType('job', 'list')!}
<div class="headerContents">
	<h3 class="filterHeading"><@s.text name="label.filter"/>:</h3>
	<div id="filter">
		<ul class="listOfLinks">
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
		
	<@s.form action="jobsSearch" id="jobSearchForm"  theme="fieldid" cssClass="fullForm">
		<div class="searchContainer">
			<label for="searchID"><@s.text name="label.find"/>:</label> 
			<@s.textfield name="searchID"/>
			<@s.submit id="searchJob" key="hbutton.search" cssClass="saveButton"/>
		</div>
	</@s.form >
</div>

<#if  page.hasResults() && page.validPage() >
	<#assign currentAction="jobs"/>
	<#include '../common/_pagination.ftl' />
	<table class="list">
		<tr>
			<th><@s.text name="label.type"/></th>
			<th><@s.text name="label.projectid"/></th>
			<th><@s.text name="label.title" /></th>
			<th><@s.text name="label.organization" /></th>
			<th><@s.text name="label.customer" /></th>
			<th><@s.text name="label.division" /></th>
			
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
				<td>${(project.owner.internalOrg.name?html)!}</td>
				<td>${(project.owner.customerOrg.name?html)!}</td>
				<td>${(project.owner.divisionOrg.name?html)!}</td>
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



