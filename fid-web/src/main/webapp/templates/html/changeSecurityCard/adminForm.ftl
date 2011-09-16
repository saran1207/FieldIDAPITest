${action.setPageType('user', 'change_mobile_passcode')!}

<head> 
	<@n4.includeStyle href="mobilePasscode" type="page"/>
</head>

<@s.form action="adminUpdateMobilePasscode"  theme="fieldid" cssClass="crudForm largeForm">
	<@s.hidden name="uniqueID" />
	<#include "/templates/html/common/_formErrors.ftl"/>

	<div class="infoSet">
		<span class="egColor"><@s.text name="message.mobile_passcode_instructions"/></span>
		<@s.textfield name="securityCardNumber" />
	</div>
	
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