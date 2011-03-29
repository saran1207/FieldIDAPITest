var assets = new Array();
var schedules = new Array();

function sendRequests() {
	var eventCreator = new EventCreator(assets, schedules);
	eventCreator.start();
}

function sendRequest(asset, schedule, eventCreator) {
	if (schedule == undefined){
		schedule = 0;
	}
	
	$('assetId').value= asset.id;
	$('scheduleId').value=schedule;
	$('eventCreate').request({
		onSuccess: contentCallback,
		onComplete: function(){	
			eventCreator.completedCreation();
		}
	});
} 

function EventCreator(assets, schedules) {
	this.totalAssets = assets.length;
	this.assets = assets;
	this.schedules = schedules;
	this.completed = 0;
	
	this.start = function () {
		sendRequest(assets[this.completed],this.schedules[this.completed] , this);
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
			sendRequest(assets[this.completed], schedules[this.completed],this);
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

function storeScheduleId(index, scheduleSelectBox){
	schedules[index]=scheduleSelectBox.value;
}

function selectUnscheduledForAllDropDowns(){
	$$('.eventSchedules').each(function(selectBox) {
		selectBox[0].selected=true;
	});	
}
