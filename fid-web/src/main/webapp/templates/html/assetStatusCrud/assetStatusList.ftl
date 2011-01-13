
${action.setPageType('asset_status', 'list')!}
<#if assetStatuses.size() != 0 >
	<table class="list">
		<tr>
			<th><@s.text name="label.assetstatus" /></th>
			<th><@s.text name="label.created_by" /></th>
			<th><@s.text name="label.last_modified_by" /></th>		
			<th></th>
		</tr>
	
		<#list assetStatuses as assetStatus >

			<tr >
				<td>${assetStatus.name}</td>
				<td><#if assetStatus.createdBy?exists>${assetStatus.createdBy.fullName!},&nbsp;</#if>${action.formatDateTime(assetStatus.created)}</td>
				<td><#if assetStatus.modifiedBy?exists>${assetStatus.modifiedBy.fullName!},&nbsp;</#if>${action.formatDateTime(assetStatus.modified)}</td>
				<td>
					<a href="<@s.url value="assetStatusEdit.action" uniqueID="${assetStatus.id}" />" ><@s.text name="label.edit" /></a>&nbsp;&nbsp;
					<a href="<@s.url value="assetStatusRemove.action" uniqueID="${assetStatus.id}" />" onclick="return confirm('<@s.text name="label.areyousure" />');" ><@s.text name="label.remove" /></a>
				</td>
			</tr>
		</#list>
	</table>
<#else>
	<div class="emptyList" >
		<p>
		<@s.text name="label.emptylistassetstatuses" />
		</p>
	</div>
</#if>

