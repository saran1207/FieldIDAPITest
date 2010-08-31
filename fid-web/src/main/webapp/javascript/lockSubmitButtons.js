var buttons = new Array();
function lockSubmitButtons() {
		
	if(typeof(buttons) != 'undefined' ) {
		for( var key = 0; key < buttons.length; key++ ) { 
			var tagSubmit = $( buttons[key] );
			if( tagSubmit != null ) {
				tagSubmit.disable();
				tagSubmit.value = buttonLockMessages[key];
			}
		}
	}
} 	
function unlockSubmitButtons() {
	
	if( typeof(buttons) != 'undefined' ) {
		for( var key = 0; key < buttons.length; key++ ) { 
			var tagSubmit = $( buttons[key] );
			if( tagSubmit != null ) {
				tagSubmit.enable();
				tagSubmit.value = buttonMessages[key];
			}
		}
	}
}