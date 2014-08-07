<#--${action.setPageType('user','adduser')!}-->

<head>
	<@n4.includeStyle href="../newCss/user/user_select" type="page"/>
    <@n4.includeStyle href="../newCss/component/matt_buttons" type="page"/>
    <@n4.includeStyle href="../plugins/tipsy/tipsy.css" type="page"/>
    <@n4.includeScript src="tipsy/jquery.tipsy.js"/>
	<title><@s.text name="label.add_user" /></title>
</head>

<@s.url id="addFullUserUrl" namespace="/" listFilter="${listFilter!}" currentPage="${currentPage!}" action="addEmployeeUser"/>
<@s.url id="addLiteUserUrl" namespace="/" listFilter="${listFilter!}" currentPage="${currentPage!}" action="addLiteUser"/>
<@s.url id="addReadOnlyUserUrl" namespace="/" listFilter="${listFilter!}" currentPage="${currentPage!}" action="addReadOnlyUser"/>
<@s.url id="addPersonUrl" namespace="/" value="w/setup/addPerson"/>
<@s.url id="addUsageBasedUserUrl" namespace="/" value="w/setup/addUsageBasedUser"/>

<div class="viewLinks">
    <a class="mattButtonLeft" href="/fieldid/w/setup/usersList"><@s.text name="nav.view_all"/></a>
    <a class="mattButtonRight" href="/fieldid/w/setup/archivedUsersList">"><@s.text name="nav.archived"/></a>
    <a class="mattButton padLeft" href="<@s.url action='userImportExport'/>"><@s.text name="nav.import"/></a>
</div>

<script>
    jQuery(document).ready(function() { jQuery('.limitIcon').tipsy({gravity:'e', fade:true, delayIn:250})});
</script>

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
			<div class="addUserAction">
                <#if userLimitService.employeeUsersAtMax>
                    <img class="limitIcon" src="/fieldid/images/warning-icon.png" title="<@s.text name="label.full_user_limit_reached" />	">
                <#else>
                    <input id="addFullUser" type="button" value="<@s.text name="label.add_new_full_user" />" onclick="return redirect('${addFullUserUrl}');" />
                </#if>
			</div>
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

            <div class="addUserAction">
                <#if userLimitService.liteUsersAtMax>
                    <img class="limitIcon" src="/fieldid/images/warning-icon.png" title="<@s.text name="label.lite_user_limit_reached" />	">
                <#else>
                    <input id="addLiteUser"  type="button" value="<@s.text name="label.add_new_lite_user" />" onclick="return redirect('${addLiteUserUrl}');" />
                </#if>
            </div>
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
            <div class="addUserAction">
                <#if userLimitService.readOnlyUsersAtMax>
                    <img class="limitIcon" src="/fieldid/images/warning-icon.png" title="<@s.text name="label.readonly_user_limit_reached" />	">
                <#else>
                    <input id="addReadOnlyUser" type="button" value="<@s.text name="label.add_new_read_only_user" />" onclick="return redirect('${addReadOnlyUserUrl}');"/>
                </#if>
            </div>

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

    <#if userLimitService.usageBasedUsersEnabled>
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
    </#if>

</div>