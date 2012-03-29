/**
 * methods used when displaying a 100% wide page (e.g. for search results).  
 * these pages have a left hand panel which can be shown/hidden.  
 *
 */

// XXX : need to break this code out (similar to wide.css).   some of this is specific to menus, other is left-panel specific.
//  they should be in appropriate separate files.
var fieldIdWidePage = (function() { 

	var init = function(showLeft) {
		addMenuHandlers();
        addControllerHandler();
		initLeftMenu(showLeft);
	};
	
	// show either filters or columns panel.
	var showConfig = function(showFilters) {
		if (showFilters) { 
			$('.sub-menu .config .filters').addClass('true');
			$('.sub-menu .config .columns').removeClass('true');
			$('#left-panel .columns').hide();
			$('#left-panel .filters').show();
		} else {
			$('.sub-menu .config .columns').addClass('true');
			$('.sub-menu .config .filters').removeClass('true');
			$('#left-panel .filters').hide();
			$('#left-panel .columns').show();
		}
		showLeftPanel();
	};

	var showLeftPanel = function() {
		$('#left-panel').addClass('show').removeClass('hide');
        $('#page .centre').addClass('narrow');
        $('#left-panel-controller .close').show();
        $('#left-panel-controller .open').hide();
    };
	
	var hideLeftPanel = function() {
		$('.sub-menu .config').find('a').removeClass('true');  // remove the true state from toggle buttons.		
		$('#page .centre').removeClass('narrow');
		$('#left-panel').removeClass('show').addClass('hide');
        $('#left-panel-controller .open').show();
        $('#left-panel-controller .close').hide();
	};
			
	var initLeftMenu = function(showLeft) {
		if (showLeft) {
			// set filter toggle button to true (it's the default config panel to be shown).
			showConfig(true);
		} else {
			hideLeftPanel();
		}
	};
				
	function addMenuHandlers() { 
		$(document).delegate('.actions .menu > a', 'click', function() {
			$(this).siblings('.menu-items').first().show();
			return false;
		});		
		// in an ideal world we would have a single top level div surrounding  all content instead of listing all (page,pageHeader...) in selector. 
		$(document).delegate('"#fieldidBody', 'click', function(e) {
			$('.actions .menu-items').hide();
		});
	}

    /* bar at left of screen that shows/hides left panel */
    function addControllerHandler() {
        $('#left-panel-controller').click(function() { toggleLeftMenu() });
    }

    function toggleLeftMenu() {
        if ($('#left-panel').is(':visible')) {
            hideLeftPanel();
        } else {
            showLeftPanel();
        }
    }

		
	return { 
		init : init,
    	showConfig : showConfig
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





