<#if eventFormHelper.getVisibleResults( formEvent ).get( section )?exists >
	<#assign results=eventFormHelper.getVisibleResults( formEvent ).get( section )>
    <#list results as criteriaResult >
        <div class="infoSet">

            <label class="label eventFormLabel">${criteriaResult.criteria.displayName}</label>

            <#if criteriaResult.criteria.criteriaType.name() == 'ONE_CLICK'>
                <#include '_oneClickCriteriaResultDisplay.ftl'>
            <#elseif criteriaResult.criteria.criteriaType.name() == 'TEXT_FIELD'>
                <#include '_textFieldCriteriaResultDisplay.ftl'>
            <#elseif criteriaResult.criteria.criteriaType.name() == 'SELECT'>
                <#include '_selectCriteriaResultDisplay.ftl'>
            <#elseif criteriaResult.criteria.criteriaType.name() == 'COMBO_BOX'>
                <#include '_comboBoxCriteriaResultDisplay.ftl'>
            <#elseif criteriaResult.criteria.criteriaType.name() == 'UNIT_OF_MEASURE'>
                <#include '_unitOfMeasureCriteriaResultDisplay.ftl'>
            <#elseif criteriaResult.criteria.criteriaType.name() == 'SIGNATURE'>
                <#include '_signatureCriteriaResultDisplay.ftl'>
            <#elseif criteriaResult.criteria.criteriaType.name() == 'DATE_FIELD'>
                <#include '_dateFieldCriteriaResultDisplay.ftl'>
            <#elseif criteriaResult.criteria.criteriaType.name() == 'SCORE'>
                <#include '_scoreCriteriaResultDisplay.ftl'>
            <#elseif criteriaResult.criteria.criteriaType.name() == 'NUMBER_FIELD'>
                <#include '_numberFieldCriteriaResultDisplay.ftl'>
            </#if>

            <#include "../observationsCrud/observationsShow.ftl"/>

        </div>
    </#list>
</#if>