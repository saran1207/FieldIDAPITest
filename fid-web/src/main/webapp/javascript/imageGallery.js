
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


		function galleryImageClicked(e) {
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

	var init = function(id, options) {
		var g = gallery(id,options);
		$('#'+id).data('gallery',g);
		g.init();
		return g;
	}

	var add = function(id,imageData) {
		// TODO : use variable instead of passing around id.
		// e.g.   var editor=init(id,options);  editor.add(imageData);

		$('#'+id).data('gallery').add(imageData);
	}

	var edit = function(id,options) {
		$('#'+id).data('gallery').edit(options);
	}

	return {
		init : init,
		add : add,
		edit : edit
	}

})();
