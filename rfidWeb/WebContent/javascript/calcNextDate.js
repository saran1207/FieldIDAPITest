var updateNextDateUrl = '';
var productTypeId;

function updateNextDate() {	
	var inspTypeId = $('inspectionTypeId').getValue();
	var ownerId = $('ownerId').getValue();
	var inspDate = $('inspectionDate').getValue();
	
	var url = updateNextDateUrl + '?inspectionTypeId=' + inspTypeId + '&productTypeId=' + productTypeId + '&startDate=' + inspDate + '&ownerId=' + ownerId;
	
	getResponse(url);
}