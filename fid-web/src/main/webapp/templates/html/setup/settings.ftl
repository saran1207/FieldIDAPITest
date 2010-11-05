${action.setPageType('setup','settings')!}

<title><@s.text name="label.setup" /></title>
<head> 
	<@n4.includeStyle href="settings" type="page"/>
</head>

<div class="setupList">
	<#if sessionUser.hasAccess("managesystemconfig")>
		<div class="setupOption organizations">
			<h1><a href="<@s.url action="organizations"/>" ><@s.text name="title.manage_organizational_units.plural" /></a></h1>
			<p><@s.text name="label.manage_organizational_units_msg" /></p>
		</div>
	
		<div class="setupOption systemConfig">
			<h1><a href="<@s.url action="systemSettingsEdit"/>" ><@s.text name="title.manage_system_settings.plural" /></a></h1>
			<p><@s.text name="label.manage_system_settings_msg" /></p>
		</div>
	
		<#if securityGuard.brandingEnabled>
			<div class="setupOption branding">
				<h1><a href="<@s.url action="branding"/>" ><@s.text name="title.manage_branding.plural" /></a></h1>
				<p><@s.text name="label.manage_branding_msg" /></p>
			</div>
		</#if>
	
		<div class="setupOption fieldIdPlan">
			<h1><a href="<@s.url action="yourPlan"/>" ><@s.text name="title.manage_field_id_plan.plural" /></a></h1>
			<p><@s.text name="label.manage_field_id_plan_msg" /></p>
		</div>
	</#if>		
</div>