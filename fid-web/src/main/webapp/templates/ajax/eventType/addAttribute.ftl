<#escape x as x?js_string >
<#assign infoField_index=fieldIndex/>
<#assign html>
	<#include "/templates/html/eventType/_eventAttributeForm.ftl" >
</#assign>
	$('infoFields').insert( { bottom: "${html}"} );
</#escape>