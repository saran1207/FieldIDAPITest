<#include "_eventViewLightBoxOptions.ftl"/>
<a href='<@s.url action="event" namespace="/aHtml/iframe"  assetId="${asset.id}" uniqueID="${event.id}"/>${additionsToQueryString!}'  ${eventLightViewOptions} >
	<#if urlLabel?exists>
		 <@s.text name="${urlLabel}"/>
	<#else>
		<@s.text name="label.view"/>
	</#if>
</a>