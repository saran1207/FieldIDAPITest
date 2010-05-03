${action.setPageType('inspection', 'add')!}

<#if !eventTypes?exists || eventTypes.empty>
	<@s.text name="error.no_common_inspection_types"/>
<#else>
	<#list eventTypes as eventType>
		<a href="#">${eventType.name?html}</a>
	</#list>
</#if>

<#list assetIds as assetId>
		${assetId}

</#list>

<#list findCommonEventTypes as productType>
	${productType.name}
</#list>