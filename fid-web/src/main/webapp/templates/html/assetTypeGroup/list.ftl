${action.setPageType('asset_type_group', 'list')!}

<head>
	<@n4.includeStyle href="assetTypeGroupList" type="page"/>
</head>

<#if !groups.empty >
	<div class="formAction">
		<button onclick="redirect('<@s.url action="reorderAssetTypeGroups"/>');"><@s.text name="label.reorder"/></button>
	</div>
	
	<table class="list">
		<tr>
			<th><@s.text name="label.name" /></th>
			<th><@s.text name="label.created" /></th>
			<th><@s.text name="label.last_modified" /></th>		
			<th></th>
		</tr>
		<#list groups as group > 
			<tr id="group_${group.id}" >
				<td><a href="<@s.url action="assetTypeGroup" uniqueID="${group.id}"/>" >${group.name?html}</a></td>
				<td><#if group.createdBy?exists>${group.createdBy.fullName!},&nbsp;</#if>${action.formatDateTime(group.created)}</td>
				<td><#if group.modifiedBy?exists>${group.modifiedBy.fullName!},&nbsp;</#if>${action.formatDateTime(group.modified)}</td>
				<td>
					<a id="edit_${group.id}" href="<@s.url action="assetTypeGroupEdit" uniqueID="${group.id}"/>"><@s.text name="label.edit"/></a>
					<a id="delete_${group.id}" href="<@s.url action="assetTypeGroupDeleteConfirm" uniqueID="${group.id}"/>"><@s.text name="label.delete"/></a>
				</td>
			</tr>	
		</#list>
	</table>
	
<#else>
	<#include "/templates/html/common/_formErrors.ftl" />	
	<div class="initialMessage">
		<div class="textContainer" >
			<h1><@s.text name="label.create_asset_type_group"/></h1>
			<p><@s.text name="label.create_asset_type_group_message" /></p>
		</div>
		<@s.form action="assetTypeGroupCreateFromList" theme="fieldid" cssClass="crudForm bigForm pageSection layout">
			<@s.textfield name="name" required="true"/>
			<@s.submit key="label.create_asset_type_group_now"/>
		</@s.form>
	</div>

	
</#if>	
