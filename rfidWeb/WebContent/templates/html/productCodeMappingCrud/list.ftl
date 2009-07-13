${action.setPageType('product_code_mapping','list')!}
<#if productCodeMappings.size() != 0 >
	<table class="list">
	<tr>
		<th><@s.text name="label.productcode" /></th>
		<th></th>
	<tr>
	
	<#list productCodeMappings as productCodeMapping >
		<tr >
			<td>${productCodeMapping.productCode}</td>
			<td>
				<a href="<@s.url value="productCodeMappingEdit.action" uniqueID="${productCodeMapping.uniqueID}" />" ><@s.text name="label.edit" /></a>&nbsp;&nbsp;
				<a href="<@s.url value="productCodeMappingRemove.action" uniqueID="${productCodeMapping.uniqueID}" />" onclick="return confirm('<@s.text name="label.areyousure" />');" ><@s.text name="label.remove" /></a>
			</td>
		</tr>
	</#list>
	</table>
<#else>
	<div class="emptyList" >
		<h2><@s.text name="label.noresults" /></h2>
		<p>
			<@s.text name="label.emptylistproductcodemappings" />
		</p>
	</div>
</#if>

