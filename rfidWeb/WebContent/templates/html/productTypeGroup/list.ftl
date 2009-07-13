<head>
	<style>
		div.formAction {
			text-align:left;
		}
	</style>
</head>
${action.setPageType('product_type_group', 'list')!}
<#if !groups.empty >
	<div class="formAction">
		<button onclick="redirect('<@s.url action="reorderProductTypeGroups"/>');"><@s.text name="label.reorder"/></button>
	</div>
	
	<table class="list">
		<tr>
			<th><@s.text name="label.name" /></th>
			<th><@s.text name="label.datecreated" /></th>
			<th></th>
		</tr>
		<#list groups as group > 
			<tr id="group_${group.id}" >
				<td><a href="<@s.url action="productTypeGroup" uniqueID="${group.id}"/>" >${group.name?html}</a></td>
				<td>${action.formatDate(group.created, true)}</td>
				<td>
					<a id="edit_${group.id}" href="<@s.url action="productTypeGroupEdit" uniqueID="${group.id}"/>"><@s.text name="label.edit"/></a> 
					<a id="delete_${group.id}" href="<@s.url action="productTypeGroupDeleteConfirm" uniqueID="${group.id}"/>"><@s.text name="label.delete"/></a>
				</td>
			</tr>	
		</#list>
	</table>
	
<#else>
	<div class="emptyList" >
		<h2><@s.text name="label.noresults" /></h2>
		<p>
			<@s.text name="label.emptyproducttypegrouplist" /> <a href="<@s.url action="productTypeGroupAdd"/>"><@s.text name="label.addfirstproducttypegroup" /></a>
		</p>
	</div>
</#if>	
