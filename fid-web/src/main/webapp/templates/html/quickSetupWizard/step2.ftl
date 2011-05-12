<title><@s.text name="label.quick_setup_wizard"/> - <@s.text name="label.step_x_of_y"><@s.param>2</@s.param><@s.param>5</@s.param></@s.text></title>
<head>
	<@n4.includeStyle type="page" href="quick_setup_wizard"/>
</head>

<div class="setupContainer">
	<div class="quickSetupHeader">
		<h2><@s.text name="label.quick_setup_wizard"/> - <@s.text name="label.step_x_of_y"><@s.param>2</@s.param><@s.param>5</@s.param></@s.text></h2>
	</div>
	
	<@s.form action="step2Complete" cssClass="fullForm fluentSets" theme="fieldid">	
		<div id="setupWizardStep2" class="setupWizardContent">
			<h2><@s.text name="label.enter_first_customer"/></h2>
			<p><@s.text name="message.quick_setup_instructions"/></p>
			<br/>
			<span class="weak">
			<div class="infoSet">
				<span class="fieldHolder">
					<@s.textfield id ="customerName" name="customerName"/>
					<@s.hidden id="customerId" name="customerId"/>
				</span>
			</div>
			</span>
		</div>
		<div class="prominent">
			<@s.url id="cancelUrl" action="step1"/>			
			<@s.submit key="label.next" onclick="$('customerId').value = $('customerName').value;"/>
			<@s.text name="label.or"/>
			<a href="#" onclick="return redirect( '${cancelUrl}' );" ><@s.text name="label.back"/></a>
		</div>			
	</@s.form>
</div>