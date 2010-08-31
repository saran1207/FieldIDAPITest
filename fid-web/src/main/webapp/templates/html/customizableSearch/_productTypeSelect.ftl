<head>
	<@n4.includeScript>
		<#list productTypes.groups as group>
			<#if productTypes.getGroupedProductTypes(group)?exists >
				groupToProductType['${group?js_string}'] = ${json.toJson(productTypes.getGroupedProductTypes(group))}; 
			</#if>
		</#list>
		groupToProductType["${action.getText('label.all')}"] = ${json.toJson(productTypes.productTypes)};
	</@n4.includeScript>
</head>
<div class="infoSet">
	<label for="criteria.productTypeGroup"><@s.text name="label.product_type_group"/></label>
	<@s.select id="productTypeGroup" name="criteria.productTypeGroup" headerKey="" headerValue="${action.getText('label.all')}" onchange="productTypeGroupChanged(this)" list="productTypes.productTypeGroups" listKey="id" listValue="name"/>
</div>

<div class="infoSet">
	<label for="criteria.productType"><@s.text name="label.producttype"/></label>
	<@s.select id="productType" name="criteria.productType" emptyOption="true" onchange="productTypeChanged(this)" list="%{productTypes.getGroupedProductTypesById(${criteria.productTypeGroup?default(-1)})}" listKey="id" listValue="name"/>
</div>