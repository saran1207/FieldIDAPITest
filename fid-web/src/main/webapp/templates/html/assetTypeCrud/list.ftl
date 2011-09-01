${action.setPageType('asset_type', 'list')!}

<head>
	<@n4.includeStyle href="listFilter" type="page"/>
</head>

<div class="listFilter quickForm" >
	<div id="listFilterHeader">
		<h2><@s.text name="label.filter"/></h2>
		&nbsp;
		<span class="egColor"><@s.text name="message.filter_asset_types"/></span>
	</div>
	<@s.form id="listFilterForm" method="get">
		<@s.textfield key="label.name" name="nameFilter" id="nameFilter" labelposition="left" />
		<@s.select key="label.asset_type_group" name="groupFilter" id="groupFilter" list="filteringAssetTypeGroups" listKey="id" listValue="name" headerKey="" headerValue="All" labelposition="left"/>
		<div class="formAction filterAction">
			<@s.submit key="hbutton.filter" />
			<span><@s.text name="label.or" /></span>
			<a href="javascript:void(0);" onClick="$('nameFilter').value = '';$('groupFilter').selectedIndex = 0;$('listFilterForm').submit();"> <@s.text name="hbutton.clear"/></a>
		</div>
	</@s.form>
</div>

<#if assetTypes?exists && !assetTypes.isEmpty() >

	<table class="list">
		<tr>
			<th><@s.text name="label.name"/></th>
			<th><@s.text name="label.asset_type_group"/></th>
			<th><@s.text name="label.created"/></th>
			<th><@s.text name="label.last_modified"/></th>		
			<th></th>
		</tr>
		
		<#list assetTypes as assetType>
			<tr>
				<td><a href="<@s.url action="assetType" uniqueID="${assetType.id}" />">${assetType.name?html}</a></td>
				<td><#if assetType.group?exists><a href="<@s.url action="assetTypeGroup" uniqueID="${assetType.group.id}" />">${assetType.group.name!}</a><#else>&nbsp;</#if></td>
				<td><#if assetType.createdBy?exists>${assetType.createdBy.fullName!},&nbsp;</#if>${action.formatDateTime(assetType.created)}</td>
				<td><#if assetType.modifiedBy?exists>${assetType.modifiedBy.fullName!},&nbsp;</#if>${action.formatDateTime(assetType.modified)}</td>
				
				<td>
					<a href="<@s.url action="assetTypeEdit" uniqueID="${assetType.id}" />"><@s.text name="label.edit" /></a> |
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
