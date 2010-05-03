package com.n4systems.model.inspectiontype;

import java.util.List;

import com.n4systems.model.ProductType;
import com.n4systems.persistence.Transaction;

public interface CommonProductTypeLoader {

	public CommonProductTypeLoader forAssets(List<Long> assetIds);

	public List<ProductType> load();
	
	public List<ProductType> load(Transaction transaction);
	

}