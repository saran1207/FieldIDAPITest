var assets = new Array();
function sendRequests() {
	var totalAssets = assets.length;
	var completed = 0;
	var totalComplete;
	
	assets.each(function(asset) {
		$('productId').value= asset.id;
		
		
		$('inspectionCreate').request({
			asynchronous:false,	
			onSuccess: contentCallback});
		
		completed++;
		totalComplete = completed/totalAssets*100;
		
		$$('#step4 .percentBarUsed').first().setStyle({width: totalComplete + "%"});
		$('completedInspections').update(completed);
		
	}); 
}


onDocumentLoad(function() {
	
	
	$('saveInspections').observe('click', function(event) {
		event.stop();
		
		toStep(4);
	
		
		sendRequests();
	});
});