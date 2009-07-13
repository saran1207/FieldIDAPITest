
<#list inspectionTypes as inspectionType>
	<li>
		<#if subInspection?exists && subInspection  > 
			<@s.url id="inspectionUrl" action="subInspectionAdd" productId="${subProduct.product.id}" type="${inspectionType.id}" parentProductId="${productId}" token="${token}" namespace="/"/>
		<#else>
			<@s.url id="inspectionUrl" action="selectInspectionAdd" productId="${uniqueID}" type="${inspectionType.id}" inspectionGroupId="${(inspectionGroup.id)!}"/>
		</#if>
		<a href="${inspectionUrl}" >${inspectionType.name}</a>
	</li>
</#list>
<#if inspectionTypes.isEmpty() >
	<li>
		<@s.text name="label.noinspectiontypes"/>
	</li>
</#if>