<#list assetTypes.groups as group>
	<#if assetTypes.getGroupedAssetTypes(group)?exists >
		<@s.optgroup label="${group?html}" list="%{assetTypes.getGroupedAssetTypes('${group?js_string}')}" listKey="id" listValue="name" />
	</#if>
</#list>
<@s.optgroup label="${action.getText('label.ungrouped')}" list="assetTypes.ungroupedAssetTypes" listKey="id" listValue="name" />
