

var procedureDefinitionPage = (function() {

	var closeIsolationPointEditor = function() {
		$('.isolation-point-editor').animate({'margin-left':'130%'},400);
//		$('.isolation-point-list').animate({'margin-left':'0%'},400);
	}

	var openIsolationPointEditor = function() {
		$('.isolation-point-editor').animate({'margin-left':'5px'},400);
//		$('.isolation-point-list').animate({'margin-left':'-120%'},400);
	}

	return {
			openIsolationPointEditor : openIsolationPointEditor,
			closeIsolationPointEditor : closeIsolationPointEditor
		};

})();
