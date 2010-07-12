<#assign form_action="EDIT" /> 
${action.setPageType('inspection', 'edit')!}

<@s.form action="inspectionUpdate" cssClass="fullForm fluidSets" theme="fieldid" onsubmit="return checkForUploads()" >
	<#include "_form.ftl"/>

	<div class="actions">
		<@s.submit key="hbutton.save" /> |
		<button onclick="return redirect('<@s.url action="inspectionDelete" uniqueID="${uniqueID}" productId="${productId}" />' );"><@s.text name="label.delete"/></button> 
		<@s.text name="label.or"/>
		<a href="<@s.url action="inspection" uniqueID="${uniqueID}" productId="${productId}" />"><@s.text name="label.cancel"/></a>
	</div>
</@s.form>