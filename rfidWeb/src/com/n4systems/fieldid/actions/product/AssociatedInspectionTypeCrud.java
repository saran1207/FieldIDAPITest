package com.n4systems.fieldid.actions.product;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.fieldid.actions.api.AbstractCrud;
import com.n4systems.handlers.remover.InspectionFrequenciesDeleteHandler;
import com.n4systems.handlers.remover.InspectionFrequenciesDeleteHandlerImpl;
import com.n4systems.model.AssociatedInspectionType;
import com.n4systems.model.InspectionType;
import com.n4systems.model.ProductType;
import com.n4systems.model.api.Archivable.EntityState;
import com.n4systems.model.inspectiontype.AssociatedInspectionTypeSaver;
import com.n4systems.model.inspectiontype.InspectionFrequencySaver;
import com.n4systems.persistence.Transaction;
import com.n4systems.util.persistence.QueryBuilder;

public class AssociatedInspectionTypeCrud extends AbstractCrud {

	private static Logger logger = Logger.getLogger(AssociatedInspectionTypeCrud.class);
	private static final long serialVersionUID = 1L;

	private ProductType productType;
	private List<InspectionType> inspectionTypes;
	private List<Boolean> productTypeInspections;

	public AssociatedInspectionTypeCrud(PersistenceManager persistenceManager) {
		super(persistenceManager);
	}

	@Override
	protected void initMemberFields() {
	}

	@Override
	protected void loadMemberFields(Long uniqueId) {
	}

	public String doList() {
		return SUCCESS;
	}

	public String doSave() {
		
		Transaction transaction = com.n4systems.persistence.PersistenceManager.startTransaction();
		List<InspectionType> selectedInspectionTypes = findInspectionsTypesSet();
		List<AssociatedInspectionType> types = getLoaderFactory().createAssociatedInspectionTypesLoader().setProductType(productType).load(transaction);
		
		try {
			
			List<AssociatedInspectionType> toBeAdded = findInspectionTypesToAdd(selectedInspectionTypes, types);
			List<AssociatedInspectionType> toBeRemoved = findInspectionTypesToRemoved(selectedInspectionTypes, types);
			AssociatedInspectionTypeSaver saver = new AssociatedInspectionTypeSaver();
			
			InspectionFrequenciesDeleteHandler frequenciesDeleteHandler = new InspectionFrequenciesDeleteHandlerImpl(getLoaderFactory().createInspectionFrequenciesListLoader(), new InspectionFrequencySaver());
			
			for (AssociatedInspectionType associatedInspectionType : toBeRemoved) {
				saver.remove(transaction, associatedInspectionType);
				frequenciesDeleteHandler.forAssociatedInspectionType(associatedInspectionType).remove(transaction);
			}
			
			for (AssociatedInspectionType associatedInspectionType : toBeAdded) {
				saver.save(transaction, associatedInspectionType);
			}

			
			transaction.commit();
			
			addFlashMessageText("message.inspectiontypesselected");
		} catch (Exception e) {
			addActionErrorText("error.failedtosaveinspectiontypeselection");
			logger.error("failed to change the inspection type selection", e);
			transaction.rollback();
			
			return ERROR;
		}

		return SUCCESS;
	}

	private List<AssociatedInspectionType> findInspectionTypesToAdd(List<InspectionType> selectedInspectionTypes, List<AssociatedInspectionType> types) {
		List<AssociatedInspectionType> toBeAdded = new ArrayList<AssociatedInspectionType>();
		for (InspectionType selectedInspectionType : selectedInspectionTypes) {
			boolean found = false;
			for (AssociatedInspectionType associatedInspectionType : types) {
				if (associatedInspectionType.getInspectionType().equals(selectedInspectionType)) {
					found = true;
				}
			}
			if (!found) {
				toBeAdded.add(new AssociatedInspectionType(selectedInspectionType, productType));
			}
		}
		return toBeAdded;
	}
	
	private List<AssociatedInspectionType> findInspectionTypesToRemoved(List<InspectionType> selectedInspectionTypes, List<AssociatedInspectionType> types) {
		List<AssociatedInspectionType> toBeRemoved = new ArrayList<AssociatedInspectionType>();
		for (AssociatedInspectionType associatedInspectionType : types) {
			boolean found = false;
			
			for (InspectionType selectedInspectionType : selectedInspectionTypes) {
				if (associatedInspectionType.getInspectionType().equals(selectedInspectionType)) {
					found = true;
				}
			}
			if (!found) {
				toBeRemoved.add(associatedInspectionType);
			}
		}
		return toBeRemoved;
	}
	

	private List<InspectionType> findInspectionsTypesSet() {
		List<InspectionType> selectedInspectionTypes = new ArrayList<InspectionType>();

		for (InspectionType inspectionType : getInspectionTypes()) {
			if (productTypeInspections.get(getInspectionTypes().indexOf(inspectionType))) {
				selectedInspectionTypes.add(inspectionType);
			}
		}

		return selectedInspectionTypes;
	}

	/**
	 * @return the productType
	 */
	public Long getProductTypeId() {
		return (productType != null) ? productType.getId() : null;
	}

	/**
	 * @param productType
	 *            the productType to set
	 */
	public void setProductTypeId(Long productType) {
		try {
		if (productType == null) {
			this.productType = null;
		} else if (this.productType == null || productType.equals(this.productType.getId())) {
			this.productType = persistenceManager.find(new QueryBuilder<ProductType>(ProductType.class, getSecurityFilter())
														.addSimpleWhere("id", productType).addFetch("inspectionTypes"));
		}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public ProductType getProductType() {
		return productType;
	}

	/**
	 * @return the productTypeEvents
	 */
	public List<Boolean> getProductTypeInspectionTypes() {
		if (productTypeInspections == null) {
			productTypeInspections = new ArrayList<Boolean>();
			for (InspectionType inspectionType : getInspectionTypes()) {
				boolean found = false;
				for (AssociatedInspectionType associatedInspectionType : associatedInspectionTypes()) {
					if (inspectionType.equals(associatedInspectionType.getInspectionType())) {
						productTypeInspections.add(true);
						found = true;
						break;
					}
				}
				if (!found) {
					productTypeInspections.add(false);
				}
			}
		}
		return productTypeInspections;
	}

	private List<AssociatedInspectionType> associatedInspectionTypes() {
		return getLoaderFactory().createAssociatedInspectionTypesLoader().setProductType(productType).load();
	}

	/**
	 * @param productTypeEvents
	 *            the productTypeEvents to set
	 */
	public void setProductTypeInspectionTypes(List<Boolean> productTypeInspections) {
		this.productTypeInspections = productTypeInspections;
	}

	/**
	 * @return the eventTypes
	 */
	public List<InspectionType> getInspectionTypes() {
		if (inspectionTypes == null) {
			QueryBuilder<InspectionType> queryBuilder = new QueryBuilder<InspectionType>(InspectionType.class, getSecurityFilter());
			queryBuilder.addSimpleWhere("state", EntityState.ACTIVE);
			queryBuilder.addOrder("name");
			inspectionTypes = persistenceManager.findAll(queryBuilder);
		}
		return inspectionTypes;
	}

}
