package com.n4systems.fieldid.viewhelpers.handlers;

import com.n4systems.fieldid.service.download.TableGenerationContext;
import com.n4systems.fieldid.service.download.WebOutputHandler;
import com.n4systems.model.Asset;
import com.n4systems.model.security.SecurityLevel;
import com.n4systems.util.ConfigContext;
import com.n4systems.util.ConfigEntry;

public class EventRfidNumberHandler extends WebOutputHandler {

	public EventRfidNumberHandler(TableGenerationContext action) {
		super(action);
	}
	
	public String handleWeb(Long entityId, Object value) {
		Asset asset = (Asset)value;
		
		if (asset.getRfidNumber() == null) {
			// if the rfid is null, there's no point doing any more work
			return "";
		}
		
		SecurityLevel level = asset.getSecurityLevel(contextProvider.getOwner());
		
		// build the asset info link for local assets, just show the serial for network assets
		String rfidNumber;
		if (level.isLocal()) {
            String absoluteUrl = getAbsoluteUrl();
            absoluteUrl += "/fieldid/asset.action?uniqueId="+asset.getId();
			rfidNumber = String.format("<a class=\"identifierLink\" href='%s'>%s</a>", absoluteUrl, asset.getRfidNumber());
		} else {
			rfidNumber = asset.getRfidNumber();
		}
		
		return rfidNumber;
	}

    public Object handleExcel(Long entityId, Object value) {
		Asset asset = (Asset)value;
		
		return (asset.getRfidNumber() != null) ? asset.getRfidNumber() : "";
	}
	
}
