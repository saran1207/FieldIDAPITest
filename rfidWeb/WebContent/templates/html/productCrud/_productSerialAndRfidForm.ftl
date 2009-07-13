<div class="formRowHolder" id="serialNumberRow_${uniqueID!}">
	<@s.textfield id="serialNumberText"  name="serialNumber" labelposition="left" required="true" label="${action.getText( Session.sessionUser.serialNumberLabel )}" onchange="checkSerialNumber(${uniqueID!});"/>
		
	<div class="wwgrp"><a href="#" onclick="generateSerialNumber('serialNumberText', ${uniqueID!'\'\''});return false;"><@s.text name="label.generate" /></a>
		<span class="serialNumberStatus"></span>
	</div>
</div>
<div class="formRowHolder">
	<@s.textfield id="rfidNumber" key="label.rfidnumber" name="rfidNumber" labelposition="left"/>
	<@s.textfield id="customerRefNumber" key="label.referencenumber" name="customerRefNumber" labelposition="left"/>
	
</div>