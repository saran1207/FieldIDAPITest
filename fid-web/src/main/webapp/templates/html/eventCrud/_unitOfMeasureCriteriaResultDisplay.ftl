<div class="textCriteriaDisplayContainer">

    <#if criteriaResult.primaryValue?exists>
        ${criteriaResult.primaryValue} ${criteriaResult.criteria.primaryUnit.name}
    </#if>
    <#if criteriaResult.secondaryValue?exists>
        ${criteriaResult.secondaryValue} ${criteriaResult.criteria.secondaryUnit.name}
    </#if>

</div>
