<div class="criteriaEditContainer">
    <select name="criteriaResults[${criteriaCount}].textValue" class="selectCriteria">
        <option></option>
        <#list criteria.options as option>
            <#if criteriaResult?exists && criteriaResult.textValue?exists && criteriaResult.textValue == option>
                <#assign selectedStr = 'selected'/>
            <#else>
                <#assign selectedStr = ''/>
            </#if>

            <option ${selectedStr}>${option}</option>
        </#list>
    </select>
</div>
