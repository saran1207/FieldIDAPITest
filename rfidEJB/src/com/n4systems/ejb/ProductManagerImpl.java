package com.n4systems.ejb;

import java.util.Date;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import rfid.ejb.entity.InfoFieldBean;
import rfid.ejb.entity.ProductCodeMappingBean;
import rfid.ejb.entity.UserBean;

import com.n4systems.exceptions.InvalidQueryException;
import com.n4systems.exceptions.NonUniqueProductException;
import com.n4systems.exceptions.UsedOnMasterInspectionException;
import com.n4systems.model.Inspection;
import com.n4systems.model.InspectionSchedule;
import com.n4systems.model.Product;
import com.n4systems.model.ProductType;
import com.n4systems.model.ProductTypeGroup;
import com.n4systems.model.Project;
import com.n4systems.model.SubProduct;
import com.n4systems.model.api.Archivable.EntityState;
import com.n4systems.model.product.ProductSaver;
import com.n4systems.model.security.OpenSecurityFilter;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.model.security.TenantOnlySecurityFilter;
import com.n4systems.model.utils.FindSubProducts;
import com.n4systems.services.product.ProductMerger;
import com.n4systems.taskscheduling.TaskExecutor;
import com.n4systems.taskscheduling.task.ArchiveProductTypeTask;
import com.n4systems.util.GUIDHelper;
import com.n4systems.util.ListingPair;
import com.n4systems.util.ProductRemovalSummary;
import com.n4systems.util.ProductTypeGroupRemovalSummary;
import com.n4systems.util.ProductTypeRemovalSummary;
import com.n4systems.util.ServiceLocator;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereParameter;
import com.n4systems.util.persistence.WhereParameter.Comparator;

@Stateless
public class ProductManagerImpl implements ProductManager {

	private static Logger logger = Logger.getLogger(ProductManagerImpl.class);

	private static final String unitName = "rfidEM";

	@PersistenceContext(unitName = unitName)
	private EntityManager em;

	@EJB
	private PersistenceManager persistenceManager;

	@EJB
	private ProjectManager projectManager;

	public List<Product> findProductByIdentifiers(SecurityFilter filter, String searchValue) {
		return findProductByIdentifiers(filter, searchValue, null);
	}

	/**
	 * this will locate a set of product serial that have the exact serial
	 * number of rfid number given in the search value variable. this will
	 * filter on tenant, owner and division.
	 */
	@SuppressWarnings("unchecked")
	public List<Product> findProductByIdentifiers(SecurityFilter filter, String searchValue, ProductType productType) {
		String queryString = "FROM Product p WHERE ( UPPER( p.serialNumber ) = :searchValue OR UPPER( p.rfidNumber ) = :searchValue OR UPPER(p.customerRefNumber) = :searchValue ) AND " + filter.produceWhereClause(Product.class, "p");

		if (productType != null) {
			queryString += " AND p.type = :productType ";
		}

		queryString += " ORDER BY p.created ";

		Query query = em.createQuery(queryString);
		filter.applyParameters(query, Product.class);
		query.setParameter("searchValue", searchValue.toUpperCase());
		if (productType != null) {
			query.setParameter("productType", productType);
		}
		return query.getResultList();

	}

	public Product findProduct(Long id) {
		QueryBuilder<Product> qBuilder = new QueryBuilder<Product>(Product.class, new OpenSecurityFilter());

		qBuilder.setSimpleSelect();
		qBuilder.addSimpleWhere("id", id);
		qBuilder.addSimpleWhere("state", EntityState.ACTIVE);

		try {
			return persistenceManager.find(qBuilder);
		} catch (InvalidQueryException e) {
			logger.error("Unable to load Product", e);
			return null;
		}
	}

	public Product findProductAllFields(Long id, SecurityFilter filter) {
		Product product =  findProduct(id, filter, "infoOptions", "type.inspectionTypes", "type.attachments", "type.subTypes", "projects");
		product = fillInSubProductsOnProduct(product);
		
		// load linked products all the way up the chain
		Product linkedProduct = product.getLinkedProduct(); 
		while (linkedProduct != null) {
			linkedProduct = linkedProduct.getLinkedProduct();
		}
		
		return product;
	}

	public Product findProduct(Long id, SecurityFilter filter) {
		return findProduct(id, filter, (String[]) null);
	}

	public Product findProduct(Long id, SecurityFilter filter, String... postFetchFields) {

		QueryBuilder<Product> qBuilder = basicProductQuery(filter);
		qBuilder.addSimpleWhere("id", id);

		Product product = null;
		try {
			product = qBuilder.getSingleResult(em);

			if (postFetchFields != null && postFetchFields.length > 0) {
				persistenceManager.postFetchFields(product, postFetchFields);
			}

		} catch (NoResultException e) {
			return null;
		} catch (InvalidQueryException e) {
			logger.error("Unable to load ProductSerial", e);
		}

		return product;
	}

	/**
	 * Returns a single product by its serial number and owner customer id
	 * Notice that the customer id passed in is not the "security filter"
	 * customer id
	 */
	public Product findProductBySerialNumber(String rawSerialNumber, Long tenantId, Long customerId) throws NonUniqueProductException {
		Product product = null;
		SecurityFilter filter = new TenantOnlySecurityFilter(tenantId);

		try {
			QueryBuilder<Product> qBuilder = basicProductQuery(filter);
			qBuilder.addWhere(Comparator.EQ, "serialNumber", "serialNumber", rawSerialNumber.trim(), WhereParameter.IGNORE_CASE);
			
			if (customerId == null) {
				qBuilder.addWhere(new WhereParameter<Long>(WhereParameter.Comparator.NULL, "owner.customerOrg"));
			} else {
				qBuilder.addSimpleWhere("owner.customerOrg.id", customerId);
			}

			
			int firstPage = 0;
			int pageSizeToFindIfThisIsANonUniqueSerialNumber = 2;
			List<Product> products = persistenceManager.findAll(qBuilder, firstPage, pageSizeToFindIfThisIsANonUniqueSerialNumber);
			switch (products.size()) {
				case 0: 
					product = null;
					break;
				case 1:
					product = products.get(0);
					break;
				default:
					throw new NonUniqueProductException("there is more than one product with the same customer and serial number [" + rawSerialNumber + "]");
			} 
		} catch (InvalidQueryException e) {
			logger.error("query was wrong", e);
		} catch (NonUniqueResultException e) {
			throw new NonUniqueProductException(e);
		} catch (Exception e) {
			throw new NonUniqueProductException(e);
		}

		return product;
	}

	// TODO resolve this so it can only ever find 1 value for the mobile guid.
	public Product findProductByGUID(String mobileGUID, SecurityFilter filter) {
		Product product = null;
		if (GUIDHelper.isNullGUID(mobileGUID)) {
			return null;
		}

		QueryBuilder<Product> qBuilder = basicProductQuery(filter);
		qBuilder.addSimpleWhere("mobileGUID", mobileGUID.trim()).addPostFetchPaths("subProducts");

		try {
			product = persistenceManager.find(qBuilder);
			product = fillInSubProductsOnProduct(product);
		} catch (NonUniqueResultException e) {
			logger.error("found more than one product with the GUID " + mobileGUID, e);
		} catch (InvalidQueryException e) {
			logger.error("query is incorrect", e);
		}

		return product;
	}

	public List<Product> findProductsByRfidNumber(String rfidNumber, SecurityFilter filter, String... postFetchFields) {
		if (rfidNumber == null) {
			return null;
		}

		QueryBuilder<Product> qBuilder = basicProductQuery(filter);
		qBuilder.addSimpleWhere("rfidNumber", rfidNumber.trim());

		try {
			List<Product> products = persistenceManager.findAll(qBuilder);

			return persistenceManager.postFetchFields(products, postFetchFields);
		} catch (InvalidQueryException e) {
			logger.error("query incorrect", e);
		}
		return null;
	}

	/**
	 * ensures the filter is set up correctly and the state of the product is
	 * active.
	 * 
	 * @param filter
	 * @return
	 */
	private QueryBuilder<Product> basicProductQuery(SecurityFilter filter) {
		QueryBuilder<Product> qBuilder = new QueryBuilder<Product>(Product.class, filter);

		qBuilder.setSimpleSelect();
		qBuilder.addSimpleWhere("state", EntityState.ACTIVE);
		return qBuilder;
	}

	/**
	 * returns the Parent Product of the given product or null if there is no
	 * parent product.
	 */
	public Product parentProduct(Product product) {
		QueryBuilder<SubProduct> query = new QueryBuilder<SubProduct>(SubProduct.class, new OpenSecurityFilter()).addSimpleWhere("product", product);
		try {
			SubProduct p = (SubProduct)persistenceManager.find(query);
			if (p != null) {
				Product master = p.getMasterProduct();
				return fillInSubProductsOnProduct(master);
			}
			return null;
		} catch (NoResultException e) {
			return null;
		} catch (Exception e) {
			logger.error("Could not check if sub product", e);
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public List<ListingPair> getAllowedSubTypes(SecurityFilter filter, ProductType type) {
		String jpql = "SELECT new com.n4systems.util.ListingPair(id, name ) FROM ProductType pt";
		jpql += " WHERE " + filter.produceWhereClause(ProductType.class, "pt") + " AND pt.subTypes IS EMPTY AND pt.id != :productTypeId AND state = :activeState ORDER BY pt.name";

		Query query = em.createQuery(jpql);
		filter.applyParameters(query, ProductType.class);
		query.setParameter("productTypeId", type.getId());
		query.setParameter("activeState", EntityState.ACTIVE);

		return (List<ListingPair>) query.getResultList();
	}

	public boolean partOfAMasterProduct(Long typeId) {
		String str = "select count(p) From ProductType p, IN( p.subTypes ) s WHERE s.id = :typeId ";
		Query query = em.createQuery(str);
		query.setParameter("typeId", typeId);

		try {

			Long count = (Long) query.getSingleResult();
			return (count > 0);
		} catch (NoResultException e) {
			return false;
		} catch (Exception e) {
			logger.error("Could not check if sub product", e);
			return false;
		}
	}

	public Product archive(Product product, UserBean archivedBy) throws UsedOnMasterInspectionException {
		product = persistenceManager.reattach(product);
		product = fillInSubProductsOnProduct(product);
		if (!testArchive(product).validToDelete()) {
			throw new UsedOnMasterInspectionException();
		}

		if (product.isMasterProduct()) {
			for (SubProduct subProduct : product.getSubProducts()) {
				persistenceManager.delete(subProduct);
			}
			product.getSubProducts().clear();
		}

		Product parentProduct = parentProduct(product);
		if (parentProduct != null) {
			SubProduct subProductToRemove = parentProduct.getSubProducts().get(parentProduct.getSubProducts().indexOf(new SubProduct(product, parentProduct)));
			persistenceManager.delete(subProductToRemove);
			parentProduct.getSubProducts().remove(subProductToRemove);
			save(parentProduct, archivedBy);
		}

		product.archiveEntity();
		product.archiveSerialNumber();

		archiveInspections(product, archivedBy);
		archiveSchedules(product, archivedBy);
		detatachFromProjects(product, archivedBy);

		return save(product, archivedBy);
	}

	private void detatachFromProjects(Product product, UserBean archivedBy) {
		for (Project project : product.getProjects()) {
			projectManager.detachAsset(product, project, archivedBy.getId());
		}
	}

	private void archiveInspections(Product product, UserBean archivedBy) {
		String updateQuery = "UPDATE " + Inspection.class.getName() + " SET state = :archiveState,  modifiedBy = :archivingUser , modified = :now "
				+ " WHERE product = :product AND state = :activeState ";

		Query update = em.createQuery(updateQuery);
		update.setParameter("archiveState", EntityState.ARCHIVED);
		update.setParameter("archivingUser", archivedBy);
		update.setParameter("now", new Date());

		update.setParameter("product", product);
		update.setParameter("activeState", EntityState.ACTIVE);

		update.executeUpdate();
	}

	private void archiveSchedules(Product product, UserBean archivedBy) {
		String updateQuery = "UPDATE " + InspectionSchedule.class.getName() + " SET state = :archiveState,  modifiedBy = :archivingUser , modified = :now "
				+ " WHERE product = :product AND state = :activeState ";

		Query update = em.createQuery(updateQuery);
		update.setParameter("archiveState", EntityState.ARCHIVED);
		update.setParameter("archivingUser", archivedBy);
		update.setParameter("now", new Date());

		update.setParameter("product", product);
		update.setParameter("activeState", EntityState.ACTIVE);

		update.executeUpdate();
		logger.info("archived schedules for product " + product);
	}

	protected Product save(Product product, UserBean modifiedBy) {
		ProductSaver productSaver = new ProductSaver();
		productSaver.setModifiedBy(modifiedBy);
		
		product = productSaver.update(em, product);
		
		return product;
	}

	public ProductType archive(ProductType productType, Long archivedBy, String deletingPrefix) {
		if (testArchive(productType).validToDelete()) {

			productType.archiveEntity();
			productType.archivedName(deletingPrefix);

			productType.getSubTypes().clear();
			ProductType type = persistenceManager.update(productType, archivedBy);
			
			ArchiveProductTypeTask archiveTask = new ArchiveProductTypeTask();
			
			archiveTask.setArchivedById(archivedBy);
			archiveTask.setProductTypeId(productType.getId());
			archiveTask.setProductTypeName(productType.getArchivedName());

			TaskExecutor.getInstance().execute(archiveTask);
			
			return type;
		} else {
			throw new RuntimeException("product type can not be validated.");
		}

	}

	public ProductTypeRemovalSummary testArchive(ProductType productType) {
		ProductTypeRemovalSummary summary = new ProductTypeRemovalSummary(productType);
		try {
			QueryBuilder<Product> productCount = new QueryBuilder<Product>(Product.class, new OpenSecurityFilter());
			productCount.setCountSelect().addSimpleWhere("type", productType).addSimpleWhere("state", EntityState.ACTIVE);
			summary.setProductsToDelete(persistenceManager.findCount(productCount));

			QueryBuilder<Inspection> inspectionCount = new QueryBuilder<Inspection>(Inspection.class, new OpenSecurityFilter());
			inspectionCount.setCountSelect().addSimpleWhere("product.type", productType).addSimpleWhere("state", EntityState.ACTIVE);
			summary.setInspectionsToDelete(persistenceManager.findCount(inspectionCount));

			QueryBuilder<InspectionSchedule> scheduleCount = new QueryBuilder<InspectionSchedule>(InspectionSchedule.class, new OpenSecurityFilter());
			scheduleCount.setCountSelect().addSimpleWhere("product.type", productType);
			summary.setSchedulesToDelete(persistenceManager.findCount(scheduleCount));

			String subInspectionQuery = "select count(i) From " + Inspection.class.getName() + " i, IN( i.subInspections ) si WHERE si.product.type = :productType AND i.state = :activeState ";
			Query subInspectionCount = em.createQuery(subInspectionQuery);
			subInspectionCount.setParameter("productType", productType).setParameter("activeState", EntityState.ACTIVE);
			summary.setProductsUsedInMasterInpsection((Long) subInspectionCount.getSingleResult());

			String subProductQuery = "select count(DISTINCT s.masterProduct) From SubProduct s WHERE s.product.type = :productType ";
			Query subProductCount = em.createQuery(subProductQuery);
			subProductCount.setParameter("productType", productType);
			summary.setSubProductsToDettach((Long) subProductCount.getSingleResult());

			String subMasterProductQuery = "select count(s) From SubProduct s WHERE s.masterProduct.type = :productType ";
			Query subMasterProductCount = em.createQuery(subMasterProductQuery);
			subMasterProductCount.setParameter("productType", productType);
			summary.setMasterProductsToDettach((Long) subMasterProductCount.getSingleResult());

			String partOfProjectQuery = "select count(p) From Project p, IN( p.products ) s WHERE s.type = :productType";
			Query partOfProjectCount = em.createQuery(partOfProjectQuery);
			partOfProjectCount.setParameter("productType", productType);
			summary.setAssetsToDettachFromProjects((Long) partOfProjectCount.getSingleResult());

			String subProductTypeQuery = "select count(p) From ProductType p, IN( p.subTypes ) s WHERE s = :productType ";
			Query subProductTypeCount = em.createQuery(subProductTypeQuery);
			subProductTypeCount.setParameter("productType", productType);
			summary.setProductTypesToDettachFrom((Long) subProductTypeCount.getSingleResult());

			QueryBuilder<ProductCodeMappingBean> productCodeMappingCount = new QueryBuilder<ProductCodeMappingBean>(ProductCodeMappingBean.class, new OpenSecurityFilter());
			productCodeMappingCount.setCountSelect().addSimpleWhere("productInfo", productType);
			summary.setProductCodeMappingsToDelete(persistenceManager.findCount(productCodeMappingCount));

		} catch (InvalidQueryException e) {
			logger.error("bad summary query", e);
			summary = null;
		}
		return summary;
	}

	@SuppressWarnings("unchecked")
	public void removeAsASubProductType(ProductType productType, Long archivedBy) {
		Query masterTypeQuery = em.createQuery("select p From ProductType p, IN( p.subTypes ) st WHERE st = :productType ");
		masterTypeQuery.setParameter("productType", productType);

		List<ProductType> masterTypes = (List<ProductType>) masterTypeQuery.getResultList();
		for (ProductType masterType : masterTypes) {
			ProductType productTypeToRemoveFromSet = null;
			for (ProductType subType : masterType.getSubTypes()) {
				if (subType.equals(productType)) {
					productTypeToRemoveFromSet = subType;
					break;
				}
			}
			if (productTypeToRemoveFromSet != null) {
				masterType.getSubTypes().remove(productTypeToRemoveFromSet);
			}
			persistenceManager.update(masterType, archivedBy);
		}
	}

	public void removeProductCodeMappingsThatUse(ProductType productType) {
		QueryBuilder<ProductCodeMappingBean> productCodeMappingQuery = new QueryBuilder<ProductCodeMappingBean>(ProductCodeMappingBean.class, new OpenSecurityFilter());
		productCodeMappingQuery.setSimpleSelect().addSimpleWhere("productInfo", productType);
		try {
			for (ProductCodeMappingBean mapping : persistenceManager.findAll(productCodeMappingQuery)) {
				em.remove(mapping);
			}
		} catch (InvalidQueryException e) {
			logger.error("bad query for product code mappings", e);
		}
	}

	public ProductRemovalSummary testArchive(Product product) {
		ProductRemovalSummary summary = new ProductRemovalSummary(product);
		try {
			QueryBuilder<Inspection> inspectionCount = new QueryBuilder<Inspection>(Inspection.class, new OpenSecurityFilter());
			inspectionCount.setCountSelect().addSimpleWhere("product", product).addSimpleWhere("state", EntityState.ACTIVE);
			summary.setInspectionsToDelete(persistenceManager.findCount(inspectionCount));

			QueryBuilder<InspectionSchedule> scheduleCount = new QueryBuilder<InspectionSchedule>(InspectionSchedule.class, new OpenSecurityFilter());
			scheduleCount.setCountSelect().addSimpleWhere("product", product);
			summary.setSchedulesToDelete(persistenceManager.findCount(scheduleCount));

			String subInspectionQuery = "select count(i) From " + Inspection.class.getName() + " i, IN( i.subInspections ) si WHERE si.product = :product AND i.state = :activeState ";
			Query subInspectionCount = em.createQuery(subInspectionQuery);
			subInspectionCount.setParameter("product", product).setParameter("activeState", EntityState.ACTIVE);
			summary.setProductUsedInMasterInpsection((Long) subInspectionCount.getSingleResult());
			product = fillInSubProductsOnProduct(product);
			summary.setSubProductsToDettach((long) product.getSubProducts().size());

			summary.setDetatachFromMaster(parentProduct(product) != null);

			String partOfProjectQuery = "select count(p) From Project p, IN( p.products ) s WHERE s = :product";
			Query partOfProjectCount = em.createQuery(partOfProjectQuery);
			partOfProjectCount.setParameter("product", product);
			summary.setProjectToDetachFrom((Long) partOfProjectCount.getSingleResult());

		} catch (InvalidQueryException e) {
			logger.error("bad summary query", e);
			summary = null;
		}
		return summary;
	}

	/*
	 * TESTING injection hooks.
	 */
	protected void setEntityManager(EntityManager em) {
		this.em = em;
	}

	protected void setPersistenceManager(PersistenceManager persistenceManager) {
		this.persistenceManager = persistenceManager;
	}

	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public SortedSet<String> findAllCommonInfoFieldNames(SecurityFilter filter) {
		// find all the product types for a tenant and compute the common info
		// fields
		return findAllCommonInfoFieldNames(persistenceManager.findAll(new QueryBuilder<ProductType>(ProductType.class, filter)));
	}

	@SuppressWarnings("unchecked")
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public SortedSet<String> findAllCommonInfoFieldNames(List<ProductType> productTypes) {
		/*
		 * This algorithm works by initializing our name set with all the
		 * InfoField names from the first product type returned. We then iterate
		 * the rest of the product types removing entries from our common name
		 * set that do not match up to an infofield on the iterated product
		 * type. This way, the resulting set will contain only entries appearing
		 * in all product types.
		 */
		Long countOfProductTypes = new Long(productTypes.size());
		String query = "SELECT TRIM(name) FROM " + InfoFieldBean.class.getName() + " WHERE productInfo IN(:productTypes) ";
		query += "GROUP BY TRIM(name) HAVING COUNT(id) = :numberOfProductTypes";

		Query commonNamesQuery = em.createQuery(query).setParameter("productTypes", productTypes).setParameter("numberOfProductTypes", countOfProductTypes);
		return new TreeSet<String>((List<String>) commonNamesQuery.getResultList());
	}

	public void deleteProductTypeGroup(ProductTypeGroup group) {
		ProductTypeGroup groupToDelete = persistenceManager.find(ProductTypeGroup.class, group.getId());

		Query query = em.createQuery("UPDATE " + ProductType.class.getName() + " productType SET productType.group = null WHERE productType.group = :group");
		query.setParameter("group", groupToDelete);
		query.executeUpdate();
		persistenceManager.delete(groupToDelete);
	}

	public ProductTypeGroupRemovalSummary testDelete(ProductTypeGroup group) {
		ProductTypeGroupRemovalSummary summary = new ProductTypeGroupRemovalSummary(group);
		QueryBuilder<ProductType> countQuery = new QueryBuilder<ProductType>(ProductType.class, new OpenSecurityFilter());
		countQuery.addSimpleWhere("group", group);
		summary.setProductTypesConnected(persistenceManager.findCount(countQuery));
		return summary;
	}

	public Product fillInSubProductsOnProduct(Product product) {
		return new FindSubProducts(persistenceManager, product).fillInSubProducts();
	}
	
	public List<SubProduct> findSubProductsForProduct(Product product) {
		return new FindSubProducts(persistenceManager, product).findSubProducts();
	}

	public Product mergeProducts(Product winningProduct, Product losingProduct, UserBean user) {
		ProductMerger merger = new ProductMerger(persistenceManager, this, ServiceLocator.getInspectionManager(), user);
		// reload the winning and losing products so they are fully under managed scope.
		Product reloadedWinner = persistenceManager.find(Product.class, winningProduct.getId());
		Product reloadedLoser = persistenceManager.find(Product.class, losingProduct.getId());
		
		return merger.merge(reloadedWinner, reloadedLoser);
	}

	
	
	
}
