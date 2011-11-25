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