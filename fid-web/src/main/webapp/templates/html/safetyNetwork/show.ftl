<title><@s.text name="label.safety_network" /></title>
<head>
	<@n4.includeStyle href="safetyNetwork" type="page"/>
</head>

<div id="leftConnectionsColumn">
	<ul>
		<li id="inbox">
			<a href="<@s.url action="messages"/>"><@s.text name="label.inbox"/> (${unreadMessageCount})</a>
		</li>
		<li id="manage_catalog">
			<a href="<@s.url action="catalog"/>"><@s.text name="label.your_catalog"/></a>
		</li>
		<li id="privacy_settings">
			<a href="<@s.url action="privacySettings"/>"><@s.text name="label.privacy_settings"/></a>
		</li>
	</ul>
	
	<p id="yourConnections">
		<@s.text name="label.your_connections"/>
	</p>
	<#list connections.list as connection>
		<ul id="safetyNetworkCustomerList" class="safetyNetworkCustomerList">
			<@s.text name="label.customers"/>
				<#if connection.customerConnection>
					<li>
						<#if action.hasAPublishedCatalog(connection.connectedOrg)>
							<a href="<@s.url action="publishedCatalog" uniqueID="${connection.connectedOrg.tenant.id}"/>">	${(connection.connectedOrg.primaryOrg.name?html)!}</a>
						<#else>
								${(connection.connectedOrg.primaryOrg.name?html)!}
						</#if>
					</li>
				</#if>
		</ul>
		<ul id="safetyNetworkVendorList" class="safetyNetworkCustomerList">
			<@s.text name="label.vendors"/>
			<#if connection.vendorConnection>
				<li>
					<#if action.hasAPublishedCatalog(connection.connectedOrg)>
						<a href="<@s.url action="publishedCatalog" uniqueID="${connection.connectedOrg.tenant.id}"/>">	${(connection.connectedOrg.primaryOrg.name?html)!}</a>
					<#else>
							${(connection.connectedOrg.primaryOrg.name?html)!}
					</#if>
				</li>
			</#if>
		</ul>
		<ul id="safetyNetworkCatalogOnlyList" class="safetyNetworkCustomerList">
			<@s.text name="label.catalog_only"/>
			<#if connection.catalogOnlyConnection>
				<li>
					<#if action.hasAPublishedCatalog(connection.connectedOrg)>
						<a href="<@s.url action="publishedCatalog" uniqueID="${connection.connectedOrg.tenant.id}"/>">	${(connection.connectedOrg.primaryOrg.name?html)!}</a>
					<#else>
							${(connection.connectedOrg.primaryOrg.name?html)!}
					</#if>
				</li>
			</#if>
		</ul>
	</#list>
</div>

<div id="safetyNetworkSplash">
	<h1><@s.text name="label.safety_network"/></h1>
	<p id="safetyNetworkDescription">
		<@s.text name="label.safety_network_info.2"/>
	</p>
	
	<p class="videoLinks">
		<a id="help_link" href="${helpUrl}" target="_blank"><@s.text name="label.safety_network_info.help"/></a>
		<a id="video_link" href="${videoUrl}" target="_blank"><@s.text name="label.safety_network_info.video"/></a>
	</p>
	<@s.form action="findConnections" theme="fieldid" cssClass="fullForm">
		<p>
			<span class="splashHeading"><@s.text name="label.find_a_company"/></span><br/>
			<@s.textfield id="companySearchBox"  name="searchText" cssClass="inputAlign"/>
			<@s.submit id="searchForCompanyButton"  key="hbutton.search" cssClass="saveButton save inputAlign"/>
		</p>
	</@s.form>
	<p id="inviteCompany">
		<span class="safetyHeading"><a href="<@s.url action="invite"/>"><@s.text name="label.invite_a_company"/></a></span>
		<a href="<@s.url action="invite"/>"><@s.text name="label.invite_a_company.full"/></a>
	</p>
</div>

