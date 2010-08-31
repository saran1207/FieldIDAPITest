package com.n4systems.model.builders;

import static com.n4systems.model.builders.ProductBuilder.*;

import com.n4systems.model.Product;
import com.n4systems.model.SubProduct;

public class SubProductBuilder extends BaseBuilder<SubProduct> {

	private Product product;
	private Product masterProduct;
	
	public static SubProductBuilder aSubProduct() {
		return new SubProductBuilder(aProduct().build(), aProduct().build());
	}
	
	public SubProductBuilder(Product product, Product masterProduct) {
		super();
		this.product = product;
		this.masterProduct = masterProduct;
	}
	
	public SubProductBuilder withMasterProduct(Product masterProduct) {
		return new SubProductBuilder(product, masterProduct);
	}
	
	public SubProductBuilder containingProduct(Product product) {
		return new SubProductBuilder(product, masterProduct);
	}
	
	@Override
	public SubProduct build() {
		SubProduct subProduct = new SubProduct();
		subProduct.setId(id);
		subProduct.setProduct(product);
		subProduct.setMasterProduct(masterProduct);
		
		return subProduct;
	}

}
