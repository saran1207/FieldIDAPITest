${ action.clearFlashMessages()! }${ action.clearFlashErrors()! }
updateMessages(${json.toJson(actionMessages)}, ${json.toJson(actionErrors)});
$('resultsTable').remove();