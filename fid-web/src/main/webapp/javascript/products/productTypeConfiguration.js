function removeProductEvent( event ) {
	removeSubProduct( Event.element(event).productId, event );
} 
var removeString = "";
function includeSubProduct( event ) {
	if( event ) {
		event.stop();
	}
	var subProduct = $('addSubProduct');
	var productName = subProduct.options[subProduct.selectedIndex].text;
	var item = new Element( 'li', { id: "subProduct_" + subProduct.getValue() } );
	var productNameSpan = new Element( 'span', {id: "productName_" + subProduct.getValue() } ).update(productName);
	
	item.insert( new Element( 'input', { type: "hidden", name: "subProductIds["+ numberOfSubTypes +"]", value: subProduct.getValue() } ) );
	subProductId= subProduct.getValue();
	numberOfSubTypes++;
	var productLink = new Element( 'a', { id: "removeProductLink_" + subProduct.getValue(), href: "removeSubProduct" , assetTypeId: subProductId } ).update(removeString) ;
	productLink.observe('click', removeProductEvent );
	productLink.productId = subProduct.getValue();
	item.insert( productNameSpan );
	item.insert( productLink );
	$( 'subProducts' ).insert( item );
	subProduct.remove( subProduct.selectedIndex );
	
}

var numberOfSubTypes = 0;
function removeSubProduct( subProductId, event ) {
	if( event ) {
		event.stop();
		var element = Event.element( event );
		subProductId = element.readAttribute( "assetTypeId" );
	}
	var subProduct = $( 'subProduct_' + subProductId );
	
	var subProductList = $( 'addSubProduct' );
	var newOption = new Element( 'option' );

	newOption.text= $( 'productName_' + subProductId ).innerHTML;
	newOption.value= subProductId;
	subProductList.options.add( newOption );
	sortList( subProductList.id );
	subProduct.remove();
	
}