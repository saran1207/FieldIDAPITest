${action.setPageType('safety_network_connections', 'list')!}

<head>
	<@n4.includeStyle href="safetyNetwork" type="page"/>
</head>


<table class="list" id="safetyNetworkConnections">
	<tr>
		<th></th>
		<th><@s.text name="label.company_name"/></th>
		<th><@s.text name="label.connection_type"/></th>
		<th><@s.text name="label.catalog"/></th>
		
	</tr>
	
	<#list connections.list as connection>
		<tr>
			<td>
				<#assign tenant=connection.connectedOrg.tenant/>
				<#include "../common/_displayTenantLogo.ftl"/>
			</td>
			<td>
			${(connection.connectedOrg.primaryOrg.name?html)!}<br/>
			${(connection.connectedOrg.secondaryOrg.name?html)!}
			</td>
			<td><@s.text name="${connection.connectionType.label}"/></td>
			<td>
				<#if action.hasAPublishedCatalog(connection.connectedOrg)>
					<a href="<@s.url action="publishedCatalog" uniqueID="${connection.connectedOrg.tenant.id}"/>"><@s.text name="label.view_catalog"/></a>
				<#else>
					<@s.text name="label.not_published"/>
				</#if>
			</td>
		</tr>
	</#list>
</table>
