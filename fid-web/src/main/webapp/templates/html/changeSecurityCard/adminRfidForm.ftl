${action.setPageType('user', 'change_rfid_number')!}

<@s.form action="adminUpdateSecurityCard"  theme="fieldid" cssClass="crudForm" cssStyle="float:left;">
	<@s.hidden name="uniqueID" />
	<#include "/templates/html/common/_formErrors.ftl"/>
	<p>
		<label><@s.text name="label.new_security_card_number"/></label>
		<@s.textfield name="securityCardNumber" />
	</p>
	
	<div class="formAction borderLessFormAction">
		<@s.url id="cancelUrl" action="${user.employee?string('employeeUserEdit', 'readOnlyUserEdit')}" uniqueID="${uniqueID}"/>
		<@s.submit key="hbutton.save"/>
		<@s.text name="label.or"/>
	
		<a href="#" onclick="redirect('${cancelUrl}'); return false;"><@s.text name="label.cancel"/></a>
		
	</div>
</@s.form>