/**
 * usage notes : expects a json object= item {
 *    label : the text for the item it
 *    tooltip : the "title" html element.
 *    matchStart : index of first matching char.
 *    matchCount : length of match.
 *    desc : description.
 */

var autoCompleter = (function() {

    var descriptionWidth = 120;
    var fudgeFactor = 10;

	/**
	 *  private data & functions
	 */

	function renderItem(ul,item) {
        return renderImpl(ul, item, descFor(item) + linkFor(item));
    }

    function renderNoResults(ul,item) {
        ul.width(100);
        return renderImpl(ul, item,  descFor(item) );
    }

    function renderMaxResults(ul, item) {
        return renderImpl(ul, item, descFor(item), 'ui-menu-item max-results-container');
    }
    
    function renderImpl(ul, item, anchor, cssClass) {
        var li = '<li></li>';
        if (cssClass) {
            li = '<li class="' + cssClass + '"></li>';
        }
        return $(li)
            .data('item.autocomplete', item)
            .append( anchor )
            .appendTo( ul );
    }

	function ajaxTextField(options) {
		if (!options.parent || !options.child) {
			// usage note : pass in id of parent element and selector to text field we are binding to.
			// note that text field could be updated via ajax so we need to use "delegate".
			throw('autocomplete must specify parent and child selector.');
		}
		var $parent = $(options.parent);
		var text;

		var defaults = {
			delay:500,
			minLength:0
		};
		var options = $.extend(defaults, options);

		var keyTimer;
		$parent.delegate(options.child, 'keyup', function(e) {
			var newInput = e.target.value;
			if (newInput==text) {
				return;
			}
			if (newInput && newInput.length<options.minLength) {
				return;
			}
			text = newInput;
			if (keyTimer) {
				window.clearTimeout(keyTimer);
			}
			keyTimer = window.setTimeout(function () {
				var url = new String(options.callback)+'&text='+ newInput;
				var wcall = wicketAjaxGet(url, function() {}, function() {});  // note currently don't do anything on success/failure.  could be a future option.
				keyTimer = null;
			}, options.delay);
			return true;
		});

	}

    function linkFor(item) {
        var label = item.label;
        var start = item.matchStart;
        var prefix = '';
        var suffix = '';
        var end = item.matchStart + item.matchCount;
        if (item.matchStart>=0) {
            prefix = item.label.substring(0, start);
            label = '<span class="matched">' + item.label.substring(item.matchStart,end) + '</span>';
            suffix = item.label.substring(end);
        }

        return '<a title="' + item.tooltip + '" class="link">' + prefix + label + suffix + '</a>';
    }

    function descFor(item) {
        return item.desc ?
            '<span class="'+item.descClass+'">' + item.desc + '</span>' :
            '';
    }


	/*
	 * public methods exposed.
	 */
	var createAjaxTextField = function(options) {
		var textField = ajaxTextField(options);
		return textField;
	}

	var render = function(ul, item) {
		return (item.descClass=='min-characters') ?
			renderNoResults(ul, item) :
			(item.descClass=='no-results') ?
			renderNoResults(ul, item) :
			(item.descClass=='max-results') ?
				renderMaxResults(ul,item) :
				renderItem(ul, item);
	};

	/**
	 * id : id of text input element that will have autocomplete menu associated with it.
	 * containers : if the element (id) is in a scrollable entity, you will probably want to make the menu hide when a scroll event occurs.
	 */
	var init = function(id, containers) {
		var auto = $("#"+id);
		auto.initialized=false;

		auto.bind("autocompleteopen", function(event, ui) {
			var menu = $('.ui-autocomplete:visible');
			menu.css('width',Math.max(menu.width()+10,300)); // add room because we've styled it with extra padding between description and result columns.
			auto.initialized=true;
			$('.ui-autocomplete .link').tipsy({gravity: 'n', fade:true, delayIn:355});
			$('.tipsy').remove();  // make sure all pre-existing tooltips are removed.
			// hack to remove jquery styling because rounded corners mucks up the border.
			$('.ui-autocomplete').removeClass('ui-corner-all');
		});
		auto.bind("autocompleteclose", function(event, ui) {
			$("#"+id).focus();
			var container = $('#'+id).closest('.auto-complete-container');
			if (typeof(container.attr('onchange')) == 'string') {
				eval(container.attr('onchange'));
			}
		});
		if (containers) {
			$.each(containers,function(index,selector) {
				$(selector).scroll(function() {
					// bug fix...autocomplete menu should hide when mouse wheel (or other scrolling) happens.  if it's in a scrollable container then
					// you should probably hook this up.  otherwise the menu will just sit there when you scroll the associated text field away.
					$('.ui-autocomplete:visible').hide();
				});
			});
		}
	};



	return {
        render : render,
		createAjaxTextField : createAjaxTextField,
        init : init
    };

})();




