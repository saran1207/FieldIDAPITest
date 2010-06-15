${action.setPageType('inspection_type', 'show')!}
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
			${inspectionType.name?html}
		</span>
	</p>
	<p>
		<label><@s.text name="label.group"/></label>
		<span class="fieldValue">
			<a href="<@s.url action="eventTypeGroup" uniqueID="${inspectionType.group.id}"/>">${inspectionType.group.name?html}</a>
		</span>
	</p>
	<p>
		<label><@s.text name="label.type"/></label>
		<span class="fieldValue">
			<#if inspectionType.master > ${action.getText('label.master')} <#else> ${action.getText('label.standard')} </#if>
		</span>
	</p>		
	
	<p>
		<label><@s.text name="label.assigned_to_can_be_updated"/></label>
		<span class="fieldValue">
			${inspectionType.assignedToAvailable?string(action.getText('label.yes'), action.getText('lable.no'))} 
		</span>
	</p>	
	
	<h2><@s.text name="label.supportedprooftesttypes"/></h2>
	<#if inspectionType.supportedProofTests?size != 0 >
		<#list inspectionType.supportedProofTests as proofTestType >
			<p class="fieldValue">${ action.getText( proofTestType.displayName! ) }</p>
		</#list>
	<#else>
		<p class="fieldValue"><@s.text name="label.noprooftestavailable"/></p>
	</#if>


	<#if inspectionType.infoFieldNames?exists && !inspectionType.infoFieldNames.isEmpty()>
		<div >
			<h2><@s.text name="label.inspectionattributes"/></h2>
			<div id="infoFields">
				<#list inspectionType.infoFieldNames as infoField >
					<#include "_inspectionAttribute.ftl"/>
				</#list>
			</div>
			
		</div>
	</#if>

	
	<h2><@s.text name="label.inspectionform"/></h2>
	<#if !inspectionType.sections.isEmpty()  >
		
		<div id="inspectionForm">
			<#list inspectionType.sections as section >
				<#if !section.retired >
					<h2>${section.title}</h2>
					<div id="${section.title}">
						<table class="criteriaList">
							<#list section.criteria as criteria >
								<#if !criteria.retired >
									<tr>
										<td class="criteriaName"><label>${criteria.displayName}</label></td>
										<td class="buttonGroupIcons">
											<#assign states=criteria.states.states/>
											<#include "../buttonGroupCrud/_buttonGroup.ftl"/>
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
		<p class="fieldValue"><@s.text name="label.noinspectionform"/></p>
	</#if>		
</div>