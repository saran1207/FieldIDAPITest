package com.n4systems.fieldid.viewhelpers.handlers;

import com.n4systems.fieldid.service.download.TableGenerationContext;
import com.n4systems.fieldid.service.download.WebOutputHandler;
import com.n4systems.tools.MoneyUtils;

public class CurrencyHandler extends WebOutputHandler {
	
	public CurrencyHandler(TableGenerationContext action) {
		super(action);
	}

	private MoneyUtils moneyUtils = new MoneyUtils();

	public String handleWeb(Long entityId, Object value) {
		return moneyUtils.fromCents((Long)value);
	}

	public Object handleExcel(Long entityId, Object value) {
		return moneyUtils.fromCentsToDouble((Long)value);
	}
}
