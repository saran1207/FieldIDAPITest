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
		$('.paginationWrapper').css('margin-left',leftMenuWidth);
		if (!leftMenu.is(':visible')) {
			$('#page .centre').css('marginLeft',contentLeft);
			leftMenu.show();
			// i'm finding animation is too slow.  could just use css for this??
//			$('#page .centre').animate({marginLeft:contentLeft},220, 'linear', function() { leftMenu.show();} );
		}
	};
	
	var hideLeftMenu = function() {
		$('.sub-menu .config').find('a').removeClass('true');  // remove the true state from toggle buttons.		
		$('.paginationWrapper').css('margin-left','0px');		
		$('#page .centre').css('marginLeft','0%');
		$('#left-menu').hide(); 		
		// i'm finding animation is too slow.  could just use css for this??
//		$('#page .centre').animate({marginLeft:'0%'},220);  
	};
			
	var initLeftMenu = function(showLeftMenu) {
		if (showLeftMenu) {
			// set filter toggle button to true (it's the default config panel to be shown).
			$('.sub-menu .config .filters').addClass('true');	
		} else {
			hideLeftMenu();
		}
	};
				
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



		
