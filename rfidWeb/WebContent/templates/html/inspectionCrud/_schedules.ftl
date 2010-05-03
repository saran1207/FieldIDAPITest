<head>
	<@n4.includeScript>
		var index = 0;
		var addScheduleUrl = '<@s.url action="addSchedule" namespace="/ajax" />';
		var autoSuggestUrl = '<@s.url action="autoSuggestSchedule" namespace="/ajax" />';
		
		function addSchedule() {
			var types = $('nextInspectionTypeSelection');
			var jobs = $('jobSelection');
			
			var params = new Object();
			params.date =  $('nextDate').getValue();
			params.inspectionType = types.options[types.selectedIndex].value;
			params.jobId = jobs.options[jobs.selectedIndex].value;
			params.index = index;
			
			getResponse(addScheduleUrl, "post", params);
			
			return false;
		}
		
		function removeSchedule(idx) {
			$('schedule_' + idx).remove();
			
			return false;
		}
		
		function autoSuggest() {
			var params = new Object();
			params.inspectionType = ${type};
			params.product = ${productId}
			params.index = index;
			
			getResponse(autoSuggestUrl, "post", params);
			
			return false;
		}
		
	</@n4.includeScript>
</head>


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
	autoSuggest();
</@n4.includeScript>