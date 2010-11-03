${action.setPageType('setup','assetsAndEvents')!}

<title><@s.text name="label.setup" /></title>
<head> 
	<@n4.includeStyle href="settings" type="page"/>
</head>

<div class="setupList">
	<#if sessionUser.hasAccess("managesystemconfig") >
		<div class="setupOption eventTypeGroup">
			<h1><a href="<@s.url action="eventTypeGroups"/>" ><@s.text name="title.manage_event_type_groups.plural"/></a></h1>
			<p><@s.text name="label.manage_event_type_groups_msg" /></p>
		</div>
	
		<div class="setupOption eventTypes">
			<h1><a href="<@s.url action="eventTypes"/>" ><@s.text name="title.manage_event_types.plural"/></a></h1>
			<p><@s.text name="label.manage_event_types_msg" /></p>
		</div>
		
		<div class="setupOption eventBooks">
			<h1><a href="<@s.url action="eventBooks"/>" ><@s.text name="title.manage_event_books.plural"/></a></h1>
			<p><@s.text name="label.manage_event_books_msg" /></p>
		</div>

		<div class="setupOption assetTypeGroups">
			<h1><a href="<@s.url action="assetTypeGroups"/>" ><@s.text name="title.manage_asset_type_groups.plural"/></a></h1>
			<p><@s.text name="label.manage_asset_type_groups_msg" /></p>
		</div>	
		
		<div class="setupOption assetTypes">
			<h1><a href="<@s.url action="assetTypes"/>" ><@s.text name="title.manage_asset_types.plural"/></a></h1>
			<p><@s.text name="label.manage_asset_types_msg" /></p>
		</div>	
		
		<div class="setupOption assetStatusList">
			<h1><a href="<@s.url action="assetStatusList"/>" ><@s.text name="title.manage_asset_statuses.plural"/></a></h1>
			<p><@s.text name="label.manage_asset_status_msg" /></p>
		</div>	
	</#if>
</div>