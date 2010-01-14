<title><@s.text name="label.quick_setup_wizard"/></title>
<head>
	<@n4.includeStyle type="page" href="quick_setup_wizard"/>
</head>
<div id="setupWizardStart" class="setupWizardContent">
	<h2 class="clean">Welcome To Field ID!</h2>
	<span class="weak">Thank you for signing up for Field ID.</span>
	
	
	<p>
		Since this is your first time using Field ID we recommend starting with the Quick Setup Wizard.<br/>
		This will only take 5 minutes and will help you configure:
	</p>
	<div class="prominent">
		<ul>
			<li id="companyProfile">Your Company Profile</li>
			<li id="systemSettings">Your System Setting</li>
		</ul>
		
		<div >
			<button id="startButton" onclick="redirect('<@s.url action="quickSetupWizardStep1"/>'); return false;">I'm Ready - Lets Go!</button>  <@s.text name="label.or"/> <a href="<@s.url action="home" namespace="/"/>" cssClass="cancel">No Thanks</a><br/>
			<span class="very-weak">(You can run the wizard at a later time by clicking the link on your dashboard)</span>
		</div>
	</div>
	
</div> 