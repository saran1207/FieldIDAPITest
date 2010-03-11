<head>
	<@n4.includeStyle type="page" href="signUp"/>
	<@n4.includeScript src="signUp"/>
	<@n4.includeScript src="timezone" />
	<@n4.includeScript src="billingInformation"/>
	<@n4.includeScript>
		countryChangeUrl = "<@s.url action="getRegions" namespace="/public/ajax" />";
		pricingUrl = '<@s.url namespace="/public/ajax" action="signUpPackagePrice"/>';
		validatePromoCodeUrl = '<@s.url namespace="/public/ajax" action="validPromoCode"/>';
	</@n4.includeScript>
</head>

<title><@s.text name="title.you_have_selected_the"/> ${signUp.signUpPackage.name?html} <@s.text name="label.plan"/></title>



<@s.form action="signUpCreate" theme="fieldid" id="mainContent" cssClass="paymentForm fullForm">
	<div id="planSelectedTitle">
		
		<h1>
			<@s.text name="title.you_have_selected_the"/> 
			<span id="planSelected">${signUp.signUpPackage.name?html}</span> 
			<@s.text name="label.plan"/>
			<span class="very-weak">			 
				 <@s.text name="label.per_user_per_month"><@s.param>${signUp.signUpPackage.defaultPricePerUserPerMonth?string.currency}</@s.param></@s.text>
			</span>
		</h1>
		<div>
			<@s.text name="label.all_plans_include_free_trial"/>
		</div>
	</div>
	<#include "/templates/html/common/_formErrors.ftl"/>
	<@s.hidden name="signUpPackageId" cssClass="changesPrice"/>
	<@s.hidden name="usingCreditCard" id="usingCreditCard" />
	<@s.hidden name="refCode" id="refCode" />
	<div class="infoSection multiColumn">
		<h2><@s.text name="label.your_login_information_and_details"/></h2>
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
			
			
			<div class="infoSet">
				<label class="label"><@s.text name="label.country"/></label>
				<@s.select name="signUp.countryId" list="countries" listKey="id" listValue="displayName" cssClass="changesTimeZone setsCountry"/>
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
	
	<div class="infoSection">
		<h2><@s.text name="label.company_info"/></h2>
		<div class="multiColumn">
			<div class="infoSet infoBlock">
				<label class="label" for="companyName"><@s.text name="label.company_name"/></label>
				<@s.textfield name="signUp.companyName"/>
			</div>
			<div class="infoSet infoBlock">
				<label class="label" for="addressLines"><@s.text name="label.address"/></label>
				<@s.textfield name="address.addressLine1"/>
			</div>
			<div class="infoSet infoBlock">
				<label class="label" for="city"><@s.text name="label.city"/></label>
				<@s.textfield name="address.city"/>
			</div>
			<div class="infoSet infoBlock">
				<label class="label" for="state"><@s.text name="label.state"/></label>
				<@s.textfield name="address.state"/>
			</div>
			
			<div class="infoSet infoBlock">
				<label class="label" for="country"><@s.text name="label.country"/></label>
				<@s.select name="address.country" list="address.countries" listKey="id" listValue="displayName" cssClass="country" />
			</div>
			<div class="infoSet infoBlock">
				<label class="label" for="postal"><@s.text name="label.zip_code"/></label>
				<@s.textfield name="address.postal"/>
			</div>
			<div class="infoSet infoBlock">
				<label class="label" for="phoneNumber"><@s.text name="label.phonenumber"/></label>
				<@s.textfield  name="signUp.phoneNumber"  />
			</div>
		</div>
	</div>

	<div class="infoSection infoBlock">
		<h2><@s.text name="label.what_do_you_want_your_field_id_address_to_be"/></h2>
		<div class="infoSet">
			<span class="weak"><@s.text name="instructions.site_address"/></span>
			<span class="fieldHolder">
				<label>http://</label><span class="shortField"><@s.textfield theme="fieldidSimple" name="signUp.tenantName"/></span><label>.fieldid.com</label>
			</span>
		</div>
	</div>
	<#if !signUp.signUpPackage.free>
		<div class="infoSection">
			<h2><@s.text name="label.number_of_users_and_other_options"/></h2>
			<div class="infoBlock ">
				<div class="infoSet">
					<label class="label" for="numberOfUsers"><@s.text name="label.number_of_employee_users"/></label>
					<span class="fieldHolder shortField">
						<@s.textfield name="signUp.numberOfUsers" theme="simple" cssClass="changesPrice"/>
					</span>
					
				</div>
				
				<div class="infoSet">
					<label class="label" for="purchasingPhoneSupport"><@s.text name="label.phone_support"/> <span><@s.checkbox name="signUp.purchasingPhoneSupport" theme="simple" cssClass="changesPrice"/></span></label>
					<span class="weak"><@s.text name="instruction.phone_support_charge"/></span>
				</div>
			
			</div>
		</div>
	</#if>
	<div class="infoSection">
		<h2><@s.text name="label.promo_code"/></h2>
		<div class="infoBlock ">
			<div class="infoSet">
				<label class="label" for="promoCode"><@s.text name="label.promo_code"/></label>
				<span class="fieldHolder shortField"><@s.textfield name="signUp.promoCode" theme="fieldidSimple" id="promoCode" cssClass="changesPrice"/></span>
			</div>
			
					
		</div>
	</div>
	
	
	<#if !signUp.signUpPackage.free>
		<div class="infoSection">
			<h2><@s.text name="label.payment_options"/></h2>
			<div class="infoBlock ">
				<div class="infoSet">
					<ul>
						<#list signUp.paymentOptions as paymentOption>
						<li class="favouredChoice">
							<#assign paymentMap><#noparse>#</#noparse>{ '${paymentOption.paymentOption}':'<@s.text name="label.${paymentOption.paymentOption}"><@s.param>${paymentOption.pricePerUserPerMonth?string.currency}</@s.param></@s.text>'}</#assign>
							<@s.radio name="signUp.paymentOption" list="${paymentMap}" theme="simple" cssClass="changesPrice"/> 
						</li>
						</#list>
					</ul>
				</div>
			</div>
		</div>
		
		<div class="infoSection" id ="totalPriceSection">
			<div class="infoBlock">
				<@s.text name="label.total_amount_payable"/> <span id="totalPrice">${price?string.currency}</span> <@s.text name="label.plus_applicable_taxes"/> 
				
				<#if !signUp.signUpPackage.free><@s.reset key="label.update_price" cssClass="updatePrice"/></#if>
			</div>
		</div>
		
		<div class="infoSection" id="creditCardInformation">
			
			<div class="multiColumn">
				<div class="infoBlock" >
					<a href="#" id="payCreditCard"><@s.text name="label.pay_by_credit_card"/></a>
					<div id="payByCC">
						<div class="infoSet">
							<label class="label" for="creditCard.type"><@s.text name="label.credit_card_type"/></label>
							<@s.select name="creditCard.cCType" list="creditCard.creditCardTypes" listKey="name()" listValue="name()" cssClass="autoSize payCC"/>
						</div>
						
						<div class="infoSet">
							<label class="label" for="creditCard.name"><@s.text name="label.name_on_card" /></label>
							<@s.textfield name="creditCard.name" cssClass="payCC" />
						</div>
						
						<div class="infoSet">
							<label class="label" for="creditCard.number"><@s.text name="label.credit_card_number" /></label>
							<@s.textfield name="creditCard.number" cssClass="payCC" />
						</div>
						
						<div class="infoSet">
							<label class="label" for="creditCard.expiry"><@s.text name="label.expiry_date"/></label>
							<span class="fieldHolder">
								<@s.select name="creditCard.expiryMonth" list="creditCard.months" theme="fieldidSimple" cssClass="autoSize payCC"/> <@s.text name="label.date_seperator"/> <@s.select name="creditCard.expiryYear" list="creditCard.years" theme="fieldidSimple" cssClass="autoSize payCC"/>
							</span>
						</div>
					</div>
				</div>
				<div class="infoBlock">
					<a href="#" id="payPurchaseOrder"><@s.text name="label.pay_by_purchase_order"/></a>
					<div id="payByPO">
						<div class="infoSet">
							<label class="label" for="po.number"><@s.text name="label.po_number"/></label>
							<@s.textfield name="signUp.purchaseOrderNumber" cssClass="payPO" />
						</div>
					</div>
				</div>
			</div>
		</div>
	</#if>
	
	
	<div class="actions">
		<@s.submit key="label.create_my_account"/> <@s.text name="label.or"/> <a href="<@s.url action="signUpPackages"/>"><@s.text name="label.choose_another_package"/></a> 
	</div>
</@s.form>

<div id="secondaryContent">
	<h3><@s.text name="label.need_help_have_question"/></h3>
	<p><@s.text name="label.email"/>: <a href="mailto:sales@fieldid.com">sales@fieldid.com</a></p>
	<p><@s.text name="label.phone"/>: 416-599-6464</p>
	<p><@s.text name="label.toll_free"/>: 1-800-996-4797</p>
	
	<h3><@s.text name="label.how_is_phone_support_charged"/></h3>
	<p class="weak"><@s.text name="description.how_is_phone_support_charged"/></p>
	
	<h3><@s.text name="label.what_is_the_account_on_the_left"/></h3>
	<p class="weak"><@s.text name="description.what_is_the_account_on_the_left"/></p>
	
	<h3><@s.text name="label.what_if_i_need_to_purchase_rfid_tags"/></h3>
	<p class="weak"><@s.text name="description.what_if_i_need_to_purchase_rfid_tags"/></p>
	
</div>