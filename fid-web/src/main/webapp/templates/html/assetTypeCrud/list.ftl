${action.setPageType('asset_type', 'list')!}

<#if !assetTypes.isEmpty() >
	<table class="list">
		<tr>
			<th><@s.text name="label.assettype"/></th>
			<th><@s.text name="label.asset_type_group"/></th>
			<th><@s.text name="label.created"/></th>
			<th><@s.text name="label.last_modified"/></th>		
			<th></th>
		</tr>
		
		<#list assetTypes as assetType>
			<tr>
				<td><a href="<@s.url action="assetType" uniqueID="${assetType.id}" />">${assetType.name?html}</a></td>
				<td><#if assetType.group?exists><a href="<@s.url action="assetTypeGroup" uniqueID="${assetType.group.id}" />">${assetType.group.name!}</a></#if></td>
				<td><#if assetType.createdBy?exists>${assetType.createdBy.fullName!},&nbsp;</#if>${action.formatDateTime(assetType.created)}</td>
				<td><#if assetType.modifiedBy?exists>${assetType.modifiedBy.fullName!},&nbsp;</#if>${action.formatDateTime(assetType.modified)}</td>
				
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
			<@s.text name="label.emptyassettypelist" />
		</p>
	</div>

</#if>
