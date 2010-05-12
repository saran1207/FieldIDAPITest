<#assign html>
	<#assign useOverRides=true/>
	<#assign actionTarget="product"/>
	<#include "/templates/html/inspectionGroup/_productList.ftl"/>
</#assign>


updateResults('${html?js_string}');
$$('#searchResults .productLink').each(function(element) { element.observe('click', selectAsset) });
