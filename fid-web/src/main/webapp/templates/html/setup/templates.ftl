${action.setPageType('setup','settings')!}

<title><@s.text name="label.setup" /></title>
<head> 
	<@n4.includeStyle href="settings" type="page"/>
</head>

<div class="setupList">
	<#if sessionUser.hasAccess("managesystemconfig") >
		<div class="setupOption autoAttributeCriteria">
			<h1><a href="<@s.url action="autoAttributeCriteriaList"/>" ><@s.text name="title.auto_attribute_wizard.plural" /></a></h1>
			<p><@s.text name="label.manage_organizational_units_msg" /></p>
		</div>
		
		<div class="setupOption commentTemplate">
			<h1><a href="<@s.url action="commentTemplateList"/>" ><@s.text name="title.manage_comment_templates.plural" /></a></h1>
			<p><@s.text name="label.manage_organizational_units_msg" /></p>
		</div>
		
		<#if securityGuard.integrationEnabled>
			<div class="setupOption assetCodeMapping">
				<h1><a href="<@s.url action="assetCodeMappingList"/>" ><@s.text name="title.manage_asset_code_mappings.plural" /></a></h1>
				<p><@s.text name="label.manage_asset_code_mapping_msg" /></p>
			</div>
		</#if>
		
	</#if>
</div>