<div id="pageHeader">
	<div id="pageActions" style="float:none;">
		<ul class="listOfLinks">				
			<li class="first">
				<@s.text name="label.welcome"/>, ${sessionUser.userID} 
			</li>
	  		<li>
	  			<a href="<@s.url action="myAccount" namespace="/"/>"><@s.text name="label.myaccount"/></a>
			</li>
	  		<li>
	  			<a href="http://www.n4systems.com/support" target="_blank"><@s.text name="label.help_support"/></a>
			</li>
			<#if userSecurityGuard.allowedAccessWebStore>
			<li>
	  			<a href="<@s.url action="redirectToWebStore"  namespace="/"/>" target="_blank" ><@s.text name="label.fieldid_webstore"/></a>
	  		</li>
			</#if>	  		
	  		<li>
				<a href="<@s.url action="logout"  namespace="/"/>" ><@s.text name="label.logout"/></a>
			</li>
  		</ul>
  	</div> 

	<#include "_companyLogo.ftl"/>
	
	<div id="pageNavigation">
		<ul>
			<li>
				<a href="<@s.url action="home"  namespace="/"/>" class="speedLink" id="menuHome"><@s.text name="speed.home"/></a>
			</li>
		
			<#if sessionUser.hasAccess("tag") == true >
				<#if securityGuard.integrationEnabled>
					<@s.url id="identifyUrl" action="identify"  namespace="/"/>
					
				<#else>
					<@s.url id="identifyUrl" action="productAdd" namespace="/" />
				</#if>
				<li>
					<a href="${identifyUrl}" class="speedLink" id="menuIdentify"><@s.text name="speed.identify"/></a>
				</li>
			</#if>
			
			<li>
				<a href="<@s.url action="startEvent" namespace="/"/>" class="speedLink" id="menuInspect"><@s.text name="speed.inspect"/></a>
			</li>
			<li>
				<a href="<@s.url action="search" namespace="/"/>" class="speedLink" id="menuAssets"><@s.text name="speed.assets" /></a>
			</li>
			<li>
				<a href="<@s.url action="report" namespace="/"/>" class="speedLink" id="menuReport"><@s.text name="speed.reporting" /></a>
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
				<li>
					<a href="<@s.url action="setup" namespace="/"/>" class="speedLink" id="menuSetup"><@s.text name="label.setup" /> </a>
				</li>
			</#if>
			
		</ul>
	</div>
	
</div>