<div id="pageHeader">
	<div id="pageActions" style="float:none;">
		<ul class="listOfLinks">				
			<li class="first">
				<@s.text name="label.welcome"/>, ${sessionUser.userID} 
			</li>
	  		<li>
	  			<a href="<@s.url action="myAccount" />"><@s.text name="label.myaccount"/></a>
			</li>
	  		<li>
	  			<a href="/fieldid_help/index.html" target="_blank"><@s.text name="label.help"/></a>
			</li>
			<li>
	  			<a href="http://n4systems.helpserve.com/" target="_blank" ><@s.text name="label.support"/></a>
	  		</li>
			<#if userSecurityGuard.allowedAccessWebStore>
			<li>
	  			<a href="<@s.url action="redirectToWebStore" />" target="_blank" ><@s.text name="label.fieldid_webstore"/></a>
	  		</li>
			</#if>	  		
	  		<li>
				<a href="<@s.url action="logout" />" ><@s.text name="label.logout"/></a>
			</li>
  		</ul>
  	</div> 

	<div id="companyLogo">
		<img width="215" height="61" src="<@s.url action="downloadTenantLogo"  namespace="/file" uniqueID="${tenant.id}" />"/>
	</div>
	
	<div id="pageNavigation">
		<ul>
			<li>
				<a href="<@s.url action="home"/>" class="speedLink" id="menuHome"><@s.text name="speed.home"/></a>
			</li>
		
			<#if sessionUser.hasAccess("tag") == true >
				<#if securityGuard.integrationEnabled>
					<@s.url id="identifyUrl" value="identify.action"/>
					
				<#else>
					<@s.url id="identifyUrl" action="productAdd" />
				</#if>
				<li>
					<a href="${identifyUrl}" class="speedLink" id="menuIdentify"><@s.text name="speed.identify"/></a>
				</li>
			</#if>
			
			<li>
				<a href="<@s.url action="inspect"/>" class="speedLink" id="menuInspect"><@s.text name="speed.inspect"/></a>
			</li>
			<li>
				<a href="<@s.url action="search"/>" class="speedLink" id="menuAssets"><@s.text name="speed.assets" /></a>
			</li>
			<li>
				<a href="<@s.url action="report"/>" class="speedLink" id="menuReport"><@s.text name="speed.reporting" /></a>
			</li>
			<li>
				<a href="<@s.url action="schedule"/>" class="speedLink" id="menuSchedule"><@s.text name="speed.schedule" /></a>
			</li>
			
			<#if userSecurityGuard.allowedManageSafetyNetwork>
				<li>
					<a href="<@s.url action="safetyNetwork"/>" class="speedLink" id="menuSafetyNetwork"><@s.text name="speed.safety_network" /></a>
				</li>
			</#if>
			<#if securityGuard.projectsEnabled>
				<li>
					<a href="<@s.url action="jobs"/>" class="speedLink" id="menuProject"><@s.text name="speed.projects"/></a>
				</li>
			</#if>
			<#if sessionUser.hasAdministrationAccess()>
				<li>
					<a href="<@s.url action="administration"/>" class="speedLink" id="menuAdministration"><@s.text name="label.administration" /> </a>
				</li>
			</#if>
			
		</ul>
	</div>
	
</div>