<#if eventSchedule.pastDue>
	<div class="failColor floatLeft inline">
		<#if eventSchedule.daysPastDue! == 1>
			<p><@s.text name="label.yesterday"/></p>
		<#else>
			<p><@s.text name="label.x_days_ago"><@s.param>${eventSchedule.daysPastDue!}</@s.param></@s.text></p>
		</#if>
	</div>
<#elseif eventSchedule.daysToDue == 0>
	<div class="passColor floatLeft inline">
		<p><@s.text name="label.due_today"/></p> 
	</div>
<#else>
	<div class="passColor floatLeft inline">
		<#if eventSchedule.daysToDue == 1>
			<p><@s.text name="label.tomorrow"/></p>
		<#else>
			<p><@s.text name="label.x_days_away"><@s.param>${eventSchedule.daysToDue!}</@s.param></@s.text></p>
		</#if>
	</div>
</#if>

<div class="scheduleDate">${action.formatDateWithTime(eventSchedule.nextDate, true)}</div>