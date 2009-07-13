<#if customer?exists >
	<#assign formId="${inspectionType.id}_customer"/>
<#else>
	<#assign formId="${inspectionType.id}"/>
</#if>
<@s.form id="schedule_${formId}" action="productTypeScheduleSave" namespace="/ajax" theme="simple" >
	<@s.hidden name="productTypeId" />
	<@s.hidden name="inspectionTypeId" value="${inspectionType.id}" />
	<@s.hidden name="uniqueID" />
	<@s.hidden name="customerForm" />
	<span class="customer">
		<#if !uniqueID?exists && customer?exists >
			<@s.select name="customer" list="customers" listKey="id" listValue="name">
				<#if (action.fieldErrors['customer'])?exists> 
					<@s.param name="cssClass">inputError</@s.param>
				</#if> 
			</@s.select>
		<#else>
			${ (schedule.customer.name )! }<@s.hidden name="customer" />
			
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
	<@s.param>customer</@s.param>
	<@s.param>frequency</@s.param>				
</@s.fielderror>