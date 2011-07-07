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
	    
	    <div id="smartSearchContainer">
			<@s.form method="get" action="assetInformation" namespace="/" id="smartSearch" theme="fieldid" >
				<label for="search"><@s.text name="label.find"/>:</label>
				<@s.hidden name="useContext" value="true"/>
				<@s.hidden name="usePagination" value="true"/>
				<@s.textfield name="search" id="searchText" value="${action.getText('label.smart_search_search_types')}" cssClass="description"/>
				<@s.submit name="load" key="label.load" id="smartSearchButton"/>
			</@s.form>
		</div>
	</div>
	
	<#include "_options.ftl"/>
</div>

<script type="text/javascript">
	document.observe("dom:loaded", function() {
		$('searchText').observe('focus', clearDescription);
		$('searchText').observe('blur', replaceDescription);
		$('smartSearch').observe('submit', submitSmartSearch);
	});
</script>