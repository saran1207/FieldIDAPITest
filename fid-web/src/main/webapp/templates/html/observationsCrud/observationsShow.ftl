<div class="recDefButtons simpleContainer">
	
	<#if (criteriaResult.recommendations.size() > 0) >
		<a id='rec_${criteriaResult.id}_button' href="javascript:void(0);">
			<img src="<@s.url value="/images/rec-plus.png"/>" id='rec_${criteriaResult.id}_img'/>
		</a>
		<script type="text/javascript">
			$("rec_${criteriaResult.id}_button").observe( 'click', function(event) { showObservations('rec_${criteriaResult.id}', event); } );
		</script>
		
		<#assign prefix="rec">
		<#assign observationList=criteriaResult.recommendations>
		<#include "../observationsCrud/_observationShow.ftl"/>
		
	</#if>
	
	<#if (criteriaResult.deficiencies.size() > 0) >
		<a id='def_${criteriaResult.id}_button' href="javascript:void(0);">
			<img src="<@s.url value="/images/def-plus.png"/>" id='def_${criteriaResult.id}_img' />
		</a>
		<script type="text/javascript">
			$("def_${criteriaResult.id}_button").observe( 'click', function(event) { showObservations('def_${criteriaResult.id}', event); } );
		</script>
							
		<#assign prefix="def">
		<#assign observationList=criteriaResult.deficiencies>
		<#include "../observationsCrud/_observationShow.ftl"/>
	</#if>
</div>