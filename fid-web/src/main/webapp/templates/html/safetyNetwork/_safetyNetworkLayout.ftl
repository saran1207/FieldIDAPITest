<title><@s.text name="label.safety_network" /></title>
<head>
	<@n4.includeStyle href="safetyNetwork" type="page"/>
</head>

<div id="leftConnectionsColumn">
	<ul>
		<li>
			<a id="inbox" href="<@s.url action="messages"/>"><@s.text name="label.inbox"/> (${unreadMessageCount})</a>
		</li>
		<li>
			<a id="manageCatalog" href="<@s.url action="catalog"/>"><@s.text name="label.your_catalog"/></a>
		</li>
		<li >
			<a id="privacySettings" href="<@s.url action="privacySettings"/>"><@s.text name="label.privacy_settings"/></a>
		</li>
	</ul>

	<p id="yourConnections">
		<@s.text name="label.your_connections"/>
	</p>

	<ul id="safetyNetworkVendorList" class="safetyNetworkCustomerList">
		<@s.text name="label.vendors"/>
		<#if !vendorConnections?has_content>
			<li class="emptyLi">
				<@s.text name="label.none"/>
			</li>
		<#else>
			<#list vendorConnections as connection>
				<li>
					<#if action.hasAPublishedCatalog(connection.connectedOrg)>
						<a href="<@s.url action="showVendor.action" uniqueID="${connection.connectedOrg.tenant.id}"/>">	${(connection.connectedOrg.primaryOrg.name?html)!}</a>
					<#else>
						<a href="#">${(connection.connectedOrg.primaryOrg.name?html)!}</a>
					</#if>
				</li>
			</#list>
		</#if>
	</ul>

	<ul id="safetyNetworkCustomerList" class="safetyNetworkCustomerList">
		<@s.text name="label.safety_network_customers"/>
		<#if !customerConnections?has_content>
			<li class="emptyLi">
				<@s.text name="label.none"/>
			</li>
		<#else>
			<#list customerConnections as connection>
				<li>
					<#if action.hasAPublishedCatalog(connection.connectedOrg)>
						<a href="<@s.url action="publishedCatalog" uniqueID="${connection.connectedOrg.tenant.id}"/>">	${(connection.connectedOrg.primaryOrg.name?html)!}</a>
					<#else>
						<a href="#">${(connection.connectedOrg.primaryOrg.name?html)!}</a>
					</#if>
				</li>
			</#list>
		</#if>
	</ul>

	<ul id="safetyNetworkCatalogOnlyList" class="safetyNetworkCustomerList">
		<@s.text name="label.catalog_only"/>
		<#if !catalogOnlyConnections?has_content>
			<li id="emptyCatalogOnly" class="emptyLi">
				<@s.text name="label.none"/>
			</li>
		<#else>
			<#list catalogOnlyConnections as connection>
				<#if connection.catalogOnlyConnection>
					<li>
						<#if action.hasAPublishedCatalog(connection.connectedOrg)>
							<a href="<@s.url action="publishedCatalog" uniqueID="${connection.connectedOrg.tenant.id}"/>">	${(connection.connectedOrg.primaryOrg.name?html)!}</a>
						<#else>
							<a href="#">${(connection.connectedOrg.primaryOrg.name?html)!}</a>
						</#if>
					</li>
				</#if>
			</#list>
		</#if>
	</ul>
</div>