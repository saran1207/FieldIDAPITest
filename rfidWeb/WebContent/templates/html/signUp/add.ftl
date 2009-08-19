<head>
	<@n4.includeStyle type="page" href="signUp"/>
</head>

<title><@s.text name="title.create_your_account"/></title>

<h1><@s.text name="title.create_your_account"/></h1>

<@s.form action="signUpCreate" theme="fieldid" id="mainContent" cssClass="fullForm">
	<#include "../common/_formErrors.ftl"/>
	<@s.hidden name="signUpPackageId"/>
	<div class="infoSection">
		
		<div class="infoBlock">
			<div class="infoSet">
				<label class="label" for="firstName"><@s.text name="label.first_name"/></label>
				<@s.textfield name="signUp.firstName"/>
			</div>
			<div class="infoSet">
				<label class="label" for="lastName"><@s.text name="label.last_name"/></label>
				<@s.textfield name="signUp.lastName"/>
			</div>
			<div class="infoSet">
				<label class="label" for="email"><@s.text name="label.email"/></label>
				<@s.textfield name="signUp.email"/>
			</div>
		</div>
		
		<div class="infoBlock">
			<div class="infoSet">
				<label class="label" for="username"><@s.text name="label.username"/></label>
				<@s.textfield name="signUp.username"/>
			</div>
			<div class="infoSet">
				<label class="label" for="password"><@s.text name="label.password"/></label>
				<@s.password name="signUp.password"/>
			</div>
			<div class="infoSet">
				<label class="label" for="passwordConfirm"><@s.text name="label.password_again"/></label>
				<@s.password name="signUp.passwordConfirm"/>
			</div>
		</div>
		
		<div class="infoBlock">
			<div class="infoSet">
				<label class="label" for="companyName"><@s.text name="label.company_name"/></label>
				<@s.textfield name="signUp.companyName"/>
			</div>
		</div>
	</div>
	
	
	<div class="infoSection infoBlock">
		<div class="infoSet">
			<label class="label" for="tenantName">http://</label>
			<@s.textfield name="signUp.tenantName"/><label>.fieldid.com</label>
		</div>
	</div>
	<div class="infoSection">
		<div class="infoBlock">
			<div class="infoSet">
				<label class="label" for="numberOfUsers"><@s.text name="label.number_of_users"/></label>
				<@s.textfield name="signUp.numberOfUsers"/>
			</div>
			<div class="infoSet">
				<label class="label" for="phoneSupport"><@s.text name="label.phone_support"/></label>
				<@s.checkbox name="signUp.phoneSupport"/>
			</div>
		</div>
	</div>
	
	<div class="infoSection">
		<div class="infoSet infoBlock">
			<label class="label" for="refcode"><@s.text name="label.referrer_code"/></label>
			<@s.textfield name="signUp.refcode"/>
		</div>
	</div>
	
	<div class="actions">
		<@s.submit key="label.finalize_order"/> <@s.text name="label.or"/> <a href="<@s.url action="signUpPackages"/>"><@s.text name="label.choose_another_package"/></a> 
	</div>
</@s.form>


