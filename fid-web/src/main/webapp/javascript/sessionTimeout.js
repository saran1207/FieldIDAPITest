var loggedInUserName = '';
var tenantName = '';
var sessionTestUrl = "";
var loginUrl = "";
var timeOfLastCheck = new Date();
var loginWindowTitle = "Session Expired";
var sessionTimeOut = 30;
var twoSeconds = 2;
var loginWindowOpen = false;

function testSession() {
	if( !loginWindowOpen ) {
		getResponseNonInteractive( sessionTestUrl, "get" );
	}
}


function minutesToSeconds( minutes ) {
	return minutes * 60;
}  

function promptForLogin() {
	
	var formParams = { userName: loggedInUserName, companyID: tenantName }

    jQuery().colorbox( {
        href: loginUrl,
        data: jQuery.param(formParams),
        overlayClose:false,
        hideClose:true,
        escKey: false,
        width:505,
        height:300
    });
    loginWindowOpen = true;
}


function quickLoginSubmit( event ) {
	
	Event.stop(event);
	var form = jQuery('#quickLoginForm');
    var data = form.serialize();

    jQuery.colorbox({
        href: form[0].action,
        data: data,
        overlayClose:false,
        hideClose:true,
        escKey: false,
        width:505,
        height:300
    });

}

function kickOtherUserSubmit( event ) {
	
	Event.stop(event);
	var form = jQuery('#kickSessionConfirm');
    var data = form.serialize();

    jQuery.colorbox({
        href: form[0].action,
        data: data,
        overlayClose:false,
        hideClose:true,
        escKey: false,
        width:505,
        height:300
    });

}


onDocumentLoad(function() { new PeriodicalExecuter(testSession, minutesToSeconds(sessionTimeOut) + twoSeconds); });

function closeLoginLightbox() {
    closeLightbox();
    loginWindowOpen = false;
}