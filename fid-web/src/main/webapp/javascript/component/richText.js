/**
 * TODO : amalgamate this and other widgets into one single widgetFactory.js file with many different flavours of create.
 */
var richTextFactory = (function() {

	var create = function(rtfId,options) {
		var textField = rtf(rtfId,options);
		textField.init();
		return textField;
	};

	var update = function(rtfId, contentId) {
		var content = $('#'+contentId).text();
		nicEditors.findEditor(rtfId).setContent(content);
	}

	function rtf(rtfId,opts) {
		var id = rtfId;
		var options = opts;
		var initialized = false;

		var init = function() {
			if (!initialized) {
				new nicEditor(options).panelInstance(id);
				if (options.disabled) {
					nicEditors.findEditor(id).disable();
				}
				$('#'+id).parent().find('.nicEdit-main').blur(function() {
//					$('#'+id).html(nicEditors.findEditor(id).getContent());
					nicEditors.findEditor(id).saveContent();
					// force text area to blur so wicket will auto-update model via behavior.
					$('#'+id).blur();
				});
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
