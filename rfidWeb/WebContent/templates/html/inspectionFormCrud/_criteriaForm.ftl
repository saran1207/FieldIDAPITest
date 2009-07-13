<#if (criteriaSections[section_index].criteria[criteriaList_index])?exists>
	<div id="criteriaHolder_${section_index}_${criteriaList_index}" 
		<#if (criteriaSections[section_index].criteria[criteriaList_index].retired)?exists && criteriaSections[section_index].criteria[criteriaList_index].retired >
			class="retired hidden"
		<#else>
			class="criteriaContainer"
		</#if>
	>
	
		<h4>
			<a href="javascript:void(0);" id="criteria_open_${section_index}_${criteriaList_index}" onclick="openSection('criteria_${section_index}_${criteriaList_index}', 'criteria_open_${section_index}_${criteriaList_index}', 'criteria_close_${section_index}_${criteriaList_index}');return false"><img src="<@s.url value="/images/expandLarge.gif" />" /></a>
			<a href="javascript:void(0);" id="criteria_close_${section_index}_${criteriaList_index}" onclick="closeSection('criteria_${section_index}_${criteriaList_index}', 'criteria_close_${section_index}_${criteriaList_index}', 'criteria_open_${section_index}_${criteriaList_index}');return false" style="display:none;"><img src="<@s.url value="/images/collapseLarge.gif"/>" /></a>				 
		
			<@s.text name="label.criterialabel" onchange="changeToForm()"/> <@s.textfield name="criteriaSections[${section_index}].criteria[${criteriaList_index}].displayText" theme="simple" onchange="changeToForm()" size="40"/>
			
			<@s.hidden id="id_${section_index}_${criteriaList_index}" name="hiddenid" value="${(criteriaSections[section_index].criteria[criteriaList_index].id)!0}"/>
			<@s.hidden id="retire_${section_index}_${criteriaList_index}" name="criteriaSections[${section_index}].criteria[${criteriaList_index}].retired" theme="simple"/>
			<a href="javascript:void(0);" onclick="retireCriteria(${section_index}, ${criteriaList_index}); changeToForm(); return false;"><img src="<@s.url value="/images/retire.gif" includeParams="none"/>" /></a>
		</h4>
		<div class="criteriaContent" id="criteria_${section_index}_${criteriaList_index}" style="display:none;">
			<span class="criteriaContentLabel"><@s.text name="label.buttongroup" onchange="changeToForm()"/></span>
			<span>
				<#if (criteriaSections[section_index].criteria[criteriaList_index].states.name)?exists >
					<span class="criteraStateName" onmouseover="toggleStateSetHighlight(${criteriaSections[section_index].criteria[criteriaList_index].states.id})" onmouseout="toggleStateSetHighlight(${criteriaSections[section_index].criteria[criteriaList_index].states.id})" >${criteriaSections[section_index].criteria[criteriaList_index].states.name}</span>
					<@s.hidden name="criteriaSections[${section_index}].criteria[${criteriaList_index}].states.iD" theme="simple"/>
				<#else>	
					<@s.select name="criteriaSections[${section_index}].criteria[${criteriaList_index}].states.iD" list="stateSets" listKey="id" listValue="name" headerKey="" headerValue="${action.getText( 'label.selectanoption' ) }" theme="simple" onchange="changeToForm()"/>
				</#if>
			</span>
			<span class="criteriaContentLabel"><@s.checkbox name="criteriaSections[${section_index}].criteria[${criteriaList_index}].principal" theme="simple" onchange="changeToForm()"/><@s.text name="label.setsresult" onchange="changeToForm()"/></span>
			<div class="observationsContainer">
				<#assign sectionIdx=section_index/>
				<#assign criteriaIdx=criteriaList_index/>
				<#assign criteria=criteriaSections[section_index].criteria[criteriaList_index]/>
				<#include "../observationsCrud/form.ftl"/>
			</div>
		</div>
	</div>

</#if>