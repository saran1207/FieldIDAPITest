<title>Upgrade Account</title>
${action.setPageType('account_settings', 'show')!}

<div id="currentPackage">
	${action.currentPackageFilter().packageName?html}
</div>

<@s.form action="accountUpgradeComplete" cssClass="fullForm" theme="fieldid">
	<#include "/templates/html/common/_formErrors.ftl"/>
	<@s.hidden name="upgradePackageId"/>
	<div id="upgradePackage">
		${upgradePackage.name?html}
	</div>

<#if upgradeCost?exists>
	<div id="immediateChargeAmount">
		${upgradeCost.immediateCharge?string.currency}
	</div>
	<div id="nextPayment">
		<span id="nextPaymentAmount">${upgradeCost.nextPayment?string.currency}</span>
		<span id="nextPaymentDate">${upgradeCost.nextPaymentDate}</span>
	</div>
	
	<div class="formActions">
		<@s.submit key="label.upgrade"/>
		<@s.text name="label.or"/>
		<a href="<@s.url action="accountUpgrades"/>"><@s.text name="label.cancel"/></a>
	</div>
<#else>
	<div class="error">
		<@s.text name="error.could_not_communicate_with_billing_provider"/>
	</div>
</#if>
</@s.form>
	

