
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
			var $originalSubmit = $('.image-gallery .add-image');
			var $indicator= $('<img class="indicator" src="/fieldid/images/loader.gif"/>');
			if ($.browser.msie) {
				// for IE use the ugly default. gives Access Denied if i try to delegate from prettier button.
				$originalSubmit.change(function() {
					$originalSubmit.attr('disabled',true);
					$indicator.addClass('ie').insertAfter($originalSubmit);
				});
				return;
			} else {
				// create a prettier submit button and delegate to the existing/underlying one.
				if ($originalSubmit.length==0) {
					throw "can't find ADD button. should have class '.image-gallery .add-image'";
				}
				$originalSubmit.hide();
				var $newSubmit = $('<a>').html('Upload New Image').addClass('add-image').addClass('mattButton');
				$originalSubmit.change(function() {
					$indicator.insertAfter($newSubmit.parent());
					$originalSubmit.attr('disabled',true);
				});
				$newSubmit.insertAfter($originalSubmit);
				$newSubmit.click(function() {
					$originalSubmit.click();
				});
			}
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
