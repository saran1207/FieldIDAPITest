
var goToPageURL = '';
function paginateGoto(event) {
	var element = Event.element(event);
	
	if (isNumber(element.getValue())) {
		
		var brokenUpUrl = goToPageURL.split('?');
		var params = brokenUpUrl[1].unescapeHTML().toQueryParams();
		params['currentPage'] = element.getValue();
		redirect(brokenUpUrl[0] + "?" + Object.toQueryString(params));
	}
}


function isNumber(value) {
	return !(isNaN(parseInt(value)));
}

Element.extend(document).observe("dom:loaded", function() {
		$$('.toPage').each( function(element) { 
				element.observe('change', paginateGoto)
				element.observe('keypress', function(event) {
						if (Event.KEY_RETURN == event.keyCode) {
								paginateGoto(event);
						}
					});
			} 
		);
		
});