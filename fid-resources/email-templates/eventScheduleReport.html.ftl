<style type='text/css'> 
table.message { 
	border-width: 1px ; 
	border-spacing: 1px; 
	border-style: none; 
	border-color: #999999; 
	border-collapse: collapse; 
	background-color: rgb(255, 255, 240); 
	width:900px;
} 
table.message th { 
	border-width: 1px; 
	padding: 4px; 
	border-style: solid; 
	border-color: #999999;
	background-color: #DDDDDD; 
	-moz-border-radius: 0px; 
	
}
table.message td { 
	border-width: 1px; 
	padding: 4px; 
	border-style: solid; 
	border-color: #999999; 
	background-color: white; 
	-moz-border-radius: 0px; 
} 

table.overdue td { 
	background-color:#FFEBEB;
} 

table.message th, table.message td, table.overdue td {
	font-size: 11px;
	font-family: "lucida grande", tahoma, verdana, arial, sans-serif;
} 

table.wrapper {
	width:900px;
	background:#FFFFFF;
	border:2px solid #CCCCCC;
}

.reportContent {
	padding-bottom: 10px;
	margin-bottom: 10px;
}

h1 {
	font-size: 26px;
	font-weight:bold;
	font-family: "lucida grande", tahoma, verdana, arial, sans-serif;
}

h3 {
    font-size: 17px;
    font-family: "lucida grande", tahoma, verdana, arial, sans-serif;
    margin-bottom:10px;
}

h4 {
    font-size: 12px;
    font-weight:normal;
    padding-top:0px;
    margin-top:0px;
    margin-bottom:10px;
    font-family: "lucida grande", tahoma, verdana, arial, sans-serif;
}

body { 
	background:#f9f9f9;
}

</style>

<table width=900 align="center" class="wrapper" >
	<tr><td style="padding: 25px;">

<div class="reportContent">
	<h1>${setting.name}</h1>
</div>

<#if failedEvents?exists>
	<div class="reportContent">
		<h3> Failed Events from ${dateFormatter.format(failedReportStart)} to ${dateFormatter.format(failedReportEnd)}</h3>
		
		<#if failedEvents.isEmpty() >
			<h4>Great news. You have no failed events for this period.</h4>
		<#else>
			<h4>To view more details about these failed events login to your Field ID account and click on Reporting to run a report</h4>
			<table class="message" cellpadding=2 cellspacing=2 border>
				<tr>
					<th>Date Performed</th>
					<th>Organization</th>
					<th>Owner</th>
					<th>ID Number</th>
					<th>Asset Type</th>
					<th>Location</th>
					<th>Event Type</th>
					<th>Comments</th>
				</tr>
			
				<#list failedEvents as event>
					<tr>
						<td>${event.date}</td>
						<td>${event.owner.internalOrg.name}</td>
						<td>
							<#if event.owner.customerOrg?exists >
								${event.owner.customerOrg.name!}
							</#if>
							<#if event.owner.divisionOrg?exists >
								&nbsp;>&nbsp;${event.owner.divisionOrg.name!}
							</#if>
						</td>
						<td>${event.asset.identifier}</td>
						<td>${event.asset.type.name}</td>
						<td>${event.asset.advancedLocation.freeformLocation}</td>
						<td>${event.type.name}</td>
						<td>${event.comments}</td>
					</tr>
				</#list>
			</table>
		</#if>
	</div>
</#if>

<#if upcomingEvents?exists>
	<div class="reportContent">
		<h3>Upcoming Scheduled Events from ${dateFormatter.format(upcomingReportStart)} to ${dateFormatter.format(upcomingReportEnd)}</h3>
		
		<#if upcomingEvents.empty>
			<h4>There are no scheduled events for this period.<h4>
		<#else>
			<h4>To view the details about these events login to your Field ID account and click on Schedules</h4>
			<table class="message" cellpadding=2 cellspacing=2 border>
				<tr>
					<th>Scheduled Date</th>
					<th>Organization</th>
					<th>Owner</th>
					<th>Asset Type</th>
					<th>Event Type</th>
					<th>Events Due</th>
				</tr>
	
				<#list upcomingEvents as isCount>
					<tr>
						<td>${dateFormatter.format(isCount.nextEventDate)}</td>
						<td>${isCount.organizationName}</td>
						<td>
							${isCount.customerName}
							<#if !isCount.divisionName.trim().isEmpty() >
								&nbsp;>&nbsp;${isCount.divisionName}
							</#if>
						</td>
						<td>${isCount.assetTypeName}</td>
						<td>${isCount.eventTypeName}</td>
						<td>${isCount.eventCount}</td>
					</tr>				
	
				</#list>
			
			</table>
		</#if>
	</div>
</#if>	
<#if overdueEvents?exists>
	<div class="reportContent">
		<h3>Overdue Scheduled Events as of ${dateFormatter.format(overdueDate)}</h3>
		
		<#if overdueEvents.empty>
			<h4>Great news. There are no overdue scheduled events for this period.</h4>
		<#else>
			<h4>To view the details about these events login to your Field ID account and click on Schedules</h4>
			<table class="message overdue" cellpadding=2 cellspacing=2 border>
				<tr>
					<th>Scheduled Date</th>
					<th>Organization</th>
					<th>Owner</th>
					<th>Asset Type</th>
					<th>Event Type</th>
					<th>Events Due</th>
				</tr>
	
				<#list overdueEvents as isCount>
					<tr>
						<td>${dateFormatter.format(isCount.nextEventDate)}</td>
						<td>${isCount.organizationName}</td>
						<td>
							${isCount.customerName}
							<#if !isCount.divisionName.trim().isEmpty() >
								&nbsp;>&nbsp;${isCount.divisionName}
							</#if>
						</td>
						<td>${isCount.assetTypeName}</td>
						<td>${isCount.eventTypeName}</td>
						<td>${isCount.eventCount}</td>
					</tr>				
				</#list>
			</table>
		</#if>	
	</div>
</#if>

</td></tr></table>