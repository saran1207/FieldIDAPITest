<title><@s.text name="label.safety_network" /></title>
<head>
	<@n4.includeStyle href="safetyNetwork" type="page"/>
</head>

<div id="leftConnectionsColumn" class="borderRight linkDecoration">
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
	
	<div id="yourConnections">
		<h3>
			<@s.text name="label.your_connections"/>
		</h3>
	</div>
	
	<ul id="safetyNetworkCustomerList">
		<@s.text name="label.customers"/>
	</ul>
	<ul id="safetyNetworkVendorList">
		<@s.text name="label.vendors"/>
	</ul>
</div>

<div id="safetyNetworkSplash">
	<h1><@s.text name="label.safety_network"/></h1>
	<p>
		<@s.text name="label.safety_network_info.2"/>
	</p>
	
	<p class="videoLinks">
		<a id="help_link" href="${helpUrl}" target="_blank"><@s.text name="label.safety_network_info.help"/></a>
		<a id="video_link" href="${videoUrl}" target="_blank"><@s.text name="label.safety_network_info.video"/></a>
	</p>
	<@s.form action="findConnections" theme="fieldid" cssClass="fullForm">
		<p>
			<@s.text name="label.find_a_company"/>
			<@s.textfield id="snSmartSearchText" name="searchText"/>
		</p>
	</@s.form>
	<p id="inviteCompany">
		<@s.text name="label.invite_a_company"/>
		<br/>
		<a><@s.text name="label.invite_a_company.full"/></a>		
	</p>
</div>

