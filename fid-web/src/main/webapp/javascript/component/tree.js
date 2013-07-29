
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
			var length = newInput.length;
			if (newInput.length==0) {
				// clear out values
				$entityId.val(null);
				$type.val(null);
			}
			if (newInput!=input) {
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

		// suggestion : move this into utils pkg.
		// note that .text() will return text of *all* descendants, not just element.
		// this is a somewhat inefficient but re-usable way to get around it.  don't use for very elements with many descendants.
		function getTextExcludingChildren(element) {
			return element
					.clone()    //clone the element
					.children() //select all the children
					.remove()   //remove all the children
					.end()  //again go back to selected element
					.text();
		}

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
					var link = $(event.target);
					var node = $(event.target).closest('li');
					$entityId.val(node.attr('id'));
					$type.val(node.attr('data'));
					$text.val(getTextExcludingChildren(link).trim());
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

