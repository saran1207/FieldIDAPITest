${action.setPageType('product_type', 'list')!}

<#if !productTypes.isEmpty() >
	<table class="list">
		<tr>
			<th><@s.text name="label.producttype" /></th>
			<th></th>
		</tr>
		
		<#list productTypes as productType>
			<tr>
				<td><a href="<@s.url action="productType" uniqueID="${productType.id}" />">${productType.name?html}</a></td>
				<td>
					<a href="<@s.url action="productTypeEdit" uniqueID="${productType.id}" />"><@s.text name="label.edit" /></a>&nbsp;
					<a href="<@s.url action="productTypeCopy" uniqueID="${productType.id}" />"><@s.text name="label.copy" /></a>
				</td>
			</tr>
		</#list>
	</table>
<#else>
	<div class="emptyList" >
		<h2><@s.text name="label.noresults"/></h2>
		<p>
			<@s.text name="label.emptyinspectiontypelist" />
		</p>
	</div>

</#if>