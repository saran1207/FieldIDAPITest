var assets = new Array();
function sendRequests() {
	var completer = new Completer(assets);
	completer.start();
}

function sendRequest(asset, completer) {
	$('productId').value= asset.id;
	$('inspectionCreate').request({
		onSuccess: contentCallback,
		onComplete: function(){	
			completer.completedCreation();
		}
	});
}

function Completer(assets) {
	this.totalAssets = assets.length;
	this.assets = assets;
	this.completed = 0;
	this.start = function () {
		sendRequest(assets[this.completed], this);
	}
	
	this.completedCreation = function() {
			this.completed++;
			var totalComplete = this.completed/this.totalAssets*100; 
			
			$$('#step3 .percentBarUsed').first().setStyle({width: totalComplete + "%"});
			$$('#completedInspections').first().update(this.completed);
			
			if (totalComplete == 100) {
				toStep(4);
			} else {
				sendRequest(assets[this.completed], this);
			}
			
		};
}


onDocumentLoad(function() {
	$('saveInspections').observe('click', function(event) {
		event.stop();
		$('saveInspections').disable();
		$$("#step3 .stepAction").invoke("hide");
		$$("#step3 .progress").invoke("show");
		sendRequests();
	});
});