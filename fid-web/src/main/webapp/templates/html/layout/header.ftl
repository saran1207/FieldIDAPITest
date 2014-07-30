<head>

	<@n4.includeStyle href="dropdown/style"/>
    <!--[if lte IE 7]>
        <@n4.includeStyle href="dropdown/ie"/>
    <![endif]-->

    <@n4.includeScript src="jquery.dropdown.js"/>

    <script type="text/javascript" src="//use.typekit.net/usa4tou.js"></script>
    <script type="text/javascript">try{Typekit.load();}catch(e){}</script>

	<script type="text/javascript">
		document.observe("dom:loaded", function() {
			$('searchText').observe('focus', clearDescription);
			$('searchText').observe('blur', replaceDescription);
			$('smartSearch').observe('submit', submitSmartSearch);
		});
	</script>

	<!--[if lte IE 8]>
        <link wicket:id="dropDownIECss" type="text/css" href="/fieldid/style/legacy/dropdown/ie.css" rel="stylesheet" media="all"/>
    <![endif]-->
    <script type="text/javascript">
        ${action.getCustomJs()}
    </script>

</head>
<div id="pageHeader" class="frontPageHeader struts-header">

	<div id="pageActions" class="clearfix content-wrap">
		<#include "_companyLogo.ftl"/>
		<div id="listContainer">
			<ul class="listOfLinks">				
				<li class="first">
					<@s.text name="label.welcome"/>, <a href="<@s.url action="myAccount" namespace="/"/>">${sessionUser.name}</a>
                </li>
                <#if action.isMultiLanguage()>
                    <li>
                        <a id="selectLanguageLink" href="javascript:void(0);"><@s.text name="label.language"/></a>

                        <script type="text/javascript">

                            jQuery(document).ready(function(){
                                jQuery("#selectLanguageLink").colorbox({iframe: true, href: '<@s.url value="/w/selectLanguage" />', width: '500px', height:'380px'});
                            });

                        </script>
                    </li>
                </#if>
				<li>
		  			<a href="${action.supportUrl}" target="_blank"><@s.text name="label.support"/></a>
				</li>
		  		<li>
					<a href="<@s.url action="logout"  namespace="/"/>" ><@s.text name="label.logout"/></a>
				</li>
                <li class="last">
                    <span>
                        <a href="javascript:void(0);" id="mySavedItemsLink"><@s.text name="label.my_saved_items"/></a>
                        <!-- <img src="<@s.url value='/images/down-arrow.png'/>" class="downArrow"> -->
                        <div id="mySavedItemsBox" class="mySavedItemsBox" style="display:none;"></div>
                    </span>
                </li>
	  		</ul>
	  	</div>
  	</div> 

	<div id="pageNavigation">
		<div id="navigationContent" class="content-wrap">
			<div id="navigationLinks">
				<ul class="dropdown clearfix">
					<li>
						<a href="/fieldid/w/dashboard" class="speedLink" id="menuHome"><span class="fontello-home">Dashboard</span></a>
					</li>
				
					<#if sessionUser.hasAccess("tag") == true >
						<#if securityGuard.integrationEnabled>
							<@s.url id="identifyUrl" action="identify"  namespace="/"/>
							
						<#else>
							<@s.url id="identifyUrl" action="assetAdd" namespace="/" />
						</#if>
						<li>
                            <#if securityGuard.integrationEnabled>
                                <a href="<@s.url action="identify" namespace="/"/>" class="speedLink textLink" id="menuIdentify">
                                    <@s.text name="speed.identify"/>
                                </a>
                            <#else>
                                <a href="/fieldid/w/identify" class="speedLink textLink" id="menuIdentify">
                                    <@s.text name="speed.identify"/>
                                </a>
                            </#if>
						</li>
					</#if>
					
					<#if sessionUser.hasAccess("createevent") && sessionUser.tenant.settings.isInspectionsEnabled()>
						<li>
							<a href="<@s.url action="startEvent" namespace="/"/>" class="speedLink textLink" id="menuEvent"><@s.text name="speed.event"/></a>
						</li>
					</#if>

					<li>
						<a href="/fieldid/w/search" class="speedLink textLink" id="menuAssets"><@s.text name="speed.search" /></a>
                     </li>

                    <#if sessionUser.tenant.settings.isInspectionsEnabled()>
                        <li>
                            <a href="/fieldid/w/reporting" class="speedLink textLink struts" id="menuReport"><@s.text name="speed.reporting" />
                                <#if securityGuard.isCriteriaTrendsEnabled() || securityGuard.isAdvancedEventSearchEnabled()>
                                    <!-- <img src="/fieldid/images/down-arrow.png" /> -->
                                </#if>
                            </a>

                            <#if securityGuard.isCriteriaTrendsEnabled()>
                                <ul class="sub_menu">
                                    <li>
                                        <a href="<@s.url value='/w/criteriaTrends'/>" ><@s.text name="nav.trending" /></a>
                                    </li>
                                </ul>
                            </#if>
                            <#if securityGuard.isAdvancedEventSearchEnabled()>
                                <ul class="sub_menu">
                                    <li>
                                        <a href="<@s.url value='/w/advancedEventSearch'/>" ><@s.text name="nav.advanced_event_search" /></a>
                                    </li>
                                </ul>
                            </#if>
                        </li>
                    </#if>

                    <li>
                        <a href="/fieldid/w/places" class="speedLink textLink" id="menuPlaces"><@s.text name="speed.places" /></a>
                    </li>

                    <#if securityGuard.lotoProceduresEnabled && sessionUser.tenant.settings.isLotoEnabled()>
                        <li>
                            <a href="javascript:void(0)" class="speedLink textLink" id="menuLoto"><@s.text name="speed.lockout_tagout"/>&nbsp;</a>
                            <ul class="sub_menu">
                                <li>
                                    <a href="/fieldid/w/publishedListAllPage"><@s.text name="speed.loto_list" /></a>
                                </li>
                                <li>
                                    <a href="/fieldid/w/procedureWaitingApprovals"><@s.text name="speed.loto_procedure_approvals" /></a>
                                </li>
                                <li>
                                    <a href="/fieldid/w/procedureAuditListPage"><@s.text name="speed.procedure_audits" /></a>
                                </li>
                                <li>
                                    <a href="/fieldid/w/procedure"><@s.text name="speed.loto_log" /></a>
                                </li>
                            </ul>
                        </li>
                    </#if>

					<#if userSecurityGuard.allowedManageSafetyNetwork && sessionUser.tenant.settings.isInspectionsEnabled()>
						<li>
							<a href="<@s.url action="safetyNetwork" namespace="/"/>" class="speedLink textLink" id="menuSafetyNetwork"><@s.text name="speed.safety_network" /></a>
						</li>
					</#if>
					<#if securityGuard.projectsEnabled && sessionUser.tenant.settings.isInspectionsEnabled()>
						<li>
							<a href="<@s.url action="jobs" namespace="/"/>" class="speedLink textLink" id="menuProject"><@s.text name="speed.projects"/></a>
						</li>
					</#if>
					<#if sessionUser.hasSetupAccess()>
						<#if sessionUser.hasAccess("managesystemconfig")>
							<li>
								<a href="<@s.url value="/w/setup/settings"/>" class="speedLink textLink" id="menuSetup">
									<@s.text name="label.setup" />
								</a>
								<ul class="sub_menu">
									<li>
										<a href="<@s.url value='/w/setup/settings'/>" ><@s.text name="nav.settings" /> » </a>
										<ul class="sub_menu">
											<li><a href="<@s.url action='organizations' namespace='/'/>"><@s.text name="title.manage_organizational_units.plural"/></a></li>
											<li><a href="<@s.url value='/w/setup/systemSettings'/>"><@s.text name="nav.system_settings"/></a></li>
											<li><a href="<@s.url action='quickSetupWizard/startWizard' namespace='/'/>"><@s.text name="label.quick_setup_wizard"/></a></li>
											<li><a href="<@s.url action='branding' namespace='/'/>"><@s.text name="title.manage_branding.plural"/></a></li>
											<li><a href="<@s.url value='/w/setup/yourPlan'/>"><@s.text name="title.manage_field_id_plan.plural"/></a></li>
										</ul>
									</li>
									<li>
										<a href="<@s.url value="/w/setup/ownersUsersLocations" />" ><@s.text name="nav.owners_users_loc"/> » </a>
										<ul class="sub_menu">
											<#if sessionUser.hasAccess("manageendusers") >
												<li><a href="<@s.url action='customerList' namespace='/' />" ><@s.text name="label.customers"/></a></li>
												<li><a href="<@s.url action='userList' namespace='/' />" ><@s.text name="label.users"/></a></li>
                                                <li><a href="/fieldid/w/setup/userGroups" ><@s.text name="label.user_groups"/></a></li>
												<#if sessionUser.tenant.settings.userLimits.maxReadOnlyUsers != 0>
													<li><a href="<@s.url action='userRequestList' namespace='/' />" ><@s.text name="title.manage_user_registrations.plural"/></a></li>
												</#if>
											</#if>
											<#if sessionUser.hasAccess("manageendusers") && locationHeirarchyFeatureEnabled>
												<li><a href="<@s.url action='predefinedLocations' namespace='/' />" ><@s.text name="title.manage_predefined_locations.plural"/></a></li>
											</#if>
										</ul>
									</li>
									<li>
										<a href="<@s.url value="/w/setup/assetsEvents" />" ><@s.text name="nav.assets_and_events"/> » </a>
										<#if sessionUser.hasAccess("managesystemconfig") >
											<ul class="sub_menu">
												<li><a href="<@s.url action='eventTypeGroups' namespace='/'/>" ><@s.text name="title.manage_event_type_groups.plural"/></a></li>
												<li><a href="<@s.url action='eventTypes' namespace='/'/>" ><@s.text name="title.manage_event_types.plural"/></a></li>
                                                <li><a href="<@s.url value='/w/eventStatusList' namespace='/'/>" ><@s.text name="title.manage_event_status.plural"/></a></li>
												<li><a href="<@s.url action='eventBooks' namespace='/'/>" ><@s.text name="title.manage_event_books.plural"/></a></li>
												<li><a href="<@s.url action='assetTypeGroups' namespace='/'/>" ><@s.text name="title.manage_asset_type_groups.plural"/></a></li>
												<li><a href="<@s.url value='/w/setup/assetTypes' namespace='/'/>" ><@s.text name="title.manage_asset_types.plural"/></a></li>
												<li><a href="<@s.url action='assetStatusList' namespace='/'/>" ><@s.text name="title.manage_asset_statuses.plural"/></a></li>
                                                <#if sessionUser.tenant.settings.isInspectionsEnabled()>
                                                    <li><a href="<@s.url value='/w/setup/priorityCodes' namespace='/'/>" ><@s.text name="title.manage_priority_code.plural"/></a></li>
                                                </#if>
											</ul>
										</#if>
									</li>
									<li>
										<a href="<@s.url value="/w/setup/import" />" ><@s.text name="nav.import"/> » </a>
										<ul class="sub_menu">
											<li><a href="<@s.url action='customerImportExport' namespace='/'/>"><@s.text name="label.import_owners"/></a></li>
											<li><a href="<@s.url action='assetImportExport' namespace='/'/>"><@s.text name="label.import_assets"/></a></li>
											<li><a href="<@s.url action='eventImportExport' namespace='/'/>"><@s.text name="label.import_events"/></a></li>
											<li><a href="<@s.url action='autoAttributeImportExport' namespace='/'/>"><@s.text name="label.import_auto_attributes"/></a></li>
											<li><a href="<@s.url action='userImportExport' namespace='/'/>"><@s.text name="label.import_users"/></a></li>
										</ul>
									</li>
									<li>
										<a href="<@s.url value="/w/setup/templates" />" ><@s.text name="nav.templates"/> » </a>
										<#if sessionUser.hasAccess("managesystemconfig") >
											<ul class="sub_menu">
												<li><a href="<@s.url action='autoAttributeCriteriaList' namespace='/'/>" ><@s.text name="title.auto_attribute_wizard.plural" /></a></li>
												<li><a href="<@s.url action='commentTemplateList' namespace='/'/>" ><@s.text name="title.manage_comment_templates.plural" /></a></li>
												<li><a href="<@s.url value='/w/setup/columnsLayout' type='ASSET'/>" ><@s.text name="title.column_layout_asset" /></a></li>
                                                <#if sessionUser.tenant.settings.isInspectionsEnabled()>
												    <li><a href="<@s.url value='/w/setup/columnsLayout' type='EVENT'/>" ><@s.text name="title.column_layout_event" /></a></li>
                                                </#if>
												<#if securityGuard.integrationEnabled>
													<li><a href="<@s.url action="assetCodeMappingList"/>" ><@s.text name="title.manage_asset_code_mappings" /></a></li>
												</#if>
											</ul>
										</#if>
									</li>
									<li>
										<a href="<@s.url value="/w/setup/widgets" />" ><@s.text name="nav.widgets"/></a>
									</li>
									<li>
										<a href="<@s.url value="/w/setup/security" />" ><@s.text name="nav.security"/> » </a>
										<ul class="sub_menu">
											<li><a href="<@s.url value='/w/setup/passwordPolicy'/>"><@s.text name="title.password_policy"/></a></li>
											<li><a href="<@s.url value='/w/setup/accountPolicy'/>"><@s.text name="title.account_lockout_policy"/></a></li>
										</ul>
									</li>
                                    <li>
                                        <a href="javascript:void(0)" ><@s.text name="speed.lockout_tagout"/> » </a>
                                        <ul class="sub_menu">
                                            <li><a href="<@s.url value='/w/setup/procedureApprover'/>"><@s.text name="title.set_procedure_approver"/></a></li>
                                            <li><a href="<@s.url value='/w/setup/enableByAssetType'/>"><@s.text name="title.enable_by_asset_type"/></a></li>
                                        </ul>
                                    </li>
                                    <li>
                                        <a href="<@s.url value="/w/setup/assetTypeGroupTranslations" />" ><@s.text name="title.translations"/></a>
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