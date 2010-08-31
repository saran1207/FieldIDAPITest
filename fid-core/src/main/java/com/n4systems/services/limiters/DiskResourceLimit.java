package com.n4systems.services.limiters;

import com.n4systems.util.DataUnit;

public class DiskResourceLimit extends ResourceLimit {
	private static final long serialVersionUID = 1L;
	private static final DataUnit dataStorageUnit = DataUnit.BYTES;
	
	public DiskResourceLimit() {
		super();
	}

	public long getMaximumMB() {
		return getMaximum(DataUnit.MEBIBYTES);
	}

	public long getUsedMB() {
		return getUsed(DataUnit.MEBIBYTES);
	}
	
	public long getMaximum(DataUnit newUnit) {
		return dataStorageUnit.convertTo(getMaximum(), newUnit);
	}

	public long getUsed(DataUnit newUnit) {
		return dataStorageUnit.convertTo(getUsed(), newUnit);
	}
	
}
