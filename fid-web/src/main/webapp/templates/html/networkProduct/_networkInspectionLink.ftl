<#include "../inspectionCrud/_inspectionViewLightBoxOptions.ftl"/>
<a href='<@s.url action="networkInspection" namespace="/aHtml/iframe"  productId="${product.id}" uniqueID="${inspection.id}" useContext="true"/>${additionsToQueryString!}'  ${inspectionLightViewOptions} >
	<@s.text name="label.view"/>
</a>