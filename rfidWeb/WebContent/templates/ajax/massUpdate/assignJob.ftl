var messages = ${json.toJSON(actionMessages)};
var errors = ${json.toJSON(actionErrors)};
updateMessages(messages, errors);
window.location=window.location;