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
				<#if userSecurityGuard.allowedAccessWebStore && action.currentPackageFilter().upgradable>
					 <a href="<@s.url action="upgradePlans"/>"><@s.text name="label.upgrade_my_plan"/></a>
				</#if>
			</span>
		</div>
		<div class="infoSet">
			<label><@s.text name="label.disk_space"/></label>
			<div class="fieldHolder" style="float:left; padding: 5px 0;">
				
				<div style="width:300px; float:left;">
					<@n4.percentbar progress="${limits.diskSpaceUsed}" total="${limits.diskSpaceMax}"/>
				</div>
				<div style="float:left; margin:5px;">${action.getHumanReadableFileSize(limits.diskSpaceUsed)} <@s.text name="label.of"/> <#if limits.diskSpaceUnlimited><@s.text name="label.unlimited"/><#else>${action.getHumanReadableFileSize(limits.diskSpaceMax)}</#if></div>
			</div>
		</div>
		<div class="infoSet">
			<label><@s.text name="label.employee_accounts"/></label>
			<div class="fieldHolder" style="float:left; padding: 5px 0;">
				
				<div style="width:300px; float:left;">
					<@n4.percentbar progress="${limits.employeeUsersUsed}" total="${limits.employeeUsersMax}"/>
				</div>
				<div style="float:left; margin:5px;">${limits.employeeUsersUsed} <@s.text name="label.of"/> <#if limits.employeeUsersUnlimited><@s.text name="label.unlimited"/><#else>${limits.employeeUsersMax}</#if></div>
				<#if userSecurityGuard.allowedAccessWebStore && !action.currentPackageFilter().legacy && !limits.employeeUsersUnlimited>
					<div style="float:left; margin:5px;"><a href="<@s.url action="increaseEmployeeLimit"/>"><@s.text name="label.i_want_more_employee_accounts"/></a></div>
				</#if>
				
			</div>
		</div>
			<div class="infoSet">
			<label><@s.text name="label.lite_user_accounts"/></label>
			<div class="fieldHolder" style="float:left; padding: 5px 0;">
				
				<div style="width:300px; float:left;">
					<@n4.percentbar progress="${limits.liteUsersUsed}" total="${limits.liteUsersMax}"/>
				</div>
				<div style="float:left; margin:5px;">${limits.liteUsersUsed} <@s.text name="label.of"/> <#if limits.liteUsersUnlimited><@s.text name="label.unlimited"/><#else>${limits.liteUsersMax}</#if></div>
			</div>
		</div>
		<div class="infoSet">
			<label><@s.text name="label.assets"/></label>
			<div class="fieldHolder" style="float:left; padding: 5px 0;">
				
				<div style="width:300px; float:left;">
					<@n4.percentbar progress="${limits.assetsUsed}" total="${limits.assetsMax}"/>
				</div>
				<div style="float:left; margin:5px;">${limits.assetsUsed} <@s.text name="label.of"/> <#if limits.assetsUnlimited><@s.text name="label.unlimited"/><#else>${limits.assetsMax}</#if></div>
			</div>
		</div>
	</div>
</div>
