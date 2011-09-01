${action.setPageType('my_account', 'security_card')!}
<@s.form action="updateSecurityCard" theme="fieldid" cssClass="crudForm">
	
	<#include "/templates/html/common/_formErrors.ftl"/>
	<div class="infoSet">
		<label><@s.text name="label.securityrfidnumber"/></label>
		<@s.textfield name="securityCardNumber" />
	</div>
	
	<div class="formAction borderLessFormAction">
		<@s.url id="cancelUrl" action="myAccount"/>
		<@s.submit key="hbutton.save"/>
		<@s.text name="label.or"/>
		<a href="#" onclick="return redirect( '${cancelUrl}' );" ><@s.text name="label.cancel"/></a>
	</div>
</@s.form>