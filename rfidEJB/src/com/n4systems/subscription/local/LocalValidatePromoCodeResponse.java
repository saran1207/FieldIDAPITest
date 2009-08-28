package com.n4systems.subscription.local;

import com.n4systems.subscription.InvalidPromoCodeReason;
import com.n4systems.subscription.ValidatePromoCodeResponse;

public class LocalValidatePromoCodeResponse implements ValidatePromoCodeResponse {

	public InvalidPromoCodeReason getReason() {
		return null;
	}

	public boolean isValid() {
		return true;
	}

	public String getResult() {
		return "OK";
	}
}
