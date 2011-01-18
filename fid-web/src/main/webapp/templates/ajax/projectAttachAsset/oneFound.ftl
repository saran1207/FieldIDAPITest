<#assign html >
	<#assign actionTarget="asset"/>
	<#include "/templates/html/eventGroup/_assetList.ftl"/>
</#assign>

<#escape x as x?js_string>
	oneResultAsset( ${page.list.get(0).id}, '${html}' );
</#escape>

