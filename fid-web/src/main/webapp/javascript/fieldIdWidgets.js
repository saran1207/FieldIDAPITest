var fieldIdWidgets = (function() {

	function imageList(el,options) {
		var il = el instanceof jQuery ? el : $(el);
		var defaults = {
			direction: 'arrow-left',
			text: 'a label',
			type: 'water',
			yPosition:'middle',
			xPosition:'left',
			centerImage:false
		};
		var options = $.extend(defaults, options);

		var init = function() {
			var numberOfImages = il.find('.notes-container').length;
			var imageWidthContainer = il.find('ul li').outerWidth(true);
			var carouselWidth = numberOfImages * imageWidthContainer;
			il.find('ul').css('width', carouselWidth);

			//Set height of images to fit in the image list without cropping
			il.find('.notes-container img').each(function(index, value){
				updateImage($(this),index);
			});

			//navigation buttons
			if (numberOfImages<5) {
				il.find('.navigation').hide();
			} else {
				il.find('.prev').click(function(){
					il.find('ul').animate({
						marginLeft: imageWidthContainer
					}, 500,
					function(){
						//Place first image after the last to create a continuous loop
						$(this).find("li:first").before($(this).find("li:last"));
						$(this).css({marginLeft: 0});
					})
				});
				il.find('.next').click(function(){
					il.find('ul').animate({
						marginLeft: -imageWidthContainer
					}, 500,
					function() {
						//Place last image before the first to create a continuous loop
						$(this).find("li:last").after($(this).find("li:first"));
						$(this).css({marginLeft: 0});
					})
				});
			}
		}


		function updateImage($this,index) {
			var optionIndex = index;
			var imageHeight = $this.height();
			var imageWidth = $this.width();
			var imageContainerHeight = $this.parent().parent().height();
			var imageContainerWidth = $this.parent().parent().width();

			if (imageHeight==0 || imageWidth==0) {
				// arggh : this is messy but i need to have the image loaded before i do this code.
				// if it's not [width()==0] then i'll just hide it, wait and try again.
				$this.parent().css('visibility','hidden');
				setTimeout(function() { updateImage($this,index); } ,300);
				return;
			}
			$this.parent().css('visibility','visible');

			if (imageHeight/imageWidth >= imageContainerHeight/imageContainerWidth) {
				var newWidth = imageWidth * (imageContainerHeight/imageHeight);
				$this.parent().css('height', imageContainerHeight).css('width',newWidth);
				$this.parent().css('margin-left',(imageContainerWidth-newWidth)/2);
				$this.css('height',imageContainerHeight);
			} else {
				var newHeight = imageHeight * (imageContainerWidth/imageWidth);
				$this.parent().css('margin-top',(imageContainerHeight-newHeight)/2);
				$this.parent().css('width', imageContainerWidth).css('height',newHeight);
				$this.css('width',imageContainerWidth);
			}

			if (options.annotationOptions) {
				var $notesContainer = $this.parent();
				$notesContainer.addAnnotations(
					function(annotation) {return createNote(annotation);},
					options.annotationOptions[optionIndex],
					options );
			}
		}

		return {
			init : init
		}
	};


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

	//	------------------------------------------------------------------------------------------------------------

	function createNote(annotation) {
      	var value = annotation?annotation.text:options.text;
		var direction = annotation.x < .5 ? 'arrow-left' : 'arrow-right';
		var type = annotation.cssStyle ? annotation.cssStyle : options.type;
		var span = $(document.createElement('span')).addClass('readonly').addClass('note').addClass(direction).addClass(type);
		var icon = $('<span/>').addClass('icon').appendTo(span);
		var editor = $('<input/>').attr({type:'text', value:value}).appendTo(span);
		editor.css('width',(editor.val().length + 1) * 6 + 'px');
		editor.attr('disabled',true);
		return span;
	}

	var createMenuButton = function(id,options) {
		var mb = menuButton($('#'+id),options);
		mb.init();
		return mb;
	};

	var createImageList = function(id, options) {
		var il = imageList($('#'+id),options);
		il.init();
		return il;
	}

	var createImageEditor = function(id,options) {
		var ie = imageEditor(id,options);
		ie.init();
		return ie;
	}

	var annotate = function(options) {

		$.each(options.images,function(index,value) {

			$('#'+value.id).parent().addAnnotations(
				function(annotation) {return createNote(annotation);},
				value.annotations,
                { xPosition:"left"} );
		});
	}

	return {
		createMenuButton : createMenuButton,
		createImageList : createImageList,
		createImageEditor : createImageEditor,
		annotate : annotate
	};

})();

