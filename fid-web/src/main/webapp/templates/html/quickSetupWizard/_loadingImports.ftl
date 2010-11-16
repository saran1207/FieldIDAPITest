<head>
	<@n4.includeStyle type="page" href="quick_setup_wizard"/>
	<@n4.includeStyle href="loadingPage" type="page"/>
	<script type="text/javascript">
		document.observe("dom:loaded", function(){$('confirmForm').submit();} );
	</script>
	
	<style>
		#pageFooter, #pageHeader {
			display: none;
		}
	</style>
</head>
<div class="centerWheel quickSetupLoadingWheel">
	<img src="<@s.url value="/images/loader.gif"/>"/>
</div>
<span style="display:none">
	<@s.form action="step3ImportCatalogOnlyImport" id="confirmForm" theme="fieldid">
	
		<@s.hidden name="quickSetupWizardCatalogImport" value="true"/>
		<@s.hidden name="uniqueID"/>
		<@s.hidden name="usingPackage"/>
		
		<#list publishedAssetTypes as assetType>
			<@s.hidden name="importAssetTypeIds['${assetType.id}']" />
		</#list>
		<#list publishedEventTypes as event>
			<@s.hidden name="importEventTypeIds['${event.id}']" />
		</#list>
			<div class="stepAction">
			<@s.submit key="label.start_import_now" id="import" onclick="$('step3Form').request(getStandardCallbacks());  toStep(4, 'step4Loading'); return false;"/>
			<@s.text name="label.or"/> <a href="javascript:void(0);" onclick="scroll(0,0); backToStep(2)"><@s.text name="label.back_to_step"/> 2</a>
		</div>
	</@s.form>
</span>