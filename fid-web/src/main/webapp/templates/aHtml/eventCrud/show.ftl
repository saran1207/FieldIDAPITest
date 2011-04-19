<head>
	<@n4.includeScript src="event"/>
	<@n4.includeStyle type="page" href="event" />
</head>

<div id="pageContent">
	<div class="crudForm eventInIframe">
		<#assign form_action="SHOW" />
		<#assign inside_iframe=true />
		<#assign current_action="eventInformation"/>
		<#include "/templates/html/eventCrud/_show.ftl" >
	</div>
</div>