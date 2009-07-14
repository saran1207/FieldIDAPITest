<title><@s.text name="title.home"/></title>
<head>
	<link rel="stylesheet" type="text/css" href="<@s.url value="/style/pageStyles/home.css"/>" />
</head>
<div id="dashboardShortCuts" class="dashboardBlock">
	<div id="quickLinks" class="dashboardSection">
		<h3><@s.text name="label.goto"/>:</h3>
		<ul id="quickLinkList">
			<li><a href="<@s.url action="schedule!createSearch"/>?criteria.status=INCOMPLETE"><@s.text name="label.viewupcominginspections"/></a></li>
 			<li><a href="<@s.url action="report"/>"><@s.text name="label.viewinspectionhistory"/></a></li>
			<li><a href="<@s.url action="search"/>"><@s.text name="label.findaproduct"/></a></li>
			<#if securityGuard.brandingEnabled && tenant.webSite?exists >
				<li><a href="${tenant.webSite?html}" target="_blank">${tenant.displayName?html} <@s.text name="label.web_site"/></a></li>
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
		<h3><@s.text name="label.newfeaturesin"/> <@s.text name="app.majorversion"/> <span class="moreLink"><a href="<@s.url value="/resources/fieldID_release_notes-${action.getText('app.majorversion')}.pdf"/>" target="_blank"><@s.text name="label.more"/></a></span></h3>
		<ul class="informationList">
			<li>New Menu with Improved Navigation</li>
		    <li>Multi-Add Assets</li>
		    <li>Integrated Smart Search</li>
		    <li>Asset Search on Field ID Mobile</li>
		    <li>Simplified Field ID Mobile Sync Process</li>
		</ul>
	</div>
	<div id="helpVideos" class="dashboardSection">
		<h3 ><@s.text name="label.instructionalvidoes"/> <span class="moreLink"><a href="<@s.url action="instructionalVideos"/>"><@s.text name="label.more"/></a></span></h3>
		<ul class="informationList">
			<#list summary as video >
				<li>${video.name?html} - <a href="${video.url}" target="_blank"><@s.text name="label.watchnow"/></a></li>
			</#list>
		</ul>
	</div>
</div>

