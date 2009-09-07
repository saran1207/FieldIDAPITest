package rfid.ejb.session;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.TransformerUtils;
import org.apache.log4j.Logger;

import rfid.ejb.entity.AddProductHistoryBean;
import rfid.ejb.entity.InfoOptionBean;
import rfid.ejb.entity.ProductSerialExtensionBean;
import rfid.ejb.entity.ProductSerialExtensionValueBean;
import rfid.ejb.entity.ProductStatusBean;
import rfid.ejb.entity.UserBean;

import com.n4systems.ejb.InspectionScheduleManager;
import com.n4systems.ejb.MassUpdateManager;
import com.n4systems.ejb.PersistenceManager;
import com.n4systems.ejb.ProductManager;
import com.n4systems.ejb.SafetyNetworkManager;
import com.n4systems.ejb.interceptor.TimingInterceptor;
import com.n4systems.exceptions.SubProductUniquenessException;
import com.n4systems.exceptions.TransactionAlreadyProcessedException;
import com.n4systems.model.Inspection;
import com.n4systems.model.InspectionSchedule;
import com.n4systems.model.Product;
import com.n4systems.model.SubProduct;
import com.n4systems.model.Tenant;
import com.n4systems.model.InspectionSchedule.ScheduleStatus;
import com.n4systems.model.api.Archivable.EntityState;
import com.n4systems.model.security.OpenSecurityFilter;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.model.security.TenantOnlySecurityFilter;
import com.n4systems.model.utils.FindSubProducts;
import com.n4systems.util.TransactionSupervisor;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereParameter.Comparator;
import com.n4systems.util.reflection.Reflector;

@Interceptors( { TimingInterceptor.class })
@Stateless
public class LegacyProductSerialManager implements LegacyProductSerial {
	private static final Logger logger = Logger.getLogger(LegacyProductSerialManager.class);

	@PersistenceContext(unitName = "rfidEM")
	protected EntityManager em;

	@EJB
	private PersistenceManager persistenceManager;

	@EJB
	private SafetyNetworkManager safetyNetworkManager;

	@EJB
	private InspectionScheduleManager inspectionScheduleManager;

	@EJB
	private ProductManager productManager;

	@EJB
	private MassUpdateManager massUpdateManager;

	private Logger auditLogger = Logger.getLogger("AuditLog");

	public ProductStatusBean findProductStatus(Long uniqueID) {
		ProductStatusBean obj = em.find(ProductStatusBean.class, uniqueID);
		return obj;
	}

	/**
	 * Sets the product status on the given product serial
	 * 
	 * @param productSerialId
	 * @param productStatusId
	 */
	public void updateProductStatus(Long productSerialId, Long productStatusId) {
		Product productSerial = productManager.findProduct(productSerialId);

		if (productStatusId == null) {
			productSerial.setProductStatus(null);
		} else {
			productSerial.setProductStatus(findProductStatus(productStatusId));
		}
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

	public ProductStatusBean FindProductStatusByName(Long tenantId, String name) {
		Query query = em.createQuery("from ProductStatusBean ps where UPPER(ps.name) = :name and ps.tenant.id = :tenantId");
		query.setParameter("tenantId", tenantId);
		query.setParameter("name", name.toUpperCase());

		try {
			return (ProductStatusBean) query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	/**
	 * Find a product serial that belongs to the list of end users OR a list of
	 * divisions. We use the primitive long type as opposed to the object type
	 * because it is supported in web services
	 */
	@SuppressWarnings("unchecked")
	public List<Product> findProductSerialByEndUserDivision(Long tenantId, Long[] customerList, Long[] divisionList, Date beginDate, int max, Long lastId) {

		List<Product> piList = null;

		// only bother going forward if there are at least some customers or
		// some divisions specified
		if (customerList.length > 0 || divisionList.length > 0) {
			// THIS NEEDS TO PULL IN THE INFOOPTIONS
			String queryString = "select DISTINCT( ps ) from Product ps where ps.tenant.id = :tenantId AND state = :activeState AND (";

			if (customerList.length > 0) {
				queryString += "ps.owner.id in (:customerList) ";

				if (divisionList.length > 0) {
					queryString += "OR ";
				}
			}

			if (divisionList.length > 0) {
				queryString += "ps.owner.division_id in (:divisionList)";
			}

			queryString += ") AND ps.modified >= :beginDate " + " AND ps.id > :beginId ORDER BY ps.id ASC";

			Query query = em.createQuery(queryString);

			if (customerList.length > 0) {
				query.setParameter("customerList", Arrays.asList(customerList));
			}

			if (divisionList.length > 0) {
				query.setParameter("divisionList", Arrays.asList(divisionList));
			}

			query.setParameter("tenantId", tenantId);
			query.setParameter("beginId", lastId);
			query.setParameter("beginDate", beginDate);
			query.setParameter("activeState", EntityState.ACTIVE);
			query.setMaxResults(max + 1);

			piList = (List<Product>) query.getResultList();

			try {
				for (Product productSerial : piList) {
					Set<InfoOptionBean> infoOptions = (Set<InfoOptionBean>) Reflector.getPathValue(productSerial, "infoOptions");
					for (InfoOptionBean infoOption : infoOptions) {
						infoOption.getUniqueID();
						break;
					}

				}

			} catch (Exception e) {
				logger.warn("Failed during post load", e);
			}
		}

		return piList;
	}

	@Deprecated
	public void updateRFID(Long rProductSerial, String rfidNumber) {
		Product obj = em.find(Product.class, rProductSerial);

		obj.setRfidNumber(rfidNumber);

		moveRfidFromProductSerials(obj);

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

	public void create(List<Product> products) throws SubProductUniquenessException {
		for (Product product: products) {
			create(product);
		}
	}
	
	public Product create(Product product) throws SubProductUniquenessException {
		linkProductOverTheSafetyNetwork(product);

		runProductSavePreRecs(product);
		Product savedProduct = persistenceManager.update(product, product.getIdentifiedBy());
		saveSubProducts(product);
		// XXX not sure if this should be here.
		inspectionScheduleManager.autoSchedule(savedProduct);
		return new FindSubProducts(persistenceManager, savedProduct).fillInSubProducts();
		
	}

	private void saveSubProducts(Product product) {
		for (SubProduct subProduct : product.getSubProducts()) {
			persistenceManager.update(subProduct);
		}
		
	}
	
	private void linkProductOverTheSafetyNetwork(Product product) {
		String linkedId = safetyNetworkManager.findProductLink(product.getRfidNumber(), product.getOwner());
		if (linkedId != null) {
			product.setLinkedUuid(linkedId);
		}
	}

	private void runProductSavePreRecs(Product product) throws SubProductUniquenessException {
		moveRfidFromProductSerials(product);
		processSubProducts(product);
	}

	public Product update(Product product) throws SubProductUniquenessException {
		product.touch();
		runProductSavePreRecs(product);
		
		/*
		 * TODO: The saving of sub products should NOT be here!!!  The list of sub products is marked as @Transient,
		 * meaning that we do not want it persisted with the Product, the following logic essentially overrides this.
		 */
		saveSubProducts(product);
		product = persistenceManager.update(product);
		
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
			massUpdateManager.updateInspectionSchedules(persistenceManager.findAll(scheduleIds), schedule, selectedAttributes);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private void processSubProducts(Product product) throws SubProductUniquenessException {

		checkForUniqueSubProducts(product);
		clearOldSubProducts(product);
		
		long weight = 0;
		for (SubProduct subProduct : product.getSubProducts()) {

			detachFromPreviousParent(product, subProduct);

			subProduct.getProduct().setOwner(product.getOwner());
			subProduct.getProduct().setLocation(product.getLocation());
			subProduct.setWeight(weight);
			em.merge(subProduct.getProduct());
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

	private void detachFromPreviousParent(Product product, SubProduct subProduct) {
		Product parentProduct = productManager.parentProduct(subProduct.getProduct());

		if (parentProduct != null && !parentProduct.equals(product)) {
			try {
				QueryBuilder<SubProduct> query = new QueryBuilder<SubProduct>(SubProduct.class, new OpenSecurityFilter()).addSimpleWhere("product", subProduct.getProduct());
				SubProduct subProductToRemove = persistenceManager.find(query);
				parentProduct.getSubProducts().remove(subProductToRemove);
				persistenceManager.delete(subProductToRemove);
				update(parentProduct);
			} catch (SubProductUniquenessException e) {
				logger.error("parnet product is in an invalid state in the database", e);
				throw new RuntimeException("parnet product is in an invalid state in the database", e);
			}
		}
	}

	

	@SuppressWarnings("unchecked")
	public void checkForUniqueSubProducts(Product product) throws SubProductUniquenessException {
		Collection<Product> products = (Collection<Product>) CollectionUtils.collect(product.getSubProducts(), TransformerUtils.invokerTransformer("getProduct"));

		Set<Product> uniqueProducts = new HashSet<Product>(products);
		if (products.size() != uniqueProducts.size()) {
			throw new SubProductUniquenessException();
		}
	}

	/**
	 * creates the product serial and updates the given users add
	 * productHistory.
	 */
	public Product createWithHistory(Product product, Long userId) throws SubProductUniquenessException {
		product = create(product);

		AddProductHistoryBean addProductHistory = getAddProductHistory(userId);

		if (addProductHistory == null) {
			addProductHistory = new AddProductHistoryBean();
			addProductHistory.setUser(em.find(UserBean.class, userId));
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

	private void moveRfidFromProductSerials(Product obj) {
		if (rfidExists(obj.getRfidNumber(), obj.getTenant().getId())) {
			Collection<Product> pSerials = productManager.findProductsByRfidNumber(obj.getRfidNumber(), new TenantOnlySecurityFilter(obj.getTenant().getId()));
			for (Product pSerial : pSerials) {
				if (!pSerial.getId().equals(obj.getId())) {
					pSerial.setRfidNumber(null);
					em.merge(pSerial);

					String auditMessage = "Moving RFID [" + obj.getRfidNumber() + "] from ProductSerial [" + pSerial.getId() + ":" + pSerial.getSerialNumber() + "] to [" + obj.getId() + ":"
							+ obj.getSerialNumber() + "]";
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
		Query query = em.createQuery("from AddProductHistoryBean aph where aph.user.uniqueID = :rFieldidUser");
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

	public void update(ProductSerialExtensionValueBean productSerialExtensionValue) {
		em.merge(productSerialExtensionValue);
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
		Inspection linkedInspection = safetyNetworkManager.findLatestLinkedProductInspection(product);

		if (linkedInspection != null && inspection != null) {
			if (linkedInspection.getDate().after(inspection.getDate())) {
				return linkedInspection;
			} else {
				return inspection;
			}
		} else if (linkedInspection == null) {
			return inspection;
		} else {
			return linkedInspection;
		}
	}

	public Long countAllInspections(Product product, SecurityFilter securityFilter) {
		Long count = countAllLocalInspections(product, securityFilter);
		count += safetyNetworkManager.findCountLinkedProductInspection(product);
		return count;
	}
	
	public Long countAllLocalInspections(Product product, SecurityFilter securityFilter) {
		Query inspectionQuery = createAllInspectionQuery(product, securityFilter, true);
		return (Long)inspectionQuery.getSingleResult();
		
	}

	@SuppressWarnings("unchecked")
	public List<Inspection> findAllInspections(Product product, SecurityFilter securityFilter) {

		Query inspectionQuery = createAllInspectionQuery(product, securityFilter, false);

		List<Inspection> inspection = null;

		try {
			inspection = (List<Inspection>) inspectionQuery.getResultList();
		} catch (NoResultException e) {
			inspection = new ArrayList<Inspection>();
			logger.error("could not load inspections from local", e);
		}
		try {
			inspection.addAll(safetyNetworkManager.findLinkedProductInspections(product));
		} catch (Exception e) {
			logger.error("could not load inspections from safety network", e);
		}
		return inspection;
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

	public Product createProductWithServiceTransaction(String transactionGUID, Product product) throws TransactionAlreadyProcessedException, SubProductUniquenessException {

		product = create(product);

		TransactionSupervisor transaction = new TransactionSupervisor(persistenceManager);
		transaction.completeProductTransaction(transactionGUID, product.getTenant());

		return product;
	}
}
