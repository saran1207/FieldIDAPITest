<head>
    <@n4.includeStyle href="../newCss/user/user_select" type="page"/>
    <@n4.includeStyle href="../newCss/component/matt_buttons" type="page"/>
    <title><@s.text name="label.upgrade" /></title>
</head>

<div class="viewLinks">
    <a class="mattButtonLeft" href="/fieldid/w/setup/usersList"><@s.text name="nav.view_all"/></a>
    <a class="mattButtonRight" href="/fieldid/w/setup/archivedUsersList"><@s.text name="nav.archived"/></a>
    <a class="mattButton padLeft" href="<@s.url action='userImportExport'/>"><@s.text name="nav.import"/></a>
</div>

<div class="horizontalGrouping">

	<div class="horizontalGroup">
		<div class="groupContents">
            <img src="<@s.url value="/images/icon-full.png"/>">
            <h2><@s.text name="label.full_user" /></h2>

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
		
	<div class="horizontalGroup">
		<div class="groupContents">
            <img src="<@s.url value="/images/icon-lite.png"/>">
			<h2><@s.text name="label.lite_user" /></h2>

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
	
    <div class="horizontalGroup">
        <div class="groupContents">
            <img src="<@s.url value="/images/icon-readonly.png"/>">
            <h2><@s.text name="label.ready_only_user" /></h2>

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
            <#if userLimitService.readOnlyUsersAtMax>
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
    </div>

    <div class="horizontalGroup">
        <div class="groupContents">
            <img src="<@s.url value="/images/icon-person.png"/>">
            <h2><@s.text name="label.person" /></h2>

            <ul class="permissionListing">
                <li><label><@s.text name="label.no_fid_login" /></label></li>
                <li><label><@s.text name="label.track_assets" /></label></li>
            </ul>
        </div>
        <#if person >
            <div class="upgradeUserAction center">
                <input type="button" value="<@s.text name="hbutton.current_user_type"/>" disabled="true" />
            </div>
        <#else>
            <div class="upgradeUserAction center">
                <@s.url id="changeToPerson" action="changeToPerson" uniqueID="${uniqueID}"/>
                <input type="button" value="<@s.text name='hbutton.change_to_person'/>" onclick="return redirect('${changeToPerson}');"/>
            </div>
        </#if>
    </div>

    <#if userLimitService.usageBasedUsersEnabled>
        <div class="horizontalGroup">
            <div class="groupContents">
                <img src="<@s.url value="/images/usage-based-user.png"/>">
                <h2><@s.text name="label.usage_based_user" /></h2>

                <ul class="permissionListing">
                    <li><label><@s.text name="label.perform_events" /></label></li>
                    <li><label><@s.text name="label.run_searches" /></label></li>
                </ul>
            </div>
            <#if usageBasedUser >
                <div class="upgradeUserAction center">
                    <input type="button" value="<@s.text name="hbutton.current_user_type"/>" disabled="true" />
                </div>
            <#else>
                <div class="upgradeUserAction center">
                    <@s.url id="changeToUsageBased" action="changeToUsageBased" uniqueID="${uniqueID}"/>
                    <input type="button" value="<@s.text name='hbutton.change_to_usage_based'/>" onclick="return redirect('${changeToUsageBased}');"/>
                </div>
            </#if>
        </div>
    </#if>
