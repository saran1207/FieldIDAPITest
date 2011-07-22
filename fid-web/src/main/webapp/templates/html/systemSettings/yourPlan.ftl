${action.setPageType('your_account', 'list')!}

<div class="pageSection crudForm largeForm">
	<div class="sectionContent">
		<div class="infoSet">
			<label  for="companyID"><@s.text name="label.companyid"/></label>
			<span class="fieldHolder">${sessionUser.tenant.name?html}</span>			
		</div>
		
		<div class="infoSet">
			<label><@s.text name="label.your_current_plan"/></label>
			<span class="fieldHolder">
				<strong id="currentPlan">${(action.currentPackageFilter().packageName?html)!}</strong>
			</span>
		</div>
		
		<div class="infoSet">
			<label><@s.text name="label.employee_accounts"/></label>
			<div class="fieldHolder" style="float:left; padding: 5px 0;">
				
				<div style="width:300px; float:left;">
					<@n4.percentbar progress="${userLimitService.employeeUsersCount}" total="${userLimitService.maxEmployeeUsers}"/>
				</div>
				<div style="float:left; margin:5px;">${userLimitService.employeeUsersCount} <@s.text name="label.of"/> <#if userLimitService.employeeUsersUnlimited><@s.text name="label.unlimited"/><#else>${userLimitService.maxEmployeeUsers}</#if></div>
				<#if userSecurityGuard.allowedAccessWebStore && !action.currentPackageFilter().legacy && !userLimitService.employeeUsersUnlimited>
					<div style="float:left; margin:5px;"><a href="<@s.url action="increaseEmployeeLimit"/>"><@s.text name="label.i_want_more_employee_accounts"/></a></div>
				</#if>
				
			</div>
		</div>
		
		<#if userLimitService.liteUsersEnabled>
		<div class="infoSet">
			<label><@s.text name="label.lite_user_accounts"/></label>
			<div class="fieldHolder" style="float:left; padding: 5px 0;">
				
				<div style="width:300px; float:left;">
					<@n4.percentbar progress="${userLimitService.liteUsersCount}" total="${userLimitService.maxLiteUsers}"/>
				</div>
				<div style="float:left; margin:5px;">${userLimitService.liteUsersCount} <@s.text name="label.of"/> <#if userLimitService.liteUsersUnlimited><@s.text name="label.unlimited"/><#else>${userLimitService.maxLiteUsers}</#if></div>
			</div>
		</div>
		</#if>
		
		<#if userLimitService.readOnlyUsersEnabled>
		<div class="infoSet">
			<label><@s.text name="label.readonly_user_accounts"/></label>
			<div class="fieldHolder" style="float:left; padding: 5px 0;">
				
				<div style="width:300px; float:left;">
					<@n4.percentbar progress="${userLimitService.readOnlyUserCount}" total="${userLimitService.maxReadOnlyUsers}"/>
				</div>
				<div style="float:left; margin:5px;">${userLimitService.readOnlyUserCount} <@s.text name="label.of"/> <#if userLimitService.readOnlyUsersUnlimited><@s.text name="label.unlimited"/><#else>${userLimitService.maxReadOnlyUsers}</#if></div>
			</div>
		</div>
		</#if>
	</div>
</div>
