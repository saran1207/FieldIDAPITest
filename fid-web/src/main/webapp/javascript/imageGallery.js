
// uses ZURB plugin.   http://www.zurb.com/playground/javascript-annotation-plugin
// GALLERIA for gallery.
// TODO DD : refactor this and put it in fieldIdWidgets.

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

		function setupForAnnotations(img,imageData) {
			img.parents('.galleria-stage').find('.image-container').remove();
			img.wrap('<div class="image-container"/>').attr('id','image_'+imageData.id);
			var $newParent = img.parent();
			$newParent.css('top',img.css('top')).css('left',img.css('left'));
			$newParent.css('width',img.css('width')).css('height',img.css('height'));
			img.css('top',0).css('left',0);
		}

		function galleryImageClicked(e) {
			var img = $(e.imageTarget);
			var imageData = e.galleriaData;
			setupForAnnotations(img, imageData);
			var url = new String(callback) +
				'&action='+'SELECT' +
				'&index=' + galleria.getIndex();
			wicketAjaxGet(url, function() {}, function() {});
		}

		function initUploadButton() {
			// create a prettier submit button and delegate to the existing/underlying one.
			var $originalSubmit = $('.image-gallery .add-image');
			if ($originalSubmit.length==0) {
				throw "can't find ADD button. should have class '.image-gallery .add-image'";
			}
			var $newSubmit = $('<a>').html('Upload New Image').addClass('add').addClass('mattButton');
			$newSubmit.appendTo($('#'+id + ' .galleria-container'));
			$newSubmit.click(function() {
				$originalSubmit.click();
			});
			$originalSubmit.hide();
		}

		var edit = function(annotationOptions) {
			imageEditor.init($(galleria.getActiveImage()),annotationOptions);
		}

		var init = function() {
			var defaults = {
				extend : function(options) {
					// initialize editors here!
					galleria = this;

					galleria.unbind('image');
					Galleria.on('image', function(e) {
						galleryImageClicked(e);
					});
				}
			};

			Galleria.loadTheme('/fieldid/javascript/galleria/themes/classic/galleria.classic.min.js');
			Galleria.run('#'+id,$.extend(defaults, options));

		}

		return {
			init : init,
			edit : edit,
			initForm : function() { setTimeout(initUploadButton,300); }
		}
	}

	var edit = function(id,options) {
		// precondition : already been initialized (which populates GALLERY data).
		$('#'+id).data('gallery').edit(options);
	}

	var init = function(id, options) {
		var g = $('#'+id).data('gallery');
		if ( !g ) {
			g = gallery(id,options);
			g.init();
			$('#'+id).data('gallery',g);
		}
		g.initForm();
		return g;
	}

	return {
		init : init,
		edit : edit
	}

})();
