	<title><@s.text name="label.quick_setup_wizard"/> - <@s.text name="label.step_x_of_y"><@s.param>1</@s.param><@s.param>3</@s.param></@s.text></title>
<head>
	<@n4.includeStyle type="page" href="quick_setup_wizard"/>
</head>
<div id="setupWizardStep1" class="setupWizardContent">
	<h2 class="clean"><@s.text name="label.your_company_profile"/></h2>
	<span class="weak"><@s.text name="label.tell_us_what_applies_to_you"/></span>
	
	<@s.form action="step1Complete" cssClass="fullForm fluentSets  prominent" theme="fieldid">	
		
		<div class="altbox">
			<div class="infoSet">
				<span class="fieldHolder">
					<@s.radio name="turnOnJobSites" list="off"/>
					<label class="description"> - <@s.text name="label.im_a_distributor_or_manufacturer"/></label>
				</span>
			</div>
			<div class="infoSet">
				<span class="fieldHolder">
					<@s.radio name="turnOnJobSites" list="on" />
					<label class="description"> - <@s.text name="label.im_an_end_user_or_manage_job_sites"/></label>
				</span>
			</div>
		</div>
		
		<div class="altbox colorBox infoSet assignToBox">
			<span class="fieldHolder">
			<@s.checkbox name="turnOnAssignedTo" theme="fieldidSimple"/>
				<label class="labelHeading">
					<@s.text name="label.enable_asset_assignment"/>
				</label>
			</span>
			<span class="description assignToDescription"><@s.text name="label.assigned_to_field_description"/></span>
		</div>
		<div class="actions">
			<@s.submit key="label.next" />
			<@s.text name="label.or"/>
			<a href="<@s.url action="home" namespace="/"/>"><@s.text name="label.cancel"/></a>
		</div>
	</@s.form>
</div>