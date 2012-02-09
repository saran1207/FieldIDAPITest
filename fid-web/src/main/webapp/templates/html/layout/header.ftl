
<div id="pageHeader">

	<div id="pageActions" style="float:none;">
		<#include "_companyLogo.ftl"/>
		<div id="listContainer">
			<ul class="listOfLinks">				
				<li class="first">
					<span class="welcome"><@s.text name="label.welcome"/>, </span><a href="<@s.url action="myAccount" namespace="/"/>">${sessionUser.name}</a> 
					<span class="separator">|</span>
				</li>
					<#if sessionUser.employeeUser && userSecurityGuard.allowedManageSafetyNetwork>
						<li>
		  					<a href="<@s.url action="invite" namespace="/"/>" onclick:target="_blank"><@s.text name="label.invite"/></a>
		  					<span class="separator">|</span>
						</li>
					<#elseif sessionUser.anEndUser>
						<li>
		  					<a href="http://www.fieldid.com/upgrade" target="_blank"><@s.text name="label.upgrade"/></a>
		  					<span class="separator">|</span>
						</li>
					</#if>
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
		<div id="navigationList">
			<ul>
				<li>
					<a href="/fieldid/w/dashboard" class="speedLink" id="menuHome"><img src="/fieldid/images/home.png"/></a>
				</li>
			
				<#if sessionUser.hasAccess("tag") == true >
					<#if securityGuard.integrationEnabled>
						<@s.url id="identifyUrl" action="identify"  namespace="/"/>
						
					<#else>
						<@s.url id="identifyUrl" action="assetAdd" namespace="/" />
					</#if>
					<li>
						<a href="${identifyUrl}" class="speedLink textLink" id="menuIdentify"><@s.text name="speed.identify"/></a>
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
							<a href="<@s.url value="/w/setup/settings"/>" class="speedLink textLink" id="menuSetup"><@s.text name="label.setup" /> </a>
						</li>
					<#else>
						<li>
							<a href="<@s.url value="/w/setup/ownersUsersLocations"/>" class="speedLink textLink" id="menuSetup"><@s.text name="label.setup" /> </a>
						</li>
					</#if>
				</#if>
				
			</ul>
		</div>
	</div>
	
</div>
