/**
 */

var calendar = (function() {

	/*
	 * public methods exposed.
	 * defaults to highlighting "today's" events.
	 * eventDays contains array of events per day.
	 */
	var init = function(id, eventDays) {
		var calendar = $('#'+id);
		calendar.datepicker(
			{
				onSelect:select,
				onChangeMonthYear: changeMonthYear
			});
		highlightEventfulDays(calendar,eventDays.events);
	};

	function highlightEventfulDays(calendar,eventDays) {
		calendar.find('eventful').each(function(index) { $(this).remove(); });
		calendar.find('table td a.ui-state-default').each(function(index) {
			if (eventDays[index]>0) {
				$(this).prepend('<p class="eventful">&#8226;</p>');
			}
		});
	}

	function changeMonthYear(year, month, inst) {
		alert(year + ' ' + month);
	}

	function select(dateText,inst) {
		inst.inline = false;
		var day = inst.selectedDay.replace(new RegExp('<(p|P)(.*)?>'),'');
		filter(day);
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




