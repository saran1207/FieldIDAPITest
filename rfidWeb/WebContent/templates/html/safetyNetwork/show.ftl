<title><@s.text name="label.safety_network" /></title>
<head>
	<@n4.includeStyle href="safetyNetwork" type="page"/>
</head>
<div id="mainContent">
	<ul class="subSystemList">
		<li id="manage_connections">
			<h2 class="clean"><a href="<@s.url action="connections"/>"><@s.text name="label.manage_connections"/></a></h2>
			<p><a href="<@s.url action="connections"/>"><@s.text name="label.manage_connections.full"/></a></p>
		</li>
		
		<li id="inbox">
			<h2 class="clean"><a href="<@s.url action="messages"/>"><@s.text name="label.inbox"/> (${unreadMessageCount})</a></h2>
			<p><a href="<@s.url action="messages"/>"><@s.text name="label.inbox.full"/></a></p>
		</li>
		
		<li id="manage_catalog">
			<h2 class="clean"><a href="<@s.url action="catalog"/>"><@s.text name="label.manage_catalog"/></a></h2>
			<p><a href="<@s.url action="catalog"/>"><@s.text name="label.manage_catalog.full"/></a></p>
		</li>
		
		<li id="privacy_settings">
			<h2 class="clean"><a href="<@s.url action="privacySettings"/>"><@s.text name="label.privacy_settings"/></a></h2>
			<p><a href="<@s.url action="privacySettings"/>"><@s.text name="label.privacy_settings.full"/></a></p>
		</li>
	</ul>
	<div id="sideBox">
		<h2><@s.text name="label.safety_network_info.title"/></h2>
		<p><@s.text name="label.safety_network_info.1"/></p>
		<p><@s.text name="label.safety_network_info.2"/></p>
		<a id="help_link" href="${helpUrl}" target="_blank"><@s.text name="label.safety_network_info.help"/></a>
		<a id="video_link" href="${videoUrl}" target="_blank"><@s.text name="label.safety_network_info.video"/></a>
	</div>
</div>
