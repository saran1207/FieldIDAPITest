<title><@s.text name="label.quick_setup_wizard"/> - <@s.text name="label.step_x_of_y"><@s.param>2</@s.param><@s.param>2</@s.param></@s.text></title>
<head>
	<@n4.includeStyle type="page" href="quick_setup_wizard"/>
</head>
<div id="setupWizardStep2" class="setupWizardContent">
	<h2 class="clean"><@s.text name="label.system_settings"/></h2>
	<span class="weak"><@s.text name="label.setup_perferred_date_format"/> <#if securityGuard.brandingEnabled><@s.text name="label.and_company_branding"/></#if></span>
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