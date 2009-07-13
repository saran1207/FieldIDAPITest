<head>
	<style type="text/css">
		.resetPassword {
			float:right;
			width: 400px;
			padding:20px 10px;
			border: 2px solid #ddd;
			text-align: center;
			background-color: #D0DAFD;
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
	
	<div class="formAction">
		<@s.url id="cancelUrl" action="userEdit" uniqueID="${uniqueID}"/>
		<@s.reset key="label.cancel" onclick="redirect('${cancelUrl}'); return false;"/>
		<@s.submit key="hbutton.save"/>
	</div>
</@s.form>

<div class="columnSeperator">
	<@s.text name="label.or"/>
</div>
<div class="resetPassword easyForm">
	<button onclick="redirect('<@s.url action="adminSendResetPassword" userName="${user.userID}" uniqueID="${uniqueID}"/>')"><@s.text name="label.send_reset_email"/></button>
</div>