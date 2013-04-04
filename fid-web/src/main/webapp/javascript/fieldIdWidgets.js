
var fieldIdWidgets = (function() {

	function imageList(el,options) {
		var il = el instanceof jQuery ? el : $(el);
		var defaults = {
			direction: 'west',
			text: 'a label',
			type: 'note',
			yPosition:'middle',
			xPosition:'left',
			centerImage:false
		};
		var options = $.extend(defaults, options);

		var init = function() {
			//Setting width of carousel dynamically
			var numberOfImages = il.find('ul li').length;
			var imageWidth = il.find('ul li').outerWidth(true);
			var carouselWidth = numberOfImages * imageWidth;
			il.find('ul').css('width', carouselWidth);

			// TODO DD : hide nav if < 4 images? need some max to compare against.
			//navigation buttons
			il.find('.prev').click(function(){
				il.find('ul').animate({
						marginLeft: imageWidth
					}, 500,
					function(){
						//Place first image after the last to create a continuous loop
						$(this).find("li:first").before($(this).find("li:last"));
						$(this).css({marginLeft: 0});
					})
			});
			il.find('.next').click(function(){
				$(".image-list ul").animate({
						marginLeft: -imageWidth
					}, 500,
					function(){
						//Place last image before the first to create a continuous loop
						$(this).find("li:last").after($(this).find("li:first"));
						$(this).css({marginLeft: 0});
					})
			});

			if (options.annotationOptions) {
				il.find('ul li').each(
					function(index) {
						$(this).addAnnotations(
							function(annotation) {
								return createNote(annotation);
							},options.annotationOptions[index],options);
					});
			}
		}


		function createNote(annotation) {
			var value = annotation?annotation.text:options.text;
			var direction = annotation.x<.5 ? 'west' : annotation.y>.5 ? 'north':'south';
			var span = $(document.createElement('span')).addClass('readonly').addClass('note').addClass(direction).addClass(options.type);
			var icon = $('<span/>').addClass('icon').appendTo(span);
			var editor = $('<input/>').attr({type:'text', value:value}).appendTo(span).width('60px');
			editor.css('width',(editor.val().length + 1) * 6 + 'px');
			editor.attr('disabled',true);
			return span;
		}


		return {
			init : init
		}
	};


	function menuButton(el) {
		var mb = el instanceof jQuery ? el : $(el);
		var items = mb.find('.menu-items');

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
			mb.find('.imageButton').click(toggleList);
			mb.mouseleave(hideItems);
			items.find('a').click(hideItems);
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

	var createImageList = function(id, options) {
		var il = imageList($('#'+id),options);
		il.init();
		return il;
	}

	return {
		createMenuButton : createMenuButton,
		createImageList : createImageList
	};

})();

