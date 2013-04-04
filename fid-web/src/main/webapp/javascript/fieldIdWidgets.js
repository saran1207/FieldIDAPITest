
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
			var numberOfImages = $('.image-list ul li').length;
			var imageWidthContainer = $('.image-list ul li').outerWidth(true);
			var carouselWidth = numberOfImages * imageWidthContainer;
			$('.image-list ul').css('width', carouselWidth);

			//Set height of images to fit in the image list without cropping
			$(".image-list img").load(function(){
				imageHeight = $(this).height();
				imageContainerHeight = $('.image-list ul li').height();

				if (imageHeight >= imageContainerHeight) {
					$(this).css("height", "100%");
				}
			});

			// TODO DD : hide nav if < 4 images? need some max to compare against.
			//navigation buttons
			$('.prev').click(function(){
				$(".image-list ul").animate({
					marginLeft: imageWidthContainer
				}, 500,
				function(){
					//Place first image after the last to create a continuous loop
					$(this).find("li:first").before($(this).find("li:last"));
					$(this).css({marginLeft: 0});
				})
			});
			$('.next').click(function(){
				$(".image-list ul").animate({
					marginLeft: -imageWidthContainer
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

