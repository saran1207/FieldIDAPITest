
/**
 */

var treeFactory = (function() {

	// suggestion : make this lazy so data isn't required 'till later.

	var create = function(treeId, textId, hiddenId, data) {
		var t = tree(treeId, textId, hiddenId, data);
		t.init();
		$('#'+treeId).hide();
		return t;
	}

	var createAndShow = function(treeId, textId, hiddenId, data) {
		var t = create(treeId, textId, hiddenId, data);
		t.init();
		return t;
	}

	function tree(id, textId, hiddenId, data) {
		var treeId = id;
		var data = data;
		var hiddenId= hiddenId;
		var textId = textId;

		var init = function() {
			var tree = $('#'+treeId);
			tree.jstree({
	       		core:{animation:100},
	       		json_data : { data: data },
	       		themes :  { dots:false },
			    plugins : [ 'themes', 'json_data', 'ui', 'hotkeys' ]
       		});

			tree.bind("select_node.jstree", function (event, data) {
					var idOfEntity = data.rslt.obj.data("id");
					$('#'+hiddenId).val(idOfEntity);
					$('#'+textId).val(data.inst.get_text(data.rslt.obj));
					$('#'+treeId).hide();
				});

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

