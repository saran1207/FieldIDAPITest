package com.n4systems.services.safetyNetwork.catalog.summary;

public class CatalogImportSummary {

	private ProductTypeGroupImportSummary productTypeGroupImportSummary = new ProductTypeGroupImportSummary();
	private ProductTypeImportSummary productTypeImportSummary = new ProductTypeImportSummary();
	private InspectionTypeImportSummary inspectionTypeImportSummary = new InspectionTypeImportSummary();
	private InspectionTypeGroupImportSummary inspectionTypeGroupImportSummary = new InspectionTypeGroupImportSummary();
	private StateSetImportSummary stateSetImportSummary = new StateSetImportSummary();
	private ProductTypeRelationshipsImportSummary productTypeRelationshipsImportSummary = new ProductTypeRelationshipsImportSummary();
	
		
	public ProductTypeImportSummary getProductTypeImportSummary() {
		return productTypeImportSummary;
	}
	
	public void setProductTypeImportSummary(ProductTypeImportSummary productTypeImportSummary) {
		this.productTypeImportSummary = productTypeImportSummary;
	}

	public InspectionTypeImportSummary getInspectionTypeImportSummary() {
		return inspectionTypeImportSummary;
	}

	public void setInspectionTypeImportSummary(InspectionTypeImportSummary inspectionTypeImportSummary) {
		this.inspectionTypeImportSummary = inspectionTypeImportSummary;
	}

	public InspectionTypeGroupImportSummary getInspectionTypeGroupImportSummary() {
		return inspectionTypeGroupImportSummary;
	}

	public void setInspectionTypeGroupImportSummary(InspectionTypeGroupImportSummary inspectionTypeGroupImportSummary) {
		this.inspectionTypeGroupImportSummary = inspectionTypeGroupImportSummary;
	}

	public StateSetImportSummary getStateSetImportSummary() {
		return stateSetImportSummary;
	}

	public void setStateSetImportSummary(StateSetImportSummary stateSetImportSummary) {
		this.stateSetImportSummary = stateSetImportSummary;
	}

	public ProductTypeGroupImportSummary getProductTypeGroupImportSummary() {
		return productTypeGroupImportSummary;
	}

	public void setProductTypeGroupImportSummary(ProductTypeGroupImportSummary productTypeGroupImportSummary) {
		this.productTypeGroupImportSummary = productTypeGroupImportSummary;
	}

	public ProductTypeRelationshipsImportSummary getProductTypeRelationshipsImportSummary() {
		return productTypeRelationshipsImportSummary;
	}

	
}
