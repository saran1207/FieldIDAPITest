<title><@s.text name="label.quick_setup_wizard"/> - <@s.text name="label.done"/></title>
<head>
	<@n4.includeStyle type="page" href="quick_setup_wizard"/>
</head>

<div class="setupContainer">
	<div class="setupWizardContent">
		<h2><@s.text name="label.bookmark_your_fieldid_login_page"/></h2>
		<div class="contentIndentation prominent">
			<p><@s.text name="label.bookmark_your_fieldid_login_page.description"/></p>
			<br/>
			<label class="urlLabel">${loginUrl}</label>
			<button onclick="window.external.AddFavorite('${loginUrl}', 'Field ID');"><@s.text name="label.click_to_bookmark"/></button>
		</div>
	</div>
	<div class="setupWizardContent">
		<h2><@s.text name="label.setup_your_mobile_device"/></h2>
		<div class="contentIndentation prominent">
			<p><@s.text name="label.setup_your_mobile_device.description"/></p>
			<br/>
			<button onclick="return redirect('<@s.text name="label.hardware_wizard_url"/>');"><@s.text name="label.start_the_hardware_wizard"/></button>
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
			<button onclick="return redirect('<@s.url namespace="/" action="home"/>');"><@s.text name="label.proceed_to_my_fieldid_account"/></button>
		</div>
</div>