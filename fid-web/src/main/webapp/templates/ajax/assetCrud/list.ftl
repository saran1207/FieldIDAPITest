<#assign html>
	<#assign useOverRides=true/>
	<#assign actionTarget="asset"/>
	<#include "/templates/html/inspectionGroup/_assetList.ftl"/>
</#assign>

$$('input[name="search"]')[1].clear();
$$('input[name="search"]')[1].focus();

updateResults('${html?js_string}');
$$('#searchResults .assetLink').each(function(element) { element.observe('click', selectAsset) });
