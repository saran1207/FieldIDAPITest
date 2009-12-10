<head>
	<@n4.includeStyle type="page" href="signUp"/>
	<@n4.includeScript>
		function updatePrice() {
			var form = $('upgradeAccount').serialize(true);
			var options = new Object();
			$$(".changesPrice").each(function(element) {
					options[element.name] = form[element.name];
				}); 
			getResponse("<@s.url action="upgradePlanPriceCheck" namespace="/ajax"/>", "get", options);
		}
		
		function payPurchaseOrderClick(event) {
			event.stop();
			enablePO();
		}
		
		function payCreditCardClick(event) {
			event.stop();
			enableCC();
		}
		
		function enableCC() {
			$("usingCreditCard").value = "true";
			$$(".payCC").invoke("enable");
			$$(".payPO").invoke("disable");
		}
		
		function enablePO() {
			$("usingCreditCard").value = "false";
			$$(".payPO").invoke("enable");
			$$(".payCC").invoke("disable");
		}
		
		document.observe("dom:loaded", function() {
				$$(".changesPrice").each(function(element) {
						element.observe('change', updatePrice);
					});
				$$("#payPurchaseOrder").each(function(element) {
					element.observe("click", payPurchaseOrderClick);
				});
				$$("#payCreditCard").each(function(element) {
					element.observe("click", payCreditCardClick);
				});
				
				if($("usingCreditCard").getValue() == "true") {
					enableCC();
				} else {
					enablePO();
				}
				$('upgradeAccount').observe('submit', function(event) { 
						var form = Event.element(event);
						$$("#" + form.id + " input[type='submit']").invoke('disable');
					});
			});
	</@n4.includeScript>
</head>

${action.setPageType('account_settings', 'upgrade')!}
<#if upgradeCost?exists>
	<@s.form action="upgradePlanComplete" id="upgradeAccount" cssClass="fullForm" theme="fieldid">
		<#include "/templates/html/common/_formErrors.ftl"/>
		<h3 class="clean">
			<@s.text name="label.upgrading_from_x_to_y">
				<@s.param>${action.currentPackageFilter().packageName?html}</@s.param>
				<@s.param>${upgradePackage.name?html}</@s.param>
			</@s.text>
		</h3>
		
		<p>
			<@s.text name="label.upgrade_information"/>
		</p>
		
		<@s.hidden name="upgradePackageId" cssClass="changesPrice"/>
		
		<#if freeAccount >
			
			<h3><@s.text name="label.payment_options"/></h3>
			<div class="infoBlock ">
				<div class="infoSet">
					<ul>
						<#list upgradePackage.paymentOptions as option>
							<li class="favouredChoice">
								<#assign paymentMap><#noparse>#</#noparse>{ '${option.paymentOption}':'<@s.text name="label.${option.paymentOption}"><@s.param>${option.pricePerUserPerMonth?string.currency}</@s.param></@s.text>'}</#assign>
								<@s.radio name="paymentOption" list="${paymentMap}" theme="simple" cssClass="changesPrice"/> 
							</li>
						</#list>
					</ul>
				</div>
			</div>
			
			<div class="infoSection">
				<h3><@s.text name="label.add_on_phone_support"/></h3>
				<div class="infoBlock ">
					<div class="infoSet">
						<label class="label" for="purchasingPhoneSupport"><@s.text name="label.phone_support"/> (30%) <span><@s.checkbox name="purchasingPhoneSupport" theme="simple" cssClass="changesPrice"/></span></label>
					</div>
				
				</div>
			</div>
		
		</#if>
		<#assign charge_label="label.you_will_be_charged_this_immediately"/>
		<#include "_charges.ftl"/>
		
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
		
		<div class="actions">
			<p id="purchaseWarning"><strong><@s.text name="label.purchase_warning"/></strong></p>
			<@s.submit key="label.please_upgrade_my_plan"/>
			<@s.text name="label.or"/>
			<a href="<@s.url action="accountUpgrades"/>"><@s.text name="label.do_not_upgrade_my_plan"/></a>
		</div>
	
	</@s.form>

<#else>
	<div class="error">
	boo
		<@s.text name="error.could_not_contact_billing_provider"/>
	</div>
</#if>
	

