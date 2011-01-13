<#assign html>
	<@s.form action="assetCreate" id="subAssetCreateForm_${assetTypeId}" namespace="/ajax" theme="fieldid" cssClass="subAssetAdd fullForm " cssStyle="display:none">
		<@s.hidden name="ownerId"/>
		<@s.hidden name="identified"/>
		<@s.hidden name="assetTypeId" id="assetType"/>
		<#include "/templates/html/common/_formErrors.ftl"/>
		<div class="infoSet" id="serialNumberRow_${assetTypeId}">
			<label for="serialNumber" class="label"><@s.text name="${Session.sessionUser.serialNumberLabel}"/> <#include "/templates/html/common/_requiredMarker.ftl"/></label>
				
			<span  class="fieldHolder">
				<span class="serialNumber">
					<@s.textfield name="serialNumber" id="subAssetSerialNumber_${assetTypeId}" onchange="checkSerialNumber('subAssetSerialNumber_${assetTypeId}', -${assetTypeId});" theme="fieldidSimple" />
				
					<span class="action">
						<a href="generateSerialNumber" target="_blank" onclick="generateSerialNumber('subAssetSerialNumber_${assetTypeId}', -${assetTypeId}); return false;" ><@s.text name="label.generate"/></a>
					</span>
					
					<br/><span class="serialNumberStatus"></span>
				</span>
			</span>
		</div>
				
		<div class="infoSet">
			<label for="rfidNumber" class="label"><@s.text name="label.rfidnumber"/></label>
			<@s.textfield name="rfidNumber" id="subAssetRfidNumber_${assetTypeId}" />
		</div>
		
		<div class="infoSet">
			<label for="customerRefNumber" class="label"><@s.text name="label.referencenumber"/></label>
			<@s.textfield id="subcustomerRefNumber_${assetTypeId}" name="customerRefNumber" />		
		</div>
		
		<div class="infoSet">
			<label for="assetStatus" class="label"><@s.text name="label.assetstatus"/></label>
			<@s.select name="assetStatus" list="assetStatuses" listKey="Id" listValue="name" emptyOption="true"  />
		</div>

		<#include "/templates/html/assetCrud/_attributes.ftl"/>
		
		<div class="actions">
			<@s.submit key="label.save" onclick="checkDuplicateRfids('subAssetRfidNumber_${assetTypeId}', this); return false;" />
			<@s.text name="label.or"/>
			<a href="#" onclick="Effect.BlindUp( 'subAssetCreateForm_${assetTypeId}', { afterFinish: function() { $('subAssetCreateForm_${assetTypeId}').remove(); } } ); return false;" ><@s.text name="label.cancel"/></a>
		</div>
	</@s.form>
</#assign>

<#escape x as x?js_string>
	var formCode = '${html}';
	
	
	
	
	if( $( 'subAssetCreateForm_${assetTypeId}' ) ) {
	
		$( 'subAssetCreateForm_${assetTypeId}' ).remove();
		$( 'subAssetType_${assetTypeId}' ).insert( formCode );
		$( 'subAssetCreateForm_${assetTypeId}' ).show();
	} else {
		$( 'subAssetType_${assetTypeId}' ).insert( formCode );
	}
		
	if( ! $( 'subAssetCreateForm_${assetTypeId}' ).visible() ) {
		Effect.BlindDown( 'subAssetCreateForm_${assetTypeId}' );
	}
	$( 'subAssetCreateForm_${assetTypeId}' ).observe( 'submit', submitCreateForm );
	
</#escape>