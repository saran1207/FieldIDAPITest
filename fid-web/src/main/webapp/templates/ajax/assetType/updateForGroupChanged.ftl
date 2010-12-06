<#escape x as x?js_string >
<#assign html>
<@s.select cssClass="assetTypeSelect" id="assetType" name="criteria.assetType" emptyOption="true" list="assetTypes" listKey="id" listValue="name" onchange="assetTypeChanged(this)"/>
</#assign>
	$$('.assetTypeSelect').each(function(element) {
		element.replace('${html}');
    });
	assetTypeChanged($('assetType'));
</#escape>
