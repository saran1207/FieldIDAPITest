<#if eventSchedule.pastDue>
	<div class="failColor floatLeft inline">
		<p><@s.text name="label.overdue"/></p> 
	</div>
<#elseif eventSchedule.daysToDue == 0>
	<div class="passColor floatLeft inline">
		<p><@s.text name="label.today"/></p> 
	</div>
<#else>
	<div class="passColor floatLeft inline">
		<p><@s.text name="label.in_x_days"><@s.param>${eventSchedule.daysToDue!}</@s.param></@s.text></p>
	</div>
</#if>

<div class="scheduleDate">${action.formatDate(eventSchedule.nextDate, false)}</div>