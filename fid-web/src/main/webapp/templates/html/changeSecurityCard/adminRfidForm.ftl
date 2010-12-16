${action.setPageType('user', 'change_rfid_number')!}

<@s.form action="adminUpdateSecurityCard"  theme="fieldid" cssClass="crudForm" cssStyle="float:left;">
	<@s.hidden name="uniqueID" />
	<#include "/templates/html/common/_formErrors.ftl"/>
	<p>
		<label><@s.text name="label.new_security_card_number"/></label>
		<@s.textfield name="securityCardNumber" />
	</p>
	
	<div class="formAction borderLessFormAction">
		<#if user.fullUser || user.admin>
			<@s.url id="cancelUrl" action="employeeUserEdit" uniqueID="${uniqueID}"/>		
		<#elseif user.liteUser>
			<@s.url id="cancelUrl" action="liteUserEdit" uniqueID="${uniqueID}"/>
		<#else>
			<@s.url id="cancelUrl" action="readOnlyUserEdit" uniqueID="${uniqueID}"/>
		</#if>
		<@s.submit key="hbutton.save"/>
		<@s.text name="label.or"/>
	
		<a href="#" onclick="redirect('${cancelUrl}'); return false;"><@s.text name="label.cancel"/></a>
		
	</div>
</@s.form>