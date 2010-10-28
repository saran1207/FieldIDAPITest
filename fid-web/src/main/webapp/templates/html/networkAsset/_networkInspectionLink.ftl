<#include "../inspectionCrud/_inspectionViewLightBoxOptions.ftl"/>
<a href='<@s.url action="networkEvent" namespace="/aHtml/iframe"  assetId="${asset.id}" uniqueID="${inspection.id}" useContext="true"/>${additionsToQueryString!}'  ${inspectionLightViewOptions} >
	<@s.text name="label.view"/>
</a>