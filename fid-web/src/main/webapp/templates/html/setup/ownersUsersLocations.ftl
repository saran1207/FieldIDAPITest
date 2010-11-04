${action.setPageType('setup','ownersUsersLocations')!}

<title><@s.text name="label.setup" /></title>
<head> 
	<@n4.includeStyle href="settings" type="page"/>
</head>

<div class="setupList">
	<#if sessionUser.hasAccess("manageendusers") >
		<div class="setupOption customers">
			<h1><a href="<@s.url action="customerList"/>" ><@s.text name="title.manage_customers.plural" /></a></h1>
			<p>
				<@s.text name='label.manage_customers_msg'>
					<@s.param><@s.text name='label.customers'/></@s.param>
				</@s.text> 
			</p>
		</div>
	</#if>
	
	<#if sessionUser.hasAccess("managesystemusers") >
		<div class="setupOption users">
			<h1><a href="<@s.url action="userList"/>" ><@s.text name="title.manage_users.plural" /></a></h1>
			<p><@s.text name='label.manage_users_msg' /></p>
		</div>
		
		<#if securityGuard.partnerCenterEnabled>
			<div class="setupOption userRegistrations">
				<h1><a href="<@s.url action="userRequestList"/>" ><@s.text name="title.manage_user_registrations.plural" /></a></h1>
				<p>
					<@s.text name='label.manage_user_registrations_msg'>
						<@s.param><@s.text name='label.customer'/></@s.param>
					</@s.text> 
				</p>
			</div>
		</#if>
		
	</#if>
	
	<#if sessionUser.hasAccess("managesystemconfig") >
	
		<#if locationHeirarchyFeatureEnabled>
			<div class="setupOption locations">
				<h1><a href="<@s.url action="predefinedLocations"/>" ><@s.text name="title.manage_predefined_locations.plural" /></a></h1>
				<p><@s.text name='label.manage_predefined_locations'/></p>
			</div>
		</#if>
	
	</#if>
</div>