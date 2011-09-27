<div class="criteriaEditContainer scoreEditContainer">

    <#list criteria.scoreGroup.scores as score>

        <#if criteriaResult?exists && criteriaResult.stateId?exists && criteriaResult.stateId = score.id>
            <#assign checkedStr = 'checked'/>
        <#else>
            <#assign checkedStr = ''/>
        </#if>

        <input type="radio" name="criteriaResults[${currentCriteriaIndex}].stateId" value="${score.id}" ${checkedStr}> <div class="scoreName">${score.name}</div>

    </#list>

</div>