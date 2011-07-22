${action.setPageType('user','change')!}
<head>
	<@n4.includeStyle href="user" type="page"/>
</head>

<div class="horizontalGrouping">

	<h2>
		<@s.text name="label.upgrade_user_heading">
			<@s.param>${user.userType.label}</@s.param>
			<@s.param>${userId}</@s.param>
		</@s.text>
	</h2>

	<div class="horizontalGroup <#if !userLimitService.readOnlyUsersEnabled> twoGroup </#if> ">
		<div class="groupContents">
			<h2><@s.text name="label.full_user" /></h2>
			<p><@s.text name="label.employees_that_may" /></p>
			
			<ul class="permissionListing">
				<li><label><@s.text name="label.add_new_data" /></label></li>
				<li><label><@s.text name="label.perform_events" /></label></li>
				<li><label><@s.text name="label.manage_system_configuration" /></label></li>
				<li><label><@s.text name="label.run_searches" /></label></li>
			</ul>
		</div>
			
		<#if fullUser >
			<div class="upgradeUserAction center">
				<input type="button" value="<@s.text name="hbutton.current_user_type"/>" disabled="true" />
			</div>
		<#else>
			<#if userLimitService.employeeUsersAtMax>
				<div class="userLimitWarning">
					<@s.text name="label.full_user_limit_reached"><@s.param><a href="http://www.fieldid.com/contact"><@s.text name="label.contact_us"/></a></@s.param></@s.text>	
				</div>	
			<#else>
				<div class="upgradeUserAction center">
					<@s.url id="changeToFull" action="changeToFull" uniqueID="${uniqueID}"/>
					<input type="button" value="<@s.text name='hbutton.change_to_full'/>" onclick="return redirect('${changeToFull}');"/>
				</div>
			</#if>
		</#if>
	</div>
		
	<div class="horizontalGroup <#if !userLimitService.readOnlyUsersEnabled> increasedMargins <#else> leftRightMargins </#if> ">
		<div class="groupContents">
			<h2><@s.text name="label.lite_user" /></h2>
			<p><@s.text name="label.employees_that_may" /></p>
			
			<ul class="permissionListing">
				<li><label><@s.text name="label.perform_events" /></label></li>
				<li><label><@s.text name="label.run_searches" /></label></li>
			</ul>
		</div>
		
		<#if liteUser >
			<div class="upgradeUserAction center">
				<input type="button" value="<@s.text name="hbutton.current_user_type"/>" disabled="true" />
			</div>
		<#else>
			<#if userLimitService.liteUsersAtMax>
				<div class="userLimitWarning">
					<@s.text name="label.lite_user_limit_reached"><@s.param><a href="http://www.fieldid.com/contact"><@s.text name="label.contact_us"/></a></@s.param></@s.text>	
				</div>
			<#else>	
				<div class="upgradeUserAction center">
					<@s.url id="changeToLite" action="changeToLite" uniqueID="${uniqueID}"/>
					<input type="button" value="<@s.text name='hbutton.change_to_lite'/>" onclick="return redirect('${changeToLite}');"/>
				</div>
			</#if>
		</#if>
	</div>
	
	<#if userLimitService.readOnlyUsersEnabled>
		<div class="horizontalGroup">
			<div class="groupContents">
				<h2><@s.text name="label.ready_only_user" /></h2>
				<p><@s.text name="label.lite_users_that_may" /></p>
				
				<ul class="permissionListing">
					<li><label><@s.text name="label.view_their_assets" /></label></li>
					<li><label><@s.text name="label.run_searches" /></label></li>
				</ul>
			</div>
			<#if readOnlyUser >
				<div class="upgradeUserAction center">
					<input type="button" value="<@s.text name="hbutton.current_user_type"/>" disabled="true" />
				</div>
			<#else>
				<#if readonlyUserLimitReached>
					<div class="userLimitWarning">
						<@s.text name="label.readonly_user_limit_reached"><@s.param><a href="http://www.fieldid.com/contact"><@s.text name="label.contact_us"/></a></@s.param></@s.text>	
					</div>
				<#else>	
					<div class="upgradeUserAction center">
						<@s.url id="changeToReadOnly" action="changeToReadOnly" uniqueID="${uniqueID}"/>
						<input type="button" value="<@s.text name='hbutton.change_to_readonly'/>" onclick="return redirect('${changeToReadOnly}');"/>
					</div>
				</#if>
			</#if>
	</#if>
</div>	