var updateUrl;

function updateExample() {
	var params = new Object();
	params[$('messageBody').name] = $('messageBody').getValue();
	
	getResponse(updateUrl, 'post', params);
}