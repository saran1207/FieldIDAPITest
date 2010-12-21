${action.setPageType('user','adduser')!}

<head>
	<@n4.includeStyle href="user" type="page"/>
	<title><@s.text name="label.add_user" /></title>
</head>

<#assign backToList>
	<a href="<@s.url action="userList" currentPage="${currentPage!}" listFilter="${listFilter!}" />"><@s.text name="label.cancel" /></a>
</#assign>

<@s.url id="addFullUserUrl" namespace="/" listFilter="${listFilter!}" currentPage="${currentPage!}" action="addEmployeeUser"/>
<@s.url id="addLiteUserUrl" namespace="/" listFilter="${listFilter!}" currentPage="${currentPage!}" action="addLiteUser"/>
<@s.url id="addReadOnlyUserUrl" namespace="/" listFilter="${listFilter!}" currentPage="${currentPage!}" action="addReadOnlyUser"/>

<div class="horizontalGrouping">
	
	<h2><@s.text name="label.add_user_heading" /></h2>
	
	<div class="horizontalGroup <#if !primaryOrg.liteUsersEnabled || !primaryOrg.readOnlyUsersEnabled  > twoGroup </#if> ">
		<div class="groupContents ">
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
				<@s.text name="label.full_user_limit_reached" />	
			</div>	
		<#else>
			<div class="addUserAction">
				<input id="addFullUser" type="button" value="<@s.text name="label.add_new_full_user" />" onclick="return redirect('${addFullUserUrl}');" />
			</div>
		</#if>
	</div>
	
	<#if primaryOrg.liteUsersEnabled>
		<div class="horizontalGroup increasedMargins">
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
					<@s.text name="label.lite_user_limit_reached"/>	
				</div>		
			<#else>
				<div class="addUserAction">
					<input id="addLiteUser"  type="button" value="<@s.text name="label.add_new_lite_user" />" onclick="return redirect('${addLiteUserUrl}');" />
				</div>
			</#if>
		</div>
	</#if>
	<#if primaryOrg.readOnlyUsersEnabled>
		<div class="horizontalGroup increasedMargins ">
			<div class="groupContents">
				<h2><@s.text name="label.ready_only_user" /></h2>
				<p><@s.text name="label.lite_users_that_may" /></p>
				
				<ul class="permissionListing">
					<li><label><@s.text name="label.view_their_assets" /></label></li>
					<li><label><@s.text name="label.run_searches" /></label></li>
				</ul>
			</div>	
			<div class="addUserAction">
				<input id="addReadOnlyUser" type="button" value="<@s.text name="label.add_new_read_only_user" />" onclick="return redirect('${addReadOnlyUserUrl}');"/>
			</div>
		</div>
	</#if>
</div>