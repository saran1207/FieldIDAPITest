package com.n4systems.services.product;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rfid.ejb.entity.UserBean;

import com.n4systems.ejb.InspectionManager;
import com.n4systems.ejb.PersistenceManager;
import com.n4systems.ejb.ProductManager;
import com.n4systems.exceptions.ProcessFailureException;
import com.n4systems.exceptions.TenantNotValidForActionException;
import com.n4systems.exceptions.UsedOnMasterInspectionException;
import com.n4systems.exceptions.product.DuplicateProductException;
import com.n4systems.exceptions.product.ProductTypeMissMatchException;
import com.n4systems.model.Inspection;
import com.n4systems.model.InspectionSchedule;
import com.n4systems.model.Product;
import com.n4systems.model.SubInspection;
import com.n4systems.model.api.Archivable.EntityState;
import com.n4systems.model.security.OpenSecurityFilter;
import com.n4systems.services.InspectionScheduleService;
import com.n4systems.services.InspectionScheduleServiceImpl;
import com.n4systems.util.persistence.QueryBuilder;

public class ProductMerger {

	private final PersistenceManager persistenceManager;
	private final ProductManager productManager;
	private final InspectionManager inspectionManager;
	private final UserBean user;

	private final InspectionScheduleService scheduleService;

	public ProductMerger(PersistenceManager persistenceManager, ProductManager productManager, InspectionManager inspectionManger, UserBean user) {
		this(persistenceManager, productManager, inspectionManger, new InspectionScheduleServiceImpl(persistenceManager), user);
	}

	public ProductMerger(PersistenceManager persistenceManager, ProductManager productManager, InspectionManager inspectionManger, InspectionScheduleService inspectionScheduleService, UserBean user) {
		super();
		this.persistenceManager = persistenceManager;
		this.productManager = productManager;
		this.inspectionManager = inspectionManger;
		this.user = user;
		this.scheduleService = inspectionScheduleService;
	}

	public Product merge(Product winningProduct, Product losingProduct) {
		guard(winningProduct, losingProduct);

		moveInspections(winningProduct, losingProduct);
		moveSubInspections(winningProduct, losingProduct);
		archiveLosingProduct(losingProduct);

		return winningProduct;
	}

	private void guard(Product winningProduct, Product losingProduct) {
		if (!losingProduct.getType().equals(winningProduct.getType())) {
			throw new ProductTypeMissMatchException("product types must match");
		}
		
		if (losingProduct.equals(winningProduct)) {
			throw new DuplicateProductException("you can't merge a product into itself.");
		}

		if (!losingProduct.getTenant().equals(winningProduct.getTenant())) {
			throw new TenantNotValidForActionException("tenants must match");
		}
	}

	private void archiveLosingProduct(Product losingProduct) {
		try {
			productManager.archive(losingProduct, user);
		} catch (UsedOnMasterInspectionException e) {
			throw new ProcessFailureException("could not archive the product. still on a master", e);
		}
	}

	private void moveInspections(Product winningProduct, Product losingProduct) {
		QueryBuilder<Inspection> inspections = new QueryBuilder<Inspection>(Inspection.class, new OpenSecurityFilter()).addSimpleWhere("state", EntityState.ACTIVE).addSimpleWhere("product", losingProduct);
		List<Inspection> inspectionsToMove = persistenceManager.findAll(inspections);

		for (Inspection inspectionToMove : inspectionsToMove) {
			inspectionToMove.setProduct(winningProduct);
			updateInspection(inspectionToMove);
			updateSchedule(winningProduct, inspectionToMove.getSchedule());
		}
	}

	private void updateSchedule(Product winningProduct, InspectionSchedule schedule) {
		if (schedule != null) {
			schedule.setProduct(winningProduct);
			scheduleService.updateSchedule(schedule);
		}
	}

	private void moveSubInspections(Product winningProduct, Product losingProduct) {
		String query = "SELECT DISTINCT master from " + Inspection.class.getName() + " master, IN (master.subInspections) subInspection "
				+ "where subInspection.product = :losingProduct AND master.state = :activeState";
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("losingProduct", losingProduct);
		parameters.put("activeState", EntityState.ACTIVE);
		List<Inspection> masterInspectionsWithSubInspectionToMove = persistenceManager.passThroughFindAll(query, parameters);

		for (Inspection masterInspection : masterInspectionsWithSubInspectionToMove) {
			updateSubInspectionProducts(winningProduct, losingProduct, masterInspection);
			updateInspection(masterInspection);
		}
	}

	private void updateInspection(Inspection inspection) {
		try {
			inspectionManager.updateInspection(inspection, user.getId(), null, null);
		} catch (Exception e) {
			throw new ProcessFailureException("could not update inspections to new product", e);
		}
	}

	private void updateSubInspectionProducts(Product winningProduct, Product losingProduct, Inspection masterInspection) {
		for (SubInspection subInspection : masterInspection.getSubInspections()) {
			if (subInspection.getProduct().equals(losingProduct)) {
				subInspection.setProduct(winningProduct);
			}
		}
	}
}
