<#assign form_action="EDIT" /> 
${action.setPageType('inspection', 'edit')!}

<@s.form action="inspectionUpdate" cssClass="fullForm fluidSets" theme="fieldid" onsubmit="return checkForUploads()" >
	<#include "_form.ftl"/>
	<@s.url id="deleteUrl" action="inspectionDelete" uniqueID="${uniqueID}" assetId="${assetId}" />
	<div class="actions">
		<@s.submit key="hbutton.save" /> | 
		<a href="${deleteUrl}"><@s.text name="label.remove"/></a>
		<@s.text name="label.or"/>
		<a href="<@s.url action="inspection" uniqueID="${uniqueID}" assetId="${assetId}" />"><@s.text name="label.cancel"/></a>
	</div>
	
</@s.form>
