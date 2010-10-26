${action.setPageType('asset_type', 'list')!}

<#if !assetTypes.isEmpty() >
	<table class="list">
		<tr>
			<th><@s.text name="label.producttype" /></th>
			<th></th>
		</tr>
		
		<#list assetTypes as assetType>
			<tr>
				<td><a href="<@s.url action="assetType" uniqueID="${assetType.id}" />">${assetType.name?html}</a></td>
				<td>
					<a href="<@s.url action="assetTypeEdit" uniqueID="${assetType.id}" />"><@s.text name="label.edit" /></a>&nbsp;
					<a href="<@s.url action="assetTypeCopy" uniqueID="${assetType.id}" />"><@s.text name="label.copy" /></a>
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