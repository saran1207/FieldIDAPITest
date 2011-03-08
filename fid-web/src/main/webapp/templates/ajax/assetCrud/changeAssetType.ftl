<#escape x as x?js_string >
	<#assign options>	
		<#include "/templates/html/assetCrud/_infoOptions.ftl">
	</#assign>

	<#assign assetEventSchedules>	
		<#include "/templates/html/eventCrud/_schedules.ftl" />
	</#assign>
	
	<#assign componentTypes>
		<ul>
			<#list assetType.subTypes as type >
				<li>
					${type.name}
				</li>
			</#list>
		</ul>
	</#assign>
	
	if ($('saveAndPrintAction') != undefined){ 
		<#if assetType.hasManufactureCertificate>
			$('saveAndPrintAction').show();
		<#else>
			$('saveAndPrintAction').hide();
		</#if>
	}
	updatingAssetComplete();
	replaceInfoOptions( "${options}" ) ;
	replaceEventSchedules( "${assetEventSchedules}" );
	
</#escape>