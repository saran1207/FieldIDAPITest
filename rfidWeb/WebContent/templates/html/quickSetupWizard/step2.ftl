<title><@s.text name="label.quick_setup_wizard"/> - step 2 of 2</title>
<head>
	<@n4.includeStyle type="page" href="quick_setup_wizard"/>
</head>
<div id="setupWizardStep2" class="setupWizardContent">
	<h2 class="clean">System Settings</h2>
	<span class="weak">Setup your preferred date format <#if securityGuard.brandingEnabled>and company branding</#if></span>
	<@s.form action="quickSetupWizardStep2Complete" cssClass="fullForm  borderedSets" theme="fieldid">	
		<#include "../common/_formErrors.ftl"/>
		<div class="prominent">
			<#include "../systemSettings/_settings.ftl"/>
			
			
			<div class="actions">
				<@s.submit key="label.next" cssClass="prominent"/> 
				<@s.text name="label.or"/>
				<a href="<@s.url action="home"/>"><@s.text name="label.cancel"/></a>
			</div>
		</div>
	</@s.form>
</div>