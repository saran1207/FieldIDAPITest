
var fieldIdWidgets = (function() {

	function menuButton(el) {
		var mb = el instanceof jQuery ? el : $(el);
		var items = mb.find('.menu-items');

		function showItems() {
			items.slideDown(100);
		}
		function hideItems() {
			items.fadeOut(200);
		}

		function toggleList() {
			if (items.is(':visible')) {
				hideItems();
			} else {
				showItems();
			}
		}

		var init = function() {
			mb.click(toggleList);
			mb.mouseleave(hideItems);
		}

		return {
			init : init
		}

	}


	var createMenuButton = function(id) {
		var mb = menuButton($('#'+id));
		mb.init();
		return mb;
	};


	return {
		createMenuButton : createMenuButton
	};

})();

