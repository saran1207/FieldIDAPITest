${action.setPageType('my_account', 'change_password')!}
<@s.form action="updatePassword" theme="fieldid" cssClass="crudForm">
	
	<#include "/templates/html/common/_formErrors.ftl"/>
    <p>
        <label><@s.text name="label.currentpassword"/></label>
        <@s.password name="originalPassword"/>
    </p>
	<p>
		<label><@s.text name="label.newpassword"/></label>
		<@s.password name="newPassword" />
	</p>
	<p>
		<label><@s.text name="label.confirmpassword"/></label>
		<@s.password name="confirmPassword" />
	</p>
	
	<div class="formAction borderLessFormAction">
		<@s.submit key="hbutton.save"/>
		<@s.text name="label.or"/>
		<@s.url id="cancelUrl" action="myAccount"/>
		<a href="#" onclick="redirect('${cancelUrl}'); return false;"><@s.text name="label.cancel"/></a>
	</div>
</@s.form>