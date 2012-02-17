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
								<li>
									<a href="<@s.url action='assetMultiAdd' namespace='/' />" class="speedLink">
										<@s.text name="nav.multi_add"/>
									</a>
								</li>
								<li>
									<a href="<@s.url action='assetImportExport' namespace='/' />" class="speedLink">
										<@s.text name="nav.import"/>
									</a>
								</li>
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
								
								<ul class="sub_menu wide_menu">
									<li>
										<a href="<@s.url value="/w/setup/ownersUsersLocations" />" class="speedLink"><@s.text name="nav.owners_users_loc"/></a>
									</li>
									<li>
										<a href="<@s.url value="/w/setup/assetsEvents" />" class="speedLink"><@s.text name="nav.assets_and_events"/></a>
									</li>
									<li>
										<a href="<@s.url value="/w/setup/import" />" class="speedLink"><@s.text name="nav.import"/></a>
									</li>
									<li>
										<a href="<@s.url value="/w/setup/templates" />" class="speedLink"><@s.text name="nav.templates"/></a>
									</li>
									<li>
										<a href="<@s.url value="/w/setup/widgets" />" class="speedLink"><@s.text name="nav.widgets"/></a>
									</li>
									<li>
										<a href="<@s.url value="/w/setup/security" />" class="speedLink"><@s.text name="nav.security"/></a>
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