${action.setPageType('event_type_group', 'list')!}
<#if  page.hasResults() && page.validPage() >
	<#assign currentAction="eventTypeGroups.action" />
	<#include '../common/_pagination.ftl' />
	<table class="list">
		<tr>
			<th><@s.text name="label.name" /></th>
			<th><@s.text name="label.reporttitle" /></th>
			<th><@s.text name="label.created" /></th>
			<th><@s.text name="label.last_modified" /></th>		
			<th></th>
		</tr>
		<#list page.getList() as group > 
			<tr id="group_${group.id}" >
				<td><a href="<@s.url action="eventTypeGroup" uniqueID="${group.id}"/>" >${group.name?html}</a></td>
				<td>${(group.reportTitle)!}</td>			
				<td><#if group.createdBy?exists>${group.createdBy.fullName!},&nbsp;</#if>${action.formatDateTime(group.created)}</td>
				<td><#if group.modifiedBy?exists>${group.modifiedBy.fullName!},&nbsp;</#if>${action.formatDateTime(group.modified)}</td>
				
				<td>
					<a id="edit_${group.id}" href="<@s.url action="eventTypeGroupEdit" uniqueID="${group.id}"/>"><@s.text name="label.edit"/></a>
					<#if action.canBeDeleted(group)>
						| <a id="delete_${group.id}" href="<@s.url action="eventTypeGroupDelete" uniqueID="${group.id}"/>"><@s.text name="label.delete"/></a>
					</#if>
				</td>
			</tr>	
			
		</#list>
	</table>
	
	<#include '../common/_pagination.ftl' />
<#elseif !page.hasResults() >
	<div class="emptyList" >
		<h2><@s.text name="label.noresults" /></h2>
		<p>
			<@s.text name="label.emptyeventtypegrouplist" /> <a href="<@s.url action="eventTypeGroupAdd"/>"><@s.text name="label.addfirsteventtypegroup" /></a>
		</p>
	</div>
<#else>
	<div class="emptyList" >
		<h2><@s.text name="label.invalidpage" /></h2>
		<p>
			<@s.text name="message.invalidpage" />
			<a href="<@s.url  action="eventTypeGroups" currentPage="1"/>" ><@s.text name="message.backtopageone"/></a>
		</p>
	</div>
</#if>
	
