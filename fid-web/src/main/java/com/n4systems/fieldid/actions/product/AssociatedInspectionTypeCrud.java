package com.n4systems.fieldid.actions.product;

import java.util.ArrayList;
import java.util.List;

import com.n4systems.model.AssetType;
import org.apache.log4j.Logger;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.fieldid.actions.api.AbstractCrud;
import com.n4systems.fieldid.permissions.UserPermissionFilter;
import com.n4systems.handlers.remover.InspectionFrequenciesDeleteHandler;
import com.n4systems.handlers.remover.InspectionFrequenciesDeleteHandlerImpl;
import com.n4systems.model.AssociatedInspectionType;
import com.n4systems.model.InspectionType;
import com.n4systems.model.api.Archivable.EntityState;
import com.n4systems.model.inspectiontype.AssociatedInspectionTypeSaver;
import com.n4systems.model.inspectiontype.InspectionFrequencySaver;
import com.n4systems.persistence.Transaction;
import com.n4systems.security.Permissions;
import com.n4systems.util.persistence.QueryBuilder;

@UserPermissionFilter(userRequiresOneOf={Permissions.ManageSystemConfig})
public class AssociatedInspectionTypeCrud extends AbstractCrud {

	private static Logger logger = Logger.getLogger(AssociatedInspectionTypeCrud.class);
	private static final long serialVersionUID = 1L;

	private AssetType assetType;
	private List<InspectionType> inspectionTypes;
	private List<Boolean> assetTypeInspections;

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
		List<AssociatedInspectionType> types = getLoaderFactory().createAssociatedInspectionTypesLoader().setProductType(assetType).load(transaction);
		
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
				toBeAdded.add(new AssociatedInspectionType(selectedInspectionType, assetType));
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
			if (assetTypeInspections.get(getInspectionTypes().indexOf(inspectionType))) {
				selectedInspectionTypes.add(inspectionType);
			}
		}

		return selectedInspectionTypes;
	}

	/**
	 * @return the productType
	 */
	public Long getAssetTypeId() {
		return (assetType != null) ? assetType.getId() : null;
	}

	/**
	 * @param assetType
	 *            the productType to set
	 */
	public void setAssetTypeId(Long assetType) {
		if (assetType == null) {
			this.assetType = null;
		} else if (this.assetType == null || assetType.equals(this.assetType.getId())) {
			this.assetType = persistenceManager.find(new QueryBuilder<AssetType>(AssetType.class, getSecurityFilter())
														.addSimpleWhere("id", assetType));
		}
		
	}

	public AssetType getAssetType() {
		return assetType;
	}

	/**
	 * @return the productTypeEvents
	 */
	public List<Boolean> getAssetTypeInspectionTypes() {
		if (assetTypeInspections == null) {
			assetTypeInspections = new ArrayList<Boolean>();
			for (InspectionType inspectionType : getInspectionTypes()) {
				boolean found = false;
				for (AssociatedInspectionType associatedInspectionType : associatedInspectionTypes()) {
					if (inspectionType.equals(associatedInspectionType.getInspectionType())) {
						assetTypeInspections.add(true);
						found = true;
						break;
					}
				}
				if (!found) {
					assetTypeInspections.add(false);
				}
			}
		}
		return assetTypeInspections;
	}

	private List<AssociatedInspectionType> associatedInspectionTypes() {
		return getLoaderFactory().createAssociatedInspectionTypesLoader().setProductType(assetType).load();
	}

	/**
	 * @param productTypeEvents
	 *            the productTypeEvents to set
	 */
	public void setAssetTypeInspectionTypes(List<Boolean> assetTypeInspections) {
		this.assetTypeInspections = assetTypeInspections;
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
