${action.clearFlashScope()}
<#assign asset=product/>
var asset = null;
<#include "/templates/html/productCrud/_js_product.ftl"/>
var valueSubsitutedHtml = resultRow.replace(/%%SERIAL_NUMBER%%/g, asset.serialNumber)
							.replace(/%%RFID%%/g, asset.rfidNumber)
							.replace(/%%OWNER%%/g, asset.owner)
							.replace(/%%TYPE%%/g, asset.type)
							.replace(/%%IDENTIFIED%%/g, asset.identifiedDate)
							.replace(/%%REFERENCE_NUMBER%%/, asset.customerReferenceNumber)
							.replace(/%%CREATION_STATUS%%/, '<@s.text name="label.created"/>')
							.replace(/%%EXTRA_CLASS%%/, 'created');
							
$$('#listComplete').first().insert({bottom: valueSubsitutedHtml});



