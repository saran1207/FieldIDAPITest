


<h1>Step 2 - Customize Your Account?</h1>

<@s.form action="setupWizardStep2Complete" cssClass="fullForm" theme="fieldid">	
	<#include "../common/_formErrors.ftl"/>
	<div class="infoBlock fluentSets">
		<#include "../systemSettings/_settings.ftl"/>
	</div>
	
	<div class="actions">
		<@s.submit key="label.next"/> 
		<@s.text name="label.or"/>
		<a href="<@s.url action="setupWizard"/>"><@s.text name="label.cancel"/></a>
	</div>
</@s.form>