
var modalWindow = (function() { 

	var relativeTo = function(id) {
		var pos = $('#'+id).offset();
		var window = Wicket.Window.get().window;
		var top = pos.top-(window.offsetHeight /2);
		window.style.top=top + 'px';
		window.style.left=pos.left + 'px';
	}	
			
	return {
		relativeTo : relativeTo,
	};
	
})();



		
