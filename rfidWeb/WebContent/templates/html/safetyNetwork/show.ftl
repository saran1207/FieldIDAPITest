<title><@s.text name="label.safety_network" /></title>
<head>
	<@n4.includeStyle href="safetyNetwork" type="page"/>
</head>
<div id="mainContent">
	<ul class="subSystemList">
		<li>	
			<h2 class="clean"><a href="<@s.url action="connections"/>"><@s.text name="label.manage_connections"/></a></h2>
			<p><@s.text name="label.manage_connections.full"/></p>
		</li>
		<li>	
			<h2 class="clean"><a href="<@s.url action="catalog"/>"><@s.text name="label.manage_catalog"/></a></h2>
			<p><@s.text name="label.manage_catalog.full"/></p>
		</li>
		
		<li>	
			<h2 class="clean"><a href="<@s.url action="messages"/>"><@s.text name="label.inbox"/></a></h2>
			<p><@s.text name="label.inbox.full"/></p>
		</li>
		
		<li>	
			<h2 class="clean"><a href="<@s.url action="privacySettings"/>"><@s.text name="label.privacy_settings"/></a></h2>
			<p><@s.text name="label.privacy_settings.full"/></p>
		</li>
	</ul>
</div>