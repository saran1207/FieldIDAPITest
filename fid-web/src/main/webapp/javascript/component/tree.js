
/**
 */

var treeFactory = (function() {

	// suggestion : make this lazy so data isn't required 'till later.

	var create = function(treeId, url) {
		var t = _create(treeId, url);
		$('#'+treeId).hide();
		return t;
	}

	var createAndShow = function(treeId, url) {
		var t = _create(treeId, url);
		$('#'+treeId).show();
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
		var url = url;
		var callback = new String(url)+'&search=';

		function search(e,d) {
//			if (searching) {
//				return;
//			}
//			searching = true;
			var input = $text.val();

			callback = new String(url)+'&search='+$text.val();
			// TODO DD : update stuff in success/failure methods.  highlight node? etc...
			jQuery.jstree._reference($tree).refresh(-1);
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
					ajax: { url : function() {
								return callback;
							}
						}

				},
				plugins : [ 'themes', 'json_data', 'ui', 'hotkeys' ]
			});
			if (!initialized) {
				initialized = true;
				$tree.bind("select_node.jstree", function (event, data) {
					$entityId.val(data.rslt.obj.data("entityId"));
					$type.val(data.rslt.obj.data("type"));
					$text.val(data.inst.get_text(data.rslt.obj));
					//$tree.hide();
				});
				$text.bind('keyup', function(e,d) {
					search(e,d);
					return true;
				});
				jQuery.jstree._reference($tree).refresh(-1);
			}
		};


		return {
			init: init
		}

	}

	return {
		create:create,
		createAndShow:createAndShow
	}

})();

