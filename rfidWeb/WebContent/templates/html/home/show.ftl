<title><@s.text name="title.home"/></title>
<head>
	<@n4.includeStyle type="page" href="home" />
</head>
<div id="dashboardShortCuts" class="dashboardBlock">
	<div id="quickLinks" class="dashboardSection">
		<h3><@s.text name="label.goto"/>:</h3>
		<ul id="quickLinkList">
			<li><a href="<@s.url action="schedule!createSearch"/>?criteria.status=INCOMPLETE"><@s.text name="label.viewupcominginspections"/></a></li>
 			<li><a href="<@s.url action="report"/>"><@s.text name="label.viewinspectionhistory"/></a></li>
			<li><a href="<@s.url action="search"/>"><@s.text name="label.findaproduct"/></a></li>
			<#if securityGuard.brandingEnabled && primaryOrg.webSite?exists >
				<li><a href="${primaryOrg.webSite?html}" target="_blank">${primaryOrg.name?html} <@s.text name="label.web_site"/></a></li>
			</#if>
			<#if sessionUser.admin>
				<li><a href="<@s.url action="startWizard" namespace="quickSetupWizard"/>"><@s.text name="label.quick_setup_wizard"/></a></li>
			</#if>
		</ul>
	</div>
	
	<#if securityGuard.projectsEnabled && sessionUser.employeeUser>
		<div id="jobs" class="dashboardSection">
			<h3><@s.text name="label.jobs"/></h3>
			<table class="simpleTable decorated">
				<#if myJobs.hasResults()>
					<tr>
						<td colspan="4">
							<@s.text name="label.you_currently_have"/> <a href="<@s.url action="jobs" justAssignedOn="true"/>">${myJobs.totalResults} <@s.text name="label.open"/></a> <@s.text name="label.jobs_assigned_to_you"/>
						</td>
					</tr>
					<tr>
						<th><@s.text name="label.job_id"/></th>
						<th><@s.text name="label.title"/></th>
						<th><@s.text name="label.status"/></th>
						<th>&nbsp;</th>
					</tr>
					<#list myJobs.list as job >
						<tr>
							<td>${job.projectID?html}</td>
							<td>${job.name?html}</td>
							<td>${job.status?html}</td>
							<td><a href="<@s.url action="job" uniqueID="${job.id}"/>"><@s.text name="label.view"/></a></td>
						</tr>
					</#list>
				<#else>
					<tr>
						<td >
							<@s.text name="label.you_currently_have_no_jobs_assigned_to_you"/>
						</td>
					</tr>
				</#if>
			</table>
		</div>
	</#if>
</div>




<div id="relaseInformation" class="dashboardBlock dashboardBoardBottom">
	<div id="releaseNotes" class="dashboardSection">
		<h3>${currentReleaseNotes.title?html} <span class="moreLink"><a href="<@s.url value="${currentReleaseNotes.url}"/>" target="_blank"><@s.text name="label.more"/></a> </span></h3>
		<#if !currentReleaseNotes.bullets.empty>
			<ul class="informationList">
				<#list currentReleaseNotes.bullets as bullet>
					<li>${bullet?html}</li>
				</#list>
			</ul>
		</#if>
	</div>
	<div id="helpVideos" class="dashboardSection">
		<h3 ><@s.text name="label.instructionalvidoes"/> <span class="moreLink"><a href="<@s.url action="instructionalVideos"/>"><@s.text name="label.more"/></a></span></h3>
		<#include "../instructionalVideos/_watchIntroVideo.ftl"/>
		<ul class="informationList">
			<#list summary as video >
				<li>${video.name?html} - <a href="${video.url}" target="_blank"><@s.text name="label.watchnow"/></a></li>
			</#list>
		</ul>
	</div>
</div>

