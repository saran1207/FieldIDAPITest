<title><@s.text name="label.quick_setup_wizard"/> - Done</title>
<head>
	<@n4.includeStyle type="page" href="quick_setup_wizard"/>
</head>
<div id="setupWizardComplete" class="setupWizardContent">
	<h2 class="clean">Your Done!</h2>
	<span>Now that you're done with the initial system setup here are some suggested next steps.</span>
	
	<ul>
		<li id="setupMobile">
			<a href="/fieldid_help/Installing_Field_ID_Mobile.html">Setup your mobile computers</a><br/>
			<span>Download and install Field ID software for your mobile devices</span>
		</li>
		<#if sessionUser.hasAccess("tag") == true >
			<li id="identifyAssets">
			
				<#if securityGuard.integrationEnabled>
					<@s.url id="identifyUrl" action="identify"/>
					
				<#else>
					<@s.url id="identifyUrl" action="productAdd" />
				</#if>
				
				<a href="${identifyUrl}">Identify your first asset</a><br/>
				<span>Identification is the first step in the traceability process</span>
			</li>
		</#if>
		<li id="furtherAccountSetup">
			<a href="<@s.url action="administration"/>">Further customize your account setup</a><br/>
			<span>Control everything from event checklists to employee accounts</span>
		</li>
		<li id="visitHelp">
			<a href="/fieldid_help/index.html">Visit the help documentation</a><br/>
			<span>Getting Started to advanced customization is all covered here</span>
		</li>
		<li id="watchIntroductionVideo">
			<a href="<@s.url action="instructionalVideos"/>">Watch the introduction video</a><br/>
			<span>In under 8 minutes get a quick overview of Field ID</span>
		</li>
		<li id="needMoreHelp">
			<span>Need more help? Our support team is ready for your questions</span><br/>
			<span>Email us at: <a href="mailto:support@fieldid.com">support@fieldid.com</a> or <a href="http://n4systems.helpserve.com/">submit a support ticket</a></span>
		</li>
	</ul>
	
</div>