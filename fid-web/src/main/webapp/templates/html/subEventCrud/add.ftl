<#assign form_action="ADD" /> 
<#assign formAction="subEventCreate"/>
<#if action.isParentAsset() >
	<#assign formAction="baseEventCreate"/>
</#if>
${action.setPageType('event', 'add')!}
<@s.form action="${formAction}" theme="simple" cssClass="crudForm fullPageForm" method="post" onsubmit="return checkForUploads();" >
	<@s.hidden name="parentAssetId"/>
	<@s.hidden name="token"/>
	<#include "/templates/html/eventCrud/_form.ftl"/>

	<div class="formAction">
		<@s.submit key="hbutton.store" />
		<@s.text name="label.or"/>
		<@s.url id="cancelUrl" action="masterEventAdd" type="${masterEvent.event.type.id}" assetId="${parentAssetId}" token="${token}" />
		<a href="#" onclick="return redirect('${cancelUrl}');"><@s.text name="label.cancel"/></a>
	</div>
</@s.form>