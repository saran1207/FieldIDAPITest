<title><@s.text name="label.quick_setup_wizard"/></title>
<head>
	<@n4.includeStyle type="page" href="quick_setup_wizard"/>
</head>
<div id="setupWizardStart" class="setupWizardContent">
	<h2 class="clean"><@s.text name="label.welcome_to_field_id"/></h2>
	<span class="weak"><@s.text name="label.thank_you_for_signing_up"/></span>
	
	
	<p>
		<@s.text name="label.we_recommend_starting_with_the_wizard"/><br/>
		<@s.text name="label.this_will_take_you_5_minutes_and_will_help_you_configure"/>:
	</p>
	<div class="prominent">
		<ul>
			<li id="companyProfile"><@s.text name="label.your_company_profile"/></li>
			<li id="systemSettings"><@s.text name="label.your_system_settings"/></li>
		</ul>
		
		<div >
			<button id="startButton" onclick="redirect('<@s.url action="quickSetupWizardStep1"/>'); return false;"><@s.text name="label.im_ready_lets_go"/></button>  
			<@s.text name="label.or"/> 
			<a href="<@s.url action="home" namespace="/"/>" cssClass="cancel"><@s.text name="label.no_thanks"/></a><br/>
			<span class="very-weak">(<@s.text name="label.you_can_run_the_wizard_again"/>)</span>
		</div>
	</div>
	
</div> 