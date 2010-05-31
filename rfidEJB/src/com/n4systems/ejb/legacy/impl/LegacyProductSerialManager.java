package com.n4systems.ejb.legacy.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import rfid.ejb.entity.AddProductHistoryBean;
import rfid.ejb.entity.InfoOptionBean;
import rfid.ejb.entity.ProductSerialExtensionBean;
import rfid.ejb.entity.ProductStatusBean;

import com.n4systems.ejb.InspectionScheduleManager;
import com.n4systems.ejb.MassUpdateManager;
import com.n4systems.ejb.PersistenceManager;
import com.n4systems.ejb.ProductManager;
import com.n4systems.ejb.impl.InspectionScheduleManagerImpl;
import com.n4systems.ejb.impl.MassUpdateManagerImpl;
import com.n4systems.ejb.impl.PersistenceManagerImpl;
import com.n4systems.ejb.impl.ProductManagerImpl;
import com.n4systems.ejb.legacy.LegacyProductSerial;
import com.n4systems.exceptions.SubProductUniquenessException;
import com.n4systems.exceptions.TransactionAlreadyProcessedException;
import com.n4systems.model.ExtendedFeature;
import com.n4systems.model.Inspection;
import com.n4systems.model.InspectionSchedule;
import com.n4systems.model.Product;
import com.n4systems.model.SubProduct;
import com.n4systems.model.Tenant;
import com.n4systems.model.InspectionSchedule.ScheduleStatus;
import com.n4systems.model.api.Archivable.EntityState;
import com.n4systems.model.orgs.PrimaryOrg;
import com.n4systems.model.product.ProductSaver;
import com.n4systems.model.security.OpenSecurityFilter;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.model.security.TenantOnlySecurityFilter;
import com.n4systems.model.user.User;
import com.n4systems.model.utils.FindSubProducts;
import com.n4systems.util.ListHelper;
import com.n4systems.util.TransactionSupervisor;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereParameter.Comparator;


public class LegacyProductSerialManager implements LegacyProductSerial {
	private static final Logger logger = Logger.getLogger(LegacyProductSerialManager.class);

	
	protected EntityManager em;

	
	private PersistenceManager persistenceManager;

	
	private InspectionScheduleManager inspectionScheduleManager;

	
	private ProductManager productManager;

	
	private MassUpdateManager massUpdateManager;

	private Logger auditLogger = Logger.getLogger("AuditLog");
	
	
	
	
	
	public LegacyProductSerialManager(EntityManager em) {
		super();
		this.em = em;
		persistenceManager = new PersistenceManagerImpl(em);
		inspectionScheduleManager =  new InspectionScheduleManagerImpl(em);
		productManager =  new ProductManagerImpl(em);
		massUpdateManager = new MassUpdateManagerImpl(em);
	}

	


	public ProductStatusBean findProductStatus(Long uniqueID, Long tenantId) {
		Query query = em.createQuery("FROM ProductStatusBean ps WHERE ps.uniqueID = :uniqueID AND ps.tenant.id = :tenantId");
		query.setParameter("uniqueID", uniqueID);
		query.setParameter("tenantId", tenantId);
		ProductStatusBean obj = null;
		try {
			obj = (ProductStatusBean) query.getSingleResult();
		} catch (NoResultException e) {
			obj = null;
		}
		return obj;
	}

	@SuppressWarnings("unchecked")
	public List<ProductStatusBean> findProductStatus(Long tenantId, Date beginDate) {
		Query query = em.createQuery("from ProductStatusBean ps where ps.tenant.id = :tenantId and ps.dateCreated >= :beginDate");
		query.setParameter("tenantId", tenantId);
		query.setParameter("beginDate", beginDate);

		return query.getResultList();
	}

	public Long createProductStatus(ProductStatusBean productStatus) {
		em.persist(productStatus);

		return productStatus.getUniqueID();
	}

	public Long updateProductStatus(ProductStatusBean productStatus) {
		productStatus.setDateModified(new Date());
		em.merge(productStatus);
		return productStatus.getUniqueID();
	}

	@SuppressWarnings("unchecked")
	public List<ProductStatusBean> getAllProductStatus(Long tenantId) {
		Query query = em.createQuery("from ProductStatusBean ps where ps.tenant.id = :tenantId");
		query.setParameter("tenantId", tenantId);

		return query.getResultList();
	}

	



	public boolean rfidExists(String rfidNumber, Long tenantId) {
		return rfidExists(rfidNumber, tenantId, null);
	}

	public boolean rfidExists(String rfidNumber, Long tenantId, Long uniqueID) {
		long rfidCount = 0;
		String uniqueIDClause = "";
		// null or zero-length rfidNumbers are never duplicates
		if (rfidNumber == null || rfidNumber.trim().length() == 0) {
			return false;
		}

		if (uniqueID != null) {
			uniqueIDClause = " and p.id <> :id";
		}

		Query query = em.createQuery("select count(p) from Product p where p.state = :activeState AND UPPER( p.rfidNumber ) = :rfidNumber" + uniqueIDClause
				+ " and p.tenant.id = :tenantId group by p.rfidNumber");

		query.setParameter("rfidNumber", rfidNumber.toUpperCase());
		query.setParameter("tenantId", tenantId);
		query.setParameter("activeState", EntityState.ACTIVE);

		if (uniqueID != null) {
			query.setParameter("id", uniqueID);
		}

		try {
			rfidCount = (Long) query.getSingleResult();
		} catch (NoResultException e) {
			rfidCount = 0;
		}

		return (rfidCount > 0) ? true : false;
	}

	
	
	public Product create(Product product, User modifiedBy) throws SubProductUniquenessException {
		runProductSavePreRecs(product, modifiedBy);
		
		ProductSaver saver = new ProductSaver();
		saver.setModifiedBy(modifiedBy);
		
		product = saver.update(em, product);

		saveSubProducts(product);
		// XXX not sure if this should be here.
		inspectionScheduleManager.autoSchedule(product);
		return new FindSubProducts(persistenceManager, product).fillInSubProducts();
		
	}

	private void saveSubProducts(Product product) {
		for (SubProduct subProduct : product.getSubProducts()) {
			persistenceManager.update(subProduct);
		}
		
	}
	
	private void setCountsTowardsLimitFlagOnLinkedProducts(Product product) {
		if (product.isLinked()) {
			// if the linked product's primary org has the unlimited feature, it will not count
			PrimaryOrg linkedPrimary = product.getLinkedProduct().getOwner().getPrimaryOrg();
			boolean hasUnlimitedFeature = linkedPrimary.hasExtendedFeature(ExtendedFeature.UnlimitedLinkedAssets);
			product.setCountsTowardsLimit(!hasUnlimitedFeature);
		}
	}

	private void runProductSavePreRecs(Product product, User modifiedBy) throws SubProductUniquenessException {
		moveRfidFromProductSerials(product, modifiedBy);
		setCountsTowardsLimitFlagOnLinkedProducts(product);
		processSubProducts(product, modifiedBy);
	}

	public Product update(Product product, User modifiedBy) throws SubProductUniquenessException {
		product.touch();
		runProductSavePreRecs(product, modifiedBy);
		
		/*
		 * TODO: The saving of sub products should NOT be here!!!  The list of sub products is marked as @Transient,
		 * meaning that we do not want it persisted with the Product, the following logic essentially overrides this.
		 */
		saveSubProducts(product);
		
		ProductSaver saver = new ProductSaver();
		saver.setModifiedBy(modifiedBy);
		product = saver.update(em, product);
		
		updateSchedulesOwnership(product);
		return product;
	}

	private void updateSchedulesOwnership(Product product) {
		try {
			QueryBuilder<Long> scheduleIds = new QueryBuilder<Long>(InspectionSchedule.class, new OpenSecurityFilter()).setSimpleSelect("id").addSimpleWhere("product", product).addWhere(Comparator.NE, "status", "status",
					ScheduleStatus.COMPLETED);
			Map<String, Boolean> selectedAttributes = new HashMap<String, Boolean>();
			selectedAttributes.put("customer", true);
			selectedAttributes.put("location", true);
			InspectionSchedule schedule = new InspectionSchedule();
			schedule.setProduct(product);
			massUpdateManager.updateInspectionSchedules(ListHelper.toSet(persistenceManager.findAll(scheduleIds)), schedule, selectedAttributes);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private void processSubProducts(Product product, User modifiedBy) throws SubProductUniquenessException {

		checkForUniqueSubProducts(product);
		clearOldSubProducts(product);
		
		long weight = 0;
		for (SubProduct subProduct : product.getSubProducts()) {

			detachFromPreviousParent(product, subProduct, modifiedBy);

			subProduct.getProduct().setOwner(product.getOwner());
			subProduct.getProduct().setLocation(product.getLocation());
			subProduct.setWeight(weight);

			ProductSaver saver = new ProductSaver();
			saver.setModifiedBy(modifiedBy);
			saver.update(em, subProduct.getProduct());
			
			weight++;
		}
	}

	private void clearOldSubProducts(Product product) {
		if (!product.isNew()) {
			List<SubProduct> existingSubProducts = persistenceManager.findAll(new QueryBuilder<SubProduct>(SubProduct.class, new OpenSecurityFilter()).addSimpleWhere("masterProduct", product));
			for (SubProduct subProduct : existingSubProducts) {
				if (product.getSubProducts().contains(subProduct)) {
					SubProduct subProductToUpdate = product.getSubProducts().get(product.getSubProducts().indexOf(subProduct));
					subProductToUpdate.setCreated(subProduct.getCreated());
					subProductToUpdate.setId(subProduct.getId());
				} else {
					persistenceManager.delete(subProduct);
				}
			}
		}
	}

	private void detachFromPreviousParent(Product product, SubProduct subProduct, User modifiedBy) {
		Product parentProduct = productManager.parentProduct(subProduct.getProduct());

		if (parentProduct != null && !parentProduct.equals(product)) {
			try {
				QueryBuilder<SubProduct> query = new QueryBuilder<SubProduct>(SubProduct.class, new OpenSecurityFilter()).addSimpleWhere("product", subProduct.getProduct());
				SubProduct subProductToRemove = persistenceManager.find(query);
				parentProduct.getSubProducts().remove(subProductToRemove);
				persistenceManager.delete(subProductToRemove);
				update(parentProduct, modifiedBy);
			} catch (SubProductUniquenessException e) {
				logger.error("parnet product is in an invalid state in the database", e);
				throw new RuntimeException("parnet product is in an invalid state in the database", e);
			}
		}
	}

	

	public void checkForUniqueSubProducts(Product product) throws SubProductUniquenessException {
		Set<SubProduct> uniqueSubProducts = new HashSet<SubProduct>(product.getSubProducts());
		if (product.getSubProducts().size() != uniqueSubProducts.size()) {
			throw new SubProductUniquenessException();
		}
	}

	/**
	 * creates the product serial and updates the given users add
	 * productHistory.
	 */
	public Product createWithHistory(Product product, User modifiedBy) throws SubProductUniquenessException {
		product = create(product, modifiedBy);

		AddProductHistoryBean addProductHistory = getAddProductHistory(modifiedBy.getId());

		if (addProductHistory == null) {
			addProductHistory = new AddProductHistoryBean();
			addProductHistory.setUser(modifiedBy);
		}

		addProductHistory.setOwner(product.getOwner());
		addProductHistory.setProductType(product.getType());
		addProductHistory.setProductStatus(product.getProductStatus());
		addProductHistory.setPurchaseOrder(product.getPurchaseOrder());
		addProductHistory.setLocation(product.getLocation());
		addProductHistory.setInfoOptions(new ArrayList<InfoOptionBean>(product.getInfoOptions()));
		addProductHistory.setAssignedUser(product.getAssignedUser());

		em.merge(addProductHistory);

		return product;
	}

	private void moveRfidFromProductSerials(Product product, User modifiedBy) {
		ProductSaver saver = new ProductSaver();
		saver.setModifiedBy(modifiedBy);
		
		if (rfidExists(product.getRfidNumber(), product.getTenant().getId())) {
			Collection<Product> duplicateRfidProducts = productManager.findProductsByRfidNumber(product.getRfidNumber(), new TenantOnlySecurityFilter(product.getTenant().getId()));
			for (Product duplicateRfidProduct : duplicateRfidProducts) {
				if (!duplicateRfidProduct.getId().equals(product.getId())) {
					duplicateRfidProduct.setRfidNumber(null);
					
					saver.update(em, duplicateRfidProduct);

					String auditMessage = "Moving RFID [" + product.getRfidNumber() + "] from ProductSerial [" + duplicateRfidProduct.getId() + ":" + duplicateRfidProduct.getSerialNumber() + "] to [" + product.getId() + ":"
							+ product.getSerialNumber() + "]";
					auditLogger.info(auditMessage);
				}
			}
		}
	}

	public void removeProductStatus(ProductStatusBean obj) {
		ProductStatusBean target = (ProductStatusBean) em.find(ProductStatusBean.class, obj.getUniqueID());
		em.remove(target);
	}

	@SuppressWarnings("unchecked")
	public AddProductHistoryBean getAddProductHistory(Long rFieldidUser) {
		Query query = em.createQuery("from AddProductHistoryBean aph where aph.user.id = :rFieldidUser");
		query.setParameter("rFieldidUser", rFieldidUser);

		List<AddProductHistoryBean> addProductHistoryList = (List<AddProductHistoryBean>) query.getResultList();

		if (addProductHistoryList != null) {
			if (addProductHistoryList.size() > 0) {
				return addProductHistoryList.get(0);
			}
		}

		return null;
	}

	@SuppressWarnings("unchecked")
	public Collection<ProductSerialExtensionBean> getProductSerialExtensions(Long tenantId) {
		Query query = em.createQuery("from ProductSerialExtensionBean pse where pse.tenantId = :tenantId");
		query.setParameter("tenantId", tenantId);

		return (Collection<ProductSerialExtensionBean>) query.getResultList();
	}

	

	public boolean duplicateSerialNumber(String serialNumber, Long uniqueID, Tenant tenant) {
		String queryString = "select count(p.id) from Product p where p.tenant = :tenant " + " and lower(p.serialNumber) = :serialNumber";
		
		if (uniqueID != null) {
			queryString += " AND p.id != :uniqueId ";
		}

		Query query = em.createQuery(queryString).setParameter("tenant", tenant).setParameter("serialNumber", serialNumber.trim().toLowerCase());

		if (uniqueID != null) {
			query.setParameter("uniqueId", uniqueID);
		}

		Long value = (Long) query.getSingleResult();

		return value != 0L;
	}

	public Inspection findLastInspections(Product product, SecurityFilter securityFilter) {
		Query inspectionQuery = createAllInspectionQuery(product, securityFilter, false, true);
		Inspection inspection = null;
		try {
			inspection = (Inspection) inspectionQuery.getSingleResult();
		} catch (NoResultException e) {
		}
		return inspection;
	}

	public Long countAllInspections(Product product, SecurityFilter securityFilter) {
		Long count = countAllLocalInspections(product, securityFilter);
		return count;
	}
	
	public Long countAllLocalInspections(Product product, SecurityFilter securityFilter) {
		Query inspectionQuery = createAllInspectionQuery(product, securityFilter, true);
		return (Long)inspectionQuery.getSingleResult();
		
	}

	

	private Query createAllInspectionQuery(Product product, SecurityFilter securityFilter, boolean count) {
		return createAllInspectionQuery(product, securityFilter, count, false);
	}

	private Query createAllInspectionQuery(Product product, SecurityFilter securityFilter, boolean count, boolean lastInspection) {
		String query = "from Inspection inspection  left join inspection.product " + "WHERE  " + securityFilter.produceWhereClause(Inspection.class, "inspection")
				+ " AND inspection.product = :product AND inspection.state= :activeState";
		if (count) {
			query = "SELECT count(inspection.id) " + query;
		} else {
			query = "SELECT inspection " + query;
		}

		if (!count)
			query += " ORDER BY inspection.date DESC, inspection.created ASC";

		Query inspectionQuery = em.createQuery(query);

		if (lastInspection) {
			inspectionQuery.setMaxResults(1);
		}

		inspectionQuery.setParameter("product", product);
		securityFilter.applyParameters(inspectionQuery, Inspection.class);
		inspectionQuery.setParameter("activeState", EntityState.ACTIVE);

		return inspectionQuery;
	}

	public Product createProductWithServiceTransaction(String transactionGUID, Product product, User modifiedBy) throws TransactionAlreadyProcessedException, SubProductUniquenessException {

		product = create(product, modifiedBy);

		TransactionSupervisor transaction = new TransactionSupervisor(persistenceManager);
		transaction.completeProductTransaction(transactionGUID, product.getTenant());

		return product;
	}
}
