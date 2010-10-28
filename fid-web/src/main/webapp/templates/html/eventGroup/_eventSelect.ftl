
<#list inspectionTypes as inspectionType>
	<li>
		<#if subInspection?exists && subInspection  > 
			<@s.url id="inspectionUrl" action="subEventAdd" assetId="${subAsset.asset.id}" type="${inspectionType.id}" parentAssetId="${assetId}" token="${token}" namespace="/"/>
		<#else>
			<@s.url id="inspectionUrl" action="selectEventAdd" assetId="${uniqueID}" type="${inspectionType.id}" inspectionGroupId="${(inspectionGroup.id)!}"/>
		</#if>
		<a href="${inspectionUrl}" >${inspectionType.name}</a>
	</li>
</#list>
<#if inspectionTypes.isEmpty() >
	<li>
		<@s.text name="label.noinspectiontypes"/>
	</li>
</#if>