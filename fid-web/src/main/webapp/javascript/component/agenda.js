/**
 */

var calendar = (function() {

	/*
	 * public methods exposed.
	 * defaults to highlighting "today's" events.
	 */
	var init = function(id) {
		$('#'+id).datepicker(
			{
				onSelect:select,
				onChangeMonthYear: changeMonthYear
			});
	};

	function changeMonthYear(year, month, inst) {
		alert(year + ' ' + month);
	}

	function select(dateText,inst) {
		var date = new Date(dateText);
		filter(date.getUTCDate());
	}

	function filter(dayOfMonth) {
		// TODO - add blank slate if no events are shown.   i.e.   "No Events for _____"
		$('.agenda-days .agenda-day').each(function() {
			selectedDayOfMonth = $(this).find('.meta-data').html();
			if (selectedDayOfMonth==dayOfMonth) {
				$(this).show();
			} else {
				$(this).hide();
			}
		});
	}

	return {
		init: init
	};

})();




