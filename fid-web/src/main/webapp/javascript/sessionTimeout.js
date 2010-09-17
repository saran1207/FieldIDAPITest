var loggedInUserName = '';
var tenantName = '';
var sessionTestUrl = "";
var loginUrl = "";
var timeOfLastCheck = new Date();
var loginWindowTitle = "Session Expired";
var sessionTimeOut = 30;
var twoSeconds = 2;

function testSession() {
	if( $( 'lightview' ) == null ||  $( 'lightview' ).positionedOffset().left <= 0 ) { 
		getResponseNonInteractive( sessionTestUrl, "get" );
	}
}


function minutesToSeconds( minutes ) {
	return minutes * 60;
}  

function promptForLogin() {
	
	var formParams = { userName: loggedInUserName, companyID: tenantName }
	
	
	Lightview.show(	{
		href: loginUrl,
		rel: 'ajax',
		title: loginWindowTitle,
		options: {
			topclose: false,
			closeButton: false,
			width: 500,
			height: 300,
			ajax: {
				method: 'get',
				evalScripts: true,
				parameters: formParams
			}
		}	
	});
}


function quickLoginSubmit( event ) {
	
	Event.stop(event);
	var form = $('quickLoginForm');
	
	Lightview.show({
		href: form.action,
		rel: 'ajax',
		title: loginWindowTitle,
		options: {
			topclose: false,
			width: 500,
			height: 300,
			ajax: {
				evalScripts: true,
				method: form.method,
				parameters: form.serialize() 
			}
		}
	});
		
}

function kickOtherUserSubmit( event ) {
	
	Event.stop(event);
	var form = $('kickSessionConfirm');
	
	Lightview.show({
		href: form.action,
		rel: 'ajax',
		title: loginWindowTitle,
		options: {
			topclose: false,
			width: 500,
			height: 300,
			ajax: {
				evalScripts: true,
				method: form.method,
				parameters: form.serialize() 
			}
		}
	});
}


onDocumentLoad(function() { new PeriodicalExecuter(testSession, minutesToSeconds(sessionTimeOut) + twoSeconds); });

