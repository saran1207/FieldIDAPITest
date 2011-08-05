<head>
	<script type="text/javascript" src="http://maps.googleapis.com/maps/api/js?sensor=false"></script>
		 
	<@n4.includeScript src="event"/>
	<@n4.includeScript src="googleMaps.js"/>
	<@n4.includeStyle type="page" href="event" />
	<@n4.includeStyle type="page" href="event" />	
	
</head>

<div id="pageContent">
	<div class="crudForm eventInIframe">
		<#assign form_action="SHOW"/>
		<#assign inside_iframe=true/>
		<#assign current_action="eventInformation"/>
		<#include "/templates/html/eventCrud/_show.ftl">
	</div>
</div>