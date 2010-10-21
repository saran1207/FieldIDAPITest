<#assign html>
	<@s.form action="productCreate" id="subProductCreateForm_${assetTypeId}" namespace="/ajax" theme="fieldid" cssClass="subProductAdd fullForm " cssStyle="display:none">
		<@s.hidden name="ownerId"/>
		<@s.hidden name="identified"/>
		<@s.hidden name="assetTypeId" id="assetType"/>
		<#include "/templates/html/common/_formErrors.ftl"/>
		<div class="infoSet" id="serialNumberRow_${assetTypeId}">
			<label for="serialNumber" class="label"><@s.text name="${Session.sessionUser.serialNumberLabel}"/> <#include "/templates/html/common/_requiredMarker.ftl"/></label>
				
			<span  class="fieldHolder">
				<span class="serialNumber">
					<@s.textfield name="serialNumber" id="subProductSerialNumber_${assetTypeId}" onchange="checkSerialNumber('subProductSerialNumber_${assetTypeId}', -${assetTypeId});" theme="fieldidSimple" />
				
					<span class="action">
						<a href="generateSerialNumber" target="_blank" onclick="generateSerialNumber('subProductSerialNumber_${assetTypeId}', -${assetTypeId}); return false;" ><@s.text name="label.generate"/></a>
					</span>
					
					<br/><span class="serialNumberStatus"></span>
				</span>
			</span>
		</div>
				
		<div class="infoSet">
			<label for="rfidNumber" class="label"><@s.text name="label.rfidnumber"/></label>
			<@s.textfield name="rfidNumber" id="subProductRfidNumber_${assetTypeId}" />
		</div>
		
		<div class="infoSet">
			<label for="customerRefNumber" class="label"><@s.text name="label.referencenumber"/></label>
			<@s.textfield id="subcustomerRefNumber_${assetTypeId}"name="customerRefNumber" />		
		</div>
		
		<div class="infoSet">
			<label for="assetStatus" class="label"><@s.text name="label.productstatus"/></label>
			<@s.select name="assetStatus" list="assetStatuses" listKey="uniqueID" listValue="name" emptyOption="true"  />
		</div>

		<#include "/templates/html/productCrud/_attributes.ftl"/>
		
		<div class="actions">
			<@s.submit key="label.save" onclick="checkDuplicateRfids('subProductRfidNumber_${assetTypeId}', this); return false;" />
			<@s.text name="label.or"/>
			<a href="#" onclick="Effect.BlindUp( 'subProductCreateForm_${assetTypeId}', { afterFinish: function() { $('subProductCreateForm_${assetTypeId}').remove(); } } ); return false;" ><@s.text name="label.cancel"/></a>
		</div>
	</@s.form>
</#assign>

<#escape x as x?js_string>
	var formCode = '${html}';
	
	
	
	
	if( $( 'subProductCreateForm_${assetTypeId}' ) ) {
	
		$( 'subProductCreateForm_${assetTypeId}' ).remove();
		$( 'subProductType_${assetTypeId}' ).insert( formCode );
		$( 'subProductCreateForm_${assetTypeId}' ).show();
	} else {
		$( 'subProductType_${assetTypeId}' ).insert( formCode );
	}
		
	if( ! $( 'subProductCreateForm_${assetTypeId}' ).visible() ) {
		Effect.BlindDown( 'subProductCreateForm_${assetTypeId}' );
	}
	$( 'subProductCreateForm_${assetTypeId}' ).observe( 'submit', submitCreateForm );
	
</#escape>