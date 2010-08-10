<#escape x as x?j_string >
<#assign html>
	<#include "/templates/html/productTypeScheduleCrud/_ajaxOverride.ftl" >
</#assign>

	var containerId  = 'orgHolder';
	$(containerId).update("${html}");
	attachOrgEvents("#" + containerId);

</#escape>