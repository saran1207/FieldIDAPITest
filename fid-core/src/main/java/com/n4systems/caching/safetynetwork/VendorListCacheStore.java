package com.n4systems.caching.safetynetwork;

import java.util.List;

import com.n4systems.caching.SimpleCacheStore;
import com.n4systems.model.orgs.InternalOrg;
import com.n4systems.util.ListHelper;
import com.n4systems.util.ListingPair;

public class VendorListCacheStore extends SimpleCacheStore<VendorListCacheKey, List<InternalOrg>> {

	public VendorListCacheStore() {
		super (new VendorListCacheLoader());
	}

	public boolean contains(InternalOrg org) {
		return contains(new VendorListCacheKey(org));
	}

	public void expire(InternalOrg org) {
		expire(new VendorListCacheKey(org));
	}

	public List<InternalOrg> get(InternalOrg org) {
		return get(new VendorListCacheKey(org));
	}
	
	public List<ListingPair> getAsListingPair(InternalOrg org) {
		return ListHelper.longListableToListingPair(get(org));
	}

	public void load(InternalOrg org) {
		load(new VendorListCacheKey(org));
	}

	public void put(InternalOrg org, List<InternalOrg> value) {
		put(new VendorListCacheKey(org), value);
	}
	
}
