${action.setPageType('setup','templates')!}

<title><@s.text name="label.setup" /></title>
<head> 
	<@n4.includeStyle href="settings" type="page"/>
</head>

<div class="setupList">
	<#if sessionUser.hasAccess("managesystemconfig") >
		<div class="setupOption autoAttributeCriteria">
			<h1><a href="<@s.url action="autoAttributeCriteriaList"/>" ><@s.text name="title.auto_attribute_wizard.plural" /></a></h1>
			<p><@s.text name="label.manage_auto_attirbute_criteria_msg" /></p>
		</div>
		
		<div class="setupOption commentTemplate">
			<h1><a href="<@s.url action="commentTemplateList"/>" ><@s.text name="title.manage_comment_templates.plural" /></a></h1>
			<p><@s.text name="label.manage_comment_template_msg" /></p>
		</div>

		<div class="setupOption assetColumnsLayout">
			<h1><a href="<@s.url action="editAssetColumnLayout"/>" ><@s.text name="title.column_layout_asset" /></a></h1>
			<p><@s.text name="label.column_layout_asset" /></p>
		</div>

		<div class="setupOption eventColumnsLayout">
			<h1><a href="<@s.url action="editEventColumnLayout"/>" ><@s.text name="title.column_layout_event" /></a></h1>
			<p><@s.text name="label.column_layout_event" /></p>
		</div>

		<div class="setupOption scheduleColumnsLayout">
			<h1><a href="<@s.url action="editScheduleColumnLayout"/>" ><@s.text name="title.column_layout_schedule" /></a></h1>
			<p><@s.text name="label.column_layout_schedule" /></p>
		</div>
		
		<#if securityGuard.integrationEnabled>
			<div class="setupOption assetCodeMapping">
				<h1><a href="<@s.url action="assetCodeMappingList"/>" ><@s.text name="title.manage_asset_code_mappings.plural" /></a></h1>
				<p><@s.text name="label.manage_asset_code_mapping_msg" /></p>
			</div>
		</#if>
		
	</#if>
</div>