<head>
	<script type="text/javascript" src="<@s.url value="/javascript/event.js" />" ></script>
	<@n4.includeStyle type="page" href="event" />
</head>

<#assign form_action="SHOW" /> 
${action.setPageType('event', 'show')!}
<div class="crudForm largeForm"> 
	<#include "_show.ftl" />
</div>