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

	<div class="horizontalGroup">
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
		
		<#if employeeLimitReached>
			<div class="userLimitWarning">
				<@s.text name="label.employee_user_limit_reached"><@s.param><a href="http://www.fieldid.com/contact"><@s.text name="label.contact_us"/></a></@s.param></@s.text>	
			</div>	
		<#else>
			<div class="upgradeUserAction center">
				<#if fullUser >
					<input type="button" value="<@s.text name="hbutton.current_user_type"/>" disabled="true" />
				<#else>
					<@s.url id="changeToFull" action="changeToFull" uniqueID="${uniqueID}"/>
					<input type="button" value="<@s.text name='hbutton.change_to_full'/>" onclick="return redirect('${changeToFull}');"/>
				</#if>
			</div>
		</#if>
	</div>
	
	<div class="horizontalGroup leftRightMargins">
		<div class="groupContents">
			<h2><@s.text name="label.lite_user" /></h2>
			<p><@s.text name="label.employees_that_may" /></p>
			
			<ul class="permissionListing">
				<li><label><@s.text name="label.perform_events" /></label></li>
				<li><label><@s.text name="label.run_searches" /></label></li>
			</ul>
		</div>
		
		<#if liteUserLimitReached>
			<div class="userLimitWarning">
				<@s.text name="label.lite_user_limit_reached"><@s.param><a href="http://www.fieldid.com/contact"><@s.text name="label.contact_us"/></a></@s.param></@s.text>	
			</div>		
		<#else>
			<div class="upgradeUserAction center">
				<#if liteUser >
					<input type="button" value="<@s.text name="hbutton.current_user_type"/>" disabled="true" />
				<#else>
					<@s.url id="changeToLite" action="changeToLite" uniqueID="${uniqueID}"/>
					<input type="button" value="<@s.text name='hbutton.change_to_lite'/>" onclick="return redirect('${changeToLite}');"/>
				</#if>
			</div>
		</#if>
	</div>
	
	<div class="horizontalGroup">
		<div class="groupContents">
			<h2><@s.text name="label.ready_only_user" /></h2>
			<p><@s.text name="label.lite_users_that_may" /></p>
			
			<ul class="permissionListing">
				<li><label><@s.text name="label.view_their_assets" /></label></li>
				<li><label><@s.text name="label.run_searches" /></label></li>
			</ul>
		</div>
		<div class="upgradeUserAction center">
			<#if readOnlyUser >
				<input type="button" value="<@s.text name="hbutton.current_user_type"/>" disabled="true" />
			<#else>
				<@s.url id="changeToReadOnly" action="changeToReadOnly" uniqueID="${uniqueID}"/>
				<input type="button" value="<@s.text name='hbutton.change_to_readonly'/>" onclick="return redirect('${changeToReadOnly}');"/>
			</#if>
		</div>
	</div>
</div>	