${action.setPageType('branding', 'list')!}

<@s.form action="brandingUpdate" cssClass="crudForm pageSection largeForm" theme="fieldid">
	<#include "../common/_formErrors.ftl"/>

	<#include "_branding.ftl">

	<div class="formAction">
		<@s.submit key="label.save"/> <@s.text name="label.or"/> <a href="/fieldid/w/setup/settings"><@s.text name="label.cancel"/></a>
	</div>
</@s.form>
