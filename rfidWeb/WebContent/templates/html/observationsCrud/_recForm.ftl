<#if (criteriaSections[sectionIdx].criteria[criteriaIdx].recommendations[recommendationIdx])?exists>
	<div id="recommendation_${sectionIdx}_${criteriaIdx}_${recommendationIdx}" >
		<@s.textfield name="criteriaSections[${sectionIdx}].criteria[${criteriaIdx}].recommendations[${recommendationIdx}]" theme="simple" onchange="changeToForm()" size="40"/>
		<a href="javascript:void(0);" onclick="retireRecommendation(${sectionIdx}, ${criteriaIdx}, ${recommendationIdx}); changeToForm(); return false;">
			<img src="<@s.url value="/images/retire.gif" />" />
		</a>
	</div>
</#if>