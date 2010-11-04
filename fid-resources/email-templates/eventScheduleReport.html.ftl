<style type='text/css'> 
table.message { 
	border-width: 1px 1px 1px 1px; 
	border-spacing: 1px; 
	border-style: none none none none; 
	border-color: gray gray gray gray; 
	border-collapse: separate; 
	background-color: rgb(255, 255, 240); 
} 
table.message th { 
	border-width: 1px 1px 1px 1px; 
	padding: 4px 4px 4px 4px; 
	border-style: dotted dotted dotted dotted; 
	border-color: blue blue blue blue; 
	background-color: white; 
	-moz-border-radius: 0px 0px 0px 0px; 
} 
table.message td { 
	border-width: 1px 1px 1px 1px; 
	padding: 4px 4px 4px 4px; 
	border-style: dotted dotted dotted dotted; 
	border-color: blue blue blue blue; 
	background-color: white; 
	-moz-border-radius: 0px 0px 0px 0px; 
} 

table.overdue td { 
	background-color:#FFEBEB;
} 
</style>

<center>

	<#if upcomingEvents?exists>
		<h3>Scheduled Event Report:  ${setting.name}</h3>
		
		<#if upcomingEvents.empty>
			<h5>There are no scheduled events for this period</h5>
		<#else>
			<table class="message" cellpadding=2 cellspacing=2 border>
				<tr>
					<th>Scheduled Date</th>
					<th>Organization</th>
					<th>Customer</th>
					<th>Division</th>
					<th>Asset Type</th>
					<th>Event Type</th>
					<th>Events Due</th>
				</tr>
	
				<#list upcomingEvents as isCount>
					<tr>
						<td>${dateFormatter.format(isCount.nextEventDate)}</td>
						<td>${isCount.organizationName}</td>
						<td>${isCount.customerName}</td>
						<td>${isCount.divisionName}</td>
						<td>${isCount.assetTypeName}</td>
						<td>${isCount.eventTypeName}</td>
						<td>${isCount.eventCount}</td>
					</tr>				
	
				</#list>
			
			</table>
		</#if>
	</#if>	
	<#if overdueEvents?exists>
		<h3>Overdue Event Report:  ${setting.name} as of ${dateFormatter.format(overdueDate)}</h3>
		
		<#if overdueEvents.empty>
			<h5>There are no over due events</h5>
		<#else>
			<table class="message overdue" cellpadding=2 cellspacing=2 border>
				<tr>
					<th>Scheduled Date</th>
					<th>Organization</th>
					<th>Customer</th>
					<th>Division</th>
					<th>Asset Type</th>
					<th>Event Type</th>
					<th>Events Due</th>
				</tr>
	
				<#list overdueEvents as isCount>
					<tr>
						<td>${dateFormatter.format(isCount.nextEventDate)}</td>
						<td>${isCount.organizationName}</td>
						<td>${isCount.customerName}</td>
						<td>${isCount.divisionName}</td>
						<td>${isCount.assetTypeName}</td>
						<td>${isCount.eventTypeName}</td>
						<td>${isCount.eventCount}</td>
					</tr>				
	
				</#list>
			
			</table>
		</#if>	
	</#if>
	
	
</center>