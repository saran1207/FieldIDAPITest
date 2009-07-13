<#assign form_action="EDIT" /> 
<#assign current_action="inspectionEdit"/>

<#assign formAction="subInspectionUpdate"/>
<#if action.isParentProduct() >
	<#assign formAction="baseInspectionUpdate"/>
</#if>
${action.setPageType('inspection', 'edit')!}

<@s.form action="${formAction}" theme="simple" cssClass="crudForm largeForm" method="post" onsubmit="return checkForUploads();" >
	<@s.hidden name="parentProductId" />
	<@s.hidden name="token" />
	<#include "/templates/html/inspectionCrud/_form.ftl"/>

	<div class="formAction">
		<button onclick="return redirect('<@s.url action="masterInspectionEdit" uniqueID="${masterInspection.inspection.id}" token="${token}"/>' );"><@s.text name="label.cancel"/></button>
		<@s.submit key="hbutton.store" />
	</div>
	
</@s.form>