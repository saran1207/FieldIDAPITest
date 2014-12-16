var imageList = (function() {

	function imageList(el) {
		var il = el instanceof jQuery ? el : $(el);

		var init = function() {
			var numberOfImages = il.find('.notes-container').length;
			var imageWidthContainer = il.find('ul li').outerWidth(true);
			var carouselWidth = numberOfImages * imageWidthContainer;
			il.find('ul').css('width', carouselWidth);

			//Set height of images to fit in the image list without cropping
			il.find('.notes-container svg').each(function(index, value){
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

			if (imageWidth==0) {
				// arggh : this is messy but i need to have the image loaded before i do this code.
				// if it's not [width()==0] then i'll just hide it, wait and try again.
				$this.parent().css('visibility','hidden');
				setTimeout(function() { updateImage($this,index); } ,300);
				return;
			}
			$this.parent().css('visibility','visible');

			if (imageHeight/imageWidth >= imageContainerHeight/imageContainerWidth) {
				$this.attr('height',imageContainerHeight);
                $this.attr('width', imageContainerWidth);
			} else {
				var newHeight = imageHeight * (imageContainerWidth/imageWidth);
				$this.parent().css('margin-top',(imageContainerHeight-newHeight)/2);
				$this.attr('width',imageContainerWidth);
                $this.attr('height',newHeight);
			}

		}

		return {
			init : init
		}
	};

	var createImageList = function(id) {
		var il = imageList($('#'+id));
		il.init();
		return il;
	}

	return {
		createImageList : createImageList
	};

})();

