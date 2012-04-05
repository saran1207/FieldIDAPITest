/**
 * USAGE NOTE :
 * requires fieldIdWide.js
 */

var leftPanelController = (function() {

	var init = function(showLeft) {
        addControllerHandler();
	};


    /* controller is the vertical bar at left of screen that shows/hides left panel */
    function addControllerHandler() {
        $('#left-panel-controller').click(function() { fieldIdWidePage.toggleLeftPanel() });
    }

	return {
		init : init
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





