package com.n4systems.model.builders;

import com.n4systems.model.InspectionType;
import com.n4systems.model.ProductType;
import com.n4systems.model.ProductTypeSchedule;

public class ProductTypeScheduleBuilder extends BaseBuilder<ProductTypeSchedule> {
	
	private final ProductType productType;
	private final InspectionType inspectionType;
	private final Long frequencyInDays;
	
	
	public static ProductTypeScheduleBuilder aProductTypeSchedule() {
		return new ProductTypeScheduleBuilder(ProductTypeBuilder.aProductType().build(), InspectionTypeBuilder.anInspectionType().build(), 365L);
	}

	private ProductTypeScheduleBuilder(ProductType productType, InspectionType inspectionType, Long frequencyInDays) {
		this.productType = productType;
		this.inspectionType = inspectionType;
		this.frequencyInDays = frequencyInDays;
	}

	@Override
	public ProductTypeSchedule build() {
		ProductTypeSchedule schedule = new ProductTypeSchedule();
		
		schedule.setId(generateNewId());
		schedule.setProductType(productType);
		schedule.setInspectionType(inspectionType);
		schedule.setFrequency(frequencyInDays);
		return schedule;
	}

}
