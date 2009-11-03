
var oldSetValueOnSelect = setValueOnSelect;

setValueOnSelect = function (selectElement, valueToSelect) {
	new PeriodicalExecuter(function(pe) {
			pe.stop();	
			oldSetValueOnSelect(selectElement, valueToSelect);
		}, 1);
}
