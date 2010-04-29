<head>
	<@n4.includeScript>
		var index = 0;
		var addScheduleUrl = '<@s.url action="addSchedule" namespace="/ajax" />';
		var autoSuggestUrl = '<@s.url action="autoSuggestSchedule" namespace="/ajax" />';
		
		function addSchedule() {
			var types =$('nextInspectionTypeSelection');
		
			var params = new Object();
			params.date =  $('nextDate').getValue();
			params.inspectionType = types.options[types.selectedIndex].value;
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

<div class="infoSet">
	<span class="label">
		<@s.select name="nextInspectionTypeSelection" id="nextInspectionTypeSelection" list="%{product.type.inspectionTypes}" listKey="id" listValue="name" theme="fieldidSimple"/>
	</span>
	<span class="fieldHolder">
		<span class="date">
			on 
			<@s.datetimepicker id="nextDate" name="newScheduleDate" theme="fieldidSimple"/>
		</span>
		<span class="date">
			for job xxxxxxxxx
		</span>
		<a href="#add" name="add" onclick="return addSchedule();">add</a>
	</span>
</div>

<@n4.includeScript>
	autoSuggest();
</@n4.includeScript>