<head>
	<@n4.includeStyle type="page" href="quick_setup_wizard"/>
	<@n4.includeStyle href="loadingPage" type="page"/>
	<@n4.includeScript>
		onDocumentLoad(function() {
				return redirect('<@s.url action="step3" quickSetupWizardCatalogImport="true"/>')
		});
	</@n4.includeScript>
</head>

<div class="centerWheel quickSetupLoadingWheel">
	<img src="<@s.url value="/images/loader.gif"/>"/>
</div>



