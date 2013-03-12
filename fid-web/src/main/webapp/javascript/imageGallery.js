
// uses ZURB plugin.   http://www.zurb.com/playground/javascript-annotation-plugin
// GALLERIA for gallery.

var imageGallery = (function() {

	function gallery(galleryId,options) {
		var id = galleryId;
		var callback = options.callback;
		var defaults = {
//			annotationOptions: {centerImage:false,
		};
		var options = $.extend(defaults, options);


		function galleryImageClicked(e) {
			var imageData = e.galleriaData;
			var url = new String(callback) +
				'&action='+'SELECT' +
				'&id='+(imageData.id?imageData.id:'');
			wicketAjaxGet(url, function() {}, function() {});
		}

		var init = function() {
			Galleria.loadTheme('../../javascript/galleria/themes/classic/galleria.classic.min.js');
			Galleria.run('#'+id, options);

			Galleria.on('image', function(e) {
				galleryImageClicked(e);
			});
		}

//		Event properties:
//			galleriaData (Object) The currently active data object
//		cached (boolean) is true if the image is cached (no loading required).
//		imageTarget (HTML element) The IMG element of the now loaded image before transition.
//			thumbTarget (HTML element) The IMG element of the thumbnail that belongs to the imageTarget.
//			index (Number) The galleria index of the image loaded.
//

		return {
			init : init
		}
	}

	var init = function(id, options) {
		var g = gallery(id,options);
		g.init();
		return g;
	}


	return {
		init : init
	}

})();
