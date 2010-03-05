<#assign productId="${action.getProductIdForInspectionScheduleId(entityId)}" />
<#assign inspectionTypeId="${action.getInspectionTypeIdForInspectionScheduleId(entityId)}" />
<#assign inspectionId="${action.getInspectionIdForInspectionScheduleId(entityId)!0}" />

<td>
	<#if inspectionId != "0">
		<#include "../inspectionCrud/_inspectionViewLightBoxOptions.ftl"/>
		<a href='<@s.url action="inspection" namespace="/aHtml" uniqueID="${inspectionId}" productId="${productId}"/>'  ${inspectionLightViewOptions} >
			<@s.text name="label.viewinspection"/>
		</a>
	<#elseif sessionUser.hasAccess("createinspection") >
		<a href='<@s.url action="selectInspectionAdd" namespace="/" productId="${productId}" type="${inspectionTypeId}" scheduleId="${entityId}" />'>
			<@s.text name="label.inspect"/>
		</a>
	</#if>
</td>
<td>
	<a href='<@s.url action="inspectionScheduleList" productId="${productId}" />'>
		<@s.text name="label.view"/>
	</a>
</td>
