<#assign lightBoxProperties=":: :: width: 470, height: 300, menubar: true, topclose: false, autosize: false, scrolling: true" />

<script type="text/javascript">
	recImageUrl = '<@s.url value="/images/rec.png"/>';
	defImageUrl = '<@s.url value="/images/def.png"/>';
	recSelectImageUrl = '<@s.url value="/images/rec-plus.png"/>';
	defSelectImageUrl = '<@s.url value="/images/def-plus.png"/>';
</script>

<#list section.criteria as criteria >
	<#if !criteria.retired >				
		<div class="infoSet" >
			<label class="label">${criteria.displayName}</label>
			 
			<#assign current_state=criteria.states.availableStates[0]>
			<#if (criteriaResults[criteriaCount].state.id)?exists >
				<#list criteria.states.availableStates as state >
					<#if state.id = criteriaResults[criteriaCount].state.id >
						<#assign current_state=state>
					</#if>
				</#list>
			</#if>
			
			<#if (criteriaResults[criteriaCount].id)?exists >
				<@s.hidden id="criteriaResultId_${criteria.id}" name="criteriaResults[${criteriaCount}].iD" value="${criteriaResults[criteriaCount].id}" />
			</#if>
			<span class="criteriaButton">
				<a href="javascript:void(0);" onclick="changeButton( ${criteria.id} ); return false;">
					<img id="criteriaImage_${criteria.id}" src="<@s.url value="/images/eventButtons/${current_state.buttonName}.png"/>" />
				</a>
			</span>
			<span class="criteriaText" id="criteriaText_${criteria.id}">${current_state.displayText}</span>
			
			<@s.hidden id="criteriaResultState_${criteria.id}" name="criteriaResults[${criteriaCount}].state.iD" value="${current_state.id}" />
			<@s.hidden id="criteriaResultCriteria_${criteria.id}" name="criteriaResults[${criteriaCount}].criteria.iD" value="${criteria.id}" />
			
			<span class="recDefButtons">
				<#assign recCount=(action.countRecommendations(criteriaCount))!0 />
				<#assign defCount=(action.countDeficiencies(criteriaCount))!0 />
			
				<a id='recButton_${criteria.id}' href='#rec_${criteria.id}' title='<@s.text name="label.recommendations"/>${lightBoxProperties}' class='lightview' >
					<img id='recImage_${criteria.id}' src="<@s.url value="/images/rec.png"/>" />
				</a>
				
				<a id='defButton_${criteria.id}' href='#def_${criteria.id}' title='<@s.text name="label.deficiencies"/>${lightBoxProperties}' class='lightview' >
					<img id='defImage_${criteria.id}' src="<@s.url value="/images/def.png"/>" />
				</a>
			</span>
			<#include "../observationsCrud/_recSelect.ftl"/>
			<#include "../observationsCrud/_defSelect.ftl"/>
		</div>
		<script type="text/javascript">
			criteria = new Array();
			<#list criteria.states.getAvailableStates() as state>
				state = new Object();	
				state.stateId = '${state.id}';
				state.stateText = '${state.displayText}';
				state.stateButton = '<@s.url value="/images/eventButtons/${state.buttonName}.png"/>';
				state.status = '${state.status.name()}';
				criteria.push( state );
			</#list>
			buttonStates['criteria_${criteria.id}'] =  criteria;
			
			<#-- setup the inital rec/def counts and set the initial images -->
			recCounts[${criteria.id}] = ${recCount};
			defCounts[${criteria.id}] = ${defCount};
			updateRecommendationImage(${criteria.id});
			updateDeficiencyImage(${criteria.id});
		</script>
		<#assign criteriaCount=criteriaCount+1 />
	</#if>
</#list>
