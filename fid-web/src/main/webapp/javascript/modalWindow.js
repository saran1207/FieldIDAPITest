
var modalWindow = (function() { 

	var relativeTo = function(id) {
		var pos = $('#'+id).offset();
		var w = Wicket.Window.get().window;
		var top = pos.top-(window.offsetHeight /2);
		w.style.top=top + 'px';
		w.style.left=pos.left + 'px';
	};	
			
	return {
		relativeTo : relativeTo,
	};
	
})();



		
