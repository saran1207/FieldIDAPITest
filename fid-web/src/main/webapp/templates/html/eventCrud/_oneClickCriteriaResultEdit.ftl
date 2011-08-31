
			<#assign current_state=criteria.states.availableStates[0]>
			<#if (criteriaResult.stateId)?exists >
				<#list criteria.states.availableStates as state >
					<#if state.id = criteriaResult.stateId >
						<#assign current_state=state>
					</#if>
				</#list>
			</#if>
            <div class="criteriaEditContainer">
                <span class="criteriaButton">
                    <a href="javascript:void(0);" onclick="changeButton( ${criteria.id} ); return false;">
                        <img id="criteriaImage_${criteria.id}" src="<@s.url value="/images/eventButtons/${current_state.buttonName}.png"/>" />
                    </a>
                </span>
                <span class="criteriaText" id="criteriaText_${criteria.id}">${current_state.displayText}</span>

                <@s.hidden id="criteriaResultState_${criteria.id}" name="criteriaResults[${currentCriteriaIndex}].stateId" value="${current_state.id}" />
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
		</script>