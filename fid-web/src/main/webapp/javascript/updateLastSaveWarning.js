var warningPanelIds = [];
var minuteCountIds = [];

var minutesSinceLastSave = 0;

var intervalId = null;

function updateLastSaveWarning() {
    minutesSinceLastSave++;
    for (var i = 0; i < warningPanelIds.length; i++) {
        document.getElementById(warningPanelIds[i]).style.display = "block";
    }
    for (i = 0; i < minuteCountIds.length; i++) {
        document.getElementById(minuteCountIds[i]).innerHTML = minutesSinceLastSave;
    }
}

function startWarningTimer() {
    if (intervalId != null) {
        clearInterval(intervalId);
    }
    intervalId = setInterval("updateLastSaveWarning()", 60*1000);
}

function addEvent(obj, evType, fn){
 if (obj.addEventListener){
   obj.addEventListener(evType, fn, false);
   return true;
 } else if (obj.attachEvent){
   var r = obj.attachEvent("on"+evType, fn);
   return r;
 } else {
   return false;
 }
}

addEvent(window, 'load', startWarningTimer)