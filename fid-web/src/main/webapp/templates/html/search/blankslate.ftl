<title><@s.text name="title.assetsearch"/> <@s.text name="title.results"/></title>
<head>
	<@n4.includeStyle href="pageStyles/search"/>
</head>

<div class="initialMessage" >
	<div class="textContainer">
		<h1><@s.text name="label.no_assets" /></h1>
		<p><@s.text name="message.no_assets" /></p>
	</div>
	<div class="identifyActions">
		<input type="button" onClick="location.href='<@s.url action="assetAdd"/>'" value="<@s.text name='label.identify_first_asset' />" />
		<@s.text name="label.or" />
		<a href="<@s.url action="assetMultiAdd"/>"><@s.text name="label.mass_identify_first_asset" /></a>
		<@s.text name="label.or" />
		<a href="<@s.url value="w/setup/import"/>"><@s.text name="label.import_from_excel" /></a>
	</div>
</div>