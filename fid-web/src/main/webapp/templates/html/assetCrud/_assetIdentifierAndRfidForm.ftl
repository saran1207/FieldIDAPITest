
<div class="assetFormGroup">
	<h2><@s.text name="label.identifiers"/></h2>
	<div id="identifierRow_${uniqueID!}" class="assetFormGroup">
		<div class="infoSet">
			<label for="identifier" class="label"> <#include "../common/_requiredMarker.ftl"/><span class="identifierLabel">${identifierLabel}</span></label>
			<span class="fieldHolder withAdditionalContent">
				<@s.textfield id="identifierText"  name="identifier" onchange="checkIdentifier('IdentifierText', '${uniqueID?default('')}');"/>
				<a href="#" onclick="generateIdentifier('identifierText', ${uniqueID!'\'\''}, $('assetType').value);return false;"><@s.text name="label.generate" /></a>
			</span>
			<span class="fieldHolder identifierStatus"></span>
			
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

