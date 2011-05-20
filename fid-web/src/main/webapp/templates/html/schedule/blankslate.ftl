<title><@s.text name="title.schedulesearch"/> <@s.text name="title.results"/></title>
<head>
	<@n4.includeStyle href="pageStyles/schedules"/>
</head>
<div class="initialMessage" >
	<div class="textContainer">
		<h1><@s.text name="label.no_schedules" /></h1>
		<p><@s.text name="message.no_schedules" /></p>
	</div>
	<div class="identifyActions">
		<input type="button" onClick="location.href='<@s.url action="assetAdd"/>'" value="<@s.text name='label.identify_an_asset' />" />
	</div>
</div>