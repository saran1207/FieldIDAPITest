${action.setPageType('organization','edit')!}
<@s.form action="organizationUpdate" theme="fieldid" cssClass="crudForm largeForm bigForm">
	

	<#include "_form.ftl"/>

	<div class="formAction">
		<@s.url id="cancelUrl" action="organizations"/>
		<@s.reset key="label.cancel" onclick="return redirect( '${cancelUrl}' );" />
		<@s.submit key="label.save"/>
	</div>
</@s.form>
