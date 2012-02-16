/**
 * methods used when displaying a 100% wide page (e.g. for search results).  
 * these pages have a left hand panel which can be shown/hidden.  
 *
 */

var fieldIdWidePage = (function() { 

	var leftMenuWidth = '26%'; 
	var contentLeft = leftMenuWidth;
	var contentWidth = '74%';
	
	var init = function(leftMenuToShow) {
		$('.left-menu').show();		
		$('#page .centre').css('width',contentWidth).css('marginLeft',contentLeft);			
	};
	
	var showLeftMenu = function() {
		var leftMenu = $('.left-menu');
		if (!leftMenu.is(':visible')) {				
			$('#page .centre').animate({width:contentWidth,marginLeft:contentLeft},200, 'linear', function() { leftMenu.show();} );
		}		
	}
	
	var hideLeftMenu = function() {
		// make sure any pesky dialogs are removed first.
		$('.locationSelection').remove(); 
		$('.orgSelector').remove();
		
		$('.left-menu').hide(); 
		$('#page .centre').animate({width:'100%',marginLeft:'0%'},200);
	}
			
	return { 
		init: init,
		showLeftMenu : showLeftMenu,
		hideLeftMenu: hideLeftMenu	
	};
	
})();



		
