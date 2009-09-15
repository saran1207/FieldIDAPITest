<#escape x as x?j_string >
<#assign html>
	<#include "/templates/html/productTypeScheduleCrud/_add.ftl" >
</#assign>

	var containerId  = 'eventFrequencyOverrideForm_${inspectionTypeId}';
	$(containerId).update("${html}");
	attachOrgEvents("#" + containerId);

</#escape>