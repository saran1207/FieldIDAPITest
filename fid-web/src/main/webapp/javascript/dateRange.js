

var dateRangePicker = (function() { 
		
	
	var init = function(id) {		
		update(id);
		$('#'+id).change(function() { 
			update(id); 
		});	
	};
	
	function update(id) {
		$('.dateToFrom').toggle($('#'+id).val()=='CUSTOM'); 
	}						
	
	return {		
		init : init
	}	
	
})();
