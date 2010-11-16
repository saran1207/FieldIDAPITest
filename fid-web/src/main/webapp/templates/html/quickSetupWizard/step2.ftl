<title><@s.text name="label.quick_setup_wizard"/> - <@s.text name="label.step_x_of_y"><@s.param>2</@s.param><@s.param>4</@s.param></@s.text></title>
<head>
	<@n4.includeStyle type="page" href="quick_setup_wizard"/>
</head>

<div class="setupContainer">
	<div class="quickSetupHeader">
		<h2><@s.text name="label.quick_setup_wizard"/> - <@s.text name="label.step_x_of_y"><@s.param>2</@s.param><@s.param>4</@s.param></@s.text></h2>
	</div>
	
	<@s.form action="step2Complete" cssClass="fullForm  borderedSets" theme="fieldid">	
		<#include "../common/_formErrors.ftl"/>
		<div class="setupWizardContent">
			<h2><@s.text name="label.preferred_date_format"/></h2>
			<p><@s.text name="label.preferred_date_format_description"/></p>
			<br/>
			<div class="infoSet">
				<@s.select name="dateFormat" list="dateFormats" listKey="id" listValue="name"/>
			</div>
		</div>
		
		<#if securityGuard.brandingEnabled>
			<div class="setupWizardContent">
				
				<h2><@s.text name="label.system_logo_and_website" /></h2>
				
				<p><@s.text name="label.logo_image_quick_setup" /><@s.text name="label.account_image_looks_best" /></p>
				<#include "../systemSettings/_branding.ftl">		
			</div>
		</#if>
		<div class="prominent">
			<@s.url id="cancelUrl" action="step1"/>
			<@s.submit key="label.next"/> 
			<@s.text name="label.or"/>
			<a href="#" onclick="return redirect( '${cancelUrl}' );" ><@s.text name="label.back"/></a>
		</div>
	
	</@s.form>
</div>