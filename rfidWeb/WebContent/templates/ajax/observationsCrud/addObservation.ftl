<#escape x as x?j_string >

	<#assign sectionIdx=sectionIndex />
	<#assign criteriaIdx=criteriaIndex />
	
	<#if observationType == "RECOMMENDATION" >
	
		<#assign recommendationIdx=observationIndex />
		<#assign divPrefix="recommendations" />
		<#assign html>
			<#include "/templates/html/observationsCrud/_recForm.ftl" >
		</#assign>
	
	<#else>
	
		<#assign deficiencyIdx=observationIndex />
		<#assign divPrefix="deficiencies" />
		<#assign html>
			<#include "/templates/html/observationsCrud/_defForm.ftl" >
		</#assign>
		
	</#if>

	$('${divPrefix}_${sectionIdx}_${criteriaIdx}').insert( { bottom: "${html}"} );
	
</#escape>