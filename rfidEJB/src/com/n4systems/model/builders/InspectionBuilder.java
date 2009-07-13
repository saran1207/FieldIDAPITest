package com.n4systems.model.builders;

import static com.n4systems.model.builders.InspectionTypeBuilder.*;
import static com.n4systems.model.builders.ProductBuilder.*;

import java.util.ArrayList;
import java.util.List;

import com.n4systems.model.Inspection;
import com.n4systems.model.InspectionSchedule;
import com.n4systems.model.InspectionType;
import com.n4systems.model.Product;
import com.n4systems.model.SubInspection;

public class InspectionBuilder extends BaseBuilder<Inspection> {

	private final InspectionType inspectionType;
	private final Product product;
	private final List<SubInspection> subInspections;
	
		
	public static InspectionBuilder anInspection() {
		return new InspectionBuilder(anInspectionType().build(), aProduct().build(), new ArrayList<SubInspection>());
	}
	
	public InspectionBuilder(InspectionType type, Product product, List<SubInspection> subInspections) {
		this.inspectionType = type;
		this.product = product;
		this.subInspections = subInspections;
	}
	
	public InspectionBuilder ofType(InspectionType type) {
		return new InspectionBuilder(type, product, subInspections);
	}
	
	public InspectionBuilder on(Product product) {
		return new InspectionBuilder(inspectionType, product, subInspections);
	}
	
	public InspectionBuilder withSubInspections(List<SubInspection> subInspections) {
		return new InspectionBuilder(inspectionType, product, subInspections);
	}
	
	public InspectionBuilder withScheduleSet(InspectionSchedule schedule) {
		return new InspectionBuilder(inspectionType, product, subInspections);
	}
	
	@Override
	public Inspection build() {
		Inspection inspection = new Inspection();
		inspection.setId(id);
		inspection.setType(inspectionType);
		inspection.setProduct(product);
		inspection.setSubInspections(subInspections);
		
		
		return inspection;
	}
	
	

}
