<#escape x as x?js_string >
<#assign html>
<@s.select cssClass="assetTypeSelect" id="assetType" name="view.assetTypeId" emptyOption="true" list="assetTypes" listKey="id" listValue="name" theme="fieldidSimple"/>
</#assign>
	$$('.assetTypeSelect').each(function(element) {
		element.replace('${html}');
    });
</#escape>
