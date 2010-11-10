<title><@s.text name="label.quick_setup_wizard"/> - <@s.text name="label.step_x_of_y"><@s.param>3</@s.param><@s.param>4</@s.param></@s.text> - <@s.text name="label.importing"/></title>

<head>
	<@n4.includeStyle type="page" href="quick_setup_wizard"/>
</head>

<@s.form action="step3ImportCatalogOnlyConfirm" cssClass="" theme="fieldid">
	<div class="setupContainer">
		<div class="quickSetupHeader">
			<h2><@s.text name="label.quick_setup_wizard"/> - <@s.text name="label.step_x_of_y"><@s.param>3</@s.param><@s.param>4</@s.param></@s.text></h2>
		</div>
		
		<div class="setupWizardContent">
			<h2><@s.text name="label.import_industry_standard_templates"/></h2>
			<br/>
			
			<div class="selectOptions">
				<@s.text name="label.select"/>: <a href="javascript:void(0);" onclick="selectAll('step2Form');"><@s.text name="label.all"/></a>, <a href="javascript:void(0);" onclick="selectNone('step2Form')"><@s.text name="label.none"/></a>
			</div>
			
			<@s.hidden name="quickSetupWizardCatalogImport" value="true"/>
			<@s.hidden name="uniqueID"/>
			<@s.hidden name="usingPackage"/>	
			
			<div class="importSelection">
				<#include "../publishedCatalog/_importCustom.ftl"/>
			</div>
		
		</div>
		<div class="prominent">
			<@s.submit key="label.next" id="continue" onclick="$('step2Form').request(getStandardCallbacks()); toStep(3, 'step3Loading'); return false;"/>
			<@s.text name="label.or"/>&nbsp;<a href="<@s.url action="home" namespace="/"/>"><@s.text name="label.cancel"/></a>
		</div>	
	</div>
</@s.form>