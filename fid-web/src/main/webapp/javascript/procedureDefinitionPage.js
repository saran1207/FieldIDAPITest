

var procedureDefinitionPage = (function() {

	var closeIsolationPointEditor = function() {
		$('.isolation-point-editor').animate({'margin-left':'105%'},400);

	}

	var openIsolationPointEditor = function() {
		$('.isolation-point-editor').animate({'margin-left':'0%'},400);
	}

	return {
			openIsolationPointEditor : openIsolationPointEditor,
			closeIsolationPointEditor : closeIsolationPointEditor
		};

})();
