/**
 * TODO : refactor this to combine shared elements of tree.js
 * put both in fieldIdWidgets.js?
 */

var orgTreeFactory = (function() {

	var create = function(options) {
		var t = _create(options);
		t.update('',true);
		return t;
	}

	function _create(options) {
		var t = tree(options);
		return t;
	}

	function tree(opts) {
		var id = opts.id;
		var $tree;
		var $node;
		var initialized = false;
		var input = '';
		var $text;

		var defaults = {
			delay:500,
			minLength:0
		};
		var options = $.extend(defaults, opts);

		function getTree() {
			return lazyInit();
		}

		var update = function(newInput,force) {
			if (force || (newInput!=input)) {
				input = newInput;
				getTree().find("li.jstree-open").each(function(index) {
						$(this).removeClass('jstree-open');}   // forces tree to forget about currently selected nodes.
				);
				jQuery.jstree._reference(getTree()).refresh(-1);
			}
		}

		function showNoResultsMsg() {
			var noResults = $tree.siblings('.no-results');
			if (noResults.length==0) {
				$tree.after($('<div>No Results</div>').addClass('no-results'));
			} else {
				$(noResults[0]).show();
			}
			$tree.hide();
		}

		function showResults() {
			$tree.show();
			var noResults = $tree.siblings('.no-results');
			if (noResults.length>0) {
				$(noResults[0]).hide();
			}
		}

		function lazyInit() {
			if (!initialized) {
				initialized = true;
				$tree = $('#'+options.id);
				$text = $tree.prev('input[type=text]');
				$tree.jstree({
					core:{animation:100, html_titles:true},
					themes :  { dots:false },
					json_data : {
						ajax: {
							type : 'GET',
							url : function(node) {
								$node = node;
								var base = new String(options.updateCallback);
								if (node!=-1) {
									// search for a particular branch under the specified node.
									return base +'&nodeId='+node.attr('id')+'&nodeType='+node.attr('data');
								} else {
									return base +'&search='+input;
								}
							},
							success : function(n) {
								if (n && n.length==0 && $node==-1) {
									showNoResultsMsg();
								} else {
									showResults();
								}
							}
						}
					},
					plugins : [ 'themes', 'json_data', 'ui', 'hotkeys' ]
				});

				$tree.bind('refresh.jstree', function (e, data) {
					$('.timeago').timeago();
				});

				$tree.bind('load_node.jstree', function (e, data) {
					$('.timeago').timeago();
				});

				$tree.bind("click.jstree", function (event, data) {
					if (!$(event.currentTarget).is('a')) { return; }
					var $link = $(event.target);
					if ($link.hasClass('jstree-loading')) {return;}
					var node = $link.closest('li');
					var url = options.clickCallback + '?id=' + node.attr('id') + '&type=' + node.attr('data');
					window.location = url;
				});

				$tree.show();

				// may need to use delegate here???
				var keyTimer;
				if (!$text) return;
				$text.bind('keyup', function(e,d) {
					if (keyTimer) {
						window.clearTimeout(keyTimer);
					}
					keyTimer = window.setTimeout(function () {
						update($text.val());
						keyTimer = null;
					}, options.delay);
					return true;
				});

			}
			return $tree;
		}


		return {
			update : update
		}

	}

	return {
		create:create
	}

})();
