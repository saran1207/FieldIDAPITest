<title><@s.text name="label.quick_setup_wizard"/></title>
<head>
	<@n4.includeStyle type="page" href="quick_setup_wizard"/>
</head>
<div class="setupContainer">
	<div class="quickSetupHeader">
		<h2><@s.text name="label.quick_setup_wizard"/></h2>
	</div>
	<div id="setupWizardStart" class="setupWizardContent">
	
		<h2><@s.text name="label.welcome_to_field_id"/></h2>
	
		<p>
			<@s.text name="label.we_recommend_starting_with_the_wizard"/><br/>
			<@s.text name="label.wizard_description"/>
		</p>
	</div> 
	
	<div class="prominent">
		<input type="button" id="startButton" onclick="redirect('<@s.url action="step1"/>'); return false;" value="<@s.text name="label.im_ready_lets_go"/>"/>  
		<@s.text name="label.or"/> 
		<a href="<@s.url action="home" namespace="/"/>" cssClass="cancel"><@s.text name="label.no_thanks"/></a><br/>
	</div>
</div>