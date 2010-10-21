${action.setPageType('inspection', 'add')!}

<@s.form action="inspectionCreate" cssClass="fullForm fluidSets" theme="fieldid" method="post"  onsubmit="return checkForUploads();" >
	<#assign form_action="ADD" />
	<#include "_form.ftl"/>

	<div class="actions">
		
		<@s.submit key="hbutton.save" />
		<@s.text name="label.or"/>
		<a href="<@s.url action="inspectionGroups" uniqueID="${assetId}"/>"><@s.text name="label.cancel"/></a>
	</div>
</@s.form>