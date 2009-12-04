<h3><@s.text name="label.charges"/></h3>
<ul id="charges">
	<li>
		<@s.text name="${charge_label}"/>
		<span id="immediateChargeAmount">${upgradeCost.immediateCharge?string.currency}</span>
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