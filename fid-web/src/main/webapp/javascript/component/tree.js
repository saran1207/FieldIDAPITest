
/**
 */

var treeFactory = (function() {

	var create = function(treeId, url) {
		var t = _create(treeId, url);
		t.hideTree();
		return t;
	}

	var createAndShow = function(treeId, url) {
		var t = _create(treeId, url);
		t.showTree();
		return t;
	}

	function _create(treeId, url) {
		var t = tree(treeId, url);
		t.init();
		return t;
	}

	function tree(id, url) {
		var id = id;
		var $entityId;
		var $text;
		var $tree;
		var $type;
		var initialized = false;
		var searching = false;
		var callback = url;
		var input = '';

		function search(e,d) {
			if (searching) {
				return;
			}
			var newInput=$text.val();
			if (newInput!=input) { //} && newInput.length>1) {
				input = newInput;
				searching = true;
				jQuery.jstree._reference($tree).refresh(-1);
			}
		}

		var showTree = function() {
			$tree.show();
		}

		var hideTree = function() {
			$tree.hide();
		}

		var init = function() {
			var $widget = $('#'+id);
			$tree = $widget.find('.tree');
			$entityId = $widget.find('.entityId-input');
			$type = $widget.find('.type-input');
			$text = $widget.find('.text');
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
			if (!initialized) {
				initialized = true;
				$tree.bind("dblclick.jstree", function (event, data) {
					var node = $(event.target).closest('li');
					$entityId.val(node.attr('id'));
					$type.val(node.attr('data'));
					$text.val(node.text().trim());
					$tree.hide();
				});
				$text.bind('keyup', function(e,d) {
					search(e,d);
					return true;
				});
			}
		};

		return {
			init: init,
			hideTree : hideTree,
			showTree : showTree
		}

	}

	return {
		create:create,
		createAndShow:createAndShow
	}

})();

