<#list productTypes.groups as group>
	<#if productTypes.getGroupedProductTypes(group)?exists >
		<@s.optgroup label="${group?html}" list="%{productTypes.getGroupedProductTypes('${group?js_string}')}" listKey="id" listValue="name" />
	</#if>
</#list>
<@s.optgroup label="${action.getText('label.all')}" list="productTypes.productTypes" listKey="id" listValue="name" />
