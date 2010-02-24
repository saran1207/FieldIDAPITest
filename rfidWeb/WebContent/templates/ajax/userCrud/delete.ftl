<#assign b = { "errors": actionErrors, "messages": actionMessages, "uniqueID": uniqueID!, "status": actionErrors.size() } />
${ action.clearFlashMessages()! }${ action.clearFlashErrors()! }
var response = ${ json.toJson( b ) }

deleteUserCallback( response ); 