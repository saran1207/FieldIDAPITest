
<div class="assetFormGroup">
	<h2><@s.text name="label.identifiers"/></h2>
	<div id="serialNumberRow_${uniqueID!}" class="assetFormGroup">
		<div class="infoSet">
			<label for="serialNumber" class="label"> <#include "../common/_requiredMarker.ftl"/><@s.text name="${action.getText( Session.sessionUser.serialNumberLabel )}"/></label>
			<@s.textfield id="serialNumberText"  name="serialNumber" onchange="checkSerialNumber('serialNumberText', '${uniqueID?default('')}');"/>
			
			<span class="fieldHolder">
				<a href="#" onclick="generateSerialNumber('serialNumberText', ${uniqueID!'\'\''});return false;"><@s.text name="label.generate" /></a>
			</span>
			<span class="fieldHolder serialNumberStatus"></span>
			
		</div>		
	</div>
	
	<div class="infoSet">
		<label for="rfidNumber" class="label"><@s.text name="label.rfidnumber"/></label>
		<@s.textfield id="rfidNumber" name="rfidNumber" />		
	</div>
	
	<div class="infoSet">
		<label for="customerRefNumber" class="label"><@s.text name="label.referencenumber"/></label>
		<@s.textfield id="customerRefNumber"name="customerRefNumber" />		
	</div>
</div>

