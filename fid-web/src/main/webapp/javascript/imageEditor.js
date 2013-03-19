
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
		var directions = ['north','south','east','west'];
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

		function createPopupMenu() {
//			<ul class="direction-menu" style="display:none">
//				<li class="north"><span/><p>North</p></li>
//				<li class="south"><span/><p >South</p></li>
//				<li class="west"><span/><p>West</p></li>
//			</ul>
			var ul = $('<ul/>').addClass('direction-menu').hide();
			var li = $('<li/>').addClass('north').appendTo(ul);
			$('<span/>').appendTo(li);
			$('<p/>').html('North').appendTo(li);

			li = $('<li/>').addClass('south').appendTo(ul);
			$('<span/>').appendTo(li);
			$('<p/>').html('South').appendTo(li);

			li = $('<li/>').addClass('west').appendTo(ul);
			$('<span/>').appendTo(li);
			$('<p/>').html('West').appendTo(li);

			return ul;
		}

		function getDirectionMenu() {
			return ed.find('.direction-menu');
		}

		function createNote(annotation) {
			var value = annotation?annotation.text:options.text;
			var span = $(document.createElement('span')).addClass('readonly').addClass('note').addClass(options.direction).addClass(options.type).attr('id',options.id);
			var icon = $('<span/>').addClass('icon').appendTo(span);
			var editor = $('<input/>').attr({type:'text', value:value}).appendTo(span).width('60px');
			var direction = $('<span/>').attr({class:'menu'}).appendTo(span).hide();

			editor.css('width',(editor.val().length + 1) * 6 + 'px');

			if (options.editable) {
				direction.show();
				direction.click(function(e) {
					var note = $(e.target).parents('.note');
					// TODO : get background color from note.  yellow for now.
					// subtract to accommodate for border we're going to add.
					var width = parseInt(note.outerWidth());

					// TODO : if note is close to bottom, then make it appear above note, not below.
					// .: need to adjust
					getDirectionMenu().css('top',note.css('top')).css('left',note.css('left')).width(width).data('note',note).removeClass(directions.join(' ')).addClass(getDirection(note));
					getDirectionMenu().toggle();
					e.stopPropagation();
				});

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
			} else {
				editor.attr('disabled',true);
			}

			return span;
		}

		function getDirection(note) {
			for (var i=0;i<directions.length;i++) {
				if (note.hasClass(directions[i])) {
					return directions[i];
				}
			}
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
				$('.note input').focus();
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
			var direction = getDirection(note);
			var imageId = note.parents('image-editor').attr('id');
			var url = new String(callback) +
				'&action='+'LABEL' +
				'&id='+(id?id:'') +
				'&x='+loc.x +
				'&y='+loc.y +
				'&imageId='+(imageId?imageId:'') +
				'&style='+style +
				'&dir='+direction +
				'&text='+text;
			wicketAjaxGet(url, function() {}, function() {});
		}

		function doNote(input) {
			var note = $(input).parent();
			options.text = note.text();
			updateNote(note);
		}

		function changeDirection(note,li) {
			if (!$(li).is('li')) {
				li = $(li).parents('li');
			}

			var direction = $(li).attr('class');
			note.removeClass(getDirection(note)).addClass(direction);

			updateNote(note);

			closeDirectionMenu();
		}

		function closeDirectionMenu() {
			getDirectionMenu().data('note', '').hide();
		}

		function initPopupMenu() {
			// just create it myself here from options values.
			createPopupMenu().appendTo(ed);

			// hide context source menu when you click any where else on page.
			$(document).click(function(e) {
				var target = e.target;
				if (e.isPropagationStopped()) return;
				if (!$(target).is('.direction-menu') && !$(target).parents().is('.source-menu')) {
					closeDirectionMenu();
				}
			});

			ed.find('.direction-menu li').click(function(e) {
				var note = getDirectionMenu().data('note');
				changeDirection(note, e.target);
			});
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
				initPopupMenu();
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
		var el = $(selector);
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
