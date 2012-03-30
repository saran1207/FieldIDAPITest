var ordersUrl = "";
var marryOrderUrl = "";	
var marryOrderTitle = "";
	
function selectOrder(assetId, lineItemId) {

	var url = marryOrderUrl + "?uniqueID="+assetId+"&lineItemId="+lineItemId;
	getResponse( url );
}
	
	
function ajaxForm() {
	$('orderForm').observe('submit', submitAjaxForm);
}
			
			
function submitAjaxForm(event) {
	// block default form submit
 	event.stop();

    jQuery.get(
        ordersUrl ,
        jQuery('#orderForm').serialize(),
        function (data) {
            jQuery().colorbox({html:data, title: marryOrderTitle,  onComplete: ajaxForm});
        }
    );
    return false;
}
	