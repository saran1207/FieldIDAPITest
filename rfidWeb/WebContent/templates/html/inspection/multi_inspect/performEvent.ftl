
${action.setPageType('inspection', 'add')!}

<div id="inspectionType">${inspectionType.name}</div>

<button id="saveInspections"><@s.text name="label.save_all"/></button>

<ul id="assets">
	<#list assets as asset>
		<li class="assetLine" id="asset_${asset.id}"><span>${asset.serialNumber}</span> <span class="status"><@s.text name="label.not_sent"/></span></li> 
	</#list>
</ul>

<@s.form action="inspectionCreate" namespace="/multiInspect/ajax" id="createInspection">
	
	<@s.hidden name="type" value="${eventTypeId}"/>
	<@s.hidden name="inspector" value="${sessionUserId}"/>
	<@s.hidden name="scheduleId" value="0"/>
	<@s.hidden name="inspectionDate" value="05/04/10 9:00 AM"/>
	
	<@s.hidden name="productId" id="productId"/>
	<@s.hidden name="location" id="location"/>
	<@s.hidden name="ownerId" id="ownerId"/>
	<@s.hidden name="productStatus" id="productStatusId"/>
	
</@s.form> 

<@n4.includeScript>
	
	var assets = new Array();
	var asset = null;
	<#list assets as asset>
		asset = new Object();
		asset.id = ${asset.id};
		asset.ownerId = ${asset.owner.id};
		asset.location = "${(asset.location?js_string)!}";
		asset.productStatusId = "${(asset.productStatus.id)!}"; 
		assets.push(asset);
	</#list>
	
	
	onDocumentLoad(function() {
		assets.each(function(element) {
			$('asset_' + element.id).asset =  element;
		});
		
		$('saveInspections').observe('click', function(event) {
			event.stop();
			
			$$('.assetLine').each(function(element) {
				var asset = element.asset;
				$('productId').value= asset.id;
				$('ownerId').value= asset.ownerId;
				$('location').value= asset.location;
				$('productStatusId').value= asset.productStatusId;
				
				$('createInspection').request({
					asynchronous:false,	
					onSuccess: contentCallback});
			}); 
		});
	});
</@n4.includeScript>