/**
 * methods used when displaying a 100% wide page (e.g. for search results).  
 * these pages have a left hand panel which can be shown/hidden.  
 *
 */

var fieldIdWidePage = (function() {

    var lastShownPanel;

	var init = function(showLeft) {
		addMenuHandlers();
        addControllerHandler();
		initLeftPanel(showLeft);
	};
	
	// show either filters or columns panel.  need to do this to initialize state.
    //  after this, can just hide/show these via top level styling (showLeftPanel,hideLeftPanel)
	var showConfig = function(showFilters) {
		if (showFilters) {
            $('#left-panel').removeClass('columns').addClass('filters');
		} else {
            $('#left-panel').removeClass('filters').addClass('columns');
		}
		showLeftPanel();
	};

	function showLeftPanel() {
        $('#page').removeClass('wide').addClass('narrow');
        // restore state of toggle button.
        if (lastShownPanel) {
            lastShownPanel.addClass('mattButtonPressed');
        }
    };

    function hideLeftPanel() {
        $('#page').removeClass('narrow').addClass('wide');
        // remove the true state from toggle buttons.
        lastShownPanel = $('.sub-menu .config .mattButtonPressed');
        lastShownPanel.removeClass('mattButtonPressed');
	};

    function initLeftPanel(showLeft) {
        if (showLeft) {
            showConfig(true);
        } else {
            hideLeftPanel();
        }
    };


    function addMenuHandlers() {
		$(document).delegate('.actions .menu > a', 'click', function() {
			$(this).next('.menu-items').first().show();
			return false;
		});		
		$(document).delegate('"#fieldidBody', 'click', function(e) {
			$('.actions .menu-items').hide();
		});
	}

    /* controller is the vertical bar at left of screen that shows/hides left panel */
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





