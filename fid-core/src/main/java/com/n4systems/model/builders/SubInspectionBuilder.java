package com.n4systems.model.builders;

import static com.n4systems.model.builders.InspectionTypeBuilder.anInspectionType;
import static com.n4systems.model.builders.ProductBuilder.aProduct;
import com.n4systems.model.InspectionType;
import com.n4systems.model.Product;
import com.n4systems.model.SubInspection;

public class SubInspectionBuilder extends BaseBuilder<SubInspection> {

	private final String name;
	private final InspectionType inspectionType;
	private final Product product;
	
	public static SubInspectionBuilder aSubInspection(String name) {
		return new SubInspectionBuilder(name, anInspectionType().build(), aProduct().build());
	}
	
	
	
	public SubInspectionBuilder(String name, InspectionType inspectionType, Product product) {
		this.name = name;
		this.inspectionType = inspectionType;
		this.product = product;
	}
	
	public SubInspectionBuilder withType(InspectionType inspectionType) {
		return new SubInspectionBuilder(name, inspectionType, product);
	}
	
	public SubInspectionBuilder withProduct(Product product) {
		return new SubInspectionBuilder(name, inspectionType, product);
	}
	
	
	@Override
	public SubInspection build() {
		SubInspection subInspection = new SubInspection();
		subInspection.setId(id);
		subInspection.setName(name);
		subInspection.setType(inspectionType);
		subInspection.setProduct(product);
		return subInspection;
	}

}
