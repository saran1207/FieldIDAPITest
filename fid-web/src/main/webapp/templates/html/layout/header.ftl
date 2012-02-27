<head>

	<@n4.includeStyle href="dropdown/style"/>
    <!--[if lte IE 7]>
        <@n4.includeStyle href="dropdown/ie"/>
    <![endif]-->

	<script type="text/javascript" language="javascript" src="/fieldid/javascript/hoverIntent.js"></script>
	<script type="text/javascript" language="javascript" src="/fieldid/javascript/jquery.dropdown.js"></script>
	
	<script type="text/javascript">
		document.observe("dom:loaded", function() {
			$('searchText').observe('focus', clearDescription);
			$('searchText').observe('blur', replaceDescription);
			$('smartSearch').observe('submit', submitSmartSearch);
		});
	</script>

</head>
<div id="pageHeader" class="frontPageHeader">

	<div id="pageActions" style="float:none;">
		<#include "_companyLogo.ftl"/>
		<div id="listContainer">
			<ul class="listOfLinks">				
				<li class="first">
					<span class="welcome"><@s.text name="label.welcome"/>, </span><a href="<@s.url action="myAccount" namespace="/"/>">${sessionUser.name}</a> 
					<span class="separator">|</span>
				</li>
				<li>
		  			<a href="${action.supportUrl}" target="_blank"><@s.text name="label.support"/></a>
		  			<span class="separator">|</span>
				</li>
				<#if userSecurityGuard.allowedAccessWebStore>
					<li>
			  			<a href="<@s.url action="redirectToWebStore" namespace="/"/>" target="_blank" ><@s.text name="label.fieldid_webstore"/></a>
			  			<span class="separator">|</span>
			  		</li>
				</#if>
		  		<li>
					<a href="<@s.url action="logout"  namespace="/"/>" ><@s.text name="label.logout"/></a>
					<span class="darkseparator">|</span>
				</li>
                <li class="last">
                    <span>
                        <a href="javascript:void(0);" id="mySavedItemsLink"><@s.text name="label.my_saved_items"/></a>
                        <img src="<@s.url value='/images/down-arrow.png'/>" class="downArrow">
                        <div id="mySavedItemsBox" class="mySavedItemsBox" style="display:none;"></div>
                    </span>
                </li>
	  		</ul>
	  	</div>
  	</div> 

	<div id="pageNavigation">
		<div id="navigationContent">
			<div id="navigationLinks">
				<ul class="dropdown">
					<li>
						<a href="/fieldid/w/dashboard" class="speedLink" id="menuHome">
						</a>
					</li>
				
					<#if sessionUser.hasAccess("tag") == true >
						<#if securityGuard.integrationEnabled>
							<@s.url id="identifyUrl" action="identify"  namespace="/"/>
							
						<#else>
							<@s.url id="identifyUrl" action="assetAdd" namespace="/" />
						</#if>
						<li>
							<a href="${identifyUrl}" class="speedLink textLink" id="menuIdentify">
								<@s.text name="speed.identify"/>
								<img src="/fieldid/images/down-arrow.png" />
							</a>
							<ul class="sub_menu regular_menu">
								<li><a href="${identifyUrl}"><@s.text name="nav.single_add"/></a></li>
								<li><a href="<@s.url action='assetMultiAdd' namespace='/' />"><@s.text name="nav.multi_add"/></a></li>
								<li><a href="<@s.url action='assetImportExport' namespace='/' />"><@s.text name="nav.import"/></a></li>
							</ul>
							
						</li>
					</#if>
					
					<#if sessionUser.hasAccess("createevent") >
						<li>
							<a href="<@s.url action="startEvent" namespace="/"/>" class="speedLink textLink" id="menuEvent"><@s.text name="speed.event"/></a>
						</li>
					</#if>
					<li>
						<a href="/fieldid/w/search" class="speedLink textLink" id="menuAssets"><@s.text name="speed.search" /></a>
					</li>
					<li>
						<a href="/fieldid/w/reporting" class="speedLink textLink" id="menuReport"><@s.text name="speed.reporting" /></a>
					</li>
					<li>
						<a href="<@s.url action="schedule" namespace="/"/>" class="speedLink textLink" id="menuSchedule"><@s.text name="speed.schedules" /></a>
					</li>
					
					<#if userSecurityGuard.allowedManageSafetyNetwork>
						<li>
							<a href="<@s.url action="safetyNetwork" namespace="/"/>" class="speedLink textLink" id="menuSafetyNetwork"><@s.text name="speed.safety_network" /></a>
						</li>
					</#if>
					<#if securityGuard.projectsEnabled>
						<li>
							<a href="<@s.url action="jobs" namespace="/"/>" class="speedLink textLink" id="menuProject"><@s.text name="speed.projects"/></a>
						</li>
					</#if>
					<#if sessionUser.hasSetupAccess()>
						<#if sessionUser.hasAccess("managesystemconfig")>
							<li>
								<a href="<@s.url value="/w/setup/settings"/>" class="speedLink textLink" id="menuSetup">
									<@s.text name="label.setup" />
									<img src="/fieldid/images/down-arrow.png" />
								</a>
								<ul class="sub_menu">
									<li>
										<a href="<@s.url value='/w/setup/settings'/>" ><@s.text name="nav.settings" /></a>
										<ul class="sub_menu">
											<li><a href="<@s.url action='organizations'/>"><@s.text name="title.manage_organizational_units.plural"/></a></li>
											<li><a href="<@s.url value='/w/setup/systemSettings'/>"><@s.text name="nav.system_settings"/></a></li>
											<li><a href="<@s.url action='quickSetupWizard/startWizard'/>"><@s.text name="label.quick_setup_wizard"/></a></li>
											<li><a href="<@s.url action='branding'/>"><@s.text name="title.manage_branding.plural"/></a></li>
											<li><a href="<@s.url value='/w/setup/yourPlan'/>"><@s.text name="title.manage_field_id_plan.plural"/></a></li>
										</ul>
									</li>
									<li>
										<a href="<@s.url value="/w/setup/ownersUsersLocations" />" ><@s.text name="nav.owners_users_loc"/></a>
										<ul class="sub_menu">
											<#if sessionUser.hasAccess("manageendusers") >
												<li><a href="<@s.url action='customerList' namespace='/' />" ><@s.text name="title.manage_customers.plural"/></a></li>
												<li><a href="<@s.url action='userList' namespace='/' />" ><@s.text name="title.manage_users.plural"/></a></li>
												<#if sessionUser.tenant.settings.userLimits.maxReadOnlyUsers != 0>
													<li><a href="<@s.url action='userRequestList' namespace='/' />" ><@s.text name="title.manage_user_registrations.plural"/></a></li>
												</#if>
											</#if>
											<#if sessionUser.hasAccess("managesystemconfig") && locationHeirarchyFeatureEnabled>
												<li><a href="<@s.url action='predefinedLocations' namespace='/' />" ><@s.text name="title.manage_predefined_locations.plural"/></a></li>
											</#if>
										</ul>
									</li>
									<li>
										<a href="<@s.url value="/w/setup/assetsEvents" />" ><@s.text name="nav.assets_and_events"/></a>
										<#if sessionUser.hasAccess("managesystemconfig") >
											<ul class="sub_menu">
												<li><a href="<@s.url action="eventTypeGroups"/>" ><@s.text name="title.manage_event_type_groups.plural"/></a></li>
												<li><a href="<@s.url action="eventTypes"/>" ><@s.text name="title.manage_event_types.plural"/></a></li>
												<li><a href="<@s.url action="eventBooks"/>" ><@s.text name="title.manage_event_books.plural"/></a></li>
												<li><a href="<@s.url action="assetTypeGroups"/>" ><@s.text name="title.manage_asset_type_groups.plural"/></a></li>	
												<li><a href="<@s.url action="assetTypes"/>" ><@s.text name="title.manage_asset_types.plural"/></a></li>	
												<li><a href="<@s.url action="assetStatusList"/>" ><@s.text name="title.manage_asset_statuses.plural"/></a></li>	
											</ul>
										</#if>
									</li>
									<li>
										<a href="<@s.url value="/w/setup/import" />" ><@s.text name="nav.import"/></a>
										<ul class="sub_menu">
											<li><a href="<@s.url action='customerImportExport'/>"><@s.text name="label.import_owners"/></a></li>
											<li><a href="<@s.url action='assetImportExport'/>"><@s.text name="label.import_assets"/></a></li>
											<li><a href="<@s.url action='eventImportExport'/>"><@s.text name="label.import_events"/></a></li>
											<li><a href="<@s.url action='autoAttributeImportExport'/>"><@s.text name="label.import_auto_attributes"/></a></li>
											<li><a href="<@s.url action='userImportExport'/>"><@s.text name="label.import_users"/></a></li>
										</ul>
									</li>
									<li>
										<a href="<@s.url value="/w/setup/templates" />" ><@s.text name="nav.templates"/></a>
										<#if sessionUser.hasAccess("managesystemconfig") >
											<ul class="sub_menu">
												<li><a href="<@s.url action="autoAttributeCriteriaList"/>" ><@s.text name="title.auto_attribute_wizard.plural" /></a></li>
												<li><a href="<@s.url action="commentTemplateList"/>" ><@s.text name="title.manage_comment_templates.plural" /></a></li>
												<li><a href="<@s.url value='/w/setup/columnsLayout' type='ASSET'/>" ><@s.text name="title.column_layout_asset" /></a></li>
												<li><a href="<@s.url value='/w/setup/columnsLayout' type='EVENT'/>" ><@s.text name="title.column_layout_event" /></a></li>
												<li><a href="<@s.url value='/w/setup/columnsLayout' type='SCHEDULE'/>" ><@s.text name="title.column_layout_schedule" /></a></li>
												<#if securityGuard.integrationEnabled>
													<li><a href="<@s.url action="assetCodeMappingList"/>" ><@s.text name="title.manage_asset_code_mappings.plural" /></a></li>
												</#if>
											</ul>
										</#if>
									</li>
									<li>
										<a href="<@s.url value="/w/setup/widgets" />" ><@s.text name="nav.widgets"/></a>
									</li>
									<li>
										<a href="<@s.url value="/w/setup/security" />" ><@s.text name="nav.security"/></a>
										<ul class="sub_menu">
											<li><a href="<@s.url value='/w/setup/passwordPolicy'/>"><@s.text name="title.password_policy"/></a></li>
											<li><a href="<@s.url value='/w/setup/accountPolicy'/>"><@s.text name="title.account_lockout_policy"/></a></li>
										</ul>
										
									</li>
								</ul>
								
							</li>
						<#else>
							<li>
								<a href="<@s.url value="/w/setup/ownersUsersLocations"/>" class="speedLink textLink" id="menuSetup"><@s.text name="label.setup" /> </a>
							</li>
						</#if>
					</#if>
					
				</ul>
			</div>
		   	<div id="smartSearchContainer">
				<@s.form method="get" action="assetInformation" namespace="/" id="smartSearch" theme="fieldid" >
					<@s.hidden name="useContext" value="true"/>
					<@s.hidden name="usePagination" value="true"/>
					<@s.textfield name="search" id="searchText" value="${action.getText('label.search')}" cssClass="description"/>
				</@s.form>
			</div>

		</div>
		
	</div>
	
</div>