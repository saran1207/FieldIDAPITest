<#if action.getVisibleResults( formInspection ).get( section )?exists >
	<#assign results=action.getVisibleResults( formInspection ).get( section )>
	<#list results as criteriaResult >
		<div class="infoSet">
			<label>${criteriaResult.criteria.displayName}</label>
			<span class="criteriaButton">
				<img src="<@s.url value="/images/inspectionButtons/${criteriaResult.state.buttonName}.png" includeParams="none"/>"/>
			</span>
			<span class="criteriaText">${criteriaResult.state.displayText}</span>
			
			<#include "../observationsCrud/observationsShow.ftl"/>
		</div>
	</#list>
</#if>