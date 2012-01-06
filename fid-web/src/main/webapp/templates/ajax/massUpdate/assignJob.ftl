var messages = ${json.toJson(actionMessages)};
var errors = ${json.toJson(actionErrors)};
updateMessages(messages, errors);
window.location.reload();