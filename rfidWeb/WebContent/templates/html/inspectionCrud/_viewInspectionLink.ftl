<#include "_inspectionViewLightBoxOptions.ftl"/>
<a href='<@s.url action="inspection" namespace="/aHtml"  productId="${product.id}" uniqueID="${inspection.id}"/>'  ${inspectionLightViewOptions} >
	<@s.text name="label.view"/>
</a>