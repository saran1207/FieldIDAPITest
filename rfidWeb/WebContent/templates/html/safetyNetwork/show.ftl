<title><@s.text name="label.safety_network" /></title>
<head>
	<@n4.includeStyle href="safetyNetwork" type="page"/>
</head>
<div id="mainContent">
	<ul class="subSystemList">
		<a href="<@s.url action="connections"/>">
			<li id="manage_connections">
				<h2 class="clean"><@s.text name="label.manage_connections"/></h2>
				<p><@s.text name="label.manage_connections.full"/></p>
			</li>
		</a>
		<a href="<@s.url action="messages"/>">
			<li id="inbox">
				<h2 class="clean"><@s.text name="label.inbox"/></h2>
				<p><@s.text name="label.inbox.full"/></p>
			</li>
		</a>
		<a href="<@s.url action="catalog"/>">
			<li id="manage_catalog">
				<h2 class="clean"><@s.text name="label.manage_catalog"/></h2>
				<p><@s.text name="label.manage_catalog.full"/></p>
			</li>
		</a>
		<a href="<@s.url action="privacySettings"/>">
			<li id="privacy_settings">
				<h2 class="clean"><@s.text name="label.privacy_settings"/></h2>
				<p><@s.text name="label.privacy_settings.full"/></p>
			</li>
		</a>
	</ul>
	<div id="sideBox">
		<h2><@s.text name="label.safety_network_info.title"/></h2>
		<p><@s.text name="label.safety_network_info.1"/></p>
		<p><@s.text name="label.safety_network_info.2"/></p>
		<a id="help_link" href="${helpUrl}" target="_blank"><@s.text name="label.safety_network_info.help"/></a>
		<a id="video_link" href="${videoUrl}" target="_blank"><@s.text name="label.safety_network_info.video"/></a>
	</div>
</div>
