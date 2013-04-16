// uses ZURB plugin.   http://www.zurb.com/playground/javascript-annotation-plugin


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
			direction: 'arrow-left',
			text: 'a label',
			type: 'water',
			yPosition:'middle',
			xPosition:'left',
			editedId:null,
			centerImage:false
		};
		var options = $.extend(defaults, options);
		var callback = options.callback;  // MANDATORY!

		function createNote(annotation) {
			var value = annotation && annotation.text ? annotation.text : options.text;
			var type = annotation && annotation.type ? annotation.type : options.type;
			var direction = annotation && annotation.x ? (annotation.x <.5) ? 'arrow-right' : 'arrow-left' : options.direction;
			var id = annotation && annotation.id ? '_note'+annotation.id : options.editedId;

			var span = $(document.createElement('span')).addClass('readonly').addClass('note').addClass(direction).addClass(type).attr('id',id);
			var icon = $('<img/>').addClass('icon').appendTo(span);
			var editor = $('<input/>').attr({type:'text', value:value}).appendTo(span).width('60px');

			editor.css('width',(editor.val().length + 1) * 6 + 'px');

			if (options.editable) {

				editor.focus(function(e) {
					this.select();
					$(this).parent().removeClass('readonly');
				});

				editor.keypress(function(e) {
					this.style.width = Math.max(12,(this.value.length + 1) * 6) + 'px';
				});

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
			// remove any other "unsaved" notes. only one allowed each time.
			// this is a very specific LOTO requirement.
			removeOtherUnsavedNotes();

			var note = createNote().addClass('unsaved');

			setTimeout(function() {
				note.find('input').focus();
			},100);

			return note;
		}

		function updateNote(note) {
			// assumes note html comes right after img.
			//  <img.../>
			//  <span class="note water readonly"/>
			//
			var loc = note.seralizeAnnotations()[0];   // X,Y will be in relative:  0...1
			var type = note.attr('class');
			var text = note.find('input').val();
			var noteId = note.attr('id');
			var imageId = note.siblings('img').attr('id');
			var url = new String(callback) +
				'&noteId='+(noteId?noteId:'') +
				'&imageId='+(imageId?imageId:'') +
				'&x='+loc.x +
				'&y='+loc.y +
				'&text='+text +
				'&type='+type;
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
