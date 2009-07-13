
<#if !criteria.recommendations.isEmpty() >
<div id="recommendations" >
	<h2>
		<a href="javascript:void(0);" id="rec_open_${sectionIdx}_${criteriaIdx}" onclick="openSection('rec_${sectionIdx}_${criteriaIdx}', 'rec_open_${sectionIdx}_${criteriaIdx}', 'rec_close_${sectionIdx}_${criteriaIdx}');return false"><img src="<@s.url value="/images/expand.gif" includeParams="none"/>" /></a>
		<a href="javascript:void(0);" id="rec_close_${sectionIdx}_${criteriaIdx}" onclick="closeSection('rec_${sectionIdx}_${criteriaIdx}', 'rec_close_${sectionIdx}_${criteriaIdx}', 'rec_open_${sectionIdx}_${criteriaIdx}');return false" style="display:none;"><img src="<@s.url value="/images/collapse.gif" includeParams="none"/>" /></a>				 
		<@s.text name="label.recommendations"/>
	</h2>
	<div id="rec_${sectionIdx}_${criteriaIdx}" style="display:none;">
		<#list criteria.recommendations as recommendation >
			<p>${recommendation}</p>
		</#list>
	</div>
</div>
</#if>
<#if !criteria.deficiencies.isEmpty() >
<div id="deficiencies">
	<h2>
		<a href="javascript:void(0);" id="def_open_${sectionIdx}_${criteriaIdx}" onclick="openSection('def_${sectionIdx}_${criteriaIdx}', 'def_open_${sectionIdx}_${criteriaIdx}', 'def_close_${sectionIdx}_${criteriaIdx}');return false"><img src="<@s.url value="/images/expand.gif" includeParams="none"/>" /></a>
		<a href="javascript:void(0);" id="def_close_${sectionIdx}_${criteriaIdx}" onclick="closeSection('def_${sectionIdx}_${criteriaIdx}', 'def_close_${sectionIdx}_${criteriaIdx}', 'def_open_${sectionIdx}_${criteriaIdx}');return false" style="display:none;"><img src="<@s.url value="/images/collapse.gif" includeParams="none"/>" /></a>				 
		<@s.text name="label.deficiencies"/>
	</h2>
	<div id="def_${sectionIdx}_${criteriaIdx}" style="display:none;">
		<#list criteria.deficiencies as deficiency >
			<p>${deficiency}</p>
		</#list>
	</div>
</div>
</#if>