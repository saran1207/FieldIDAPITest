
// uses ZURB plugin.   http://www.zurb.com/playground/javascript-annotation-plugin
// GALLERIA for gallery.

var imageGallery = (function() {

	function gallery(galleryId,options) {
		var id = galleryId;
		var callback = options.callback;
		var galleria;
		var defaults = {
			transition:'slide',
			transitionSpeed:220,
			thumbCrop : true
		};
		var options = $.extend(defaults, options);

		function setupForAnnotations(img) {
			img.parent().children('.image-container').remove();
			img.wrap('<div class="image-container"/>');
			var $newParent = img.parent();
			$newParent.css('top',img.css('top')).css('left',img.css('left'));
			$newParent.css('width',img.css('width')).css('height',img.css('height'));
			img.css('top',0).css('left',0);
		}

		function galleryImageClicked(e) {
			var img = $(e.imageTarget);
			setupForAnnotations(img);
			var imageData = e.galleriaData;
			var url = new String(callback) +
				'&action='+'SELECT' +
				'&index=' + galleria.getIndex() +
				'&id='+(imageData.id?imageData.id:'');
			wicketAjaxGet(url, function() {}, function() {});
		}

		function initUploadButton() {
			// create a prettier submit button and delegate to the existing/underlying one.
			var gallery = $('#'+id);
			var form = gallery.prev('.uploadForm');
			var oldSubmit = form.children('.submit');
			var newSubmit = $('<a>').html('Add').addClass('submit');
			newSubmit.appendTo($('#'+id + ' .galleria-container'));
			newSubmit.click(function() {
				oldSubmit.click();
			});
		}

		var add = function(imageData) {
			galleria.push(imageData);
		}

		var edit = function(annotationOptions) {
			imageEditor.init($(galleria.getActiveImage()),annotationOptions);
		}

		var init = function() {
			var defaults = {
				extend : function(options) {
					// initialize editors here!
					galleria = this;
				}
			};

			Galleria.loadTheme('../../javascript/galleria/themes/classic/galleria.classic.min.js');
			Galleria.run('#'+id,$.extend(defaults, options));

			Galleria.on('image', function(e) {
				galleryImageClicked(e);
			});

			setTimeout(initUploadButton,300);
		}

		return {
			init : init,
			add : add,
			edit : edit
		}
	}

	var add = function(id,imageData) {
		// precondition : already been initialized (which populates GALLERY data).
		$('#'+id).data('gallery').add(imageData);
	}

	var edit = function(id,options) {
		// precondition : already been initialized (which populates GALLERY data).
		$('#'+id).data('gallery').edit(options);
	}

	var init = function(id, options) {
		var g = gallery(id,options);
		$('#'+id).data('gallery',g);
		g.init();
		return g;
	}

	return {
		init : init,
		add : add,
		edit : edit
	}

})();
