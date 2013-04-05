
// uses ZURB plugin.   http://www.zurb.com/playground/javascript-annotation-plugin


// TODO :
//on click = remove any "new" notes.  (when you create one, it gets a 'unsaved' css class that is outer glow L&F).
// store current name as default (do this after blur too).
// add new note with default name & style.
// attach listener to div if it isn't already there.

// on new image loaded... remove all notes from div.
// re-annotate div.
// pass image index (Galleria.getIndex()) via ajax call.   maybe pass the div index (0 or 1) as well?
// or dynamically assign id to the img....e.g. <img id="123"..../>    onclick(function() { img.id = loadedEntityId; });


var imageEditor = (function() {

	function editor(el,options) {
		var ed = el;
		var defaults = {
			direction: 'west',
			text: 'a label',
			type: 'note',
			yPosition:'middle',
			xPosition:'left',
			centerImage:false,
			id:null
		};
		var options = $.extend(defaults, options);
		var callback = options.callback;  // MANDATORY!

		function createNote(annotation) {
			var value = annotation && annotation.text ? annotation.text : options.text;
			var span = $(document.createElement('span')).addClass('readonly').addClass('note').addClass(options.direction).addClass(options.type).attr('id',options.id);
			var icon = $('<span/>').addClass('icon').appendTo(span);
			var editor = $('<input/>').attr({type:'text', value:value}).appendTo(span).width('60px');

			editor.css('width',(editor.val().length + 1) * 6 + 'px');

			if (options.editable) {

				editor.focus(function(e) {
					this.select();
					$(this).parent().removeClass('readonly');
				});

				editor.keypress(function(e) {
	//					TODO  : need to accommodate for EAST labels.  have to move them back to left?
	//					if (style==east) { this.style.margin-left = -52-(Math.max(12,(this.value.length + 1) * 6)) + 'px' ;} else...
						this.style.width = Math.max(12,(this.value.length + 1) * 6) + 'px'; }
				);

				editor.blur(function(e) {
					$(this).parent().addClass('readonly');
					doNote($(e.target));
				});

				editor.keypress(function(event) {
					var keycode = (event.keyCode ? event.keyCode : event.which);
					if (keycode=='13') {
						editor.blur();
					}
				});
			} else {
				editor.attr('disabled',true);
			}

			return span;
		}

		function removeOtherUnsavedNotes() {
			ed.parent().find('.note.unsaved').remove();
		}

		function addNewNote() {
			// remove any other "unsaved" notes. only allowed one each time.
			// this is a very specific LOTO requirement.
			removeOtherUnsavedNotes();

			var note = createNote().addClass('unsaved');

			setTimeout(function() {
				note.find('input').focus();
			},100);

			return note;
		}

		function save() {
			var url = new String(callback)+
				'&action='+'SAVE';
			wicketAjaxGet(url, function() {}, function() {});
		}

		function updateNote(note) {
			var loc = note.seralizeAnnotations()[0];   // X,Y will be in relative:  0...1
			var style = note.attr('class');
			var text = note.find('input').val();
			var id = note.attr('id');
			var imageId = note.parents('image-editor').attr('id');
			var url = new String(callback) +
				'&action='+'LABEL' +
				'&id='+(id?id:'') +
				'&x='+loc.x +
				'&y='+loc.y +
				'&imageId='+(imageId?imageId:'') +
				'&style='+style +
				'&text='+text;
			wicketAjaxGet(url, function() {}, function() {});
		}

		function doNote(input) {
			var note = $(input).parent();
			options.text = input.val();
			updateNote(note);
		}

		function initAnnotations(reset) {
			if (reset) {
				// erase all current unsaved labels.
				ed.find('.note').remove;
			}
			ed.addAnnotations(function(annotation) {
					return createNote(annotation);
				},options.annotations,options);
		}


		var init = function() {
			if (options.centerImage) {
				centerImage();
			}
		//	if (options.editable) { }
			ed.annotatableImage(function(annotation) {
				return addNewNote();
			}, options);
		}



		return {
			init : init,
			initAnnotations : initAnnotations
		};

	}


	// ----------------------------------------------------------------------------------------------------------

	var init = function(selector,options) {
		var el = selector instanceof jQuery ? selector : $(selector);
		if (el.is('img')) {
			el = el.parent();
		}
		var ed = el.data('editor');
		if (!ed) {
			ed = editor(el,options);
			ed.init();
			$(selector).data('editor',ed);
		}
		ed.initAnnotations(options.reset);
		return ed;
	}

	var reset = function(el,options) {
		options.reset = true;
		init(el,options);
	}

	return {
		init : init,
		reset : reset
	}

})();
