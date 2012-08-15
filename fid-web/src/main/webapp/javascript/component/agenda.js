/**
 */
var agendaFactory = (function() {

	var create = function(calendarId, data, url) {
		var a = agenda(calendarId, data, url);
		a.show();
		return a;
	}

	function agenda(agendaId, eventData, url) {
		var id = agendaId;
		var callback = url;
		var data = eventData;
		var initialized = false;
		var day;
		var month;
		var year;

		var show = function() {
			init();
			drawCalendarEventMarkers();
		};

		function init() {
			if (!initialized) {
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
					updateViaAjax(year, month, 0);
				});

				initialized = true;

					// TODO : include the starting day in json data.
				updateViaAjax(2012, 08, 15);
			}
		}

		function calendar() {
			return $('#'+id + ' .datepicker');
		}

		function drawCalendarEventMarkers() {
			calendar().find('table a.ui-state-default').each(function(index) {
				$(this).removeClass('eventful');
				if (data.eventMap[index]>0) {
					$(this).addClass('eventful');
				}
			});
		}

		var updateViaAjax = function (y, m, d) {
			if (d!=day || m!=month || year!=y) {
				day = d;
				month = m;
				year = y;
				var url = new String(callback)+'&year='+year+'&month='+month;
				if (day) {
					url = url + '&day=' + day;
				}
				var wcall = wicketAjaxGet(url, function() {drawCalendarEventMarkers();}, function() {});
			}
		}

		function changeMonthYear(year, month, inst) {
			updateViaAjax(year, month);
		}

		function select(dateText,inst) {
			inst.inline = false;
			var day = inst.selectedDay.replace(new RegExp('<(p|P)(.*)?>'),'');
			var month = inst.selectedMonth;
			var year = inst.selectedYear;
			// note that this code alone uses 0 indexed months.
			updateViaAjax(year, month+1, day);
		}

		return {
			show: show,
			updateViaAjax:updateViaAjax
		}

	}



	return {
		create:create
	}

})();
