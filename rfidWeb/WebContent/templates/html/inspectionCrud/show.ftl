<head>
	<script type="text/javascript" src="<@s.url value="/javascript/inspection.js" />" ></script>
	<link rel="stylesheet" type="text/css" href="<@s.url value="/style/pageStyles/inspection.css" />" />
</head>

<#assign form_action="SHOW" /> 
${action.setPageType('inspection', 'show')!}
<div class="crudForm largeForm"> 
	<#include "_show.ftl" />
</div>