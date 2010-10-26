${action.setPageType('asset_code_mapping','list')!}
<#if assetCodeMappings.size() != 0 >
	<table class="list">
	<tr>
		<th><@s.text name="label.assetcode" /></th>
		<th></th>
	<tr>
	
	<#list assetCodeMappings as assetCodeMapping >
		<tr >
			<td>${assetCodeMapping.assetCode}</td>
			<td>
				<a href="<@s.url value="assetCodeMappingEdit.action" uniqueID="${assetCodeMapping.uniqueID}" />" ><@s.text name="label.edit" /></a>&nbsp;&nbsp;
				<a href="<@s.url value="assetCodeMappingRemove.action" uniqueID="${assetCodeMapping.uniqueID}" />" onclick="return confirm('<@s.text name="label.areyousure" />');" ><@s.text name="label.remove" /></a>
			</td>
		</tr>
	</#list>
	</table>
<#else>
	<div class="emptyList" >
		<h2><@s.text name="label.noresults" /></h2>
		<p>
			<@s.text name="label.emptylistassetcodemappings" />
		</p>
	</div>
</#if>

