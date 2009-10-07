<#assign html>
	<@s.form action="productCreate" id="subProductCreateForm_${productTypeId}" namespace="/ajax" theme="fieldid" cssClass="subProductAdd fullForm " cssStyle="display:none">
		<@s.hidden name="ownerId"/>
		<@s.hidden name="identified"/>
		<@s.hidden name="productTypeId" id="productType"/>
		<#include "/templates/html/common/_formErrors.ftl"/>
		<div class="infoSet" id="serialNumberRow_${productTypeId}">
			<label for="serialNumber" class="label"><@s.text name="${Session.sessionUser.serialNumberLabel}"/> <#include "/templates/html/common/_requiredMarker.ftl"/></label>
				
			<span  class="fieldHolder">
				<span class="serialNumber">
					<@s.textfield name="serialNumber" id="subProductSerialNumber_${productTypeId}" onchange="checkSerialNumber('subProductSerialNumber_${productTypeId}', -${productTypeId});" theme="fieldidSimple" />
				
					<span class="action">
						<a href="generateSerialNumber" target="_blank" onclick="generateSerialNumber('subProductSerialNumber_${productTypeId}', -${productTypeId}); return false;" ><@s.text name="label.generate"/></a>
					</span>
					
					<br/><span class="serialNumberStatus"></span>
				</span>
			</span>
		</div>
		
		<div class="infoSet">
			<label for="rfidNumber" class="label"><@s.text name="label.rfidnumber"/></label>
			<@s.textfield name="rfidNumber" id="subProductRfidNumber_${productTypeId}" />
		</div>
		
		<#include "/templates/html/productCrud/_attributes.ftl"/>
		
		<div class="actions">
			<@s.submit key="label.save" onclick="checkDuplicateRfids('subProductRfidNumber_${productTypeId}', this); return false;" />
			<@s.text name="label.or"/>
			<a href="#" onclick="Effect.BlindUp( 'subProductCreateForm_${productTypeId}', { afterFinish: function() { $('subProductCreateForm_${productTypeId}').remove(); } } ); return false;" ><@s.text name="label.cancel"/></a>
		</div>
	</@s.form>
</#assign>

<#escape x as x?js_string>
	var formCode = '${html}';
	
	
	
	
	if( $( 'subProductCreateForm_${productTypeId}' ) ) {
	
		$( 'subProductCreateForm_${productTypeId}' ).remove();
		$( 'subProductType_${productTypeId}' ).insert( formCode );
		$( 'subProductCreateForm_${productTypeId}' ).show();
	} else {
		$( 'subProductType_${productTypeId}' ).insert( formCode );
	}
		
	if( ! $( 'subProductCreateForm_${productTypeId}' ).visible() ) {
		Effect.BlindDown( 'subProductCreateForm_${productTypeId}' );
	}
	$( 'subProductCreateForm_${productTypeId}' ).observe( 'submit', submitCreateForm );
	
</#escape>