<div class="recDefButtons">
	
	<#if (criteriaResult.recommendations.size() > 0) >
		<a id='rec_${criteriaResult.id}_button' href="javascript:void(0);"><img src="<@s.url value="/images/rec-icon.png"/>" id='rec_${criteriaResult.id}_img'/></a>
		<script type="text/javascript">
			$("rec_${criteriaResult.id}_button").observe( 'click', function(event) { showObservations('rec_${criteriaResult.id}', event); } );
		</script>
		<#assign prefix="rec">
		<#assign observationList=criteriaResult.recommendations>
		<#include "../observationsCrud/_observationShow.ftl"/>
		
	</#if>

	<#if (criteriaResult.deficiencies.size() > 0) >
		<a id='def_${criteriaResult.id}_button' href="javascript:void(0);"><img src="<@s.url value="/images/def-icon.png"/>" id='def_${criteriaResult.id}_img' /></a>
		<script type="text/javascript">
			$("def_${criteriaResult.id}_button").observe( 'click', function(event) { showObservations('def_${criteriaResult.id}', event); } );
		</script>
							
		<#assign prefix="def">
		<#assign observationList=criteriaResult.deficiencies>
		<#include "../observationsCrud/_observationShow.ftl"/>
	</#if>

    <#if (criteriaResult.criteriaImages.size() > 0) >
        <a id='img_${criteriaResult.id}_button' href='javascript:void(0);'>
            <img src="<@s.url value="/images/camera-icon.png"/>" id='img_${criteriaResult.id}_img' class='criteriaImageLightbox'/>
        </a>

        <script type="text/javascript">

            jQuery(document).ready(function(){
                jQuery('.criteriaImageLightbox').colorbox({iframe: true, href: '<@s.url value="w/criteriaImageList?uniqueID=${criteriaResult.id}" />', width: '620px', height:'700px'});
            });

        </script>
    </#if>

    <#if (criteriaResult.actions.size() > 0) >
        <a id='img_${criteriaResult.id}_button' href='javascript:void(0);'>
            <img src="<@s.url  value="/images/action-icon.png"/>" id='img_${criteriaResult.id}_img' class='criteriaImageLightbox'/>
        </a>

        <script type="text/javascript">

            jQuery(document).ready(function(){
                jQuery('.criteriaImageLightbox').colorbox({iframe: true, href: '<@s.url escapeAmp="false" value="w/viewActionsList?uniqueID=${criteriaResult.id}&readOnly"/>', width: '350px', height:'400px'});
            });

        </script>
    </#if>


</div>