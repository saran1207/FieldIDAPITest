${action.setPageType('organization','edit')!}
<@s.form action="organizationUpdate" theme="fieldid" cssClass="crudForm largeForm bigForm">

	<#include "_form.ftl"/>

	<div class="formAction">
		<@s.url id="cancelUrl" action="organizations"/>
		<@s.submit key="label.save"/>	
		<@s.text name="label.or"/>
		<a href="#" onclick="return redirect( '${cancelUrl}' );" />	<@s.text name="label.cancel"/></a>
	</div>
</@s.form>
