var changeCommentUrl = "";
function changeComments( commentSelect ) {
	if( commentSelect.options[ commentSelect.selectedIndex ].value != "" ) {
		var commentTemplateId = commentSelect.options[ commentSelect.selectedIndex ].value;
		var url = changeCommentUrl + '?uniqueID='+ commentTemplateId;

		getResponse(url, "get");
	} 
}

function updateComments( text ) {
	var commentsTextArea = $('comments');
	if( commentsTextArea.value != "" ) {
		commentsTextArea.value += "\n";
	}
	commentsTextArea.value += text;
}