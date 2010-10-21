package com.n4systems.util;

import com.n4systems.model.AssetType;

public class ProductTypeRemovalSummary {

	private AssetType assetType;

	private Long productsToDelete = 0L;
	private Long inspectionsToDelete = 0L;
	private Long subProductsToDettach = 0L;
	private Long schedulesToDelete = 0L;
	private Long productsUsedInMasterInpsection = 0L;
	private Long assetsToDettachFromProjects = 0L;
	private Long productTypesToDettachFrom = 0L;
	private Long productCodeMappingsToDelete = 0L;
	private Long masterProductsToDettach = 0L;

	public ProductTypeRemovalSummary(AssetType assetType) {
		this.assetType = assetType;
	}

	public boolean validToDelete() {
		return (productsUsedInMasterInpsection == 0);
	}

	public Long getProductsToDelete() {
		return productsToDelete;
	}

	public void setProductsToDelete(Long productsToDelete) {
		this.productsToDelete = productsToDelete;
	}

	public Long getInspectionsToDelete() {
		return inspectionsToDelete;
	}

	public void setInspectionsToDelete(Long inspectionsToDelete) {
		this.inspectionsToDelete = inspectionsToDelete;
	}

	public Long getSchedulesToDelete() {
		return schedulesToDelete;
	}

	public void setSchedulesToDelete(Long schedulesToDelete) {
		this.schedulesToDelete = schedulesToDelete;
	}

	public Long getProductsUsedInMasterInpsection() {
		return productsUsedInMasterInpsection;
	}

	public void setProductsUsedInMasterInpsection(Long productsUsedInMasterInpsection) {
		this.productsUsedInMasterInpsection = productsUsedInMasterInpsection;
	}

	public AssetType getProductType() {
		return assetType;
	}

	public Long getSubProductsToDettach() {
		return subProductsToDettach;
	}

	public void setSubProductsToDettach(Long subProductsToDettach) {
		this.subProductsToDettach = subProductsToDettach;
	}

	public Long getAssetsToDettachFromProjects() {
		return assetsToDettachFromProjects;
	}

	public void setAssetsToDettachFromProjects(Long assetsToDettachFromProjects) {
		this.assetsToDettachFromProjects = assetsToDettachFromProjects;
	}

	public Long getProductCodeMappingsToDelete() {
		return productCodeMappingsToDelete;
	}

	public void setProductCodeMappingsToDelete(Long productCodeMappingsToDelete) {
		this.productCodeMappingsToDelete = productCodeMappingsToDelete;
	}

	public Long getProductTypesToDettachFrom() {
		return productTypesToDettachFrom;
	}

	public void setProductTypesToDettachFrom(Long productTypesToDettachFrom) {
		this.productTypesToDettachFrom = productTypesToDettachFrom;
	}

	public Long getMasterProductsToDettach() {
		return masterProductsToDettach;
	}

	public void setMasterProductsToDettach(Long masterProductsToDettach) {
		this.masterProductsToDettach = masterProductsToDettach;
	}

}
