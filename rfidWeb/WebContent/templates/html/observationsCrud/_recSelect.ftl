<div class="observationBox" id="rec_${criteria.id}" style="display: none;">
	<#list criteria.recommendations as recommendation >

	  	<#assign notSelected=!(criteriaResults[criteriaCount].recommendations[recommendation_index])?exists />		
		<#if notSelected>
			<#assign stateValue="NOTSELECTED" />
		<#else>
			<#assign stateValue=criteriaResults[criteriaCount].recommendations[recommendation_index].stateString />
		</#if>
		
		<#-- comments are handled as a special case below -->
		<#if stateValue != "COMMENT">
			<#switch stateValue>
				<#case "OUTSTANDING">
					<#assign selectClass="recSelected">
					<#break>
				<#default>
					<#assign selectClass="">
			</#switch>
			
			<p id="rec_${criteria.id}_${recommendation_index}" class="observationText ${selectClass}" onclick="cycleRecommendation(${criteria.id}, ${recommendation_index})" >
				<span>${recommendation?html}</span>
				
				<#if (criteriaResults[criteriaCount].recommendations[recommendation_index].id)?exists >
					<@s.hidden id="rec_${criteria.id}_${recommendation_index}_id" name="criteriaResults[${criteriaCount}].recommendations[${recommendation_index}].iD" value="${criteriaResults[criteriaCount].recommendations[recommendation_index].id}" />
				</#if>
				
				<@s.textfield id="rec_${criteria.id}_${recommendation_index}_text" name="criteriaResults[${criteriaCount}].recommendations[${recommendation_index}].text" value="${recommendation}" theme="simple" cssClass="disabledInput" disabled="${notSelected?string}"/>
				<@s.textfield id="rec_${criteria.id}_${recommendation_index}_state" name="criteriaResults[${criteriaCount}].recommendations[${recommendation_index}].stateString" value="${stateValue}" theme="simple" cssClass="disabledInput" disabled="${notSelected?string}" />
			</p>
		</#if>
	</#list>
	
	<p class="observationComment" >
	
		<#if (criteriaResults[criteriaCount].recommendations[criteria.recommendations.size()].id)?exists >
			<@s.hidden id="rec_${criteria.id}_${criteria.recommendations.size()}_id" name="criteriaResults[${criteriaCount}].recommendations[${criteria.recommendations.size()}].iD" value="${criteriaResults[criteriaCount].recommendations[criteria.recommendations.size()].id}" />
		</#if>
		
		<label><@s.text name="label.comments"/></label>
		<span><@s.textarea  id="rec_${criteria.id}_${criteria.recommendations.size()}_text" name="criteriaResults[${criteriaCount}].recommendations[${criteria.recommendations.size()}].text" cols="53" rows="3" onfocus="captureRecCommentState(this.value);" onchange="checkRecComment(${criteria.id}, this.value);" value="${(criteriaResults[criteriaCount].recommendations[criteria.recommendations.size()].text)!}"/></span>
		<@s.hidden id="rec_${criteria.id}_${criteria.recommendations.size()}_state" name="criteriaResults[${criteriaCount}].recommendations[${criteria.recommendations.size()}].stateString" value="COMMENT" />
	</p>
	
</div>