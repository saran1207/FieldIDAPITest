var locationIndex = 0;
var addLocationUrl = '';

function addLocation() {
	getResponse( addLocationUrl + '?nodeIndex='+ locationIndex++ );
}

function removeLocation( index ) {
	$( 'node_' + index ).remove();
}