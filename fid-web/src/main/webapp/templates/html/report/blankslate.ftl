<title><@s.text name="title.report"/> <@s.text name="title.results"/>  
	<#if container.fromSavedReport>
		<@s.text name="label.for"/> - ${savedReportName?html} 
		<#if savedReportModified>
			 (<@s.text name="label.modified"/>)
		</#if>
	</#if>
	 
</title>
<head>
	<@n4.includeStyle href="pageStyles/reporting"/>
</head>

<div class="initialMessage" >
	<div class="textContainer">
		<h1><@s.text name="label.no_events" /></h1>
		<p><@s.text name="message.no_events" /></p>
	</div>
	<div class="eventActions">
		<input type="button" onClick="location.href='<@s.url action="startEvent"/>'" value="<@s.text name='label.perform_first_event' />" />
		<@s.text name="label.or" />
		<a href="<@s.url action="assetSelection"/>"><@s.text name="label.mass_perform_first_event" /></a>
		<@s.text name="label.or" />
		<a href="<@s.url value="w/setup/import"/>"><@s.text name="label.import_from_excel" /></a>
	</div>
</div>