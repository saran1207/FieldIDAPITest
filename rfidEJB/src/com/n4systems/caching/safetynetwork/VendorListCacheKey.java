package com.n4systems.caching.safetynetwork;

import com.n4systems.caching.CacheKey;
import com.n4systems.model.orgs.InternalOrg;
import com.n4systems.util.HashCode;

public class VendorListCacheKey extends CacheKey {
	private final InternalOrg internalOrg;

	public VendorListCacheKey(InternalOrg internalOrg) {
		this.internalOrg = internalOrg;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof VendorListCacheKey)) {
			return false;
		}
		
		return ((VendorListCacheKey)obj).getInternalOrg().equals(internalOrg);
	}

	@Override
	public int hashCode() {
		return HashCode.newHash().add(internalOrg).toHash();
	}

	public InternalOrg getInternalOrg() {
		return internalOrg;
	}
}
