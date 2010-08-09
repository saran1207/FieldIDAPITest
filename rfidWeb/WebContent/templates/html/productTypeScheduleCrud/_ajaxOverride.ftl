${action.setPageType('product_type', 'schedule_frequencies')!}
<#include "/templates/html/common/_orgPicker.ftl"/>

<h2><@s.text name="label.overrides_title"/></h2>
<br/>
<p><@s.text name="label.overrides_description"/></p>
<br/>
<@s.form id="schedule_${inspectionTypeId}_customer" action="inspectionFrequencyOverrideCreate" theme="fieldidSimple" >
	<@s.hidden name="productTypeId" />
	<@s.hidden name="inspectionTypeId" />
	<#assign schedule=action.newSchedule() />
	<@s.text name="label.capital_for"/>
	<span class="customer">
		<#if !uniqueID?exists >
			<@n4.orgPicker name="owner" required="true" orgType="non_primary"/>
		<#else>
			<@s.hidden name="ownerId" />
		</#if>
	</span>
	<@s.text name="label.schedule_a"/>
	<@s.text name=" ${inspectionType.name} "/>
	<@s.text name="label.every"/>
	<span class="frequency">
		<@s.textfield name="frequency"/>
	</span>
	<@s.text name="label.days"/>
	<@s.submit id="saveOverride" key="labe.add_new_override" cssClass="saveButton save"/>
</@s.form>
