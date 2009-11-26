<title>Account Upgraded</title>
${action.setPageType('account_settings', 'show')!}

<div id="oldPackage">
	${action.currentPackageFilter().packageName?html}
</div>

<div id="newPackage">
	<div id="upgradePackage">
		${upgradePackage.name?html}
	</div>

	
	<div id="immediateChargeAmount">
		${upgradeResponse.cost.immediateCharge?string.currency}
	</div>
	<div id="nextPayment">
		<span id="nextPaymentAmount">${upgradeResponse.cost.nextPayment?string.currency}</span>
		<span id="nextPaymentDate">${upgradeResponse.cost.nextPaymentDate}</span>
	</div>
	
	<div class="formActions">
		<a href="<@s.url action="systemSettingsEdit"/>"><@s.text name="label.back_to"/> <@s.text name="label.account_settings"/></a>
	</div>
</div>
