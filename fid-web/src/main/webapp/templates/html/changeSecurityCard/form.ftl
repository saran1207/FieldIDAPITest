${action.setPageType('my_account', 'mobile_passcode')!}

<head> 
	<@n4.includeStyle href="mobilePasscode" type="page"/>
</head>

<@s.form action="updateMobilePasscode" theme="fieldid" cssClass="crudForm largeForm">
	
	<#include "/templates/html/common/_formErrors.ftl"/>
	<div class="infoSet">
		<span class="egColor"><@s.text name="message.mobile_passcode_instructions"/></span>
		<@s.textfield name="securityCardNumber" />
	</div>
	
	<div class="formAction borderLessFormAction">
		<@s.url id="cancelUrl" action="viewMobilePasscode"/>
		<@s.submit key="hbutton.save"/>
		<@s.text name="label.or"/>
		<a href="#" onclick="return redirect( '${cancelUrl}' );" ><@s.text name="label.cancel"/></a>
	</div>
</@s.form>