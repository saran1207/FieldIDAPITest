<#--${action.setPageType('user','adduser')!}-->

<head>
	<@n4.includeStyle href="../newCss/user/user_select" type="page"/>
    <@n4.includeStyle href="../newCss/component/matt_buttons" type="page"/>
	<title><@s.text name="label.add_user" /></title>
</head>

<@s.url id="addFullUserUrl" namespace="/" listFilter="${listFilter!}" currentPage="${currentPage!}" action="addEmployeeUser"/>
<@s.url id="addLiteUserUrl" namespace="/" listFilter="${listFilter!}" currentPage="${currentPage!}" action="addLiteUser"/>
<@s.url id="addReadOnlyUserUrl" namespace="/" listFilter="${listFilter!}" currentPage="${currentPage!}" action="addReadOnlyUser"/>
<@s.url id="addPersonUrl" namespace="/" value="w/addPerson"/>
<@s.url id="addUsageBasedUserUrl" namespace="/" value="w/addUsageBasedUser"/>

<div class="viewLinks">
    <a class="mattButtonLeft" href="<@s.url action='userList'/>"><@s.text name="nav.view_all"/></a>
    <a class="mattButtonRight" href="<@s.url action='archivedUserList'/>"><@s.text name="nav.archived"/></a>
    <a class="mattButton padLeft" href="<@s.url action='userImportExport'/>"><@s.text name="nav.import"/></a>
</div>

<div class="horizontalGrouping">
	<div class="horizontalGroup">
		<div class="groupContents ">
            <img src="<@s.url value="/images/icon-full.png"/>">
			<h2><@s.text name="label.full_user" /></h2>

			<ul class="permissionListing">
				<li><label><@s.text name="label.add_new_data" /></label></li>
				<li><label><@s.text name="label.perform_events" /></label></li>
				<li><label><@s.text name="label.manage_system_configuration" /></label></li>
				<li><label><@s.text name="label.run_searches" /></label></li>
			</ul>
		</div>
		<#if userLimitService.employeeUsersAtMax>
			<div class="userLimitWarning">
				<@s.text name="label.full_user_limit_reached" />	
			</div>	
		<#else>
			<div class="addUserAction">
				<input id="addFullUser" type="button" value="<@s.text name="label.add_new_full_user" />" onclick="return redirect('${addFullUserUrl}');" />
			</div>
		</#if>
	</div>
	
    <div class="horizontalGroup">
        <div class="groupContents">
            <img src="<@s.url value="/images/icon-lite.png"/>">
            <h2><@s.text name="label.lite_user" /></h2>

            <ul class="permissionListing">
                <li><label><@s.text name="label.perform_events" /></label></li>
                <li><label><@s.text name="label.run_searches" /></label></li>
            </ul>
        </div>

        <#if userLimitService.liteUsersAtMax>
            <div class="userLimitWarning">
                <@s.text name="label.lite_user_limit_reached"/>
            </div>
        <#else>
            <div class="addUserAction">
                <input id="addLiteUser"  type="button" value="<@s.text name="label.add_new_lite_user" />" onclick="return redirect('${addLiteUserUrl}');" />
            </div>
        </#if>
    </div>

    <div class="horizontalGroup">
        <div class="groupContents">
            <img src="<@s.url value="/images/icon-readonly.png"/>">
            <h2><@s.text name="label.ready_only_user" /></h2>

            <ul class="permissionListing">
                <li><label><@s.text name="label.view_their_assets" /></label></li>
                <li><label><@s.text name="label.run_searches" /></label></li>
            </ul>
        </div>
        <#if userLimitService.readOnlyUsersAtMax>
            <div class="userLimitWarning">
                <@s.text name="label.readonly_user_limit_reached"/>
            </div>
        <#else>
            <div class="addUserAction">
                <input id="addReadOnlyUser" type="button" value="<@s.text name="label.add_new_read_only_user" />" onclick="return redirect('${addReadOnlyUserUrl}');"/>
            </div>
        </#if>
    </div>

    <div class="horizontalGroup">
        <div class="groupContents ">
            <img src="<@s.url value="/images/icon-person.png"/>">
            <h2><@s.text name="label.person"/></h2>

            <ul class="permissionListing">
                <li><label><@s.text name="label.no_fid_login" /></label></li>
                <li><label><@s.text name="label.track_assets" /></label></li>
            </ul>
        </div>
        <div class="addUserAction">
            <input id="addPerson" type="button" value="<@s.text name="label.add_new_person" />" onclick="return redirect('${addPersonUrl}');"/>
        </div>
    </div>


    <div class="horizontalGroup">
        <div class="groupContents ">
            <img src="<@s.url value="/images/usage-based-user.png"/>">
            <h2><@s.text name="label.usage_based_user"/></h2>

            <ul class="permissionListing">
                <li><label><@s.text name="label.perform_events" /></label></li>
                <li><label><@s.text name="label.run_searches" /></label></li>
            </ul>
        </div>
        <div class="addUserAction">
            <input id="addUsageBasedUser" type="button" value="<@s.text name="label.add_new_usage_based_user" />" onclick="return redirect('${addUsageBasedUserUrl}');"/>
        </div>
    </div>

</div>