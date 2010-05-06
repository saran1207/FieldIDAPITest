
${action.setPageType('inspection', 'add')!}

<div id="inspectionType">${inspectionType.name}</div>

<#include "_form.ftl" />

<button id="saveInspections"><@s.text name="label.save_all"/></button>

<ul id="assets">
	<#list assets as asset>
		<li class="assetLine" id="asset_${asset.id}"><span>${asset.serialNumber}</span> <span class="status"><@s.text name="label.not_sent"/></span></li> 
	</#list>
</ul>



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
			
			var options = getStandardCallbacks();
			options.asynchronous = false;
			options.parameters = $('inspectionCreate').serialize();
			options.method =  "post";
			
			new Ajax.Request('<@s.url action="inspectionCheck" namespace="ajax"/>', options);
			
			
			$$('.assetLine').each(function(element) {
				var asset = element.asset;
				$('productId').value= asset.id;
				
				
				$('inspectionCreate').request({
					asynchronous:false,	
					onSuccess: contentCallback});
			}); 
		});
	});
</@n4.includeScript>