package com.n4systems.util;

import com.n4systems.model.Product;

public class ProductRemovalSummary {
	private Product product;
	
	private Long inspectionsToDelete = 0L;
	private boolean detatachFromMaster = false;
	private Long subProductsToDettach = 0L;
	private Long schedulesToDelete = 0L;
	private Long productUsedInMasterInpsection = 0L;
	private Long projectToDetachFrom = 0L;
	

	public ProductRemovalSummary( Product product ) {
		this.product = product;
	}
	
	public boolean validToDelete() {
		return (productUsedInMasterInpsection == 0);
	}

	public Long getInspectionsToDelete() {
		return inspectionsToDelete;
	}

	public void setInspectionsToDelete(Long inspectionsToDelete) {
		this.inspectionsToDelete = inspectionsToDelete;
	}

	public boolean isDetatachFromMaster() {
		return detatachFromMaster;
	}

	public void setDetatachFromMaster(boolean detatachFromMaster) {
		this.detatachFromMaster = detatachFromMaster;
	}

	public Long getSubProductsToDettach() {
		return subProductsToDettach;
	}

	public void setSubProductsToDettach(Long subProductsToDettach) {
		this.subProductsToDettach = subProductsToDettach;
	}

	public Long getSchedulesToDelete() {
		return schedulesToDelete;
	}

	public void setSchedulesToDelete(Long schedulesToDelete) {
		this.schedulesToDelete = schedulesToDelete;
	}

	public Long getProductUsedInMasterInpsection() {
		return productUsedInMasterInpsection;
	}

	public void setProductUsedInMasterInpsection(Long productUsedInMasterInpsection) {
		this.productUsedInMasterInpsection = productUsedInMasterInpsection;
	}

	public Long getProjectToDetachFrom() {
		return projectToDetachFrom;
	}

	public void setProjectToDetachFrom(Long projectToDetachFrom) {
		this.projectToDetachFrom = projectToDetachFrom;
	}

	public Product getProduct() {
		return product;
	}

}