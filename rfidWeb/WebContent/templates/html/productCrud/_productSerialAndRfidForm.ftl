<div id="serialNumberRow_${uniqueID!}">
	<div class="infoSet">
		<label for="serialNumber" class="label"><@s.text name="${action.getText( Session.sessionUser.serialNumberLabel )}"/> <#include "../common/_requiredMarker.ftl"/></label>
		<@s.textfield id="serialNumberText"  name="serialNumber" onchange="checkSerialNumber(${uniqueID!});"/>
		
		
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


<div class="infoSet">
	<label for="linkedProduct" class="label"><@s.text name="label.register_product"/></label>
	<@n4.safetyNetworkSmartSearch name="linkedProduct"/>
</div>	
