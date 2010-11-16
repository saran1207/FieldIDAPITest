<title><@s.text name="label.quick_setup_wizard"/> - <@s.text name="label.done"/></title>
<head>
	<@n4.includeStyle type="page" href="quick_setup_wizard"/>
</head>

<div class="setupContainer">
	<div id="allDone" class="quickSetupHeader">
		<h2><@s.text name="label.all_done"/> - <@s.text name="label.next_steps"/></h2>
	</div>
		
	<div class="setupWizardContent">
		<h2><@s.text name="label.bookmark_your_fieldid_login_page"/></h2>
		<div class="contentIndentation prominent">
			<p><@s.text name="label.bookmark_your_fieldid_login_page.description"/></p>
			<br/>
			<label class="urlLabel">${loginUrl}</label>
			<button onclick="createBookmark('${loginUrl}', '<@s.text name="label.field_id"/>');"><@s.text name="label.click_to_bookmark"/></button>
		</div>
	</div>
	<div class="setupWizardContent">
		<h2><@s.text name="label.setup_your_mobile_device"/></h2>
		<div class="contentIndentation prominent">
			<p><@s.text name="label.setup_your_mobile_device.description"/></p>
			<br/>
			<button onclick="return openNewWindow('<@s.text name="label.hardware_wizard_url"/>');"><@s.text name="label.start_the_hardware_wizard"/></button>
		</div>
	</div>
	<div class="setupWizardContent">
		<h2><@s.text name="label.identify_your_first_asset"/></h2>
		<div class="contentIndentation prominent">
			<p><@s.text name="label.identify_your_first_asset.description"/></p>
			<br/>
			<button onclick="return redirect('<@s.url namespace="/" action="assetAdd"/>');"><@s.text name="label.identify_your_first_asset_now"/></button>
		</div>
	</div>
		<div class="contentIndentation prominent">
			<@s.url id="cancelUrl" action="step3" quickSetupWizardCatalogImport="true"/>
			<input type="button" onclick="return redirect('<@s.url namespace="/" action="home"/>');" value="<@s.text name="label.proceed_to_my_fieldid_account"/>"/>
			<@s.text name="label.or"/>
			<a href="#" onclick="return redirect( '${cancelUrl}' );" ><@s.text name="label.back"/></a>
		</div>
</div>