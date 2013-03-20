

var procedureDefinitionPage = (function() {

	var closeIsolationPointEditor = function(id) {
		$('#'+id + ' .isolation-point-editor').animate({'margin-left':'130%'},400);
		$('#'+id + ' .isolation-point-list').animate({'margin-left':'0%'},400);
	}

	var openIsolationPointEditor = function(id) {
		$('#'+id + ' .isolation-point-editor').animate({'margin-left':'0%'},400);
		$('#'+id + ' .isolation-point-list').animate({'margin-left':'-120%'},400);
	}

	return {
			openIsolationPointEditor : openIsolationPointEditor,
			closeIsolationPointEditor : closeIsolationPointEditor
		};

})();
