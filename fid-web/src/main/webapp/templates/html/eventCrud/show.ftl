<head>
	<script type="text/javascript" src="<@s.url value="/javascript/event.js" />" ></script>
	<@n4.includeStyle href="newCss/event/event_base" />
    <@n4.includeStyle href="newCss/event/event_show" />
    <@n4.includeStyle href="newCss/layout/feedback_errors" />
	<script type="text/javascript" src="https://maps.googleapis.com/maps/api/js?sensor=false"></script>
	<@n4.includeScript src="googleMaps.js"/>	
</head>

<#assign form_action="SHOW" /> 
<#include "_showHeader.ftl" />

<div class="eventView">
	<#include "_show.ftl" />
</div>