${action.setPageType('safety_network_connections', 'list')!}

<head>
	<@n4.includeStyle href="safetyNetwork" type="page"/>
</head>

<ul id="safetyNetworkConnections">
	<#list connections.list as connection>
		<li>
			<div class="connectionLogo">
				<#assign tenant=connection.connectedOrg.tenant/>
				<#include "../common/_displayTenantLogo.ftl"/>
			</div> 
			<div class="connectionName">
			${(connection.connectedOrg.primaryOrg.name?html)!}<br/>
			${(connection.connectedOrg.secondaryOrg.name?html)!}
			</div>
			<div class="typeAndCatalog">
				<span class="connectionType">
					<@s.text name="${connection.connectionType.label}"/>
				</span>
				<br/>
				<span class="catalogLink">
					<#if action.hasAPublishedCatalog(connection.connectedOrg)>
						<a href="<@s.url action="publishedCatalog" uniqueID="${connection.connectedOrg.tenant.id}"/>"><@s.text name="label.view_catalog"/></a>
					</#if>
				</span>
			</div>
		</li>
	</#list>
</ul>