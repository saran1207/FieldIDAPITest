package com.n4systems.fieldid.viewhelpers.handlers;

import com.n4systems.fieldid.service.download.TableGenerationContext;
import com.n4systems.fieldid.service.download.WebOutputHandler;
import com.n4systems.model.api.DisplayEnum;


public class EnumHandler extends WebOutputHandler {

	public EnumHandler(TableGenerationContext action) {
		super(action);
	}

	public String handleWeb(Long entityId, Object value) {
		DisplayEnum label = (DisplayEnum)value;
		
		return label.getLabel();
	}

	public Object handleExcel(Long entityId, Object value) {
		return handleWeb(entityId, value);
	}
	
}
