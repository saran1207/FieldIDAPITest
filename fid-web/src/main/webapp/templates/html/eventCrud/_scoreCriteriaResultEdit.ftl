<#list criteria.scoreGroup.scores as score>

    <input type="radio" name="criteriaResults[${currentCriteriaIndex}].stateId" value="${score.id}"> ${score.name}

</#list>