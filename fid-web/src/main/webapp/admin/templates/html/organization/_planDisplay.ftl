<div class="editHeader">
	<h3>${(action.currentPackageFilter().packageName?html)!} Plan</h3>
	<p> | <a href="javascript:void(0);" onClick="editPlan(${id});"><@s.text name="label.edit"/></a></p>
</div>
<div id="diskSpace" class="limit">			
	<div class="limitLabel"><@s.text name="label.disk_space"/></div>
	<@n4.percentbar progress="${limits.diskSpaceUsed}" total="${limits.diskSpaceMax}"/>
	<div class="limitInfo">
		${action.getHumanReadableFileSize(limits.diskSpaceUsed)} <@s.text name="label.of"/> <#if limits.diskSpaceUnlimited><@s.text name="label.unlimited"/><#else>${action.getHumanReadableFileSize(limits.diskSpaceMax)}</#if>
	</div>
</div>
<div id="fullAccounts" class="limit">
	<div class="limitLabel"><@s.text name="label.employee_accounts"/></div>
	<@n4.percentbar progress="${limits.employeeUsersUsed}" total="${limits.employeeUsersMax}"/>
	<div class="limitInfo">${limits.employeeUsersUsed} <@s.text name="label.of"/> <#if limits.employeeUsersUnlimited><@s.text name="label.unlimited"/><#else>${limits.employeeUsersMax}</#if></div>
	<#if !action.currentPackageFilter().legacy && !limits.employeeUsersUnlimited>
		<div id="addFullUsers"><a href="<@s.url action="increaseEmployeeLimit"/>"><@s.text name="label.i_want_more_employee_accounts"/></a></div>
	</#if>				
</div>
<div id="liteUserAccounts" class="limit">
	<div class="limitLabel"><@s.text name="label.lite_user_accounts"/></div>				
	<@n4.percentbar progress="${limits.liteUsersUsed}" total="${limits.liteUsersMax}"/>
	<div class="limitInfo"> ${limits.liteUsersUsed} <@s.text name="label.of"/> <#if limits.liteUsersUnlimited><@s.text name="label.unlimited"/><#else>${limits.liteUsersMax}</#if></div>
</div>
<div id="assets" class="limit">
	<div class="limitLabel"><@s.text name="label.assets"/></div>			
	<@n4.percentbar progress="${limits.assetsUsed}" total="${limits.assetsMax}"/>
	<div class="limitInfo">${limits.assetsUsed} <@s.text name="label.of"/> <#if limits.assetsUnlimited><@s.text name="label.unlimited"/><#else>${limits.assetsMax}</#if></div>
</div>	
<div id="secondaryOrgs" class="limit">
	<div class="limitLabel"><@s.text name="label.secondaryOrgs"/></div>			
	<@n4.percentbar progress="${limits.secondaryOrgsUsed}" total="${limits.secondaryOrgsUsed}"/>
	<div class="limitInfo">${limits.secondaryOrgsUsed} <@s.text name="label.of"/> <#if limits.secondaryOrgsUnlimited><@s.text name="label.unlimited"/><#else>${limits.secondaryOrgsUsed}</#if></div>
</div>
