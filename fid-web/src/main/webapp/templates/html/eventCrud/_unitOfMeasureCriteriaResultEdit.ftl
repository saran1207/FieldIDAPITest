<div class="criteriaEditContainer">
    <div class="textCriteriaContainer unitOfMeasureCriteriaContainer">

        <@s.textfield theme="fieldid" name="criteriaResults[${currentCriteriaIndex}].textValue" cssClass="criteriaUnitOfMeasureField"/><div class="unitOfMeasureName">${criteria.primaryUnit.name}</div>

        <#if criteria.secondaryUnit?exists>
            <div style="clear:both;padding-top:2px;"></div>
            <@s.textfield theme="fieldid" name="criteriaResults[${currentCriteriaIndex}].secondaryTextValue" cssClass="criteriaUnitOfMeasureField"/><div class="unitOfMeasureName">${criteria.secondaryUnit.name}</div>
        </#if>

        <div style="clear:both;"></div>

    </div>
</div>
