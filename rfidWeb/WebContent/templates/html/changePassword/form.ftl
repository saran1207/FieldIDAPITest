${action.setPageType('my_account', 'change_password')!}
<@s.form action="updatePassword" theme="fieldid" cssClass="crudForm">
	
	<#include "/templates/html/common/_formErrors.ftl"/>
	<#if !Session.passwordReset?exists >
		<p>
			<label><@s.text name="label.currentpassword"/></label>
			<@s.password name="originalPassword"/>
		</p>	
	</#if>
	<p>
		<label><@s.text name="label.newpassword"/></label>
		<@s.password name="newPassword" />
	</p>
	<p>
		<label><@s.text name="label.confirmpassword"/></label>
		<@s.password name="confirmPassword" />
	</p>
	
	<div class="formAction">
		<@s.url id="cancelUrl" action="myAccount"/>
		<@s.reset key="label.cancel" onclick="redirect('${cancelUrl}'); return false;"/>
		<@s.submit key="hbutton.save"/>
	</div>
</@s.form>