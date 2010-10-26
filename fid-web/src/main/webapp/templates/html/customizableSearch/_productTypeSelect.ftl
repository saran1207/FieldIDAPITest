<head>
	<@n4.includeScript>
		<#list assetTypes.groups as group>
			<#if assetTypes.getGroupedAssetTypes(group)?exists >
				groupToAssetType['${group?js_string}'] = ${json.toJson(assetTypes.getGroupedAssetTypes(group))};
			</#if>
		</#list>
		groupToAssetType["${action.getText('label.all')}"] = ${json.toJson(assetTypes.assetTypes)};
	</@n4.includeScript>
</head>
<div class="infoSet">
	<label for="criteria.assetTypeGroup"><@s.text name="label.product_type_group"/></label>
	<@s.select id="assetTypeGroup" name="criteria.assetTypeGroup" headerKey="" headerValue="${action.getText('label.all')}" onchange="assetTypeGroupChanged(this)" list="assetTypes.assetTypeGroups" listKey="id" listValue="name"/>
</div>

<div class="infoSet">
	<label for="criteria.assetType"><@s.text name="label.producttype"/></label>
	<@s.select id="assetType" name="criteria.assetType" emptyOption="true" onchange="assetTypeChanged(this)" list="%{assetTypes.getGroupedAssetTypesById(${criteria.assetTypeGroup?default(-1)})}" listKey="id" listValue="name"/>
</div>