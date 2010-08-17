${action.setPageType('user', 'change_rfid_number')!}

<@s.form action="adminUpdateSecurityCard"  theme="fieldid" cssClass="crudForm" cssStyle="float:left;">
	<@s.hidden name="uniqueID" />
	<#include "/templates/html/common/_formErrors.ftl"/>
	<h2><@s.text name="label.securityrfidnumber"/></h2>
	<p>
		<label><@s.text name="label.new_security_card_number"/></label>
		<@s.textfield name="securityCardNumber" />
	</p>
	
	<div class="formAction">
		<@s.url id="cancelUrl" action="${user.employee?string('employeeUserEdit', 'customerUserEdit')}" uniqueID="${uniqueID}"/>
		<@s.reset key="label.cancel" onclick="redirect('${cancelUrl}'); return false;"/>
		<@s.submit key="hbutton.save"/>
	</div>
</@s.form>