<#assign html >
	<#assign actionTarget="product"/>
	<#include "/templates/html/inspectionGroup/_productList.ftl"/>
</#assign>

<#escape x as x?js_string>
	
	 
	oneResultAsset( ${assets[0].id}, '${html}' );
	
	
</#escape>

