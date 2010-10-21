package com.n4systems.services.product;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


import com.n4systems.ejb.InspectionManager;
import com.n4systems.ejb.PersistenceManager;
import com.n4systems.ejb.ProductManager;
import com.n4systems.exceptions.ProcessFailureException;
import com.n4systems.exceptions.TenantNotValidForActionException;
import com.n4systems.exceptions.UsedOnMasterInspectionException;
import com.n4systems.exceptions.product.DuplicateProductException;
import com.n4systems.exceptions.product.ProductTypeMissMatchException;
import com.n4systems.model.Asset;
import com.n4systems.model.Inspection;
import com.n4systems.model.InspectionSchedule;
import com.n4systems.model.SubInspection;
import com.n4systems.model.api.Archivable.EntityState;
import com.n4systems.model.security.OpenSecurityFilter;
import com.n4systems.model.user.User;
import com.n4systems.services.InspectionScheduleService;
import com.n4systems.services.InspectionScheduleServiceImpl;
import com.n4systems.util.persistence.QueryBuilder;

public class ProductMerger {

	private final PersistenceManager persistenceManager;
	private final ProductManager productManager;
	private final InspectionManager inspectionManager;
	private final User user;

	private final InspectionScheduleService scheduleService;

	public ProductMerger(PersistenceManager persistenceManager, ProductManager productManager, InspectionManager inspectionManger, User user) {
		this(persistenceManager, productManager, inspectionManger, new InspectionScheduleServiceImpl(persistenceManager), user);
	}

	public ProductMerger(PersistenceManager persistenceManager, ProductManager productManager, InspectionManager inspectionManger, InspectionScheduleService inspectionScheduleService, User user) {
		super();
		this.persistenceManager = persistenceManager;
		this.productManager = productManager;
		this.inspectionManager = inspectionManger;
		this.user = user;
		this.scheduleService = inspectionScheduleService;
	}

	public Asset merge(Asset winningAsset, Asset losingAsset) {
		guard(winningAsset, losingAsset);

		moveInspections(winningAsset, losingAsset);
		moveSubInspections(winningAsset, losingAsset);
		archiveLosingProduct(losingAsset);

		return winningAsset;
	}

	private void guard(Asset winningAsset, Asset losingAsset) {
		if (!losingAsset.getType().equals(winningAsset.getType())) {
			throw new ProductTypeMissMatchException("asset types must match");
		}
		
		if (losingAsset.equals(winningAsset)) {
			throw new DuplicateProductException("you can't merge a asset into itself.");
		}

		if (!losingAsset.getTenant().equals(winningAsset.getTenant())) {
			throw new TenantNotValidForActionException("tenants must match");
		}
	}

	private void archiveLosingProduct(Asset losingAsset) {
		try {
			productManager.archive(losingAsset, user);
		} catch (UsedOnMasterInspectionException e) {
			throw new ProcessFailureException("could not archive the asset. still on a master", e);
		}
	}

	private void moveInspections(Asset winningAsset, Asset losingAsset) {
		QueryBuilder<Inspection> inspections = new QueryBuilder<Inspection>(Inspection.class, new OpenSecurityFilter()).addSimpleWhere("state", EntityState.ACTIVE).addSimpleWhere("asset", losingAsset);
		List<Inspection> inspectionsToMove = persistenceManager.findAll(inspections);

		for (Inspection inspectionToMove : inspectionsToMove) {
			inspectionToMove.setAsset(winningAsset);
			updateInspection(inspectionToMove);
			updateSchedule(winningAsset, inspectionToMove.getSchedule());
		}
	}

	private void updateSchedule(Asset winningAsset, InspectionSchedule schedule) {
		if (schedule != null) {
			schedule.setAsset(winningAsset);
			scheduleService.updateSchedule(schedule);
		}
	}

	private void moveSubInspections(Asset winningAsset, Asset losingAsset) {
		String query = "SELECT DISTINCT master from " + Inspection.class.getName() + " master, IN (master.subInspections) subInspection "
				+ "where subInspection.asset = :losingAsset AND master.state = :activeState";
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("losingAsset", losingAsset);
		parameters.put("activeState", EntityState.ACTIVE);
		List<Inspection> masterInspectionsWithSubInspectionToMove = persistenceManager.passThroughFindAll(query, parameters);

		for (Inspection masterInspection : masterInspectionsWithSubInspectionToMove) {
			updateSubInspectionProducts(winningAsset, losingAsset, masterInspection);
			updateInspection(masterInspection);
		}
	}

	private void updateInspection(Inspection inspection) {
		try {
			inspectionManager.updateInspection(inspection, user.getId(), null, null);
		} catch (Exception e) {
			throw new ProcessFailureException("could not update inspections to new asset", e);
		}
	}

	private void updateSubInspectionProducts(Asset winningAsset, Asset losingAsset, Inspection masterInspection) {
		for (SubInspection subInspection : masterInspection.getSubInspections()) {
			if (subInspection.getAsset().equals(losingAsset)) {
				subInspection.setAsset(winningAsset);
			}
		}
	}
}
