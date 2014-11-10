${action.setPageType('event_type', 'show')!}
<head>
    <@n4.includeStyle href="/style/legacy/newCss/component/buttons.css" type="page"/>
</head>
<style>
	p.criteria span {
		width:auto;
	} 
	.criteriaName {
		width:150px;
		padding: 20px !important;
	}
	.buttonGroupIcons {
		width: 240px;
	}
	.criteriaList {
		width:100%;
	}
	.criteriaList tr{
		border-bottom: 1px solid #D0DAFD;
	}
	.criteriaList td {
		padding: 5px;
		vertical-align: top;
	}
</style>

<div class="viewSection" >
	
	<h2><@s.text name="label.summary"/></h2>
	<p>	
		<label><@s.text name="label.name"/></label>
		<span class="fieldValue">
			${eventType.name?html}
		</span>
	</p>
	<p>
		<label><@s.text name="label.group"/></label>
		<span class="fieldValue">
			<a href="<@s.url value='/w/setup/eventTypeGroupView' uniqueID="${eventType.group.id}"/>">${eventType.group.name?html}</a>
		</span>
	</p>

    <#if eventType.thingEventType>
        <p>
            <label><@s.text name="label.type"/></label>
            <span class="fieldValue">
                <#if eventType.master > ${action.getText('label.master')} <#else> ${action.getText('label.standard')} </#if>
            </span>
        </p>
        <#if securityGuard.assignedToEnabled>
            <p>
                <label><@s.text name="label.assigned_to_can_be_updated"/></label>
                <span class="fieldValue">${eventType.assignedToAvailable?string(action.getText('label.yes'), action.getText('label.no'))}</span>
            </p>
        </#if>
    </#if>
	
	<#if securityGuard.proofTestIntegrationEnabled && eventType.thingEventType>
		<h2><@s.text name="label.supportedprooftesttypes"/></h2>
		<#if eventType.supportedProofTests?size != 0 >
			<#list eventType.supportedProofTests as proofTestType >
				<p class="fieldValue">${ action.getText( proofTestType.displayName! ) }</p>
			</#list>
		<#else>
			<p class="fieldValue"><@s.text name="label.noprooftestavailable"/></p>
		</#if>
	</#if>

	<#if eventType.infoFieldNames?exists && !eventType.infoFieldNames.isEmpty()>
		<div >
			<h2><@s.text name="label.eventattributes"/></h2>
			<div id="infoFields">
				<#list eventType.infoFieldNames as infoField >
					<#include "_eventAttribute.ftl"/>
				</#list>
			</div>
			
		</div>
	</#if>

	
	<h2><@s.text name="label.eventform"/></h2>
	<#if eventType.eventForm?exists && !eventType.eventForm.sections.isEmpty()  >
		
		<div id="eventForm">
			<#list eventType.eventForm.sections as section >
				<#if !section.retired >
					<h2>${section.title}</h2>
					<div id="${section.title}">
						<table class="criteriaList">
							<#list section.criteria as criteria >
                                <#if !criteria.retired>
                                    <tr>
                                        <td class="criteriaName"><label>${criteria.displayName}</label></td>
                                        <td class="buttonGroupIcons">
                                            <#if criteria.criteriaType.name() == 'ONE_CLICK'>
                                                <#assign buttons=criteria.buttonGroup.buttons/>
                                                <#include "../buttonGroupCrud/_buttonGroup.ftl"/>
                                            </#if>
                                            <#if criteria.criteriaType.name() == 'SELECT'>
                                                <@s.text name="label.selectbox"/>
                                            </#if>
                                            <#if criteria.criteriaType.name() == 'TEXT_FIELD'>
                                                <@s.text name="label.textfield"/>
                                            </#if>
                                            <#if criteria.criteriaType.name() == 'COMBO_BOX'>
                                                <@s.text name="label.combobox"/>
                                            </#if>
                                            <#if criteria.criteriaType.name() == 'UNIT_OF_MEASURE'>
                                                <#include "_unitOfMeasureCriteria.ftl"/>
                                            </#if>
                                            <#if criteria.criteriaType.name() == 'DATE_FIELD'>
                                                <@s.text name="label.datefield"/>
                                            </#if>
                                            <#if criteria.criteriaType.name() == 'SCORE'>
                                                <#include "_scoreCriteria.ftl"/>
                                            </#if>
                                            <#if criteria.criteriaType.name() == 'NUMBER_FIELD'>
                                                <@s.text name="label.numberfield"/>
                                            </#if>
                                        </td>
                                        <td>
                                            <#assign sectionIdx=section_index/>
                                            <#assign criteriaIdx=criteria_index/>
                                            <#include "../observationsCrud/show.ftl"/>
                                        </td>
                                    </tr>
                                </#if>
							</#list>
						</table>
					</div>
				</#if>
			</#list>
		</div>
	<#else>
		<p class="fieldValue"><@s.text name="label.noeventform"/></p>
	</#if>		
</div>