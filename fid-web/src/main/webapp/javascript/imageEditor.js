
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
//

var imageEditor = (function() {

	function editor(selector,options) {
		var id = selector;
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
			return $(selector +' .direction-menu');
		}

		function createLabel() {
			var span = $(document.createElement('span')).addClass('readonly').addClass(options.direction).addClass(options.type).attr('id',options.id);
			var icon = $('<span/>').addClass('icon').appendTo(span);
			var editor = $('<input/>').attr({type:'text', value:options.text}).appendTo(span).width('60px');
			var direction = $('<span/>').attr({class:'menu'}).appendTo(span);

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
				doLabel($(e.target));
			});

			return span;
		}

		function getDirection(note) {
			for (var i=0;i<directions.length;i++) {
				if (note.hasClass(directions[i])) {
					return directions[i];
				}
			}
		}

		function addNewLabel() {
			// hacky way of turning off annotation plugin...ugh.
			// once you create a note, we automatically leave Label mode.
			$(selector).unbind('mousedown');

			var label = createLabel();

			setTimeout(function() {
				$('.note input').focus();
			},100);

			return label;
		}

		function centerImage() {
			var img = $(selector + ' img');
			var container = $(selector +' .container');  // container will have specific width/height set.
			var width = img.width();
			var height = img.height();
			var aspect = width/height;
			var containerAspect = container.width()/container.height();
			if (containerAspect<aspect) {
				img.width('100%');
			} else {
				img.height('100%');
			}
		}

		function save() {
			var url = new String(callback)+
				'&action='+'SAVE';
			wicketAjaxGet(url, function() {}, function() {});
		}

		function updateLabel(note) {
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
				'&image='+(imageId?imageId:'') +
				'&style='+style +
				'&dir='+direction +
				'&text='+text;
			wicketAjaxGet(url, function() {}, function() {});
		}

		function doLabel(input) {
			var note = $(input).parent();
			updateLabel(note);
		}

		function changeDirection(note,li) {
			if (!$(li).is('li')) {
				li = $(li).parents('li');
			}

			var direction = $(li).attr('class');
			note.removeClass(getDirection(note)).addClass(direction);

			updateLabel(note);

			closeDirectionMenu();
		}

		function closeDirectionMenu() {
			getDirectionMenu().data('note', '').hide();
		}

		function initPopupMenu() {

			// just create it myself here from options values.
			createPopupMenu().appendTo($(selector));

			// hide context source menu when you click any where else on page.
			$(document).click(function(e) {
				var target = e.target;
				if (e.isPropagationStopped()) return;
				if (!$(target).is('.direction-menu') && !$(target).parents().is('.source-menu')) {
					closeDirectionMenu();
				}
			});

			$(selector + ' .direction-menu li').click(function(e) {
				var note = getDirectionMenu().data('note');
				changeDirection(note, e.target);
			});
		}

		var init = function() {
			initPopupMenu();
			if (options.centerImage) {
				centerImage();
			}

//			$(selector).addAnnotations(function(annotation) {
//					return createLabel(annotation);
//				},options.annotations,options);
			$(selector).annotatableImage(function(annotation) {
				return addNewLabel();
			}, options);

		}


		return {
			init : init
		};

	}


	// ----------------------------------------------------------------------------------------------------------


	var init = function(selector,options) {
		if ($(selector).data('editor')) return;
		var e = editor(selector,options);
		e.init();
		$(selector).data('editor',e);
		return e;
	};

	return {
		init : init
	}

})();
