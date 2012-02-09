<div id="contentHeader">
	<#include "_notificationArea.ftl"/>
    <div id="contentTitle">
	    <h1>
	    	<#include "_title.ftl"/>
	    </h1>
	    
	    
		<div class="backToLink">
			<#if navOptions.onSetup>
				<a href="<@s.url action="setup" namespace="/"/>">&#171; <@s.text name="label.back_to_setup"/></a>
			</#if>
			<#if navOptions.onSetupSettings>
				<a href="/fieldid/w/setup/settings">&#171; <@s.text name="label.back_to_setup"/></a>
			</#if>
			<#if navOptions.onSetupOwners>
				<a href="/fieldid/w/setup/ownersUsersLocations">&#171; <@s.text name="label.back_to_setup"/></a>
			</#if>
			<#if navOptions.onSetupAssets>
				<a href="/fieldid/w/setup/assetsEvents">&#171; <@s.text name="label.back_to_setup"/></a>
			</#if>
			<#if navOptions.onSetupTemplate>
				<a href="/fieldid/w/setup/templates">&#171; <@s.text name="label.back_to_setup"/></a>
			</#if>
			<#if navOptions.returnToReport && reportActive>
				<a href="/fieldid/w/returnToReport">&#171; <@s.text name="label.return_to_report"/></a>
			</#if>
			<#if navOptions.onSafetyNetwork>
				<a href="<@s.url action="safetyNetwork" namespace="/"/>">&#171; <@s.text name="label.back_to_safety_network"/></a>
			</#if>
		</div>
	    
	</div>
	
	<#include "_options.ftl"/>
</div>