<#escape x as x?j_string >
<#assign html>
	<#include "/templates/html/productTypeScheduleCrud/_add.ftl" >
</#assign>

	
	$('eventFrequencyOverrideForm_${inspectionTypeId}').innerHTML = "${html}";
	

</#escape>