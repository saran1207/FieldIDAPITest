package com.n4systems.services.limiters;

import java.io.Serializable;

public class ResourceLimit implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private long used = 0;
	private long maximum = 0;
	
	public ResourceLimit() {}

	public boolean isMaxed() {
		// if max is less then 0, then they have no max.  Otherwise check if used is less or equal to max
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

	@Override
	public String toString() {
		return String.format("%d/%d (%.2f%%) :: Maxed:%b", getUsed(), getMaximum(), getUsagePercent() * 100.0, isMaxed());
	}
	
	
}
