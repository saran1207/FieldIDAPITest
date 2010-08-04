
${action.setPageType('inspection_type', 'inspection_form')!}
<head>
	<@n4.includeStyle type="page" href="inspectionForm" />
	<@n4.includeScript src="inspectionForm" />

	<@n4.includeScript>
		addCriteriaUrl = '<@s.url action="criteriaAdd" namespace="/ajax"  uniqueID="${uniqueID}"/>';
		addSectionUrl = '<@s.url action="sectionAdd" namespace="/ajax" uniqueID="${uniqueID}"/>';
		addObservationUrl = '<@s.url action="observationAdd" namespace="/ajax" uniqueID="${uniqueID}"/>';
		
		inspectionFormChangedMessage = '${action.getText("warning.inspectionformnotsaved")?js_string}';
		<#list criteriaSections as section >
			<#if section?exists >
				sectionIndexes[${section_index}] = new Array();
				<#list section.criteria as criteria >
					<#if criteria?exists >
						sectionIndexes[${section_index}][${criteria_index}] = new Array();
						sectionIndexes[${section_index}][${criteria_index}]["RECOMMENDATION"] = ${criteria.recommendations?size};
						sectionIndexes[${section_index}][${criteria_index}]["DEFICIENCY"] = ${criteria.deficiencies?size};
					</#if>
				</#list>
			</#if>
		</#list>
		<#if !actionErrors.isEmpty() || !fieldErrors.isEmpty() >
			changeToForm();
		</#if>
	</@n4.includeScript>
</head>


<#include "/templates/html/common/_formErrors.ftl"/>
<div id="inspectionTypeForm">
	
	<@s.form name="inspectionTypeForm" action="inspectionTypeFormSave" theme="simple" >
		<@s.hidden name="uniqueID" />
		<div id="sectionContainer">
			<#list criteriaSections as section >
				<#include "_sectionForm.ftl" />
			</#list>
		</div>
		<div class="formAction">
			<button onclick="addSection(); changeToForm(); return false;" ><@s.text name="label.addsection"/></button>
		</div>
		
		<div id="formChangewarning" class="formChangewarning" style="display: none;">
			<h2><@s.text name="warning.inspectionformchangewarning_one"/>${name}?</h2>
			<p><@s.text name="warning.inspectionformchangewarning_two"/></p>
			<br/>
			<p><@s.text name="warning.inspectionformchangewarning_three"/></p>
			<div class="formAction" style="margin-left:auto; margin-right:auto;">
				<button onclick="Lightview.hide(); return false;" ><@s.text name="label.cancel"/></button>
				<button onclick="document.inspectionTypeForm.submit();" ><@s.text name="label.ok"/></button>
			</div>
		</div>
		
		<div class="formAction">
			<@s.url id="cancelUrl" action="inspectionType" uniqueID="${uniqueID}"/>
			<@s.reset key="hbutton.cancel" onclick="return redirect('${cancelUrl}');" />
			
			<#if newForm>
				<@s.submit key="hbutton.save" />
			<#else>
				<@s.submit key="hbutton.save" onclick="warnFormChange(); return false;" />
			</#if>
		</div>
		
	</@s.form>
</div>


<div id="buttonGroupLists" class="viewSection">
	<h2><@s.text name="label.yourbuttongroups"/> <a href="<@s.url action="buttonGroups" inspectionTypeId="${uniqueID}"/>" onclick="return hasFormChanged();"><@s.text name="label.manage"/></a></h2>
	<table class="simpleTable">
		<tr>
			<th><@s.text name="label.name"/></th>
			<th>${action.getText("label.buttonsandlabels")?html}</th>
		</tr>
		<#if stateSets?size gt 0 >
			<#list stateSets as stateSet >
				<tr id="stateSet_${stateSet.id}" >
					<#include "../buttonGroupCrud/show.ftl"/>
				</tr>
			</#list>
		<#else>
			<div class="emptyList">
				<@s.text name="message.emptybuttonlist"/>
			</div>
		</#if>
		
	</table>
</div>
