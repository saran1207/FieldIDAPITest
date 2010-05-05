<h2><@s.text name="label.schedules"/></h2>
<div id="schedules">
</div>

<div class="infoSet schedulesAdd">
	<div><@s.select name="nextInspectionTypeSelection" id="nextInspectionTypeSelection" list="%{product.type.inspectionTypes}" listKey="id" listValue="name" theme="fieldidSimple"/></div>
	<div><@s.text name="label.on"/></div>
	<div><@s.datetimepicker id="nextDate" name="newScheduleDate" theme="fieldidSimple"/></div>
	<#if securityGuard.projectsEnabled>
		<div><@s.select name="jobSelection" id="jobSelection" list="jobs" listKey="id" listValue="name" emptyOption="true"/></div>
	</#if>
	
	<a href="#add" name="add" onclick="return addSchedule();">add</a>
</div>

<@n4.includeScript>
	index = 0;
	addScheduleUrl = '<@s.url action="addSchedule" namespace="/ajax" />';
	autoSuggestUrl = '<@s.url action="autoSuggestSchedule" namespace="/ajax" />';
	dateErrorText = '<@s.text name="error.mustbeadate"/>';
	inspectionTypeId = '${type}';
	productId = '${productId}';
	
	autoSuggest();
</@n4.includeScript>