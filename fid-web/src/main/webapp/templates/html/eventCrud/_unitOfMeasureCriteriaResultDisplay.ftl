<div class="textCriteriaDisplayContainer">

    <#if criteriaResult.primaryValue?exists>
        ${criteriaResult.primaryValue} ${criteriaResult.criteria.primaryUnit.shortName}
    </#if>
    <#if criteriaResult.secondaryValue?exists>
        ${criteriaResult.secondaryValue} ${criteriaResult.criteria.secondaryUnit.shortName}
    </#if>

</div>
