<#if eventFormHelper.getVisibleResults( formEvent ).get( section )?exists >
	<#assign results=eventFormHelper.getVisibleResults( formEvent ).get( section )>
    <#list results as criteriaResult >
        <div class="infoSet">

            <label class="label eventFormLabel">${criteriaResult.criteria.displayName}</label>

            <#if criteriaResult.criteria.oneClickCriteria>
                <#include '_oneClickCriteriaResultDisplay.ftl'>
            <#elseif criteriaResult.criteria.textFieldCriteria>
                <#include '_textFieldCriteriaResultDisplay.ftl'>
            <#elseif criteriaResult.criteria.selectCriteria>
                <#include '_selectCriteriaResultDisplay.ftl'>
            <#elseif criteriaResult.criteria.comboBoxCriteria>
                <#include '_comboBoxCriteriaResultDisplay.ftl'>
            <#elseif criteriaResult.criteria.unitOfMeasureCriteria>
                <#include '_unitOfMeasureCriteriaResultDisplay.ftl'>
            <#elseif criteriaResult.criteria.signatureCriteria>
                <#include '_signatureCriteriaResultDisplay.ftl'>
            </#if>

            <#include "../observationsCrud/observationsShow.ftl"/>

        </div>
    </#list>
</#if>