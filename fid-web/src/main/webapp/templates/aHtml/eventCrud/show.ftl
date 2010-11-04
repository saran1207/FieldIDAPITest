<head>
	<@n4.includeScript src="event"/>
	<@n4.includeStyle type="page" href="event" />
</head>

<div id="pageContent">
	<div class="crudForm">
		<#assign form_action="SHOW" /> 
		<#assign current_action="eventInformation"/>
		<#include "/templates/html/eventCrud/_show.ftl" >
	</div>
</div>