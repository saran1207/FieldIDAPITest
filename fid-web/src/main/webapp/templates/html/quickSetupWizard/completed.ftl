<title><@s.text name="label.quick_setup_wizard"/> - <@s.text name="label.done"/></title>
<head>
	<@n4.includeStyle type="page" href="quick_setup_wizard"/>
</head>
<div id="setupWizardComplete" class="setupWizardContent">
	<h2 class="clean"><@s.text name="label.you_are_done"/></h2>
	<p><@s.text name="label.now_that_your_done_the_wizard_here_are_some_suggested_next_steps"/></p>
	
	<ul>
		<li id="setupMobile">
			<a href="/fieldid_help/Installing_Field_ID_Mobile.html"><@s.text name="label.setup_your_mobile_computers"/></a><br/>
			<span><@s.text name="label.setup_your_mobile_computers.description"/></span>
		</li>
		<#if sessionUser.hasAccess("tag") == true >
			<li id="identifyAssets">
			
				<#if securityGuard.integrationEnabled>
					<@s.url namespace="/"  id="identifyUrl" action="identify"/>
					
				<#else>
					<@s.url namespace="/"  id="identifyUrl" action="assetAdd" />
				</#if>
				
				<a href="${identifyUrl}"><@s.text name="label.identify_your_first_asset"/></a><br/>
				<span><@s.text name="label.identify_your_first_asset.description"/></span>
			</li>
		</#if>
		<li id="furtherAccountSetup">
			<a href="<@s.url namespace="/"  action="setup"/>"><@s.text name="label.further_customize_your_account_setup"/></a><br/>
			<span><@s.text name="label.further_customize_your_account_setup.description"/></span>
		</li>
		<li id="visitHelp">
			<a href="/fieldid_help/index.html"><@s.text name="label.visit_help_docs"/></a><br/>
			<span><@s.text name="label.visit_help_docs.description"/></span>
		</li>
		<li id="watchIntroductionVideo">
			<a href="<@s.url namespace="/"  action="instructionalVideos"/>"><@s.text name="label.watch_intro_video"/></a><br/>
			<span><@s.text name="label.watch_intro_video.description"/></span>
		</li>
		<li id="needMoreHelp">
			<span><@s.text name="label.need_more_help"/></span><br/>
			<span><@s.text name="label.email_us_at"/>: <a href="mailto:support@fieldid.com">support@fieldid.com</a> or <a href="http://n4systems.helpserve.com/"><@s.text name="label.submit_a_support_ticket"/></a></span>
		</li>
	</ul>
	
</div>