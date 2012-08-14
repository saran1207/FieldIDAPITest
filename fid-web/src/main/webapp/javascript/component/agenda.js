/**
 */

var calendar = (function() {

	var callback;
	var data;
	var id;
	var startDate;
	var startDataDate;

	/*
	 * public methods exposed.
	 * defaults to highlighting "today's" events.
	 * eventDays contains array of events per day.
	 */
	var init = function(calendarId, date, url) {
		id = calendarId;

		startDate = new Date(date);
		callback = url;

		var formattedDate = startDate.getUTCFullYear() + '-' + (startDate.getUTCMonth()+1) + '-' + startDate.getUTCDate();
		var calendar = $('#'+id);

		calendar.datepicker(
			{
				onSelect:select,
				showOtherMonths: true,
				selectOtherMonths: true,
				yearRange:'1920:2050',
				dateFormat:'yy-mm-dd',
				defaultDate : formattedDate,
				onChangeMonthYear: changeMonthYear
			});
		addEventMarkers(calendar);

		//filter(from,to);  TODAY or 1=end of month.no filtering.
	};

	function addEventMarkers(calendar) {
		// gather data from rendered listview.
		var eventData = getEventData();

		calendar.find('table td a.ui-state-default').each(function(index) {
			$(this).removeClass('eventful');
			if (eventData[index]) {
				$(this).addClass('eventful');
			}
		});
	}

	function getEventData() {
		var result = new Object();
		// CAVEAT : assumed to be sorted and in the format {millis index}    e.g. "1343692800000 4"
		$('#'+id).parent().find('.agenda-day .meta-data').each(function(index) {
			var index =  parseInt($(this).html().split(" ")[1]);
			var data = {
				index: index
			}
			result[index] = data;
		});
		return result;
	}

	function changeMonthYear(year, month, inst) {
		var url = new String(callback)+'&year='+year+'&month='+month;
		var wcall = wicketAjaxGet(url, function() { }, function() { });
	}

	function select(dateText,inst) {
		inst.inline = false;
		var day = inst.selectedDay.replace(new RegExp('<(p|P)(.*)?>'),'');
		var month = inst.selectedMonth;
		var year = inst.selectedYear;
		filter(year, month, day);
	}

	function filter(year, month, day) {
		// TODO - add blank slate if no events are shown.   i.e.   "No Events for _____"
		$('.agenda-days .agenda-day').each(function() {
			var listDate = getDateFromMetaData($(this).find('.meta-data'));
			if (listDate.getUTCDate()==day && listDate.getUTCMonth()==month && listDate.getUTCFullYear()==year) {
				$(this).show();
			} else {
				$(this).hide();
			}
		});
	}

	function getDateFromMetaData(meta) {
		return new Date(parseInt(meta.html().split(" ")[0]));
	}

	return {
		init: init
	};

})();




