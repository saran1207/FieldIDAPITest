${action.setPageType('event', 'add')!}

<@s.form action="eventCreate" theme="simple" method="post"  onsubmit="return checkForUploads();" >
	<#assign form_action="ADD" />
	<#include "_form.ftl"/>

	<div class="formAction">
		<@s.submit key="hbutton.save" />
		<@s.text name="label.or"/>
		<a href="<@s.url action="quickEvent" assetId="${assetId}"/>"><@s.text name="label.cancel"/></a>
	</div>
</@s.form>