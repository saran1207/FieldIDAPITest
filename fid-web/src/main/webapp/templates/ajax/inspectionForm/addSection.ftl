<#escape x as x?j_string >
<#assign section_index=sectionIndex/>
<#assign html>
	<#include "/templates/html/inspectionFormCrud/_sectionForm.ftl" >
</#assign>
	$('sectionContainer').insert( { bottom: "${html}"} );
</#escape>