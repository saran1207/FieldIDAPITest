<head>
	<@n4.includeScript>
		var dateErrorMessage = "";
		function addSchedule() {
			var nextDate = $('nextDate');
			nextDate.removeClassName("inputError");
			nextDate.title="";
		 	if (validDate(nextDate.getValue())) {  
		 		updateInspectionDate(nextDate.getValue());
		 		nextDate.value = "";
		 	} else {
		 		nextDate.addClassName("inputError");
		 		nextDate.title=dateErrorMessage;
		 	}
		 	
			return false;
		}
	
		function validDate(date) {
			return !(date.trim() == "");
			
		}
	
		function updateInspectionDate(nextDate) {
		 	$('nextInspectionDate').value = nextDate;
		 	$('nextInspectionDateText').update(nextDate);
		 	if (nextDate.trim() == "") {
		 		schedules.hide();
		 	} else { 
		 		schedules.show();
		 		schedules.highlight();
		 	}
		 	
		 }
		 
		 var dateErrorMessage = "<@s.text name="error.mustbeadate"/>";
	</@n4.includeScript>
</head>


<h2><@s.text name="label.schedules"/></h2>
<div class="infoSet" id="schedules" <#if !nextInspectionDate?exists>style="display:none;"</#if>>
	<label class="label">${inspectionType.name}</label>
	<span  class="fieldHolder">
		<@s.hidden name="nextInspectionDate" id="nextInspectionDate"/>
	
		<span class="date">	on <span class="date" style="display:inline; float:none;" id="nextInspectionDateText">${nextInspectionDate!}</span></span>
		<span class="date">
			for job xxxxxxxxxx
		</span>
		<a href="#remove" name="remove" onclick="return updateInspectionDate('')">remove</a>
	</span>
	
</div>
 
<div class="infoSet">
	<label class="label">${inspectionType.name}</label>
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
