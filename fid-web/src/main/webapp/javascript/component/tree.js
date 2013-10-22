
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
		var $widget = $('#'+id);
		var id = id;
		var $entityId;
		var $text;
		var $tree;
		var $type;
		var $node;
		var initialized = false;
		var callback = options.url;
		var input = '';
		var options = options;
		var threshold = 3;


		function getTree() {
			return lazyInit();
		}

		function updateTreeWithNewInput(newInput,showTreeForEmptyString) {
			if (newInput.length==0) {
				$entityId.val(null); 	// clear out values
				$type.val(null);
				return showTreeForEmptyString==true;
			} else if (newInput != input) {
				if (newInput.length >= threshold) {
					return true;
				}
				return false;
			}
			return false;
		}

		function update(newInput,showTreeForEmptyString) {
			if (updateTreeWithNewInput(newInput,showTreeForEmptyString)) {
				input = newInput;
				$tree.find("li.jstree-open").each(function(index) {
					$(this).removeClass('jstree-open');}   // forces tree to forget about currently selected nodes.
				);
				jQuery.jstree._reference(getTree()).refresh(-1);
				getTree().show();
			}
		}

		function getNoResults() {
			var noResults = $tree.siblings('.no-results');
			if (noResults.length==0) {
				return $tree.after($('<div>No Results</div>').addClass('no-results'));
			} else {
				return $(noResults[0]);
			}
		}

		function getCurrentText() {
			return $text.val().replace(/^\s+/,"");
		}

		function search(e,d) {
			var newInput= getCurrentText();
			if (newInput.length<threshold) {
				getTree().hide();
				return;
			}
			update(newInput);
		}

		var toggleTree = function() {
			if (getTree().is(':visible')) {
				getTree().hide();
			} else {
				update(getCurrentText(),true);
				getTree().show();
			}
		}

		var init = function() {
			$tree = $widget.find('.tree');
			$text = $widget.find('.text');
			$entityId = $widget.find('.entityId-input');
			$type = $widget.find('.type-input');
			$text = $widget.find('.text');

			$tree.hide();

            var keyTimer;
			$text.bind('keyup', function(e,d) {
                if (keyTimer) {
                    window.clearTimeout(keyTimer);
                }
                keyTimer = window.setTimeout(function () {
                    search(e, d);
                    keyTimer = null;
                }, 500);
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
					.end()  	//again go back to selected element
					.text();  	// and get its text (children excluded at this point)
		}

		function lazyInit() {
			if (!initialized) {
				initialized = true;
				$tree.jstree({
					ui : { initially_select:[] },
					core:{animation:100},
					themes :  { dots:false },
					json_data : {
						ajax: {
							type : 'GET',
							url : function(node) {
								$node = node;
								if (node!=-1) {
									// search for a particular branch under the specified node.
									return new String(callback)+'&nodeId='+node.attr('id')+'&nodeType='+node.attr('data');
								} else {
									return new String(callback)+'&search='+input;
								}
						},
							success : function(n) {
								if (n && n.length>0) {
									getNoResults().hide();
									$tree.show();
								} else if ($node==-1) {
									$tree.hide();
									getNoResults().show();
									setTimeout(function() { getNoResults().fadeOut(1000);},1200);
								}
							}
						}
					},
					plugins : [ 'themes', 'json_data', 'ui', 'hotkeys' ]
				});


				$('body').click(function(e) {
					if ($(e.toElement).parents().index($widget)==-1) {    // hide popup when you click somewhere else.
						$tree.hide();
					}
				});


				$tree.bind("click.jstree", function (event, data) {
					if (!$(event.currentTarget).is('a')) { return; }
					var $link = $(event.target);
					if ($link.hasClass('jstree-loading')) {return;}
					var node = $link.closest('li');
					$entityId.val(node.attr('id'));
					$type.val(node.attr('data'));
					$text.val(getTextExcludingChildren($link).trim());
					$tree.hide();
					$('#'+id).change();
				});

			}
			return $tree;
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

