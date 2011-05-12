<head>
	<@n4.includeStyle type="page" href="quick_setup_wizard"/>
	<script type="text/javascript">
		document.observe("dom:loaded", function(){$('confirmForm').submit();} );
	</script>
	
	<style>
		#pageFooter, #pageHeader {
			display: none;
		}
	</style>
</head>

<span style="display:none">
	<@s.form action="step4ImportCatalogOnlyImport" id="confirmForm" theme="fieldid">
	
		<@s.hidden name="quickSetupWizardCatalogImport" value="true"/>
		<@s.hidden name="uniqueID"/>
		<@s.hidden name="usingPackage"/>
		
		<#list publishedAssetTypes as assetType>
			<@s.hidden name="importAssetTypeIds['${assetType.id}']" />
		</#list>
		<#list publishedEventTypes as event>
			<@s.hidden name="importEventTypeIds['${event.id}']" />
		</#list>
		
	</@s.form>
</span>