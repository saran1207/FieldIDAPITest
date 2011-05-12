<title><@s.text name="label.quick_setup_wizard"/> - <@s.text name="label.step_x_of_y"><@s.param>4</@s.param><@s.param>5</@s.param></@s.text> - <@s.text name="label.importing"/></title>

<head>
	<@n4.includeStyle type="page" href="quick_setup_wizard"/>
	<script type="text/javascript">
		document.observe("dom:loaded", function() {
			Event.observe('importForm', 'submit', function(){
					$('formActions').hide();
					$('loadingWheel').show();
			});
		});
	</script>
</head>

<@s.form action="step4ImportCatalogOnlyConfirm" id="importForm" theme="fieldid">
	<div class="setupContainer">
		<div class="quickSetupHeader">
			<h2><@s.text name="label.quick_setup_wizard"/> - <@s.text name="label.step_x_of_y"><@s.param>4</@s.param><@s.param>5</@s.param></@s.text></h2>
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
		<div id="formActions" class="prominent">
			<@s.url id="cancelUrl" action="step2"/>
			<@s.submit key="label.next" id="continue" />
			<@s.text name="label.or"/>
			<a href="#" onclick="return redirect( '${cancelUrl}' );" ><@s.text name="label.back"/></a>
		</div>	
		<div id="loadingWheel" class="prominent centerWheel" style="display:none">
			<img src="<@s.url value="/images/loader.gif"/>"/>
		</div>
	</div>
	
</@s.form>