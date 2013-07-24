
/**
 */

var treeFactory = (function() {

	var create = function(treeId, options) {
		var t = _create(treeId, options);
		return t;
	}

	var createAndShow = function(treeId, options) {
		options.show = true;
		var t = _create(treeId, options);
		return t;
	}

	function _create(treeId, options) {
		var t = tree(treeId, options);
		t.init();
		return t;
	}

	function tree(id, options) {
		var id = id;
		var $entityId;
		var $text;
		var $tree;
		var $type;
		var initialized = false;
		var searching = false;
		var callback = options.url;
		var input = '';
		var options = options;

		function search(e,d) {
			if (searching) {
				return;
			}
			var newInput=$text.val();
			if (newInput!=input && newInput.length>1) {
				input = newInput;
				searching = true;
				lazyInit();
				jQuery.jstree._reference($tree).refresh(-1);
			}
		}

		var toggleTree = function() {
			lazyInit();
			$tree.toggle();
		}

		var init = function() {
			var $widget = $('#'+id);
			$tree = $widget.find('.tree');
			$text = $widget.find('.text');
			$entityId = $widget.find('.entityId-input');
			$type = $widget.find('.type-input');
			$text = $widget.find('.text');

			if (options.show) {
				$tree.show();
			} else {
				$tree.hide();
			}
			$text.bind('keyup', function(e,d) {
				search(e,d);
				return true;
			});
		};

		function lazyInit() {
			if (!initialized) {
				initialized = true;
				$tree.jstree({
					core:{animation:100},
					themes :  { dots:false },
					json_data : {
						ajax: {
							type : 'GET',
							url : function(node) {
								if (node!=-1) {
									// search for a particular branch under the specified node.
									return new String(callback)+'&nodeId='+node.attr('id')+'&type='+node.attr('data');
								} else {
									return new String(callback)+'&search='+input;
								}
							},
							success : function(n) {
								searching = false;
								$tree.show();
							}
						}
					},
					plugins : [ 'themes', 'json_data', 'ui', 'hotkeys' ]
				});
				$tree.bind("click.jstree", function (event, data) {
					if (!$(event.currentTarget).is('a')) { return; }
					var node = $(event.target).closest('li');
					$entityId.val(node.attr('id'));
					$type.val(node.attr('data'));
					$text.val(node.text().trim());
					$tree.hide();
				});

			}
		}


		return {
			init: init,
			toggleTree : toggleTree
		}

	}

	return {
		create:create,
		createAndShow:createAndShow
	}

})();

