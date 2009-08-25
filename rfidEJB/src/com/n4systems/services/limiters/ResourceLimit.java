package com.n4systems.services.limiters;

import java.io.Serializable;

import com.n4systems.model.tenant.TenantLimit;

public abstract class ResourceLimit implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private long used = 0;
	private long maximum = 0;
	
	public ResourceLimit() {}

	public boolean isMaxed() {
		return (isUnlimited()) ? false : (used >= maximum);
	}
	
	public boolean isUnlimited() {
		return (maximum < 0);
	}
	
	public double getUsagePercent() {
		if (maximum == 0) {
			// if maximum is 0, always assume 100%
			return 1.0;
		} else if (isUnlimited()) {
			// if maximum < 0, always assume 0%
			return 0.0;
		} else {
			return ((double)used / (double)maximum);
		}
	}
	
	public long getUsed() {
		return used;
	}

	public void setUsed(long used) {
		this.used = used;
	}

	public long getMaximum() {
		return maximum;
	}

	public void setMaximum(long maximum) {
		this.maximum = maximum;
	}
	
	public long getAvailable() {
		if (isMaxed()) {
			// this ensures that if they're over 100%, we don't return a negative 
			return 0;
		} else if (isUnlimited()) {
			// Use the TenantLimits representation of unlimited
			return TenantLimit.UNLIMITED;
		} else {
			return maximum - used;
		}
	}

	@Override
	public String toString() {
		String max = (isUnlimited()) ? "unlimited" : String.valueOf(getMaximum());
		return String.format("%d/%s (%.2f%%) :: Maxed:%b", getUsed(), max, getUsagePercent() * 100.0, isMaxed());
	}
	
}
