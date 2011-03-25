var assets = new Array();
function sendRequests() {
	var eventCreator = new EventCreator(assets);
	eventCreator.start();
}

function sendRequest(asset, eventCreator) {
	$('assetId').value= asset.id;
	$('eventCreate').request({
		onSuccess: contentCallback,
		onComplete: function(){	
			eventCreator.completedCreation();
		}
	});
}

function EventCreator(assets) {
	this.totalAssets = assets.length;
	this.assets = assets;
	this.completed = 0;
	this.start = function () {
		sendRequest(assets[this.completed], this);
	};
	
	this.completedCreation = function() {
			this.eventCompleted();
			this.updateProgress();
			this.sendNextEvent();
		};
	this.eventCompleted = function() {
		this.completed++;
	};
	
	this.sendNextEvent = function() {
		if (this.completed == this.totalAssets) {
			toStep(5);
		} else {
			sendRequest(assets[this.completed], this);
		}
	};
	
	this.updateProgress = function() {
		var percentComplete = (this.completed/this.totalAssets) * 100;
		
		$$('#step4 .percentBarUsed').first().setStyle({width: percentComplete + "%"});
		$$('#completedEvents').first().update(this.completed);
	};
}


onDocumentLoad(function() {
	$('saveEvents').observe('click', function(event) {
		event.stop();
		$('saveEvents').disable();
		$$("#step4 .stepAction").invoke("hide");
		$$("#step4 .progress").invoke("show");
		sendRequests();
	});
});