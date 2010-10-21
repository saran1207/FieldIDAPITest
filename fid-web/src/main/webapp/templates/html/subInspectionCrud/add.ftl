<#assign form_action="ADD" /> 
<#assign formAction="subInspectionCreate"/>
<#if action.isParentAsset() >
	<#assign formAction="baseInspectionCreate"/>
</#if>
${action.setPageType('inspection', 'add')!}
<@s.form action="${formAction}" theme="simple" cssClass="crudForm fullPageForm" method="post" onsubmit="return checkForUploads();" >
	<@s.hidden name="parentAssetId"/>
	<@s.hidden name="token"/>
	<#include "/templates/html/inspectionCrud/_form.ftl"/>

	<div class="formAction">
		<button onclick="return redirect('<@s.url action="masterInspectionAdd" type="${masterInspection.inspection.type.id}" assetId="${parentAssetId}" token="${token}"/>' );"><@s.text name="label.cancel"/></button>
		<@s.submit key="hbutton.store" />
	</div>
</@s.form>