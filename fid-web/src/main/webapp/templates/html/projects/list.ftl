<head>
	<style type="text/css">
		#filter h3{
			margin: 5px 0 0 0;
			font-size:1.2em;
		}
		
		#filter .listOfLinks{
			margin: 7px 20px 0 10px;		
		}
		
		#filter .listOfLinks, #filter h3 {
			float: left;
		}

		.searchContainer label {
			margin: 0 10px 3px 0;
			font-size: 13px;
			font-weight: bold;
		}
		
		.searchJobButtons{
			padding: 2px;
		}
		
		.fieldHolder .textbox{
			font-size:1.2em;
			border:1px solid #666666;
			padding: 3px;
			margin-right: 7px;
			width: 200px;
		}
	
		.headerContents{
			background-color: #eeeeee;
			padding: 10px;
		}
		
		.list th,.list td {
			min-width: 60px;
		}
	</style>
</head>
${action.setPageType('job', 'list')!}
<div class="headerContents">
	
	<div id="filter">
	<h3 class="filterHeading"><@s.text name="label.filter"/>:</h3>
		<ul class="listOfLinks inline">
			<li class="first <#if !justAssignedOn?exists || !justAssignedOn >selected</#if>">
				<#if justAssignedOn?exists && justAssignedOn >
					<a href="<@s.url action="jobs"/>"><@s.text name="label.all_jobs"/></a>
				<#else>
					<span class="linkTitle"><@s.text name="label.all_jobs"/></span>
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
		<div class="searchContainer">
			<@s.form action="jobsSearch" id="jobSearchForm"  theme="fieldid">
				<label for="searchID"><@s.text name="label.find"/>:</label> 
				<@s.textfield name="searchID"/>
				<@s.submit cssClass="searchJobButtons" key="hbutton.search"/>
				<@s.url id="cancelUrl" action="jobs"/>
				<@s.reset cssClass="searchJobButtons" key="label.reset" onclick="return redirect( '${cancelUrl}' );" />
			</@s.form >
		</div>
	</div>
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
				<td class="notranslate">${project.projectID?html}</td>
				<td class="notranslate"><a href="<@s.url action="job" uniqueID="${project.id}" />" >${project.name?html}</td>
				<td class="notranslate">${(project.owner.internalOrg.name?html)!}&nbsp;</td>
				<td class="notranslate">${(project.owner.customerOrg.name?html)!}&nbsp;</td>
				<td class="notranslate">${(project.owner.divisionOrg.name?html)!}&nbsp;</td>
				<td class="notranslate">${action.formatDateTime(project.started)}&nbsp;</td>
				<td class="notranslate">${action.formatDateTime(project.estimatedCompletion)}&nbsp;</td>
				<td class="notranslate">${action.formatDateTime(project.actualCompletion)}&nbsp;</td>
				<td class="notranslate">${project.status?html}&nbsp;</td>
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
		<h2 class="decoratedHeader"><@s.text name="label.invalidpage" /></h2>
		<p>
			<@s.text name="message.invalidpage" />
			<a href="<@s.url  action="jobs" currentPage="1" includeParams="get"/>" ><@s.text name="message.backtopageone"/></a>
		</p>
	</div>
</#if>



