<#assign assetId="${action.getProductIdForInspectionScheduleId(entityId)}" />
<#assign inspectionTypeId="${action.getInspectionTypeIdForInspectionScheduleId(entityId)}" />
<#assign inspectionId="${action.getInspectionIdForInspectionScheduleId(entityId)!0}" />

<td>
	<#if inspectionId != "0">
		<#include "../inspectionCrud/_inspectionViewLightBoxOptions.ftl"/>
		<a href='<@s.url action="inspection" namespace="/aHtml/iframe" uniqueID="${inspectionId}" assetId="${assetId}"/>'  ${inspectionLightViewOptions} >
			<@s.text name="label.viewinspection"/>
		</a>
	<#elseif sessionUser.hasAccess("createinspection") >
		<a href='<@s.url action="selectInspectionAdd" namespace="/" assetId="${assetId}" type="${inspectionTypeId}" scheduleId="${entityId}" />'>
			<@s.text name="label.inspect"/>
		</a>
	</#if>
</td>
<td>
	<a href='<@s.url action="inspectionScheduleList" assetId="${assetId}" />'>
		<@s.text name="label.view"/>
	</a>
</td>
