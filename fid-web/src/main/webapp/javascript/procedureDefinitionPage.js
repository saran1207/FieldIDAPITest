

var procedureDefinitionPage = (function() {

	var closeIsolationPointEditor = function() {
		$('.isolation-point-editor').animate({'margin-left':'105%'},400);
		$('input[name="sourceText"]').unbind('change.ipe');
	}

	var openIsolationPointEditor = function() {
		$('.isolation-point-editor').animate({'margin-left':'0%'},400);
		// bind text field to annotation's text field.
		$('input[name="identifier"]').bind('change.ipe', function(e) {
			$('.notes-container .note input').val($(e.target).val());
		});
	}

	return {
			openIsolationPointEditor : openIsolationPointEditor,
			closeIsolationPointEditor : closeIsolationPointEditor
		};

})();
