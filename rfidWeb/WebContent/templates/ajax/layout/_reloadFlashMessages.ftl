<#escape x as x?js_string >
	<#noescape>
		var messages = ${json.toJson(actionMessages)};
		var errors = ${json.toJson(actionErrors)};
	</#noescape>
	${action.clearFlashMessages()!}
	${action.clearFlashErrors()!}
	updateMessages(messages, errors);
</#escape>