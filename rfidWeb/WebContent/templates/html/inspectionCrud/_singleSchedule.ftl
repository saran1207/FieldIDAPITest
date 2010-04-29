<div class="infoSet" id="schedule_${index}">
	<label class="label" id="nextInspectionTypeText">${type.name}</label>
	<span  class="fieldHolder">
		<@s.hidden name="nextInspectionTypes[${index}]" id="nextInspectionType_${index}" value="${type.id}"/>
		<@s.hidden name="nextInspectionDates[${index}]" id="nextInspectionDate_${index}" value="${date}"/>
	
		<span class="date">	on <span class="date" style="display:inline; float:none;" id="nextInspectionDateText_${index}">${date}</span></span>
		<span class="date">
			for job xxxxxxxxxx
		</span>
		<a href="#remove" name="remove" onclick="return removeSchedule(${index})">remove</a>
	</span>
</div>