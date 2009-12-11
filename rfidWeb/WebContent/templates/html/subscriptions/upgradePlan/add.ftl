<head>
	<@n4.includeStyle type="page" href="signUp"/>
	<@n4.includeScript src="signUp"/>
	<@n4.includeScript>
		pricingUrl = '<@s.url action="upgradePlanPriceCheck" namespace="/ajax"/>';
		pricingFormId = 'upgradeAccount';
		updatingMessage = '<@s.text name="label.updating_cost"/>';
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
		<#include "../common/_charges.ftl"/>
		
		<#include "../common/_billing_information.ftl"/>
		
		<div class="actions">
			<p id="purchaseWarning"><strong><@s.text name="label.purchase_warning"/></strong></p>
			<@s.submit key="label.please_upgrade_my_plan"/>
			<@s.text name="label.or"/>
			<a href="<@s.url action="systemSettingsEdit"/>"><@s.text name="label.do_not_upgrade_my_plan"/></a>
		</div>
	
	</@s.form>

<#else>
	<div class="error">
		<@s.text name="error.could_not_contact_billing_provider"/>
	</div>
</#if>
	

