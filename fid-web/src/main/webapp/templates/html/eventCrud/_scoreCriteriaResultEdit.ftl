<#list criteria.scoreGroup.scores as score>

    <#if criteriaResult?exists && criteriaResult.stateId = score.id>
        <#assign checkedStr = 'checked'/>
    <#else>
        <#assign checkedStr = ''/>
    </#if>

    <input type="radio" name="criteriaResults[${currentCriteriaIndex}].stateId" value="${score.id}" ${checkedStr}> ${score.name}

</#list>