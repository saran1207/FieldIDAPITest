${action.setPageType('organization','add')!}

<@s.form action="organizationCreate" theme="fieldid" cssClass="crudForm largeForm">
	<#include "_form.ftl"/>
	<div class="formAction borderLessFormAction">
		<@s.url id="cancelUrl" action="organizations"/>
		<@s.submit key="label.save"/>	
		<@s.text name="label.or"/>
		<a href="#" onclick="return redirect( '${cancelUrl}' );" ><@s.text name="label.cancel"/></a>
	</div>
</@s.form>