<title><@s.text name="label.quick_setup_wizard"/> - <@s.text name="label.step_x_of_y"><@s.param>3</@s.param><@s.param>4</@s.param></@s.text> - <@s.text name="label.importing"/></title>

<head>
	<@n4.includeStyle type="page" href="quick_setup_wizard"/>
	<@n4.includeStyle href="loadingPage" type="page"/>
</head>

<div id="loading" class="centerWheel quickSetupLoadingWheel">
	<img src="<@s.url value="/images/loader.gif"/>"/>
</div>

<@s.form action="step3ImportCatalogOnlyConfirm" id="importForm" theme="fieldid">
	<div class="setupContainer">
		<div class="quickSetupHeader">
			<h2><@s.text name="label.quick_setup_wizard"/> - <@s.text name="label.step_x_of_y"><@s.param>3</@s.param><@s.param>4</@s.param></@s.text></h2>
		</div>
		
		<div class="setupWizardContent">
			<h2><@s.text name="label.import_common_templates"/></h2>
			<p><@s.text name="label.setup_wizard_catalog_description"/></p>
			<br/>
			
			<div class="selectOptions">
				<@s.text name="label.select"/>: <a href="javascript:void(0);" onclick="selectAll('importForm');"><@s.text name="label.all"/></a>, <a href="javascript:void(0);" onclick="selectNone('importForm')"><@s.text name="label.none"/></a>
			</div>
			
			<@s.hidden name="quickSetupWizardCatalogImport" value="true"/>
			<@s.hidden name="uniqueID"/>
			<@s.hidden name="usingPackage" value="true"/>	
			
			<div class="importSelection">
					<#include "../publishedCatalog/_importByPackageGrouped.ftl"/>
			</div>
		
		</div>
		<div class="prominent">
			<@s.url id="cancelUrl" action="step2"/>
			<@s.submit key="label.next" id="continue" />
			<@s.text name="label.or"/>
			<a href="#" onclick="return redirect( '${cancelUrl}' );" ><@s.text name="label.back"/></a>
		</div>	
	</div>
		<script>
 			 $('loading').hide();
		</script>
</@s.form>


