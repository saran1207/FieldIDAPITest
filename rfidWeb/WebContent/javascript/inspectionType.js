var inspectionAttributeIndex = 0;
var addInspectionAttributeUrl = '';

function addInspectionAttribute() {
	getResponse( addInspectionAttributeUrl + '?fieldIndex='+ inspectionAttributeIndex++ );
}


function removeInspectionAttribute( index ) {
	$( 'attribute_' + index ).remove();
}