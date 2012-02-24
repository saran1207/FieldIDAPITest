/**
 * methods used when displaying a 100% wide page (e.g. for search results).  
 * these pages have a left hand panel which can be shown/hidden.  
 *
 */

var fieldIdWidePage = (function() { 

	var leftMenuWidth = '345px'; 
	var contentLeft = leftMenuWidth;	
	
	var init = function(showLeftMenu) {
		addMenuHandlers();
		initLeftMenu(showLeftMenu);
	};
	
	var showLeftMenu = function() {	
		var leftMenu = $('#left-menu');
		$('.paginationWrapper').css('left',leftMenuWidth);
		if (!leftMenu.is(':visible')) {				
			$('#page .centre').animate({marginLeft:contentLeft},220, 'linear', function() { leftMenu.show();} );
		}
	};

	
	var hideLeftMenu = function() {
		hideLeftImpl();
		$('#page .centre').animate({marginLeft:'0%'},220);
	};
			
	var initLeftMenu = function(showLeftMenu) {
		if (showLeftMenu) {
			// set filter toggle button to true (it's the default config panel to be shown).
			$('.sub-menu .config .filters').addClass('true');	
		} else {
			hideLeftImpl();
			$('#page .centre').css('marginLeft','0%');
		}
	};
				
	function hideLeftImpl() { 
		$('.sub-menu .config').find('a').removeClass('true');  // remove the true state from toggle buttons.		
		$('.paginationWrapper').css('left','0px');		
		$('#left-menu').hide(); 		
	}
		
	function addMenuHandlers() { 
		$(document).delegate('.asset-actions  .menu >  a', 'click', function() {
			$('.asset-actions .menu-items').show();	
			return false;
		});		
		// in an ideal world we would have a single top level div surrounding  all content instead of listing all (page,pageHeader...) in selector. 
		$(document).delegate('"#page, #pageHeader', 'click', function(e) {
			$('.asset-actions .menu-items').hide();					
		});		
	}
	
	return { 
		init : init,
		showLeftMenu : showLeftMenu,
		hideLeftMenu: hideLeftMenu	
	};
	
})();



		
