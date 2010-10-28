${action.clearFlashScope()}

var asset = null;
<#include "/templates/html/assetCrud/_js_asset.ftl"/>
var valueSubsitutedHtml = resultRow.replace(/%%SERIAL_NUMBER%%/g, asset.serialNumber)
							.replace(/%%RFID%%/g, asset.rfidNumber)
							.replace(/%%OWNER%%/g, asset.owner)
							.replace(/%%TYPE%%/g, asset.type)
							.replace(/%%IDENTIFIED%%/g, asset.identifiedDate)
							.replace(/%%REFERENCE_NUMBER%%/, asset.customerReferenceNumber)
							.replace(/%%CREATION_STATUS%%/, '<@s.text name="label.failed"/>')
							.replace(/%%EXTRA_CLASS%%/, 'failed');
							
$$('#listComplete .header , #creationError .header ').each(function(element) { 
		element.insert({after: valueSubsitutedHtml});
		element.up('table').show();
});



