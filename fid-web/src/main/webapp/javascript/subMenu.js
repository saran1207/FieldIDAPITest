/**
 * used with submenu.
 * handles the clicking on menu-items.  (i.e. when clicked, show menu...when you click somewhere else
 *  hide any currently showing menus)
 */

var subMenu = (function() {

	var init = function(showLeft) {
		addMenuHandlers();
	};

    function addMenuHandlers() {
		$(document).delegate('.actions .menu > a', 'click', function() {
			$(this).next('.menu-items').first().show();
			return false;
		});
		$(document).delegate('"#fieldidBody', 'click', function(e) {
			$('.actions .menu-items').hide();
		});
	};

	return { 
		init : init
	};
	
})();

