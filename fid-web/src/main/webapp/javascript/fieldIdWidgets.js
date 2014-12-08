var fieldIdWidgets = (function() {

	function menuButton(el,options) {
		var mb = el instanceof jQuery ? el : $(el);
		var items = mb.find('.menu-items');
		var defaults = {
			ajaxButton:true
		};
		var options = $.extend(defaults, options);

		function showItems() {
			items.slideDown(100);
		}
		function hideItems() {
			items.fadeOut(150);
		}

		function toggleList() {
			if (items.is(':visible')) {
				hideItems();
			} else {
				showItems();
			}
		}

		var init = function() {
			if (!options.ajaxButton) {
				mb.click(toggleList);
			} else {
				mb.find('.imageButton').click(toggleList);
			}
			mb.mouseleave(hideItems);
			items.find('a').click(hideItems);
		}

		return {
			init : init
		}

	}

	var createMenuButton = function(id,options) {
		var mb = menuButton($('#'+id),options);
		mb.init();
		return mb;
	};

	return {
		createMenuButton : createMenuButton
	};

})();

