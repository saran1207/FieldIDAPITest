/**
 * TODO : amalgamate this and other widgets into one single widgetFactory.js file with many different flavours of create.
 */
var richTextFactory = (function() {

	var create = function(rtfId,options) {
		var textField = rtf(rtfId,options);
		textField.init();
		return textField;
	};

	// TODO : this is more edit EventForm related, not rich text in general.
	// should move to eventForm.js or possibly popup.js?
	var update = function(rtfId, contentId) {
		var content = $('#'+contentId).text();
		nicEditors.findEditor(rtfId).setContent(content);
		$('.nicEdit-main').parent().css('border','0px solid blue');
		$('.rich-text').height('0%');
	}

	function rtf(rtfId,opts) {
		var id = rtfId;
		var options = opts;
		var initialized = false;

		var init = function() {
			if (!initialized) {
				new nicEditor(options).panelInstance(id).addEvent('blur', function() {
					nicEditors.findEditor(id).saveContent();
					// force text area to blur so wicket will auto-update model via behavior.
					$('#'+id).blur();
				});
				if (options.disabled) {
					nicEditors.findEditor(id).disable();
				}
			}
			initialized = true;
		};

		return {
			init:init
		}

	}

	return {
		create:create,
		update:update
	}

})();
