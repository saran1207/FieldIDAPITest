<head>
	<style type="text/css">
		.resetPassword {
			float:right;
			width: 400px;
			padding:20px 10px;
			border: 2px solid #ccc;
			text-align: center;
			background-color: #eee;
		}
		.crudForm h2 { margin-top: 0; }
	</style>
</head>
${action.setPageType('user', 'change_password')!}

<@s.form action="adminUpdatePassword" theme="fieldid" cssClass="crudForm" cssStyle="float:left;">
	<@s.hidden name="uniqueID"/>
	<#include "/templates/html/common/_formErrors.ftl"/>
	<h2><@s.text name="label.change_password"/></h2>
	<p>
		<label class='label'><@s.text name="label.newpassword"/></label>
		<@s.password name="newPassword" />
	</p>
	<p>
		<label class='label'><@s.text name="label.confirmpassword"/></label>
		<@s.password name="confirmPassword" />
	</p>
	
	<div class="formAction borderLessFormAction">
		<@s.submit key="hbutton.save"/>
		<@s.text name="label.or"/>
		
		<#if user.fullUser || user.admin>
			<@s.url id="cancelUrl" action="employeeUserEdit" uniqueID="${uniqueID}"/>		
		<#elseif user.liteUser>
			<@s.url id="cancelUrl" action="liteUserEdit" uniqueID="${uniqueID}"/>
		<#else>
			<@s.url id="cancelUrl" action="readOnlyUserEdit" uniqueID="${uniqueID}"/>
		</#if>
		<a href="#" onclick="redirect('${cancelUrl}'); return false;"><@s.text name="label.cancel"/></a>
	</div>
</@s.form>

<div class="columnSeperator">
	<@s.text name="label.or"/>
</div>
<div class="resetPassword easyForm">
	<button onclick="redirect('<@s.url action="adminSendResetPassword" userName="${user.userID}" uniqueID="${uniqueID}"/>')"><@s.text name="label.send_reset_email"/></button>
</div>