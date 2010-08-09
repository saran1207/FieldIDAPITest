<#include "/templates/html/common/_orgPicker.ftl"/>
<@s.form id="schedule_${inspectionTypeId}_customer" action="inspectionFrequencyOverrideCreate" theme="fieldidSimple" >
	<@s.hidden name="productTypeId" />
	<@s.hidden name="inspectionTypeId" />
	<#assign schedule=action.newSchedule() />
	<span class="customer">
		<#if !uniqueID?exists >
			<@n4.orgPicker name="owner" required="true" orgType="non_primary"/>
		<#else>
			<@s.hidden name="ownerId" />
		</#if>
	</span>
	<span class="frequency">
		<@s.textfield name="frequency"/><@s.text name="label.days"/>
	</span>
	
	<@s.submit id="save" key="Add Schedule" cssClass="saveButton save"/>
</@s.form>
