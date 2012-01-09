


var dateRangePicker = (function() { 
	var id;
	
	function init(id) {
		this.id = id;
		update(id);
		$('#'+id).change(function() { 
			update(id); 
		});	
	}    
	
	function update(id) {
		$('.dateToFrom').toggle($('#'+id).val()=='CUSTOM'); 
	}						
	
	return {		
		init : init,
	};

	
})();
