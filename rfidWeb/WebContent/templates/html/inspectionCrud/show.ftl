<head>
	<script type="text/javascript" src="<@s.url value="/javascript/inspection.js" />" ></script>
	<@n4.includeStyle href="pageStyles/inspection" />
</head>

<#assign form_action="SHOW" /> 
${action.setPageType('inspection', 'show')!}
<div class="crudForm largeForm"> 
	<#include "_show.ftl" />
</div>