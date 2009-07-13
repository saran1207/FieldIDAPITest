package com.n4systems.fieldid.viewhelpers.handlers;

import com.n4systems.tools.MoneyUtils;

public class CurrencyHandler implements OutputHandler {
	private MoneyUtils moneyUtils = new MoneyUtils();

	public String handle(Long entityId, Object value) {
		return moneyUtils.fromCents((Long)value);
	}

	public boolean isLabel() {
		return false;
	}
}
