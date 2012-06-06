/**
 * methods used when displaying a 100% wide page (e.g. for search results).  
 * these pages have a left hand panel which can be shown/hidden.  
 *
 */

var fieldIdWidePage = (function() {

    // use this to store state of which of columns/filters panel was shown.
    var lastShownPanel;

	var init = function(showLeft) {
        // show either filters or columns panel.  need to do this to initialize state.
        //  after this, can just hide/show these via top level styling (showLeftPanel,hideLeftPanel)
        if (showLeft) {
            showConfig(true);
        } else {
            hideLeftPanel();
		}
		// hack : move bottom bar down in DOM hierarchy so it isn't within a scrollable div (IE occasionally doesn't render it in this state...arghhh).
		$('#page').append($('.bottom-bar'));
	};

	var showConfig = function(showFilters) {
		if (showFilters) {
            //suggestion : would $(..).attr('css','filters') be more efficient?
            $('#left-panel').removeClass('columns').addClass('filters');
		} else {
            $('#left-panel').removeClass('filters').addClass('columns');
		}
		showLeftPanel();
	};

	function showLeftPanel() {
        $('#page').removeClass('without-left-panel').addClass('with-left-panel');
        // restore state of toggle button.
        if (lastShownPanel) {
            lastShownPanel.addClass('mattButtonPressed');
        }
    };

    function hideLeftPanel() {
        $('#page').removeClass('with-left-panel').addClass('without-left-panel');
        // remove the pressed state from toggle buttons.
        lastShownPanel = $('.sub-menu .config .mattButtonPressed');
        lastShownPanel.removeClass('mattButtonPressed');
	};

    var toggleLeftPanel = function() {
        if ($('#left-panel').is(':visible')) {
            hideLeftPanel();
        } else {
            showLeftPanel();
        }
    };



	return { 
		init : init,
        toggleLeftPanel : toggleLeftPanel,
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





