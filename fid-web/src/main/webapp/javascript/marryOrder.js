var ordersUrl = "";
var marryOrderUrl = "";	
var marryOrderTitle = "";
	
function selectOrder(assetSerialId, lineItemId) {

	var url = marryOrderUrl + "?uniqueID="+assetSerialId+"&lineItemId="+lineItemId;
	getResponse( url );
}
	
	
function ajaxForm() {
	$('orderForm').observe('submit', submitAjaxForm);
}
			
			
function submitAjaxForm(event) {
	// block default form submit
 	event.stop();
  
	Lightview.show({
		href: ordersUrl,
		rel: 'ajax',
		title: marryOrderTitle,
		options: {
			menubar: true,
			topclose: false,
			autosize: true,
			
			ajax: {
				parameters: Form.serialize('orderForm'),
				onComplete: ajaxForm
			}
		}
	});
}
	