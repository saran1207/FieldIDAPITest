<@s.form id="schedule_${inspectionType.id}_customer" action="productTypeScheduleCreateOverride" namespace="/ajax" theme="fieldidSimple" >
	<@s.hidden name="productTypeId" />
	<@s.hidden name="inspectionTypeId" value="${inspectionType.id}"/>
	<@s.hidden name="uniqueID" />
	<@s.hidden name="customerForm" value="true" />
	<span class="customer">
		<#if !uniqueID?exists >
			<@n4.orgPicker name="owner" />
		<#else>
			<@s.hidden name="ownerId" />
		</#if>
	</span>
	
	<span class="frequency">
		<@s.textfield name="frequency">
			<#if (action.fieldErrors['frequency'])?exists > 
				<@s.param name="cssClass">inputError</@s.param>
			</#if>
		</@s.textfield> 
		 <@s.text name="label.days"/>
	</span>
	
	<span class="actions">
		 <a href="javascript:void(0);" onclick="saveSchedule( '${inspectionType.id}_customer', ${inspectionType.id} ); return false;" ><@s.text name="label.add" /></a>  
	</span>
	<@s.fielderror>
		<@s.param>owner</@s.param>
		<@s.param>frequency</@s.param>				
	</@s.fielderror>
</@s.form>
