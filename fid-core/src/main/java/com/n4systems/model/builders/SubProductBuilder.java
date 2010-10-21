package com.n4systems.model.builders;

import static com.n4systems.model.builders.AssetBuilder.*;

import com.n4systems.model.Asset;
import com.n4systems.model.SubProduct;

public class SubProductBuilder extends BaseBuilder<SubProduct> {

	private Asset asset;
	private Asset masterAsset;
	
	public static SubProductBuilder aSubProduct() {
		return new SubProductBuilder(anAsset().build(), anAsset().build());
	}
	
	public SubProductBuilder(Asset product, Asset masterAsset) {
		this.asset = product;
		this.masterAsset = masterAsset;
	}
	
	public SubProductBuilder withMasterProduct(Asset masterAsset) {
		return new SubProductBuilder(asset, masterAsset);
	}
	
	public SubProductBuilder containingProduct(Asset product) {
		return new SubProductBuilder(product, masterAsset);
	}
	
	@Override
	public SubProduct createObject() {
		SubProduct subProduct = new SubProduct();
		subProduct.setId(id);
		subProduct.setAsset(asset);
		subProduct.setMasterProduct(masterAsset);
		
		return subProduct;
	}

}
