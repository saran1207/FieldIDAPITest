/**
 * methods used when displaying a 100% wide page (e.g. for search results).  
 * these pages have a left hand panel which can be shown/hidden.  
 *
 */

var fieldIdWidePage = (function() { 

	var leftMenuWidth = '325px'; 
	var contentLeft = leftMenuWidth;
	var contentWidth = '';
	
	var init = function(leftMenuToShow) {
		$('.left-menu').show();		
		$('#page .centre').css('width',contentWidth).css('marginLeft',contentLeft);			
	};
	
	var showLeftMenu = function() {
		var leftMenu = $('.left-menu');
		if (!leftMenu.is(':visible')) {				
			$('#page .centre').animate({marginLeft:contentLeft},200, 'linear', function() { leftMenu.show();} );
		}		
	}
	
	var hideLeftMenu = function() {
		// make sure any pesky dialogs are removed first.
		$('.locationSelection').remove(); 
		$('.orgSelector').remove();
		
		$('.left-menu').hide(); 
		$('#page .centre').animate({marginLeft:'0%'},200);
	}
			
	return { 
		init: init,
		showLeftMenu : showLeftMenu,
		hideLeftMenu: hideLeftMenu	
	};
	
})();



		
