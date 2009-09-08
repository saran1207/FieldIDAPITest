${action.setPageType('organization','add')!}
<@s.form action="organizationCreate" theme="fieldid" cssClass="crudForm largeForm">
	<#include "_form.ftl"/>
	<div class="formAction">
		<@s.url id="cancelUrl" action="organizations"/>
		<@s.reset key="label.cancel" onclick="return redirect( '${cancelUrl}' );" />
		<@s.submit key="label.save"/>
	</div>
	
</@s.form>
