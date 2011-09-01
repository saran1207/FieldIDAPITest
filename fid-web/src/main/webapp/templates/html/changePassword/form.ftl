${action.setPageType('my_account', 'change_password')!}
<@s.form action="updatePassword" theme="fieldid" cssClass="crudForm">
	
	<#include "/templates/html/common/_formErrors.ftl"/>
    <div class="infoSet">
        <label><@s.text name="label.currentpassword"/></label>
        <@s.password name="originalPassword"/>
    </div>
	<div class="infoSet">
		<label><@s.text name="label.newpassword"/></label>
		<@s.password name="newPassword" />
	</div>
	<div class="infoSet">
		<label><@s.text name="label.confirmpassword"/></label>
		<@s.password name="confirmPassword" />
	</div>
	
	<div class="formAction">
		<@s.submit key="hbutton.save"/>
		<@s.text name="label.or"/>
		<@s.url id="cancelUrl" action="myAccount"/>
		<a href="#" onclick="redirect('${cancelUrl}'); return false;"><@s.text name="label.cancel"/></a>
	</div>
</@s.form>