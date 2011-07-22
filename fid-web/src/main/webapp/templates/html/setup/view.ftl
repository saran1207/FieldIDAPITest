${action.setPageType('setup','setup')!}

<title><@s.text name="label.setup" /></title>
<head>
	<@n4.includeStyle type="page" href="setup" />
 </head>  
   <div class="viewSection setup">
		<h2 id="systemAccess"><@s.text name="label.systemaccessandsetup" /></h2>
		
		<#if sessionUser.hasAccess("managesystemconfig") == true >
			<p>
				<label><a href="<@s.url action="organizations"/>" ><@s.text name="title.manage_organizational_units.plural" /></a></label>
				<span >Create and manage departments and divisions within your company.</span>
			</p>
		</#if>
		<#if sessionUser.hasAccess("manageendusers") == true >
			<p>
				<label><a href="<@s.url action="customerList"/>" ><@s.text name="title.manage_customers.plural" /></a></label>
				<span >View and setup all of your <@s.text name='label.customers'/> in Field ID.</span>
			</p>
		</#if>
		
			
		<#if sessionUser.hasAccess("managesystemusers") == true >
			<p>
				<label><a href="<@s.url action="userList"/>" ><@s.text name="title.manage_users.plural" /></a></label>
				<span >View and setup all of your users in Field ID.</span>
			</p>
			<#if userLimitService.readOnlyUsersEnabled>
				<p>
					<label><a href="<@s.url action="userRequestList"/>" ><@s.text name="title.manage_user_registrations.plural" /></a></label>
					<span >Accept or deny requests for <@s.text name='label.customer'/> user accounts.</span>
				</p>
			</#if>
		</#if>
		
		<#if sessionUser.hasAccess("managesystemconfig") == true >
			<p>
				<label><a href="<@s.url action="systemSettingsEdit"/>" ><@s.text name="title.manage_system_settings.plural" /></a></label>
				<span>Find information about your system setup and adjust your company's branding.</span>
			</p>
		</#if>	
		
	</div>
	
	<#if sessionUser.hasAccess("managesystemconfig") == true >
		<div class="viewSection setup">
			<h2 id="yourAssetSetup"><@s.text name="label.yourassetsetup" /></h2>
			<p>
				<label><a href="<@s.url action="eventTypeGroups"/>" ><@s.text name="title.manage_event_type_groups.plural"/></a></label>
				<span >Create and manage event type groups to allow you to group event types and pick the certificate for those event types.</span>
			</p>
			
			<p>
				<label><a href="<@s.url action="eventTypes"/>" ><@s.text name="title.manage_event_types.plural" /></a></label>
				<span>Create event types to allow you to do different types of events.</span>
			</p>
		
			<p>
				<label><a href="<@s.url action="assetTypeGroups"/>" ><@s.text name="title.manage_asset_type_groups.plural" /></a></label>
				<span>Manage all of your asset type groups.</span>
			</p>
			<p>
				<label><a href="<@s.url action="assetTypes"/>" ><@s.text name="title.manage_asset_types.plural" /></a></label>
				<span>Manage all of your assets and their attributes.</span>
			</p>
				
			<p>
				<label><a href="<@s.url action="assetStatusList"/>" ><@s.text name="title.manage_asset_statuses.plural" /></a></label>
				<span>Create asset statuses that let you identify the state of an asset.</span>
			</p>
			
			<p>
				<label><a href="<@s.url action="eventBooks"/>" ><@s.text name="title.manage_event_books.plural" /></a></label>
				<span>Create event books to organize your events.</span>
			</p>
			<#if locationHeirarchyFeatureEnabled>
				<p>
					<label><a href="<@s.url action="predefinedLocations"/>" ><@s.text name="title.manage_predefined_locations.plural" /></a></label>
					<span>Manage your location hierarchy.</span>
				</p>
			</#if>
		
		</div>
	</#if>
	
	<#if sessionUser.hasAccess("managesystemconfig") == true >
		<div class="viewSection setup">
			<h2 id="notificationAndTemplates"><@s.text name="label.autocomplete_templates" /></h2>
			<p>
				<label><a href="<@s.url action="autoAttributeCriteriaList"/>" ><@s.text name="title.auto_attribute_wizard.plural" /></a></label>
				<span>Easily map variables to pre-defined values for assets.</span>
			</p>
			<p>
				<label><a href="<@s.url action="commentTemplateList"/>" ><@s.text name="title.manage_comment_templates.plural" /></a></label>
				<span>Speed up the entry of comments by creating frequently used templates.</span>
			</p>
			<#if securityGuard.integrationEnabled>
				<p>
					<label><a href="<@s.url action="assetCodeMappingList"/>" ><@s.text name="title.manage_asset_code_mappings.plural" /></a></label>
					<span>Manage and map asset information from your accounting or ERP system into Field ID.</span>
				</p>
			</#if>
		</div>
	</#if>
	
	<#if sessionUser.hasAccess("managesystemconfig") == true >
		<div class="viewSection setup">
			<h2 id="dataSync"><@s.text name="label.datasync" /></h2>
			<p>
				<label><a href="<@s.url action="dataLog"/>" class="blue"><@s.text name="title.data_log" /></a></label>
				<span>
					Information about data from FieldID Mobile and proof test imports.
				</span>
			</p>
		</div>
			
	</#if>
	
	
