<div class="criteriaEditContainer">
    <div class="textCriteriaContainer">
    	<#if criteria.includeTime>
    		<@s.textfield theme="fieldid" name="criteriaResults[${currentCriteriaIndex}].textValue" cssClass="dateCriteria datetimepicker" />
        <#else>
        	<@s.textfield theme="fieldid" name="criteriaResults[${currentCriteriaIndex}].textValue" cssClass="dateCriteria datepicker" />
        </#if>
        <script type="text/javascript">
			initDatePicker();
		</script>
    </div>
</div>
