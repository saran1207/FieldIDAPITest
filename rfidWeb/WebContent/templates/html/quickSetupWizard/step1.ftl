<title><@s.text name="label.quick_setup_wizard"/> - step 1 of 2</title>
<head>
	<@n4.includeStyle type="page" href="quick_setup_wizard"/>
</head>
<div id="setupWizardStep1" class="setupWizardContent">
	<h2 class="clean">Your Company Profile</h2>
	<span class="weak">Tell us which one of these applies to your company</span>
	
	<@s.form action="quickSetupWizardStep1Complete" cssClass="fullForm fluentSets  prominent" theme="fieldid">	
		
		<div class="infoSet">
			<span class="fieldHolder">
				<@s.radio name="turnOnJobSites" list="off"/>
				<label class="description"> - I am a distributor or manufacturer something for various customers</label>
			</span>
		</div>
		<div class="infoSet">
			<span class="fieldHolder">
				<@s.radio name="turnOnJobSites" list="on" />
				<label class="description"> - I am an end user and manage multiple job sites where equipment is assigned to my employees</label>
			</span>
		</div>
		
		
		<div class="actions">
			<@s.submit key="label.next" />
			<@s.text name="label.or"/>
			<a href="<@s.url action="home"/>"><@s.text name="label.cancel"/></a>
		</div>
	</@s.form>
</div>