<h4>
	<a href="javascript:void(0);" id="obs_open_${sectionIdx}_${criteriaIdx}" onclick="openSection('observations_${sectionIdx}_${criteriaIdx}', 'obs_open_${sectionIdx}_${criteriaIdx}', 'obs_close_${sectionIdx}_${criteriaIdx}');return false"><img src="<@s.url value="/images/expand.gif" includeParams="none"/>" /></a>
	<a href="javascript:void(0);" id="obs_close_${sectionIdx}_${criteriaIdx}" onclick="closeSection('observations_${sectionIdx}_${criteriaIdx}', 'obs_close_${sectionIdx}_${criteriaIdx}', 'obs_open_${sectionIdx}_${criteriaIdx}');return false" style="display:none;"><img src="<@s.url value="/images/collapse.gif" includeParams="none"/>" /></a>				 
	<@s.text name="label.observations"/>
</h4>
<div id="observations_${sectionIdx}_${criteriaIdx}" style="display:none;">
	<div id="recommendations_${sectionIdx}_${criteriaIdx}" >
		<h5>
			<@s.text name="label.recommendations"/>
			<button id="addRecommendation_${sectionIdx}_${criteriaIdx}" onclick="addObservation('RECOMMENDATION', ${sectionIdx}, ${criteriaIdx}); return false;" ><@s.text name="label.add"/></button>
		</h5>
		<#list criteria.recommendations as recommendation >
			<#assign recommendationIdx=recommendation_index/>
			<#include "_recForm.ftl"/>
		</#list>
	</div>
	<div id="deficiencies_${sectionIdx}_${criteriaIdx}">
		<h5>
			<@s.text name="label.deficiencies"/>
			<button id="addDeficiencies_${sectionIdx}_${criteriaIdx}" onclick="addObservation('DEFICIENCY', ${sectionIdx}, ${criteriaIdx}); return false;" ><@s.text name="label.add"/></button>
		</h5>
		<#list criteria.deficiencies as deficiency >
			<#assign deficiencyIdx=deficiency_index/>
			<#include "_defForm.ftl"/>
		</#list>
	</div>
</div>
