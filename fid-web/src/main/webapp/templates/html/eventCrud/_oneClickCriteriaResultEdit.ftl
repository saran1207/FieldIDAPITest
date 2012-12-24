
			<#assign current_button=criteria.buttonGroup.availableButtons[0]>
			<#if (criteriaResult.stateId)?exists >
				<#list criteria.buttonGroup.availableButtons as button >
					<#if button.id = criteriaResult.stateId >
						<#assign current_button=button>
					</#if>
				</#list>
			</#if>
            <div class="criteriaEditContainer">
                <span class="criteriaButton">
                    <a href="javascript:void(0);" onclick="changeButton( ${criteria.id} ); return false;">
                        <img id="criteriaImage_${criteria.id}" src="<@s.url value="/images/eventButtons/${current_button.buttonName}.png"/>" />
                    </a>
                </span>
                <span class="criteriaText" id="criteriaText_${criteria.id}">${current_button.displayText}</span>

                <@s.hidden id="criteriaResultState_${criteria.id}" name="criteriaResults[${currentCriteriaIndex}].stateId" value="${current_button.id}" />
            </div>


		<script type="text/javascript">
			criteria = new Array();
			<#list criteria.buttonGroup.getAvailableButtons() as button>
				button = new Object();
                button.stateId = '${button.id}';
                button.stateText = '${button.displayText}';
                button.stateButton = '<@s.url value="/images/eventButtons/${button.buttonName}.png"/>';
                button.status = '${button.eventResult.name()}';
				criteria.push( button );
			</#list>
			buttonStates['criteria_${criteria.id}'] =  criteria;
		</script>