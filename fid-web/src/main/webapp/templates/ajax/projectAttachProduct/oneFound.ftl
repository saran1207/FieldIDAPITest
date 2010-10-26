<#assign html >
	<#assign actionTarget="asset"/>
	<#include "/templates/html/inspectionGroup/_assetList.ftl"/>
</#assign>

<#escape x as x?js_string>
	
	 
	oneResultAsset( ${assets[0].id}, '${html}' );
	
	
</#escape>

