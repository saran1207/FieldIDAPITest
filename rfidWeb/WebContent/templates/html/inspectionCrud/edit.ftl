<#assign form_action="EDIT" /> 
${action.setPageType('inspection', 'edit')!}

<@s.form action="inspectionUpdate" theme="simple" cssClass="crudForm largeForm" onsubmit="return checkForUploads()" >
	<#include "_form.ftl"/>

	<div class="formAction">
		<button onclick="return redirect('<@s.url action="inspection" uniqueID="${uniqueID}" productId="${productId}" />' );"><@s.text name="label.cancel"/></button>
		<button onclick="return redirect('<@s.url action="inspectionDelete" uniqueID="${uniqueID}" productId="${productId}" />' );"><@s.text name="label.delete"/></button>
		<@s.submit key="hbutton.save" />
	</div>
</@s.form>