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

	function tree(options) {
		var id = options.id;
		var $tree;
		var $node;
		var initialized = false;
		var input = '';
		// TODO DD : add default options here...
		var options = options;

		function getTree() {
			return lazyInit();
		}

		var update = function(newInput,force) {
			if (force || (newInput!=input && newInput.length>options.minLength)) {
				input = newInput;
				getTree().find("li.jstree-open").each(function(index) {
						$(this).removeClass('jstree-open');}   // forces tree to forget about currently selected nodes.
				);
				jQuery.jstree._reference(getTree()).refresh(-1);
			}
		}

		function lazyInit() {
			if (!initialized) {
				initialized = true;
				$tree = $('#'+options.id);

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
					// TODO DD: fire ajax nodeClick event with id of node in url.
				});

				$tree.show();

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
