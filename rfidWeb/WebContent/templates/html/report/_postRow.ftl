<#assign localInspection=action.isLocalInspection(rowIdx)/>
<#if criteria.includeNetworkResults>
	<td>
		<#if localInspection>
			<@s.text name="label.no"/>
		<#else>
			<@s.text name="label.yes"/>
		</#if>
	</td>
</#if>
<td>
	<#include "../inspectionCrud/_inspectionViewLightBoxOptions.ftl"/>
	<a href='<@s.url action="inspection" namespace="/ajax" uniqueID="${entityId}"/>'  ${inspectionLightViewOptions} ><@s.text name="link.view" /></a>
	<#if sessionUser.hasAccess("editinspection") && localInspection>
		 | <a href='<@s.url action="selectInspectionEdit" namespace="/" uniqueID="${entityId}"/>'><@s.text name="label.edit" /></a>
	</#if>
</td>