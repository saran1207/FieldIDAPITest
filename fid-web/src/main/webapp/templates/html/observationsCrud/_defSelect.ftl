<div class="observationBox" id="def_${criteria.id}" style="display: none;">
	<#list criteria.deficiencies as deficiency >

	  	<#assign notSelected=!(criteriaResults[criteriaCount].deficiencies[deficiency_index])?exists />
		<#if notSelected>
			<#assign stateValue="NOTSELECTED" />
		<#else>
			<#assign stateValue=criteriaResults[criteriaCount].deficiencies[deficiency_index].stateString />
		</#if>

		<#-- comments are handled as a special case below -->
		<#if stateValue != "COMMENT">
			<#switch stateValue>
				<#case "OUTSTANDING">
					<#assign selectClass="defSelected">
					<#break>
				<#case "REPAIREDONSITE">
					<#assign selectClass="defRepairedOnSite">
					<#break>
				<#case "REPAIRED">
					<#assign selectClass="defRepaired">
					<#break>
				<#default>
					<#assign selectClass="">
			</#switch>
		
			<p id="def_${criteria.id}_${deficiency_index}" class="observationText ${selectClass}" onclick="cycleDeficiency(${criteria.id}, ${deficiency_index})" >
				<span>${deficiency?html} </span>
				
				<#if (criteriaResults[criteriaCount].deficiencies[deficiency_index].id)?exists >
					<@s.hidden id="def_${criteria.id}_${deficiency_index}_id" name="criteriaResults[${criteriaCount}].deficiencies[${deficiency_index}].iD" value="${criteriaResults[criteriaCount].deficiencies[deficiency_index].id}" />
				</#if>
				
				<@s.textfield id="def_${criteria.id}_${deficiency_index}_text" name="criteriaResults[${criteriaCount}].deficiencies[${deficiency_index}].text" value="${deficiency}" theme="simple" cssClass="disabledInput" disabled="${notSelected?string}"/>
				<@s.textfield id="def_${criteria.id}_${deficiency_index}_state" name="criteriaResults[${criteriaCount}].deficiencies[${deficiency_index}].stateString" value="${stateValue}" theme="simple" cssClass="disabledInput" disabled="${notSelected?string}" />
			</p>
		</#if>
	</#list>
	
	<p class="observationComment" >
	
		<#if (criteriaResults[criteriaCount].deficiencies[criteria.deficiencies.size()].id)?exists >
			<@s.hidden id="def_${criteria.id}_${criteria.deficiencies.size()}_id" name="criteriaResults[${criteriaCount}].deficiencies[${criteria.deficiencies.size()}].iD" value="${criteriaResults[criteriaCount].deficiencies[criteria.deficiencies.size()].id}" />
		</#if>
		
		<label><@s.text name="label.comments"/>:</label>
		<span><@s.textarea  id="def_${criteria.id}_${criteria.deficiencies.size()}_text" name="criteriaResults[${criteriaCount}].deficiencies[${criteria.deficiencies.size()}].text" cols="53" rows="3" onfocus="captureDefCommentState(this.value);" onchange="checkDefComment(${criteria.id}, this.value);" value="${(criteriaResults[criteriaCount].deficiencies[criteria.deficiencies.size()].text)!}"/></span>
		<@s.hidden id="def_${criteria.id}_${criteria.deficiencies.size()}_state" name="criteriaResults[${criteriaCount}].deficiencies[${criteria.deficiencies.size()}].stateString" value="COMMENT" />
	</p>
	
</div>