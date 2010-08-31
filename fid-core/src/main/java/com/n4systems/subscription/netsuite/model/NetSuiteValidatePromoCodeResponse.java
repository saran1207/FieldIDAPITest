package com.n4systems.subscription.netsuite.model;

import com.n4systems.subscription.InvalidPromoCodeReason;

public class NetSuiteValidatePromoCodeResponse extends BaseResponse implements com.n4systems.subscription.ValidatePromoCodeResponse {

	private String details;

	public void setDetails(String details) {
		this.details = details;
	}

	public InvalidPromoCodeReason getReason() {
		if (isValid()) return null;
		
		InvalidPromoCodeReason reason = InvalidPromoCodeReason.OTHER_REASON;
		
		if (details.equals("NOTVALID")) {
			reason = InvalidPromoCodeReason.NOT_AN_ACTIVE_CODE;
		} else if (details.equals("NOTAVAILABLE")) {
			reason = InvalidPromoCodeReason.NO_LONGER_AVAILABLE;
		}
		
		return reason;
	}

	public boolean isValid() {
		return requestRespondedWithSuccess();
	}
}
