<h1>Step 1 - What do you do?</h1>

<@s.form action="setupWizardStep1Complete" cssClass="fullForm bigForm" theme="simple">	
	
	<div>
		<@s.radio name="turnOnJobSites" list="off"/> <label class="description"> - I am a distributor or manufacturer something for various customers</label
	</div>
	<div>
		<@s.radio name="turnOnJobSites" list="on" /><label class="description"> - I am an end user and manage multiple job sites where equipment is assigned to my employees</label>
	</div>
	
	
	<div class="actions">
		<@s.submit key="label.next"/>
		<@s.text name="label.or"/>
		<a href="<@s.url action="setupWizard"/>"><@s.text name="label.cancel"/></a>
	</div>
</@s.form>