<#escape x as x?js_string >
<#assign infoField_index=fieldIndex/>
<#assign html>
	<#include "/templates/html/inspectionType/_inspectionAttributeForm.ftl" >
</#assign>
	$('infoFields').insert( { bottom: "${html}"} );
</#escape>