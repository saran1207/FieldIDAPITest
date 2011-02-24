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
            </#if>

            <#include "../observationsCrud/observationsShow.ftl"/>

        </div>
    </#list>
</#if>