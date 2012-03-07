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
	
	// show either filters or columns panel.
	var showConfig = function(showFilters) {
		if (showFilters) { 
			$('.sub-menu .config .filters').addClass('true');
			$('.sub-menu .config .columns').removeClass('true');
			$('#left-menu .columns').hide();
			$('#left-menu .filters').show();
		} else {
			$('.sub-menu .config .columns').addClass('true');
			$('.sub-menu .config .filters').removeClass('true');
			$('#left-menu .filters').hide();				
			$('#left-menu .columns').show();				
		}
		showLeftMenu();
	};
		
	
	var showLeftMenu = function() {	
		var leftMenu = $('#left-menu');
		$('.paginationWrapper').css('left',leftMenuWidth);
		if (!leftMenu.is(':visible')) {
			$('#page .centre').css('marginLeft',contentLeft);   // move centre stuff over to make room...
			leftMenu.show();									//  ...now show the left component
			
			// i'm finding animation is too slow.  could just use css for this??
//			$('#page .centre').animate({marginLeft:contentLeft},220, 'linear', function() { leftMenu.show();} );
		}
	};
	
	var hideLeftMenu = function() {
		$('.sub-menu .config').find('a').removeClass('true');  // remove the true state from toggle buttons.		
		$('.paginationWrapper').css('left','0px');		
		$('#page .centre').css('marginLeft','0%');
		$('#left-menu').hide(); 		
		// i'm finding animation is too slow.  could just use css for this??
//		$('#page .centre').animate({marginLeft:'0%'},220);  
	};
			
	var initLeftMenu = function(showLeftMenu) {
		if (showLeftMenu) {
			// set filter toggle button to true (it's the default config panel to be shown).
			showConfig(true);
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
		showConfig : showConfig,
		showLeftMenu : showLeftMenu,
		hideLeftMenu: hideLeftMenu	
	};
	
})();


/** 
 * CAVEAT : 
 *  deliberately overriding the existing function that positions loading panel because it is behaves differently in wide screen mode. 
 *  remove this when merging of asset search pages is complete.  at that point we will be more free to refactor loading panel to have custom
 *   javascript emitted.
 * note : you may want to adjust the "top" value of the modalPanel if you don't want the translucent overlay to cover the entire page (skip the header).  
 **/

function positionModalContainer(modalPanelId, componentToCoverId) {
	var modelPanel = $("#"+modalPanelId);
	var componentToCover = $("#page");
	
	translatePosition(modelPanel, componentToCover, 0, 0);
	modelPanel.css({
		'width': "100%",
		'left' :'0px',
		'height': componentToCover.height() + "px"});
}





