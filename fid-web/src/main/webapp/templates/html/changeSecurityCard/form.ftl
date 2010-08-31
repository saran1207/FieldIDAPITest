${action.setPageType('my_account', 'security_card')!}
<@s.form action="updateSecurityCard" theme="fieldid" cssClass="crudForm">
	
	<#include "/templates/html/common/_formErrors.ftl"/>
	<p>
		<label><@s.text name="label.securityrfidnumber"/></label>
		<@s.textfield name="securityCardNumber" />
	</p>
	
	<div class="formAction">
		<@s.url id="cancelUrl" action="myAccount"/>
		<@s.reset key="label.cancel" onclick="return redirect('${cancelUrl}');"/>
		<@s.submit key="hbutton.save"/>
	</div>
</@s.form>