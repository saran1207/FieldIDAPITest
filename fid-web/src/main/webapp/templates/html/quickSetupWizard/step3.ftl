<title><@s.text name="label.quick_setup_wizard"/> - <@s.text name="label.step_x_of_y"><@s.param>3</@s.param><@s.param>3</@s.param></@s.text> - <@s.text name="label.importing"/></title>

	<@s.form action="step3ImportCatalogOnlyConfirm" cssClass="" id="" theme="fieldid">
		<div class="selectOptions">
			<@s.text name="label.select"/>: <a href="javascript:void(0);" onclick="selectAll('step2Form');"><@s.text name="label.all"/></a>, <a href="javascript:void(0);" onclick="selectNone('step2Form')"><@s.text name="label.none"/></a>
		</div>
		<@s.hidden name="quickSetupWizardCatalogImport" value="true"/>
		<@s.hidden name="uniqueID"/>
		<@s.hidden name="usingPackage"/>	
		<div class="importSelection">
			<#include "../publishedCatalog/_importCustom.ftl"/>
		</div>
	
		<div class="stepAction">
			<@s.submit key="label.continue" id="continue" onclick="$('step2Form').request(getStandardCallbacks()); toStep(3, 'step3Loading'); return false;"/>
			<@s.text name="label.or"/> <a href="javascript:void(0);" onclick="scroll(0,0); backToStep(1)"><@s.text name="label.back_to_step"/> 1</a>
		</div>	
	</@s.form>
