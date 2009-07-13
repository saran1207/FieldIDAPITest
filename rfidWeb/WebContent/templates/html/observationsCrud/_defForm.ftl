<#if (criteriaSections[sectionIdx].criteria[criteriaIdx].deficiencies[deficiencyIdx])?exists>
	<div id="deficiency_${sectionIdx}_${criteriaIdx}_${deficiencyIdx}" >
		<@s.textfield name="criteriaSections[${sectionIdx}].criteria[${criteriaIdx}].deficiencies[${deficiencyIdx}]" theme="simple" onchange="changeToForm()" size="40"/>
		<a href="javascript:void(0);" onclick="retireDeficiency(${sectionIdx}, ${criteriaIdx}, ${deficiencyIdx}); changeToForm(); return false;">
			<img src="<@s.url value="/images/retire.gif" />" />
		</a>
	</div>
</#if>