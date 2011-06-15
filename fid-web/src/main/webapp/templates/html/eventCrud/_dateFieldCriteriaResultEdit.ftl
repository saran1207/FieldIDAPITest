<div class="criteriaEditContainer">
    <div class="textCriteriaContainer">
    	<#if criteria.includeTime>
    		<@s.datetimepicker theme="fieldid" name="criteriaResults[${criteriaCount}].textValue" cssClass="dateCriteria" type="datetime"/>
        <#else>
        	<@s.datetimepicker theme="fieldid" name="criteriaResults[${criteriaCount}].textValue" cssClass="dateCriteria" type="date"/>
        </#if>
    </div>
</div>
