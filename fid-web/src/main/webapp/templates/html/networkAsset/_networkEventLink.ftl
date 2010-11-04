<#include "../eventCrud/_eventViewLightBoxOptions.ftl"/>
<a href='<@s.url action="networkEvent" namespace="/aHtml/iframe"  assetId="${asset.id}" uniqueID="${event.id}" useContext="true"/>${additionsToQueryString!}'  ${eventLightViewOptions} >
	<@s.text name="label.view"/>
</a>