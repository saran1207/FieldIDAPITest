var assets = new Array();
function sendRequests() {
	var inspectionCreator = new InpsectionCreator(assets);
	inspectionCreator.start();
}

function sendRequest(asset, inspectionCreator) {
	$('productId').value= asset.id;
	$('inspectionCreate').request({
		onSuccess: contentCallback,
		onComplete: function(){	
			inspectionCreator.completedCreation();
		}
	});
}

function InpsectionCreator(assets) {
	this.totalAssets = assets.length;
	this.assets = assets;
	this.completed = 0;
	this.start = function () {
		sendRequest(assets[this.completed], this);
	};
	
	this.completedCreation = function() {
			this.inspectionCompleted();
			this.updateProgress();
			this.sendNextInspection();
		};
	this.inspectionCompleted = function() {
		this.completed++;
	};
	
	this.sendNextInspection = function() {
		if (this.completed == this.totalAssets) {
			toStep(4);
		} else {
			sendRequest(assets[this.completed], this);
		}
	};
	
	this.updateProgress = function() {
		var percentComplete = (this.completed/this.totalAssets) * 100;
		
		$$('#step3 .percentBarUsed').first().setStyle({width: percentComplete + "%"});
		$$('#completedInspections').first().update(this.completed);
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