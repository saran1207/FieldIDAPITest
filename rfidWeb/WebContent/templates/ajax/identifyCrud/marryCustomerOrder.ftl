${ action.clearFlashMessages()! }${ action.clearFlashErrors()! }
updateMessages(${json.toJSON(actionMessages)}, ${json.toJSON(actionErrors)});
$('resultsTable').remove();