<#include "../inspectionCrud/_inspectionViewLightBoxOptions.ftl"/>
<a href='<@s.url action="inspection" namespace="/aHtml/iframe"  assetId="${asset.id}" uniqueID="${inspection.id}"/>${additionsToQueryString!}'  ${inspectionLightViewOptions} >
	<@s.text name="label.view"/>
</a>