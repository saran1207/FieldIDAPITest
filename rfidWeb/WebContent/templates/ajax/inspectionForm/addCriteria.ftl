<#escape x as x?j_string >
<#assign criteriaList_index=criteriaIndex/>
<#assign section_index=sectionIndex/>
<#assign html>
	<#include "/templates/html/inspectionFormCrud/_criteriaForm.ftl" >
</#assign>
	$('section_${sectionIndex}').insert( { bottom: "${html}"} );
	openSection('criteria_${sectionIndex}_${criteriaList_index}', 'criteria_open_${sectionIndex}_${criteriaList_index}', 'criteria_close_${sectionIndex}_${criteriaList_index}');
</#escape>