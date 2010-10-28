<#assign form_action="EDIT" /> 
<#assign current_action="eventEdit"/>

<#assign formAction="subEventUpdate"/>
<#if action.isParentAsset() >
	<#assign formAction="baseEventUpdate"/>
</#if>
${action.setPageType('inspection', 'edit')!}

<@s.form action="${formAction}" theme="simple" cssClass="crudForm largeForm" method="post" onsubmit="return checkForUploads();" >
	<@s.hidden name="parentAssetId" />
	<@s.hidden name="token" />
	<#include "/templates/html/eventCrud/_form.ftl"/>

	<div class="formAction">
		<button onclick="return redirect('<@s.url action="masterEventEdit" uniqueID="${masterInspection.inspection.id}" token="${token}"/>' );"><@s.text name="label.cancel"/></button>
		<@s.submit key="hbutton.store" />
	</div>
	
</@s.form>