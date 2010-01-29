<head>
	<@n4.includeStyle type="page" href="signUp"/>
</head>

<title><@s.text name="title.account_created"/></title>

<div id="mainContent" class="fullForm">
	<h1><@s.text name="title.account_created"/></h1>

	<div class="infoSection multiColumn">
		<h2><@s.text name="label.your_login_information_and_details"/></h2>
		<div class="infoBlock">
			<div class="infoSet">
				<label class="label" for="firstName"><@s.text name="label.first_name"/></label>
				<span class="fieldHolder">${signUp.firstName}</span>
			</div>
			<div class="infoSet">
				<label class="label" for="lastName"><@s.text name="label.last_name"/></label>
				<span class="fieldHolder">${signUp.lastName}</span>
			</div>
			<div class="infoSet">
				<label class="label" for="email"><@s.text name="label.email"/></label>
				<span class="fieldHolder">${signUp.email}</span>
			</div>
			
			<div class="infoSet">
				<label class="label"><@s.text name="label.timezone"/></label>
				<span class="fieldHolder">${signUp.fullTimeZone}</span>
			</div>
		</div>
		
		<div class="infoBlock">
			<div class="infoSet">
				<label class="label" for="username"><@s.text name="label.username"/></label>
				<span class="fieldHolder">${signUp.username}</span>
			</div>
		</div>
		
	</div>
	
	
	<div class="infoSection">
		<h2><@s.text name="label.company_info"/></h2>
		<div class="multiColumn">
			<div class="infoSet infoBlock">
				<label class="label" for="companyName"><@s.text name="label.company_name"/></label>
				<span class="fieldHolder">${signUp.companyName}</span>
			</div>
			<div class="infoSet infoBlock">
				<label class="label" for="addressLines"><@s.text name="label.address"/></label>
				<span class="fieldHolder">${address.addressLine1}</span>
			</div>
			<div class="infoSet infoBlock">
				<label class="label" for="city"><@s.text name="label.city"/></label>
				<span class="fieldHolder">${address.city}</span>
			</div>
			<div class="infoSet infoBlock">
				<label class="label" for="state"><@s.text name="label.state"/></label>
				<span class="fieldHolder">${address.state}</span>
			</div>
			
			<div class="infoSet infoBlock">
				<label class="label" for="country"><@s.text name="label.country"/></label>
				<span class="fieldHolder">${address.countryFullName}</span>
				
			</div>
			<div class="infoSet infoBlock">
				<label class="label" for="postal"><@s.text name="label.zip_code"/></label>
				<span class="fieldHolder">${address.postal}</span>
			</div>
			<div class="infoSet infoBlock">
				<label class="label" for="phoneNumber"><@s.text name="label.phonenumber"/></label>
				<span class="fieldHolder">${signUp.phoneNumber}</span>
			</div>
		</div>
	</div>
	
	
	
	<#if !signUp.signUpPackage.free>
		<div class="infoSection">
			<h2><@s.text name="label.number_of_users_and_other_options"/></h2>
			<div class="infoBlock ">
				<div class="infoSet">
					<label class="label" for="numberOfUsers"><@s.text name="label.number_of_employee_users"/></label>
					<span class="fieldHolder">
						${signUp.numberOfUsers}
					</span>
				</div>
				
				<div class="infoSet">
					<label class="label" for="purchasingPhoneSupport"><@s.text name="label.phone_support"/></label>
					<span class="fieldHolder"> 
						${signUp.purchasingPhoneSupport?string(action.getText("label.yes"), action.getText("label.no"))}
					<span>
				</div>
				
			</div>
		</div>
		
		

		<div class="infoSection">
			<h2><@s.text name="label.payment_options"/></h2>
			<div class="infoBlock ">
				<div class="infoSet">
					<label class="label"><@s.text name="label.selected_payment_option"/></label>
					<span class="fieldHolder">
						<@s.text name="label.${signUp.paymentOption}"><@s.param>${signUpPackage.getPaymentOptionWithType(signUp.paymentOption).pricePerUserPerMonth?string.currency}</@s.param></@s.text>
					</span>
				</div>
			</div>
		</div>
		
		
		<div class="infoSection" id="totalPriceSection">
			<div class="infoBlock">
				<@s.text name="label.total_amount_payable"/> <span id="totalPrice">${price?string.currency}</span> <@s.text name="label.plus_applicable_taxes"/>
			</div>
		</div>
		
	
	</#if>
	<div id="signIndButton">
		<a href="${action.getLoginUrlForTenant(signUp.tenantName)}"><span><@s.text name="label.sign_in_now"/></span></a>
	</div>
</div>	  