${action.setPageType('product', 'add')!}
<script type="text/javascript" >


	var newWindow = window.open( '<@s.url action="downloadManufacturerCert" namespace="/file" includeParams="none" uniqueID="${product.uniqueID}" />', '_blank' );
	if( newWindow != null ) {
		window.location = '<@s.url action="productAdd" includeParams="none" />';
	} 
</script> 
<p>

The manufacturer certificate for the product you just created should begin downloading automatically if it doesn't click <a target="_blank" href="<@s.url action="downloadManufacturerCert" namespace="/file" includeParams="none" uniqueID="${product.uniqueID}" />">here</a>.
</p>
<p>
Click <a href="<@s.url action="productAdd" />">here</a> to add another product. 
</p>