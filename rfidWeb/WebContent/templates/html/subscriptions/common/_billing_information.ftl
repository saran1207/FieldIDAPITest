<head>
	<@n4.includeScript src="billingInformation"/>
</head>

<h3><@s.text name="label.billing_information"/></h3>
<#if currentSubscription.upgradeRequiresBillingInformation>
	
	<@s.hidden name="usingCreditCard" id="usingCreditCard" />
	<div id="billing_information">
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
							<@s.textfield name="purchaseOrderNumber" cssClass="payPO" />
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
<#else>
	<p>
		<@s.text name="label.your_credit_card_on_file_will_be_used"/>
	</p>
</#if>