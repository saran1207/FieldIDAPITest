package com.n4systems.ejb.impl;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import javax.persistence.EntityManager;

import org.apache.log4j.Logger;

import rfid.ejb.entity.InfoFieldBean;
import rfid.ejb.entity.InfoOptionBean;
import rfid.ejb.entity.PopulatorLogBean;
import rfid.ejb.entity.UserBean;

import com.n4systems.ejb.InspectionManager;
import com.n4systems.ejb.PersistenceManager;
import com.n4systems.ejb.ProductManager;
import com.n4systems.ejb.ProofTestHandler;
import com.n4systems.ejb.legacy.LegacyProductSerial;
import com.n4systems.ejb.legacy.LegacyProductType;
import com.n4systems.ejb.legacy.PopulatorLog;
import com.n4systems.ejb.legacy.User;
import com.n4systems.ejb.legacy.impl.LegacyProductSerialManager;
import com.n4systems.ejb.legacy.impl.PopulatorLogManager;
import com.n4systems.ejb.legacy.impl.UserManager;
import com.n4systems.ejb.parameters.CreateInspectionParameterBuilder;
import com.n4systems.exceptions.FileProcessingException;
import com.n4systems.exceptions.NonUniqueProductException;
import com.n4systems.exceptions.SubProductUniquenessException;
import com.n4systems.fileprocessing.ProofTestType;
import com.n4systems.model.Inspection;
import com.n4systems.model.InspectionBook;
import com.n4systems.model.InspectionType;
import com.n4systems.model.Product;
import com.n4systems.model.ProductType;
import com.n4systems.model.Tenant;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.orgs.CustomerOrg;
import com.n4systems.model.orgs.LegacyFindOrCreateCustomerOrgHandler;
import com.n4systems.model.orgs.PrimaryOrg;
import com.n4systems.reporting.PathHandler;
import com.n4systems.tools.FileDataContainer;
import com.n4systems.util.DateHelper;
import com.n4systems.util.FuzzyResolver;
import com.n4systems.util.StringUtils;

@SuppressWarnings("deprecation")

public class ProofTestHandlerImpl implements ProofTestHandler {
	private Logger logger = Logger.getLogger(ProofTestHandler.class);
	
	 private LegacyProductSerial legacyProductManager;
	 private ProductManager productManager;
	 private LegacyProductType productTypeManager;
	 private PersistenceManager persistenceManager;
	 private InspectionManager inspectionManager;
	 private User userManager;
	 private PopulatorLog populatorLogManager;

	private InspectionSaver inspectionSaver;
	
	public ProofTestHandlerImpl(EntityManager em) {
		this.legacyProductManager = new LegacyProductSerialManager(em);
		this.productManager = new ProductManagerImpl(em);
		this.legacyProductManager = new LegacyProductSerialManager(em);
		this.persistenceManager = new PersistenceManagerImpl(em);
		this.inspectionManager = new InspectionManagerImpl(em);
		this.userManager = new UserManager(em);
		this.populatorLogManager = new PopulatorLogManager(em);
		this.inspectionSaver = new ManagerBackedInspectionSaver(new LegacyProductSerialManager(em), 
				persistenceManager, em, new EntityManagerLastInspectionDateFinder(persistenceManager, em));
		
	}
	
	
	

	/*
	 *  TODO: we should look at splitting this apart as it's getting pretty fat.  I would also consider splitting the logic of PT's coming from databridge and multi-proof.  There's
	 *  probably as much logic in here separating the two methods as there is shared.
	 */
	
	/*
	 * XXX Lame alert: all of the createOrUpdateProofTest methods, return a Map<String, Inspection>.  This is a map of serial numbers processed from the file
	 * to the inspections they created or updated (or null on failure).  This sucks 'cause really only the Roberts processor has the ability
	 * to hold more then a single serial number per file and even then it's only the way in which CG uses it that makes it possible.
	 * Basically what I'm saying is that constantly having to handle a proof test with multiple serials creates a lot of complexity for
	 * something that only one customer uses and is really just a hack in the first place. =;<
	 */
	public Map<String, Inspection> multiProofTestUpload(File proofTestFile, ProofTestType type, Long tenantId, Long userId, Long ownerId, Long inspectionBookId) throws FileProcessingException {
		FileDataContainer fileData = null;
		
		// first we need to process this proof test
		try {
			fileData = type.getFileProcessorInstance().processFile(proofTestFile);
		} catch (IllegalAccessException e) {
			logger.error("Failed processing proof test at [" + proofTestFile + "]", e);
			throw new FileProcessingException("Failed to process Proof Test", e);
		} catch (InstantiationException e) {
			logger.error("Failed processing proof test at [" + proofTestFile + "]", e);
			throw new FileProcessingException("Failed to process Proof Test", e);
		} 
		// now lets look up our beans
		UserBean user = userManager.findUser(userId, tenantId);
		BaseOrg customer = persistenceManager.find(BaseOrg.class, ownerId, tenantId);
		InspectionBook book = persistenceManager.find(InspectionBook.class, inspectionBookId, tenantId);
		
		return createOrUpdateProofTest(fileData, user, customer, book, false);
	}
	
	public Map<String, Inspection> inspectionServiceUpload(FileDataContainer fileData, UserBean inspector) throws FileProcessingException  {
		return createOrUpdateProofTest(fileData, inspector, null, null, true);
	}
	
	/*
	 * @returns		A map of Serial Numbers to Inspections.  A null inspection means that processing failed for that Serial Number
	 */
	private Map<String, Inspection> createOrUpdateProofTest(FileDataContainer fileData, UserBean inspector, BaseOrg customer, InspectionBook book, boolean productOverridesInspector) throws FileProcessingException {
		Map<String, Inspection> inspectionMap = new HashMap<String, Inspection>();
		
		logger.info("Started processing of file [" + fileData.getFileName() + "]");
		
		Tenant tenant = inspector.getTenant();
		PrimaryOrg primaryOrg = inspector.getOwner().getPrimaryOrg();
		
		
		
		
		// sending a null customer will lookup products with no customer (rather then products for any customer)
		Long customerId = (customer != null) ? customer.getId() : null;
		
		//if our customer is null, then let's see if we're supposed to resolve a customer by name from the FileDataContainer
		if(customerId == null && fileData.isResolveCustomer()) {
			// lets resolve a customer from the file data container.  This will also create a customer if resolution fails and createCustomer is set
			
			customer = findOrCreateCustomer(primaryOrg, inspector, fileData.getCustomerName(), fileData.isCreateCustomer());
						
			// If the customer is still null then, we were unalbe to find or create a customer.  Let's assume we we're supposed to use no customer
			if(customer == null) {
				logger.warn("Unable to find or create customer.  Assuming no customer.");
				customerId = null;
			} else {
				customerId = customer.getId();
			}

		} 
		Date inspectionDate = fileData.getInspectionDate();

		Product product;
		List<Inspection> inspections;
		Inspection inspection;
		// since proof tests may have multiple serial numbers, we'll need to do this process for each
		for (String serialNumber : fileData.getSerialNumbers()) {
			inspection = null;
			try {
				// find a product for this tenant, serial and customer
				product = findOrCreateProduct(primaryOrg, inspector, serialNumber, customer, fileData);
			} catch (NonUniqueProductException e) {
				writeLogMessage(tenant, "There are multiple Product with serial number[" + serialNumber + "] in file [" + fileData.getFileName() + "]", false, null);
				inspectionMap.put(serialNumber, null);
				continue;
			}
			
			// if the product is null, log it and move on to the next serial
			if (product == null) {
				writeLogMessage(tenant, "Could not find/create Product [" + serialNumber + "] referenced in file [" + fileData.getFileName() + "]", false, null);
				inspectionMap.put(serialNumber, null);
				continue;
			}
			
			/*
			 *  If the product identifiedBy is set to override the inspector (databridge upload uses this)
			 *  and the two are different, then set the inspector to be the identifiedBy from the product
			 */
			if (productOverridesInspector) {
				inspector = product.getIdentifiedBy();
			}
			
			// convert date to utc using the inspectors time.
			Date inspectionDateInUTC = DateHelper.convertToUTC(inspectionDate, inspector.getTimeZone());
			Date inspectionDateRangeStartInUTC = DateHelper.convertToUTC(DateHelper.getBeginingOfDay(inspectionDate), inspector.getTimeZone());
			Date inspectionDateRangeEndInUTC = DateHelper.convertToUTC(DateHelper.getEndOfDay(inspectionDate), inspector.getTimeZone());

			// if we find a product then it's time to try and find an inspection inside the same day as given.
			inspections = inspectionManager.findInspectionsByDateAndProduct(inspectionDateRangeStartInUTC, inspectionDateRangeEndInUTC, product, inspector.getSecurityFilter());
			
			// now we need to find the inspection, supporting out ProofTestType, and does not already have a chart
			for (Inspection insp: inspections) {
				if(insp.getType().supports(fileData.getFileType()) && !chartImageExists(insp)) {
					// we have found our inspection, move on
					inspection = insp;
					break;
				}
			}
			
			// if we were unable to locate an inspection, then we'll need to create a new one
			if (inspection == null) {
				inspection = createInspection(tenant, inspector, customer, product, book, inspectionDateInUTC, fileData);
			} else {
				try {
					// we have a valid inspection, now we can update it
					inspectionManager.updateInspection(inspection, inspector.getUniqueID(), fileData, null);
					writeLogMessage(tenant, "Updated Inspection for Product with serial [" + serialNumber + "] and Inspection date [" + inspection.getDate() + "]");
				} catch(Exception e) {
					// we don't want a failure in one inspection to cause the others to fail, so we will simply log these expections and move on
					writeLogMessage(tenant, "Failed to update Inspection for Product with serial [" + serialNumber + "] and Inspection date [" + inspection.getDate() + "]", false, e);
					inspectionMap.put(serialNumber, null);
					continue;
				}
			}

			// update our map with the serial and inspection
			inspectionMap.put(serialNumber, inspection);
		}
		
		logger.info("Completed processing of file [" + fileData.getFileName() + "]");
		
		return inspectionMap;
	}
	
	private CustomerOrg findOrCreateCustomer(PrimaryOrg primaryOrg, UserBean user, String customerName, boolean createCustomer) {
		// if the customer name is null or empty, we'll assume it's for no customer
		if (StringUtils.isEmpty(customerName)) {
			return null;
		}
		
		LegacyFindOrCreateCustomerOrgHandler findOrCreateCust = getFindOrCreateCustomerHandler();
	
		
		
		CustomerOrg customer = null;
		String cleanedCustomerName = cleanCustomerName(customerName);
		
		if (createCustomer) {
			customer = findOrCreateCust.findOrCreate(primaryOrg, cleanedCustomerName); 
		} else {
			customer = findOrCreateCust.find(primaryOrg, cleanedCustomerName);
		}
		
			
		if (customer != null) {
			// if we've found or created a customer, log about it
			
			if (findOrCreateCust.orgWasCreated()) {
				writeLogMessage(primaryOrg.getTenant(), "Created Customer [" + customer.getId() +  "] for Name [" + customer.getName() + "] on CustId [" + customer.getCode() + "]");
			} else {
				logger.debug("Found Customer [" + customer.getId() +  "] for Name [" + customer.getName() + "] on CustId [" + customer.getCode() + "]");
			}
		}
			
		return customer;
	}

	private String cleanCustomerName(String customerName) {
		return customerName.trim();
	}
	
	private Product findOrCreateProduct(PrimaryOrg primaryOrg, UserBean user, String serial, BaseOrg customer, FileDataContainer fileData) throws NonUniqueProductException {
		Long customerId = (customer != null) ?  customer.getId() : null;
		
		// we must have a valid serial number
		if(serial == null || serial.length() == 0) {
			return null;
		}
		
		// find a product for this tenant, serial and customer
		Product product = productManager.findProductBySerialNumber(serial, primaryOrg.getTenant().getId(), customerId);
		
		if(product == null) {
			if(!fileData.isCreateProduct()) {
				// if we're not supposted to create new products and we could not find one, return null
				return null;
			} else {
				// create product is set, lets create a default
				product = createProduct(primaryOrg, user, customer, serial, fileData.getExtraInfo());
			}
		}
		
		return product;
	}

	private Product createProduct(PrimaryOrg primaryOrg, UserBean user, BaseOrg owner, String serialNumber, Map<String, String> productOptions) {
		Product product = new Product();
		
		product.setTenant(primaryOrg.getTenant());
		product.setSerialNumber(serialNumber);
		
		ProductType productType = productTypeManager.findDefaultProductType(primaryOrg.getTenant().getId());
		product.setType(productType);
		
		product.setIdentifiedBy(user);
		product.setModifiedBy(user);
		
		if (owner != null) {
			product.setOwner(owner);
		} else {
			// if the owner was null, it goes against the primary
			product.setOwner(primaryOrg);
		}
		
		Date now = new Date();
		product.setIdentified(now);
		product.setCreated(now);
		product.setModified(now);
		
		try {
			// now lets's try and resolve the infofield names from our productoptions
			String infoFieldName, infoOptionName;
			InfoFieldBean infoField;
			InfoOptionBean infoOption;
			for(Map.Entry<String, String> optEntry: productOptions.entrySet()) {
				infoFieldName = optEntry.getKey();
				infoOptionName = optEntry.getValue();
	
				// we'll use the fuzzy resolver to try and find the info field we're looking for
				infoField = FuzzyResolver.resolve(infoFieldName, product.getType().getInfoFields(), "name", false);
				
				if(infoField == null) {
					// if we didn't find one, move on
					continue;
				}
				
				// we currently do not support resolving static info options here so we need to check if dynamic options are supported
				if(!infoField.acceptsDyanmicInfoOption()) {
					// we'll just have to log this and move on to the next option
					logger.warn("Resolved InfoField [" + infoField.getName() + " (" + infoField.getUniqueID() + ")] but it did not support dynamic options");
					continue;
				}
				
				// if we're still here it means the InfoField is valid and we should be good to create our InfoOption
				infoOption = new InfoOptionBean();
				infoOption.setInfoField(infoField);
				infoOption.setStaticData(false);
				infoOption.setWeight(product.getNextInfoOptionWeight());
				infoOption.setName(infoOptionName);
				
				// make sure the product has an infoOption set ready to go
				if(product.getInfoOptions() == null) {
					product.setInfoOptions(new TreeSet<InfoOptionBean>());
				}
				
				// now we can set it on the product
				product.getInfoOptions().add(infoOption);
			}
			
		} catch(Exception e) {
			// these could be thrown out of the resolver since it uses reflection.  We'll just log these and move on
			logger.error("Unable to resolve info fields", e);
			
			// we should also null our info option list since we don't know what state it's in
			product.setInfoOptions(null);
		}
		
		try {
			product =  legacyProductManager.create(product, user);
		} catch( SubProductUniquenessException e ) {
			logger.error( "received a subproduct uniquness error this should not be possible form this type of update.", e );
			throw new RuntimeException( e );
		}

		String message = "Created Product [" + product.toString() + "] Owner [" + product.getOwner().getName() + "]";
		writeLogMessage(primaryOrg.getTenant(), message);
		
		return product;
	}
	
	private Inspection createInspection(Tenant tenant, UserBean inspector, BaseOrg owner, Product product, InspectionBook book, Date inspectionDate, FileDataContainer fileData) {
		Inspection inspection = new Inspection();
		inspection.setTenant(tenant);
		
		if (owner != null) {
			/*
			 * if the products owner is a division, we need to see if the resolved owner is the parent
			 * of the products owner.  If that is the case, we preserve the division from the product on the inspection.
			 * In all other cases we will use the resolved owner directly.
			 */
			if (product.getOwner().isDivision() && product.getOwner().getParent().equals(owner)) {
				inspection.setOwner(product.getOwner());
			} else {
				inspection.setOwner(owner);
			}
			
		} else {
			// if the passed in owner is null, use the one from the product
			// this can happen if the proof test had no customer set, meaning this product is from the primary
			inspection.setOwner(product.getOwner());
		}
		
		inspection.setProduct(product);
		inspection.setProductStatus(product.getProductStatus());
		inspection.setDate(inspectionDate);
		inspection.setInspector(inspector);
		inspection.setBook(book);
		inspection.setComments(fileData.getComments());
		inspection.setLocation(product.getLocation());
		
		// find the first inspection that for this product that supports our file type
		InspectionType inspType = findSupportedInspectionTypeForProduct(fileData.getFileType(), product);
		
		// if we were unable to find an inspection type, we cannot continue.
		if(inspType == null) {
			writeLogMessage(tenant, "Unable to find InspectionType for ProductType: [" + product.getType().getId() + "], Proof Test Type: [" + fileData.getFileType().name() + "]", false, null);
			return null;
		}
		
		inspection.setType(inspType);
		inspection.setPrintable(inspType.isPrintable());
		
		
		
		// let's see if there are any inspection info fields that need to be set
		String infoFieldName, infoOptionName, resolvedInfoField;
		for(Map.Entry<String, String> optEntry: fileData.getExtraInfo().entrySet()) {
			infoFieldName = optEntry.getKey();
			infoOptionName = optEntry.getValue();
			
			// see if our inspection type supports this info field
			resolvedInfoField = FuzzyResolver.resolveString(infoFieldName, inspection.getType().getInfoFieldNames(), false);
			
			// if we've found one, let's update the inspection
			if(resolvedInfoField != null) {
				inspection.getInfoOptionMap().put(resolvedInfoField, infoOptionName);
			}
		}
		
		try {
			
			
			inspectionSaver.createInspection(
					new CreateInspectionParameterBuilder(inspection,inspector.getUniqueID())
					.withProofTestFile(fileData).build());
			
			writeLogMessage(tenant, "Created Inspection for Product with serial [" + product.getSerialNumber() + "] and Inspection date [" + inspection.getDate() + "]");
		} catch(Exception e) {
			// we failed to create an inspection, log the failure
			writeLogMessage(tenant, "Failed to create Inspection for Product with serial [" + product.getSerialNumber() + "] and Inspection date [" + inspection.getDate() + "]", false, e);
			return null;
		}
		
		return inspection;
	}
	
	private InspectionType findSupportedInspectionTypeForProduct(ProofTestType proofTestType, Product product) {
		InspectionType type = null;
		
		// here we simply find the first inspection type that supports this product type and proof test type
		for(InspectionType inspType: product.getType().getInspectionTypes()) {
			if(inspType.supports(proofTestType)) {
				type = inspType;
				break;
			}
		}

		return type;
	}
	
	private boolean chartImageExists(Inspection inspection) {
		return PathHandler.getChartImageFile(inspection).exists();
	}
	
	private void writeLogMessage(Tenant tenant, String message) {
		writeLogMessage(tenant, message, true, null);
	}
	
	private void writeLogMessage(Tenant tenant, String message, boolean success, Exception e) {
		
		if(success) {
			logger.info("<" + tenant.getName() + "> " + message);
		} else {
			logger.warn("<" + tenant.getName() + "> " + message, e);
		}
		
		PopulatorLog.logStatus status = (success) ? PopulatorLog.logStatus.success : PopulatorLog.logStatus.error;		
		populatorLogManager.createPopulatorLog(new PopulatorLogBean(tenant, message, status, PopulatorLog.logType.prooftest));
	}
	
	protected LegacyFindOrCreateCustomerOrgHandler getFindOrCreateCustomerHandler() {
		return new LegacyFindOrCreateCustomerOrgHandler(persistenceManager);
	}
}
