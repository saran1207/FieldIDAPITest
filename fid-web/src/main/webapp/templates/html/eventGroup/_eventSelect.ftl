
<#list eventTypes as eventType>
	<li>
		<#if subEvent?exists && subEvent  > 
			<@s.url id="eventUrl" action="subEventAdd" assetId="${subAsset.asset.id}" type="${eventType.id}" parentAssetId="${assetId}" token="${token}" namespace="/"/>
		<#else>
			<@s.url id="eventUrl" action="selectEventAdd" assetId="${uniqueID}" type="${eventType.id}" eventGroupId="${(eventGroup.id)!}"/>
		</#if>
		<a href="${eventUrl}" >${eventType.name}</a>
	</li>
</#list>
<#if eventTypes.isEmpty() >
	<li>
		<@s.text name="label.noeventtypes"/>
	</li>
</#if>