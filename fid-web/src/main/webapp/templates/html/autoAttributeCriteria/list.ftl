
${action.setPageType('auto_attribute', 'list')!}

<#if assetTypes.size() != 0 >
	<table class="list">
	<tr>
		<th><@s.text name="label.producttype" /></th>
	
	<tr>
	<#list assetTypes as assetType >
	<tr >
		<td>
			<a href="<@s.url action="autoAttributeCriteriaOpen" uniqueID="${(assetType.id)!}" />" > ${assetType.name! } </a>
		</td>
	</tr>
	</#list>
	</table>
<#else>
	<div class="emptyList" >
		<h2><@s.text name="label.noresults" /></h2>
		<p>
			<@s.text name="label.emptylistautoattribute" />
		</p>
	</div>
</#if>

