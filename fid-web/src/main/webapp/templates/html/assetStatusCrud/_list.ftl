<#if statusList.size() != 0 >
	<table class="list">
		<tr>
			<th><@s.text name="label.assetstatus" /></th>
			<th><@s.text name="label.created" /></th>
			<th><@s.text name="label.last_modified" /></th>		
			<th></th>
		</tr>
	
		<#list statusList as assetStatus >

			<tr >
				<td>${assetStatus.name}</td>
				<td><#if assetStatus.createdBy?exists>${assetStatus.createdBy.fullName!},&nbsp;</#if>${action.formatDateTime(assetStatus.created)}</td>
				<td><#if assetStatus.modifiedBy?exists>${assetStatus.modifiedBy.fullName!},&nbsp;</#if>${action.formatDateTime(assetStatus.modified)}</td>
				<td>
					<a href="<@s.url value="assetStatusEdit.action" uniqueID="${assetStatus.id}" />" ><@s.text name="label.edit" /></a>&nbsp;&nbsp;
					<#if isArchivedList>
						<a href="<@s.url value="assetStatusUnarchive.action" uniqueID="${assetStatus.id}" />"><@s.text name="label.unarchive" /></a>
					<#else>
						<a href="<@s.url value="assetStatusArchive.action" uniqueID="${assetStatus.id}" />"><@s.text name="label.archive" /></a>
					</#if>
				</td>
			</tr>
		</#list>
	</table>
<#else>
	<div class="emptyList" >
		<p>
		<@s.text name="${emptyMsg}" />
		</p>
	</div>
</#if>