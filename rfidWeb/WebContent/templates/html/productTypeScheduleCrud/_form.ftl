<#if customer?exists >
	<#assign formId="${inspectionType.id}_customer"/>
<#else>
	<#assign formId="${inspectionType.id}"/>
</#if>
<@s.form id="schedule_${formId}" action="productTypeScheduleSave" namespace="/ajax" theme="fieldidSimple" >
	<@s.hidden name="productTypeId" />
	<@s.hidden name="inspectionTypeId" value="${inspectionType.id}" />
	<@s.hidden name="uniqueID" />
	<@s.hidden name="customerForm" />
	<span class="customer">
		<#if !uniqueID?exists && customerForm>
			<@n4.orgPicker name="owner" required="true" orgType="non_primary"/>
		<#else>
			<#if schedule.override >
				${schedule.owner.name}
			<#else>
				<@s.text name="label.default"/> 
			</#if>
			<@s.hidden name="ownerId"/>
		</#if>
	</span>
	
	<span class="frequency">
		<@s.textfield name="frequency" /> <@s.text name="label.days"/>
	</span>
	<span class="autoSchedule">
			, <@s.text name="label.autoscheduled"/> <@s.checkbox name="autoSchedule" /> 			
	</span>
	<span class="actions">
		<a href="javascript:void(0);" onclick="saveSchedule( ${formId} ); return false;" ><@s.text name="label.save" /></a> | 
		<a href="javascript:void(0);" onclick="cancelSchedule( ${formId}, ${productType.uniqueID}, ${uniqueID!"null"} ); return false;" ><@s.text name="label.cancel" /></a>
	</span>
</@s.form>


<@s.fielderror>
	<@s.param>owner</@s.param>
	<@s.param>frequency</@s.param>				
</@s.fielderror>