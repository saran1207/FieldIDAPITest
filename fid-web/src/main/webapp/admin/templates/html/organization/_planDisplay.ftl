<div class="editHeader">
	<h3><span class="currentPlanDisplay">${primaryOrg.signUpPackage.displayName}</span> Plan</h3>
	<p> | <a href="javascript:void(0);" onClick="editPlan(${id});"><@s.text name="label.edit"/></a></p>
</div>
<div id="fullAccounts" class="limit">
	<div class="limitLabel"><@s.text name="label.employee_accounts"/></div>
	<@n4.percentbar progress="${userLimitService.employeeUsersCount}" total="${userLimitService.maxEmployeeUsers}"/>
	<div class="limitInfo">${userLimitService.employeeUsersCount} <@s.text name="label.of"/> <#if userLimitService.employeeUsersUnlimited><@s.text name="label.unlimited"/><#else>${userLimitService.maxEmployeeUsers}</#if></div>		
</div>
<div id="liteUserAccounts" class="limit">
	<div class="limitLabel"><@s.text name="label.lite_user_accounts"/></div>				
	<@n4.percentbar progress="${userLimitService.liteUsersCount}" total="${userLimitService.maxLiteUsers}"/>
	<div class="limitInfo"> ${userLimitService.liteUsersCount} <@s.text name="label.of"/> <#if userLimitService.liteUsersUnlimited><@s.text name="label.unlimited"/><#else>${userLimitService.maxLiteUsers}</#if></div>
</div>
<div id="readonlyUserAccounts" class="limit">
	<div class="limitLabel"><@s.text name="label.readonly_user_accounts"/></div>				
	<@n4.percentbar progress="${userLimitService.readOnlyUserCount}" total="${userLimitService.maxReadOnlyUsers}"/>
	<div class="limitInfo"> ${userLimitService.readOnlyUserCount} <@s.text name="label.of"/> <#if userLimitService.readOnlyUsersUnlimited><@s.text name="label.unlimited"/><#else>${userLimitService.maxReadOnlyUsers}</#if></div>
</div>