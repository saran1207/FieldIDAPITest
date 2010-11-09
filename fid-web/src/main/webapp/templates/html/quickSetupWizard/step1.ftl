<title><@s.text name="label.quick_setup_wizard"/> - <@s.text name="label.step_x_of_y"><@s.param>1</@s.param><@s.param>3</@s.param></@s.text></title>
<head>
	<@n4.includeStyle type="page" href="quick_setup_wizard"/>
</head>

<div class="setupContainer">
	<div class="quickSetupHeader">
		<h2><@s.text name="label.quick_setup_wizard"/> - <@s.text name="label.step_x_of_y"><@s.param>1</@s.param><@s.param>3</@s.param></@s.text></h2>
	</div>
	
	<@s.form action="step1Complete" cssClass="fullForm fluentSets" theme="fieldid">	
		<div id="setupWizardStep1" class="setupWizardContent">
			<h2><@s.text name="label.your_company_profile"/></h2>
			<p><@s.text name="label.tell_us_what_applies_to_you"/></p>
			<br/>
			<span class="weak">
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
			</span>
		</div>
		<div class="setupWizardContent">
			<h2><@s.text name="label.enable_asset_assignment"/></h2>
			<p><@s.text name="label.assigned_to_field_description"/></p>
			<br/>
			<div class="infoSet weak">
				<span class="fieldHolder">
				
					<@s.checkbox name="turnOnAssignedTo" theme="fieldidSimple"/>
					<span class="description checkBoxLabel"><@s.text name="label.yes_i_want_to_assign_assets"/></span>
				</span>
			</div>
		</div>
		
		<div class="prominent">
			<@s.submit key="label.next" />
			<@s.text name="label.or"/>
			<a href="<@s.url action="home" namespace="/"/>"><@s.text name="label.cancel"/></a>
		</div>
			
	</@s.form>
</div>