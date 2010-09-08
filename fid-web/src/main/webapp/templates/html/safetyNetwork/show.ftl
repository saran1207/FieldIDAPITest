<title><@s.text name="label.safety_network" /></title>
<head>
	<@n4.includeStyle href="safetyNetwork" type="page"/>
	<script type="text/javascript">
		function removeEmptyLi(tagIdToRemove){
	
		}
	</script>
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
		<li id="emptyVendors" class="emptyLi">
			<@s.text name="label.none"/>			
		</li>
		<#list connections.list as connection>
			<#if connection.vendorConnection>
				<script type="text/javascript">removeEmptyLi(emptyVendors);</script>
				<li>
					<#if action.hasAPublishedCatalog(connection.connectedOrg)>
						<a href="<@s.url action="publishedCatalog" uniqueID="${connection.connectedOrg.tenant.id}"/>">	${(connection.connectedOrg.primaryOrg.name?html)!}</a>
					<#else>
							${(connection.connectedOrg.primaryOrg.name?html)!}
					</#if>
				</li>
			</#if>
		</#list>
	</ul>

	<ul id="safetyNetworkCustomerList" class="safetyNetworkCustomerList">
		<@s.text name="label.customers"/>
		<li id="emptyCustomers" class="emptyLi">
			<@s.text name="label.none"/>			
		</li>
		<#list connections.list as connection>
			<#if connection.customerConnection>
				<script type="text/javascript">	$('emptyCustomers').remove();</script>
				<li>
					<#if action.hasAPublishedCatalog(connection.connectedOrg)>
						<a href="<@s.url action="publishedCatalog" uniqueID="${connection.connectedOrg.tenant.id}"/>">	${(connection.connectedOrg.primaryOrg.name?html)!}</a>
					<#else>
							${(connection.connectedOrg.primaryOrg.name?html)!}
					</#if>
				</li>
			</#if>
		</#list>
	</ul>
	
	<ul id="safetyNetworkCatalogOnlyList" class="safetyNetworkCustomerList">
		<@s.text name="label.catalog_only"/>
		<li id="emptyCatalogOnly" class="emptyLi">
			<@s.text name="label.none"/>			
		</li>
		<#list connections.list as connection>
			<#if connection.catalogOnlyConnection>
				<script type="text/javascript">	$('emptyCatalogOnly').remove();</script>
				<li>
					<#if action.hasAPublishedCatalog(connection.connectedOrg)>
						<a href="<@s.url action="publishedCatalog" uniqueID="${connection.connectedOrg.tenant.id}"/>">	${(connection.connectedOrg.primaryOrg.name?html)!}</a>
					<#else>
							${(connection.connectedOrg.primaryOrg.name?html)!}
					</#if>
				</li>
			</#if>
		</#list>
	</ul>
	
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

