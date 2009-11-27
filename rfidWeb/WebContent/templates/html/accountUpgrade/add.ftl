<head>
	<@n4.includeStyle type="page" href="signUp"/>
</head>
${action.setPageType('account_settings', 'upgrade')!}
<#if upgradeCost?exists>
	<@s.form action="accountUpgradeComplete" id="upgradeAccount" cssClass="fullForm" theme="fieldid">
	
		<h2 class="clean">
			<@s.text name="label.upgrading_from_x_to_y">
				<@s.param>${action.currentPackageFilter().packageName?html}</@s.param>
				<@s.param>${upgradePackage.name?html}</@s.param>
			</@s.text>
		</h2>
		
		<p>
			<@s.text name="label.upgrade_information"/>
		</p>
		
	
		<#include "/templates/html/common/_formErrors.ftl"/>
		<@s.hidden name="upgradePackageId"/>
	
	
		<h3><@s.text name="label.charges"/></h3>
		<ul id="charges">
			<li>
				<@s.text name="label.you_will_be_charged_this_immediately"/>
				<span id="immediateChargeAmount">${upgradeCost.immediateCharge?string.currency}<span>
			</li>
		
			<li>
				<@s.text name="label.you_will_be_charged_this_at_your_next_billing_cycle"/>
				<span id="nextPaymentAmount">${upgradeCost.nextPayment?string.currency}</span>
			</li>
			<li>
				<@s.text name="label.your_next_billing_date_is"/>
				<span id="nextPaymentDate">${upgradeCost.nextPaymentDate}</span>
			</li>
		</ul>
		
		<h3><@s.text name="label.billing_information"/></h3>
		<div id="billingInformation">
			<div class="infoBlock">
				<div class="infoSet">
					<label class="label" for="po.number"><@s.text name="label.po_number"/></label>
					<@s.textfield name="purchaseOrderNumber"/>
				</div>
			</div>
		</div>
		
		
		
		<div class="actions">
			<@s.submit key="label.please_upgrade_my_account"/>
			<@s.text name="label.or"/>
			<a href="<@s.url action="accountUpgrades"/>"><@s.text name="label.do_not_upgrade_my_account"/></a>
		</div>
	
	</@s.form>

<#else>
	<div class="error">
		<@s.text name="error.could_not_contact_billing_provider"/>
	</div>
</#if>
	

