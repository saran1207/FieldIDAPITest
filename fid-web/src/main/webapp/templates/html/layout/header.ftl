
<div id="pageHeader">
	
		<div id="pageActions" style="float:none;">
		<div id="listContainer">
			<ul class="listOfLinks">				
				<li class="first">
					<@s.text name="label.welcome"/>, <a href="<@s.url action="myAccount" namespace="/"/>">${sessionUser.name}</a> 
				</li>
					<#if sessionUser.employeeUser && userSecurityGuard.allowedManageSafetyNetwork>
						<li>
		  					<a href="<@s.url action="invite" namespace="/"/>" onclick:target="_blank"><@s.text name="label.invite"/></a>
						</li>
					<#elseif sessionUser.anEndUser>
						<li>
		  					<a href="http://www.fieldid.com/upgrade" target="_blank"><@s.text name="label.upgrade"/></a>
						</li>
					</#if>
				<li>
		  			<a href="http://help.fieldid.com/" target="_blank"><@s.text name="label.support"/></a>
				</li>
				<#if userSecurityGuard.allowedAccessWebStore>
					<li>
			  			<a href="<@s.url action="redirectToWebStore" namespace="/"/>" target="_blank" ><@s.text name="label.fieldid_webstore"/></a>
			  		</li>
				</#if>
		  		<li>
					<a href="<@s.url action="logout"  namespace="/"/>" ><@s.text name="label.logout"/></a>
				</li>
	  		</ul>
	  	</div>
  	</div> 

	<#include "_companyLogo.ftl"/>
	
	<div id="pageNavigation">
		<ul>
			<li>
				<a href="/fieldid/w/dashboard" class="speedLink" id="menuHome"><@s.text name="speed.home"/></a>
			</li>
		
			<#if sessionUser.hasAccess("tag") == true >
				<#if securityGuard.integrationEnabled>
					<@s.url id="identifyUrl" action="identify"  namespace="/"/>
					
				<#else>
					<@s.url id="identifyUrl" action="assetAdd" namespace="/" />
				</#if>
				<li>
					<a href="${identifyUrl}" class="speedLink" id="menuIdentify"><@s.text name="speed.identify"/></a>
				</li>
			</#if>
			
			<#if sessionUser.hasAccess("createevent") >
				<li>
					<a href="<@s.url action="startEvent" namespace="/"/>" class="speedLink" id="menuEvent"><@s.text name="speed.event"/></a>
				</li>
			</#if>
			<li>
				<a href="/fieldid/w/search" class="speedLink" id="menuAssets"><@s.text name="speed.search" /></a>
			</li>
			<li>
				<a href="/fieldid/w/reporting" class="speedLink" id="menuReport"><@s.text name="speed.reporting" /></a>
			</li>
			<li>
				<a href="<@s.url action="schedule" namespace="/"/>" class="speedLink" id="menuSchedule"><@s.text name="speed.schedules" /></a>
			</li>
			
			<#if userSecurityGuard.allowedManageSafetyNetwork>
				<li>
					<a href="<@s.url action="safetyNetwork" namespace="/"/>" class="speedLink" id="menuSafetyNetwork"><@s.text name="speed.safety_network" /></a>
				</li>
			</#if>
			<#if securityGuard.projectsEnabled>
				<li>
					<a href="<@s.url action="jobs" namespace="/"/>" class="speedLink" id="menuProject"><@s.text name="speed.projects"/></a>
				</li>
			</#if>
			<#if sessionUser.hasSetupAccess()>
				<#if sessionUser.hasAccess("managesystemconfig")>
					<li>
						<a href="<@s.url value="/w/setup/settings"/>" class="speedLink" id="menuSetup"><@s.text name="label.setup" /> </a>
					</li>
				<#else>
					<li>
						<a href="<@s.url value="/w/setup/ownersUsersLocations"/>" class="speedLink" id="menuSetup"><@s.text name="label.setup" /> </a>
					</li>
				</#if>
			</#if>
			
		</ul>
	</div>
	
</div>
