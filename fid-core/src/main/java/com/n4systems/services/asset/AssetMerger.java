package com.n4systems.services.asset;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


import com.n4systems.ejb.AssetManager;
import com.n4systems.ejb.InspectionManager;
import com.n4systems.ejb.PersistenceManager;
import com.n4systems.exceptions.ProcessFailureException;
import com.n4systems.exceptions.TenantNotValidForActionException;
import com.n4systems.exceptions.UsedOnMasterInspectionException;
import com.n4systems.exceptions.asset.AssetTypeMissMatchException;
import com.n4systems.exceptions.asset.DuplicateAssetException;
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

public class AssetMerger {

	private final PersistenceManager persistenceManager;
	private final AssetManager assetManager;
	private final InspectionManager inspectionManager;
	private final User user;

	private final InspectionScheduleService scheduleService;

	public AssetMerger(PersistenceManager persistenceManager, AssetManager assetManager, InspectionManager inspectionManger, User user) {
		this(persistenceManager, assetManager, inspectionManger, new InspectionScheduleServiceImpl(persistenceManager), user);
	}

	public AssetMerger(PersistenceManager persistenceManager, AssetManager assetManager, InspectionManager inspectionManger, InspectionScheduleService inspectionScheduleService, User user) {
		this.persistenceManager = persistenceManager;
		this.assetManager = assetManager;
		this.inspectionManager = inspectionManger;
		this.user = user;
		this.scheduleService = inspectionScheduleService;
	}

	public Asset merge(Asset winningAsset, Asset losingAsset) {
		guard(winningAsset, losingAsset);

		moveInspections(winningAsset, losingAsset);
		moveSubInspections(winningAsset, losingAsset);
		archiveLosingAsset(losingAsset);

		return winningAsset;
	}

	private void guard(Asset winningAsset, Asset losingAsset) {
		if (!losingAsset.getType().equals(winningAsset.getType())) {
			throw new AssetTypeMissMatchException("asset types must match");
		}
		
		if (losingAsset.equals(winningAsset)) {
			throw new DuplicateAssetException("you can't merge an asset into itself.");
		}

		if (!losingAsset.getTenant().equals(winningAsset.getTenant())) {
			throw new TenantNotValidForActionException("tenants must match");
		}
	}

	private void archiveLosingAsset(Asset losingAsset) {
		try {
			assetManager.archive(losingAsset, user);
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
			updateSubInspectionAssets(winningAsset, losingAsset, masterInspection);
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

	private void updateSubInspectionAssets(Asset winningAsset, Asset losingAsset, Inspection masterInspection) {
		for (SubInspection subInspection : masterInspection.getSubInspections()) {
			if (subInspection.getAsset().equals(losingAsset)) {
				subInspection.setAsset(winningAsset);
			}
		}
	}
}
