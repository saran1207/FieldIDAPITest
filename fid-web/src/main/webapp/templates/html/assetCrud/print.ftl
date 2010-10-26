${action.setPageType('asset', 'add')!}
<script type="text/javascript" >


	var newWindow = window.open( '<@s.url action="downloadManufacturerCert" namespace="/file" includeParams="none" uniqueID="${asset.uniqueID}" />', '_blank' );
	if( newWindow != null ) {
		window.location = '<@s.url action="assetAdd" includeParams="none" />';
	} 
</script> 
<p>

The manufacturer certificate for the asset you just created should begin downloading automatically. If it doesn't click <a target="_blank" href="<@s.url action="downloadManufacturerCert" namespace="/file" includeParams="none" uniqueID="${asset.uniqueID}" />">here</a>.
</p>
<p>
Click <a href="<@s.url action="assetAdd" />">here</a> to add another asset. 
</p>