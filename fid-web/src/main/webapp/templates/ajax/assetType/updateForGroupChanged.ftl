<#escape x as x?js_string >
<#assign html>
<@s.select cssClass="assetTypeSelect" name="criteria.assetType" list="assetTypes" listKey="id" listValue="name" emptyOption="true" theme="fieldidSimple"/>
</#assign>
	$$('.assetTypeSelect').each(function(element) {
        element.replace('${html}');
    });
</#escape>