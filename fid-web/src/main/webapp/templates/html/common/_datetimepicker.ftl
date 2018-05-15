<@n4.includeScript src="jquery-ui-1.8.13.custom.min"/>
<@n4.includeScript src="jquery-ui-timepicker-addon"/>
<@n4.includeScript src="jquery.columnview"/>
<@n4.includeStyle href="datetimepicker"/>
<@n4.includeStyle href="jquery-redmond/jquery-ui-1.8.13.custom"/>

<script type="text/javascript"> 
    document.observe("dom:loaded", function() {
		initDatePicker();
	});
	
	function initDatePicker() {
		jQuery( '.datetimepicker' ).datetimepicker({
			showOn: "button",
			buttonImage: "/fieldid/images/calendar-icon.png",
			buttonImageOnly: true,
			numberOfMonths: 3,
			showButtonPanel: true,
			dateFormat: "${sessionUser.jqueryDateFormat}",
			ampm: true,
			timeFormat: "hh:mm TT",
			changeMonth: true,
			changeYear: true
		});

		jQuery( '.datepicker' ).datepicker({
			showOn: "button",
			buttonImage: "/fieldid/images/calendar-icon.png",
			buttonImageOnly: true,
			numberOfMonths: 3,
			showButtonPanel: true,
			dateFormat: "${sessionUser.jqueryDateFormat}", 
			changeMonth: true,
			changeYear: true,
			beforeShow: function(input, inst) {
				jQuery('#ui-datepicker-div').addClass("notranslate");
			}
		});
	}
</script>