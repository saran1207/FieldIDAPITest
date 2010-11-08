${action.setPageType('setup','import')!}

<title><@s.text name="label.setup" /></title>
<head> 
	<@n4.includeStyle href="settings" type="page"/>
	<script type="text/javascript">
		importAssets = '<@s.url action="assetImportExport" />';
		importEvents = '<@s.url action="eventImportExport" />';
		importAutoAttributes = '<@s.url action="autoAttributeImportExport" />'; 		
			
		function redirectImportLink(url, param, id) {
			return redirect(url + param + id);
		}
	</script>
</head>

<div class="setupList">

	<div class="importInfo">
		<div class="setupOption import">
			<h1><@s.text name="label.import_your_data" /></h1>
		</div>
		<p><@s.text name="label.import_your_data_msg1" /></p>
		<p><@s.text name="label.import_your_data_msg2" /></p>	
	</div>

	<fieldset>
		<legend><@s.text name="label.import_owners" /></legend>
		<p><@s.text name="label.import_owners_msg" /></p>
		<@s.url id="importCustomers" action="customerImportExport"/>
		<div class="importAction">
			<input type="submit" value="<@s.text name="label.start_import"/>"onclick="return redirect('${importCustomers}');"/>
		</div>
	</fieldset>
	
	<fieldset>
		<legend><@s.text name="label.import_assets" /></legend>
		<p><@s.text name="label.import_assets_msg" /></p>
		
		<div class="importAction">
			<#if assetTypes.isEmpty()>				
				<a href='<@s.url action="assetTypeEdit" />'><@s.text name="label.import_assets_empty" /></a>
			<#else>
				<@s.select id="assetType" list="assetTypes" listValue="name" listKey="id" />
				<input type="submit" value="<@s.text name="label.start_import" />" onclick="return redirectImportLink(importAssets, '?assetTypeId=', $('assetType').getValue());"/>
			</#if>
		</div>
	</fieldset>
	
	<fieldset>
		<legend><@s.text name="label.import_events" /></legend>
		<p><@s.text name="label.import_events_msg" /></p>

		<div class="importAction">
			<#if eventTypes.isEmpty()>
				<a href='<@s.url action="eventTypeAdd" />'><@s.text name="label.import_events_empty" /></a>
			<#else>
				<@s.select id="eventType" list="eventTypes" listValue="name" listKey="id" />
				<input type="submit" value="<@s.text name="label.start_import" />" onclick="return redirectImportLink(importEvents, '?uniqueID=', $('eventType').getValue());"/>
			</#if>
		</div>
	</fieldset>
	
	<fieldset>
		<legend><@s.text name="label.import_auto_attributes" /></legend>
		<p><@s.text name="label.import_auto_attributes_msg" /></p>

		<div class="importAction">
			<#if attributeTypes.isEmpty()>
				<a href='<@s.url action="autoAttributeCriteriaList" />'><@s.text name="label.import_auto_attributes_empty" /></a>
			<#else>
				<@s.select id="attributeType" list="attributeTypes" listValue="name" listKey="autoAttributeCriteria.id" />
				<input type="submit" value="<@s.text name="label.start_import" />" onclick="return redirectImportLink(importAutoAttributes, '?criteriaId=', $('attributeType').getValue());"/>
			</#if>
		</div>
	</fieldset>

</div>