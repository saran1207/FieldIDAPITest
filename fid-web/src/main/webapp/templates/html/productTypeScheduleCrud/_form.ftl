<#if customer?exists >
	<#assign formId="${inspectionType.id}_customer"/>
<#else>
	<#assign formId="${inspectionType.id}"/>
</#if>
<@s.form id="schedule_${formId}" action="productTypeScheduleSave" namespace="/ajax" theme="fieldidSimple" >
	<@s.hidden name="assetTypeId" />
	<@s.hidden name="inspectionTypeId" value="${inspectionType.id}" />
	<@s.hidden name="uniqueID" />
	<@s.hidden name="customerForm" />
	<span class="customer">
		<#if !uniqueID?exists && customerForm>
			<@n4.orgPicker name="owner" required="true" orgType="non_primary"/>
		<#else>
			<#if schedule.override >
				<@s.text name="label.capital_for"/> <b>${schedule.owner.name}&nbsp;</b>
			</#if>
			<@s.hidden name="ownerId"/>
		</#if>
	</span>
	
	<span class="frequency">
		<@s.text name="label.capital_schedule_a"/> <b>${inspectionType.name}</b> &nbsp; <@s.text name="label.every"/>
		<@s.textfield name="frequency" onkeypress="if (event.keyCode==13) {saveSchedule( ${formId} ); return false;} return true;" /> <@s.text name="label.days"/>
	</span>
	<br/>
	<span class="autoSchedule">
			<br/>
			<@s.checkbox name="autoSchedule" /> <@s.text name="label.automatically_schedule_checkbox_1"/> <b>${assetType.name}</b>
			<@s.text name="label.automatically_schedule_checkbox_2"/> <b>${inspectionType.name}</b>		
	</span>
	<span class="actions">
		<a href="javascript:void(0);" onclick="saveSchedule( ${formId} ); return false;" ><@s.text name="label.save" /></a> | 
		<a href="javascript:void(0);" onclick="cancelSchedule( ${formId}, ${assetType.uniqueID}, ${uniqueID!"null"} ); return false;" ><@s.text name="label.cancel" /></a>
	</span>
</@s.form>


<@s.fielderror>
	<@s.param>owner</@s.param>
	<@s.param>frequency</@s.param>				
</@s.fielderror>