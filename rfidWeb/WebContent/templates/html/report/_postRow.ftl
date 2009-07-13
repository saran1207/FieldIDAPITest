<td>
	<#include "../inspectionCrud/_inspectionViewLightBoxOptions.ftl"/>
	<a href='<@s.url action="inspection" namespace="/ajax" uniqueID="${entityId}"/>'  ${inspectionLightViewOptions} ><@s.text name="link.view" /></a>
	<#if sessionUser.hasAccess("editinspection")>
		 | <a href='<@s.url action="selectInspectionEdit" namespace="/" uniqueID="${entityId}"/>'><@s.text name="label.edit" /></a>
	</#if>
</td>