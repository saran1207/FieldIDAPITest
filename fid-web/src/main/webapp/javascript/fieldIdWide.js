/**
 * methods used when displaying a 100% wide page (e.g. for search results).  
 * these pages have a left hand panel which can be shown/hidden.  
 *
 */

var fieldIdWidePage = (function() { 

	var leftMenuWidth = '325px'; 
	var contentLeft = leftMenuWidth;	
	
	var init = function() {
		$(document).delegate('.asset-actions  .menu >  a', 'click', function() {
			$('.asset-actions .menu-items').show();	
			return false;
		});		
		// in an ideal world we would have a single top level div surrounding  all content instead of listing all in multiple selector. 
		$(document).delegate('"#page, #pageHeader', 'click', function(e) {
			$('.asset-actions .menu-items').hide();					
		});		
	};
	
	var showLeftMenu = function() {	
		var leftMenu = $('#left-menu');
		$('.paginationWrapper').css('left',leftMenuWidth);
		if (!leftMenu.is(':visible')) {				
			$('#page .centre').animate({marginLeft:contentLeft},220, 'linear', function() { leftMenu.show();} );
		}
		// need to restore "true" state of toggle button...
	};
	
	var hideLeftMenu = function() {
		$('.paginationWrapper').css('left','0px');		
		$('.sub-menu .config').find('a').removeClass('true');  // remove the true state from toggle buttons.		
		$('#left-menu').hide(); 
		$('#page .centre').animate({marginLeft:'0%'},220);
	};
			
	return { 
		init : init,
		showLeftMenu : showLeftMenu,
		hideLeftMenu: hideLeftMenu	
	};
	
})();



		
