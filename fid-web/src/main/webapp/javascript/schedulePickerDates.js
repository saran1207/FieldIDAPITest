function formatDate(date, dateFormat) {
	return jQuery.datepicker.formatDate(dateFormat, date);
}
function addDays(date, days) {
	return new Date( date.setDate(date.getDate() + days) );
}
function addMonths(date, months) {
	return new Date( date.setMonth(date.getMonth() + months) );
}
function addYears(date, years) {
	return new Date( date.setFullYear(date.getFullYear() + years) );
}

function updateDateTimePicker(source,target) {
	// source (All Day checkbox) is checked, turn off time picking ability.
	if (source.checked) {
		jQuery.datepicker._disableTimepickerDatepicker(jQuery(target)[0]);
	} else {
		jQuery.datepicker._enableTimepickerDatepicker(jQuery(target)[0]);
	}
}