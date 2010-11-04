<#include "_eventViewLightBoxOptions.ftl"/>
<a href='<@s.url action="event" namespace="/aHtml/iframe"  assetId="${asset.id}" uniqueID="${event.id}"/>${additionsToQueryString!}'  ${eventLightViewOptions} >
	<@s.text name="label.view"/>
</a>