${action.setPageType('inspection', 'add')!}

<@s.form action="inspectionCreate" theme="simple" cssClass="crudForm fullPageForm" method="post"  onsubmit="return checkForUploads();" >
	<#assign form_action="ADD" />
	<#include "_form.ftl"/>

	<div class="formAction">
		<button onclick="return redirect('<@s.url action="inspectionGroups" uniqueID="${productId}"/>' );"><@s.text name="label.cancel"/></button>
		<@s.submit key="hbutton.save" />
	</div>
</@s.form>