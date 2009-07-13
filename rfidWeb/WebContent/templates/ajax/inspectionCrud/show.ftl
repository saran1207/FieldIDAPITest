<head>
	<script type="text/javascript" src="<@s.url value="/javascript/inspection.js" />" ></script>
	<link rel="stylesheet" type="text/css" href="<@s.url value="/style/pageStyles/inspection.css" />" />
</head>

<div id="pageContent">
	<div class="crudForm">
		<#assign form_action="SHOW" /> 
		<#assign current_action="inspectionInformation"/>
		<#include "/templates/html/inspectionCrud/_show.ftl" >
	</div>
</div>