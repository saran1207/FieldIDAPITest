<head>
	<@n4.includeStyle type="page" href="signUp"/>
	<@n4.includeScript src="signUp"/>
	<@n4.includeScript src="timezone.js" />
	<@n4.includeScript>
		countryChangeUrl = "<@s.url action="getRegions" namespace="/ajax" />";
		pricingUrl = '<@s.url namespace="/public/ajax" action="signUpPackagePrice"/>';
	</@n4.includeScript>
</head>

<title><@s.text name="title.create_your_account"/></title>



<@s.form action="signUpCreate" theme="fieldid" id="mainContent" cssClass="fullForm">
	<h1><@s.text name="title.create_your_account"/></h1>
	<#include "../common/_formErrors.ftl"/>
	<@s.hidden name="signUpPackageId" cssClass="changesPrice"/>
	<div class="infoSection multiColumn">
		<div class="infoBlock">
			<div class="infoSet">
				<label class="label" for="companyName"><@s.text name="label.company_name"/></label>
				<@s.textfield name="signUp.companyName"/>
			</div>
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
			
			<div class="infoSet">
				<label class="label" for="phoneNumber"><@s.text name="label.phonenumber"/></label>
				<@s.textfield  name="signUp.phoneNumber"  />
			</div>
			
			<div class="infoSet">
				<label class="label"><@s.text name="label.country"/></label>
				<@s.select name="signUp.countryId" list="countries" listKey="id" listValue="displayName" cssClass="changesTimeZone"/>
			</div>
			<div class="infoSet">
				<label class="label"><@s.text name="label.timezone"/></label>
				<@s.select id="tzlist" name="signUp.timeZone" list="timeZones" listKey="id" listValue="displayName" emptyOption="false"/>
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
	</div>
	
	<hr/>
	<div class="infoSection infoBlock">
		<div class="infoSet">
			<label class="label" for="tenantName"><@s.text name="label.site_address"/></label>
			<span class="fieldHolder">
				<label>http://</label><span class="shortField"><@s.textfield theme="fieldidSimple" name="signUp.tenantName"/></span><label>.fieldid.com</label>
			</span>
		</div>
	</div>
	<hr/>
	<div class="infoSection">
		<div class="infoBlock fluidSets">
			<div class="infoSet">
				<label class="label" for="numberOfUsers"><@s.text name="label.number_of_users"/></label>
				<span class="fieldHolder shortField"><@s.textfield name="signUp.numberOfUsers" theme="simple" cssClass="changesPrice"/></span>
			</div>
			
			<div class="infoSet">
				<label class="label" for="purchasePhoneSupport"><@s.text name="label.phone_support"/></label>
				<span class="fieldHolder shortField"><@s.checkbox name="signUp.purchasePhoneSupport" theme="simple" cssClass="changesPrice"/></span>
			</div>
		
		</div>
	</div>
	
	<hr/>
	
	<div class="infoSection">
		<div class="infoBlock">
			<@s.text name="label.total_amount_payable"/> $<span id="totalPrice">????</span>  <@s.reset key="label.update_price" cssClass="updatePrice"/>
		</div>
	</div>
	
	<div class="actions">
		<@s.submit key="label.create_my_account"/> <@s.text name="label.or"/> <a href="<@s.url action="signUpPackages"/>"><@s.text name="label.choose_another_package"/></a> 
	</div>
</@s.form>


