var loggedInUserName = '';
var tenantName = '';
var sessionTestUrl = "";
var sessionTimeOut = 30;
var twoSeconds = 2000;
var loginWindowOpen = false;


function minutesToMilliseconds( minutes ) {
	return minutes * 60 * 1000;
}

function testSession() {
	if( !loginWindowOpen ) {
        $.ajax(
        {
            url: sessionTestUrl,
            async:true,
            type: "GET",
            dataType: "script"
        });
	}
}

function promptForLogin() {
    var href = $(".loginLinkSection a")[0].href;

	var formParams = { userName: loggedInUserName, companyID: tenantName };

    $.colorbox({
        href: href,
        data: $.param(formParams),
        overlayClose:false,
        escKey: false,
        width:505,
        height:300
    });
    loginWindowOpen = true;
}

function quickLoginSubmit( event ) {

	event.preventDefault();
	var form = $('#quickLoginForm');
    var data = form.serialize();

    $.colorbox({
        href: form[0].action,
        data: data,
        overlayClose:false,
        escKey: false,
        width:505,
        height:300
    });

}

function interceptQuickLoginEvent(formId) {
    // For some reason, only the class selector works properly in jquery after we've just updated a region by ajax
    $('.'+formId).bind('submit', quickLoginSubmit);
}

function closeLoginLightbox() {
    $.colorbox.close();
    loginWindowOpen = false;
}

$(document).ready(function() {
    var delay = minutesToMilliseconds(sessionTimeOut) + twoSeconds;
    $('#fieldidBody').at_intervals(testSession, { delay: delay});
});
