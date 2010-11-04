<#if eventFormHelper.getVisibleResults( formEvent ).get( section )?exists >
	<#assign results=eventFormHelper.getVisibleResults( formEvent ).get( section )>
	<#list results as criteriaResult >
		<div class="infoSet">
			<label class="label">${criteriaResult.criteria.displayName}</label>
			<span class="criteriaButton">
				<img src="<@s.url value="/images/eventButtons/${criteriaResult.state.buttonName}.png" includeParams="none"/>"/>
			</span>
			<span class="criteriaText">${criteriaResult.state.displayText}</span>
			
			<#include "../observationsCrud/observationsShow.ftl"/>
		</div>
	</#list>
</#if>