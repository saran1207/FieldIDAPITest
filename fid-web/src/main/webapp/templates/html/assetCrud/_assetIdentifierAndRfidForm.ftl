
<div class="assetFormGroup">
	<h2><@s.text name="label.identifiers"/></h2>

	<div class="infoSet increasedWidthFieldHolder">
		<label for="" class="label"><#include "../common/_requiredMarker.ftl"/><@s.text name="label.identified"/></label>
        <#if bulkRegister?exists>
            <div style="display:block;">
                <input type="checkbox" name="useDatesFromAssets" onchange="$('identified').disabled = this.checked;"/> <@s.text name="label.use_dates_from_assets"/>
            </div>
        </#if>
		<@s.textfield id="identified" name="identified" cssClass="datepicker" onchange="updateIdentified();"/>
	</div>
	
	<div id="identifierRow_${uniqueID!}" class="assetFormGroup">
		<div class="infoSet">
			<label for="identifier" class="label"> <#include "../common/_requiredMarker.ftl"/><span class="identifierLabel">${identifierLabel}</span></label>
			<span class="fieldHolder withAdditionalContent">
				<@s.textfield id="identifierText"  name="identifier" onchange="checkIdentifier(this.id, '${uniqueID?default('')}');"/>
				<a href="#" onclick="generateIdentifier('identifierText', ${uniqueID!'\'\''}, $('assetType').value);return false;"><@s.text name="label.generate" /></a>
			</span>
			<span class="fieldHolder identifierStatus" style="display:none"></span>
			
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


	




