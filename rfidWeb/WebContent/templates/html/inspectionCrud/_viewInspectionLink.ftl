<#include "_inspectionViewLightBoxOptions.ftl"/>
<#if inspection.tenant.id == tenantId >
	<a href='<@s.url action="inspection" namespace="/ajax"  productId="${product.id}" uniqueID="${inspection.id}"/>'  ${inspectionLightViewOptions} >
<#else>			
	<a href='<@s.url action="linkedInspection" namespace="/ajax"  productId="${product.id}" uniqueID="${inspection.id}"/>' ${inspectionLightViewOptions} >
</#if>
	<@s.text name="label.view"/>
</a>