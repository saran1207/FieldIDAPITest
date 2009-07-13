<#escape x as x?js_string >
	<#noescape>
		var messages = ${json.toJSON(actionMessages)};
		var errors = ${json.toJSON(actionErrors)};
	</#noescape>
	${action.clearFlashMessages()!}
	${action.clearFlashErrors()!}
	updateMessages(messages, errors);
</#escape>