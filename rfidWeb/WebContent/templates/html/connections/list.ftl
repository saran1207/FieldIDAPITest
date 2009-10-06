${action.setPageType('safety_network_connections', 'list')!}


<h2 class="clean"><@s.text name="label.connections"/></h2>

<ul id="safetyNetwork">
	<#list connections.list as connection>
		<li>
			${(connection.connectedOrg.primaryOrg.name?html)!}
			${(connection.connectedOrg.secondaryOrg.name?html)!}
			
			<@s.text name="${connection.connectionType.label}"/>
			
			<#if action.hasAPublishedCatalog(connection.connectedOrg)>
				<a href="<@s.url action="publishedCatalog" uniqueID="${connection.connectedOrg.tenant.id}"/>"><@s.text name="view_catalog"/></a>
			</#if>
		</li>
	</#list>
</ul>