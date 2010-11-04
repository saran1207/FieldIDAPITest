var eventAttributeIndex = 0;
var addEventAttributeUrl = '';

function addEventAttribute() {
	getResponse( addEventAttributeUrl + '?fieldIndex='+ eventAttributeIndex++ );
}


function removeEventAttribute( index ) {
	$( 'attribute_' + index ).remove();
}