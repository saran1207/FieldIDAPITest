$("immediateChargeAmount").update('${upgradeCost.immediateCharge?string.currency}');
$("nextPaymentAmount").update('${upgradeCost.nextPayment?string.currency}');
$("nextPaymentDate").update('${upgradeCost.nextPaymentDate}');