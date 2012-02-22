/**
 * methods used when displaying a 100% wide page (e.g. for search results).  
 * these pages have a left hand panel which can be shown/hidden.  
 *
 */

var fieldIdWidePage = (function() { 

	var leftMenuWidth = '325px'; 
	var contentLeft = leftMenuWidth;
	var contentWidth = '';
	var leftMenu = $('#left-menu'); 
	
	var showLeftMenu = function() {	
		var leftMenu = $('#left-menu');
		$('.paginationWrapper').css('left',leftMenuWidth);
		if (!leftMenu.is(':visible')) {				
			$('#page .centre').animate({marginLeft:contentLeft},300, 'linear', function() { leftMenu.show();} );
		}
		// need to restore "true" state of toggle button...
	}
	
	var hideLeftMenu = function() {
		// make sure any pesky dialogs are removed first.
		$('.paginationWrapper').css('left','0px');
		
		$('.sub-menu .config').find('a').removeClass('true');  // remove the true state from toggle buttons.
		
		$('#left-menu').hide(); 
		$('#page .centre').animate({marginLeft:'0%'},300);
	}
			
	return { 
		showLeftMenu : showLeftMenu,
		hideLeftMenu: hideLeftMenu	
	};
	
})();



		
