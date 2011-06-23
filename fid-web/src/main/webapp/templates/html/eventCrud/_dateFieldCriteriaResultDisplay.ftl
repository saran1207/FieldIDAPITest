<div class="textCriteriaDisplayContainer">
    <#if criteriaResult.value?exists && criteriaResult.criteria.includeTime>
        ${action.formatDateTime(criteriaResult.value)}
    <#else>
    	${action.formatDate(criteriaResult.value, false)}
    </#if>
</div>
