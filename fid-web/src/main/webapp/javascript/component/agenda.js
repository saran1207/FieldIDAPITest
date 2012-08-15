/**
 */
var agendaFactory = (function() {

	var create = function(calendarId, date, url) {
		var a = agenda(calendarId, date, url);
		a.show();
		return a;
	}

	function agenda(agendaId, date, url) {
		var id = agendaId;
		var callback = url;
		var startDate = new Date(date);
		var initialized = false;

		var show = function() {
			if (!initialized) {
				init();
			}
			updateCalendarEventMarkers();
			// TODO : filtering.
		};

		function init() {
			calendar().datepicker(
				{	onSelect:select,
					showOtherMonths: true,
					selectOtherMonths: true,
					onChangeMonthYear: changeMonthYear
				});

			// attach button to toggle calendar.
			$('#'+id + ' .toggle-button').click(function() {
				calendar().slideToggle(200);
				calendar().toggleClass('on');
			});
			// TODO : resize (enlarge) listview when calendar closes.

			initialized = true;
		}

		function calendar() {
			return $('#'+id + ' .datepicker');
		}

		var updateCalendarEventMarkers = function() {
			// gather data from rendered listview.
			var eventData = getEventData();

			calendar().find('table a.ui-state-default').each(function(index) {
				$(this).removeClass('eventful');
				if (eventData[index]) {
					$(this).addClass('eventful');
				}
			});
		}

		function getEventData() {
			var result = new Object();
			// CAVEAT : assumed to be sorted and in the format {MILLISECONDS INDEX}    e.g. "1343692800000 4"
			$('#'+id).parent().find('.meta-data').each(function(index) {
				var index =  parseInt($(this).html().split(" ")[1]);
				result[index] = "hasData";
			});
			return result;
		}

		function changeMonthYear(year, month, inst) {
			var url = new String(callback)+'&year='+year+'&month='+month;
			var wcall = wicketAjaxGet(url, function() {updateCalendarEventMarkers();filter();}, function() {});
		}

		function select(dateText,inst) {
			// TODO : highlight the day?
			inst.inline = false;
			var day = inst.selectedDay.replace(new RegExp('<(p|P)(.*)?>'),'');
			var month = inst.selectedMonth;
			var year = inst.selectedYear;
			filter(year, month, day);
		}

		function filter(year, month, day) {
			if (year && month && day) {
				// TODO - add blank slate if no events are shown.   i.e.   "No Events for _____"
				// ??? change this query to just $('.agenda-day')  test performance....?
				$('#'+id + ' .agenda-day').each(function() {
					var listDate = getDateFromMetaData($(this).find('.meta-data'));
					if (listDate.getUTCDate()==day && listDate.getUTCMonth()==month && listDate.getUTCFullYear()==year) {
						$(this).show();
					} else {
						$(this).hide();
					}
				});
			} else {   // show everything.
				$('#'+id +' .agenda-day').show();
			}
		}

		function getDateFromMetaData(meta) {
			return new Date(parseInt(meta.html().split(" ")[0]));
		}

		return {
			show: show,
			updateMarkers:updateCalendarEventMarkers
		}

	}



	return {
		create:create
	}

})();
