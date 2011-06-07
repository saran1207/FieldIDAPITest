${action.setPageType('event', 'quickEvent')!}
<title>
	<@s.text name="label.perform_an_event_on">
		<@s.param>${asset.serialNumber}</@s.param>
	</@s.text>
</title>
<head>
	<@n4.includeStyle href="quick_event" type="page"/>
</head>
<fieldset class="leftColumn">
	<legend class="schedule">
		<span class="labeltext"><@s.text name="label.perform_scheduled_event"/></span>
	</legend>
	
	<#if eventSchedules.isEmpty() >
		<p class="emptyScheduleList"><@s.text name="label.no_schedules"/></p>
	<#else>
		<#list eventSchedules as schedule>
			<table>
				<tbody>
					<tr>
						<td>
			<#if schedule.isPastDue()>
				<div class="failColor">
					<#if schedule.daysPastDue! == 1>
						<p><@s.text name="label.yesterday"/></p>
					<#else>
						<p><@s.text name="label.x_days_ago"><@s.param>${schedule.daysPastDue!}</@s.param></@s.text></p>
					</#if>
				</div>
			<#elseif schedule.daysToDue == 0>
				<div class="passColor">
					<p><@s.text name="label.due_today"/></p> 
				</div>
			<#else>
				<div class="passColor">
					<#if schedule.daysToDue == 1>
						<p><@s.text name="label.tomorrow"/></p>
					<#else>
						<p><@s.text name="label.x_days_away"><@s.param>${schedule.daysToDue!}</@s.param></@s.text></p>
					</#if>
				</div>
			</#if>
				</td>
				<td>
			
				<label>${schedule.nextDate?string("MM/dd/yy")}</label>  
				</td>
				<td>
					<@s.url id="performSchedule" action="selectEventAdd" assetId="${schedule.asset.id}" type="${schedule.eventType.id}" scheduleId="${schedule.id}"/>
					<#if schedule.project?exists>
						<@s.text name="label.quickEventSchedule">
							<@s.param>
								<a href="${performSchedule}">${schedule.eventType.name}</a>
							</@s.param>
							<@s.param>${schedule.project.name}</@s.param>
						</@s.text>
					<#else>
						<#if schedule.isPastDue()>
							<a class="red" href="${performSchedule}">
						<#else>
							<a href="${performSchedule}">
						</#if>
						${schedule.eventType.name}</a>
					</#if>
				</td>
			</tr>
		</#list>
		</tbody>
	</table>
	</#if>

	<p class="footer"> 
		<@s.url id="manageSchedule" action="eventScheduleList" assetId="${asset.id}" />
		<a href='${manageSchedule}'><@s.text name="label.manage_schedules"/></a> 
	</p>
</fieldset>

<fieldset>
	<legend class="adhoc">
		<span class="labeltext"><@s.text name="label.perform_ad_hoc_event"/></span>
	</legend>
	
	<#if associatedEventTypes.isEmpty()>
		<p>
			<@s.text name="label.no_events">
				<@s.param>${asset.type.name}</@s.param>
			</@s.text>
		</p>
		<p>
			<@s.url id="addEventType" action="selectEventTypes" assetTypeId="${asset.type.id}" />
			<@s.text name="label.click_here_to_add_one">
				<@s.param><a href="${addEventType}"><@s.text name="label.click_here"/></a></@s.param>
			</@s.text>
		</p>
	<#else>
		<#list associatedEventTypes as associatedEventType>
			<p>
				<@s.url id="performEvent" action="selectEventAdd" assetId="${asset.id}" type="${associatedEventType.eventType.id}"/>
				<a href="${performEvent}">${associatedEventType.eventType.name}</a>
			</p>
		</#list>
	</#if>
</fieldset>
	
	
	
