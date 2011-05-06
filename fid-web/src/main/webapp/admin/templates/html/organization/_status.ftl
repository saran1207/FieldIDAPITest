<@s.form id="statusForm" action="updateTenantStatus" namespace="/adminAjax" theme="fieldidSimple" >
	<@s.hidden name="id"/>
	
	<#if tenant.disabled> 
		<@s.hidden name="tenant.disabled" value="false"/>
	<#else> 
		<@s.hidden name="tenant.disabled" value="true"/>
	</#if>
	<label class="bold">Status: </label>${tenant.disabled?string("Inactive", "Active")} 
	| 
	<a href="javascript:void(0);" onClick="updateStatus();">
		<#if tenant.disabled>Activate<#else>Make Inactive</#if>
	</a>
</@s.form>
