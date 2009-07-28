var updateNextDateUrl = '';
var productTypeId;

function updateNextDate() {	
	var inspTypeId = $('inspectionTypeId').getValue();
	var customerId = $('customer').getValue();
	var inspDate = $('inspectionDate').getValue();
	
	var url = updateNextDateUrl + '?inspectionTypeId=' + inspTypeId + '&productTypeId=' + productTypeId + '&startDate=' + inspDate + '&customerId=' + customerId;
	
	getResponse(url);
}