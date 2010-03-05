<head>
	<@n4.includeScript src="inspection"/>
	<@n4.includeStyle type="page" href="inspection" />
</head>

<div id="pageContent">
	<div class="crudForm">
		<#assign form_action="SHOW" /> 
		<#assign current_action="inspectionInformation"/>
		<#include "/templates/html/inspectionCrud/_show.ftl" >
	</div>
</div>