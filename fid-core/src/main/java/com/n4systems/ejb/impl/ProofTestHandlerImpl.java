package com.n4systems.ejb.impl;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import javax.persistence.EntityManager;

import com.n4systems.ejb.AssetManager;
import com.n4systems.ejb.EventManager;
import com.n4systems.ejb.legacy.LegacyAsset;
import com.n4systems.ejb.legacy.LegacyAssetType;
import com.n4systems.ejb.legacy.impl.LegacyAssetManager;
import com.n4systems.exceptions.SubAssetUniquenessException;
import com.n4systems.model.Asset;
import com.n4systems.model.AssetType;
import com.n4systems.model.Event;
import com.n4systems.model.EventType;
import org.apache.log4j.Logger;

import rfid.ejb.entity.InfoFieldBean;
import rfid.ejb.entity.InfoOptionBean;
import rfid.ejb.entity.PopulatorLogBean;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.ejb.ProofTestHandler;
import com.n4systems.ejb.legacy.PopulatorLog;
import com.n4systems.ejb.legacy.impl.PopulatorLogManager;
import com.n4systems.ejb.parameters.CreateInspectionParameterBuilder;
import com.n4systems.exceptions.FileProcessingException;
import com.n4systems.exceptions.NonUniqueAssetException;
import com.n4systems.fileprocessing.ProofTestType;
import com.n4systems.model.EventBook;
import com.n4systems.model.Tenant;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.orgs.CustomerOrg;
import com.n4systems.model.orgs.LegacyFindOrCreateCustomerOrgHandler;
import com.n4systems.model.orgs.PrimaryOrg;
import com.n4systems.model.user.User;
import com.n4systems.reporting.PathHandler;
import com.n4systems.tools.FileDataContainer;
import com.n4systems.util.DateHelper;
import com.n4systems.util.FuzzyResolver;
import com.n4systems.util.StringUtils;

@SuppressWarnings("deprecation")

public class ProofTestHandlerImpl implements ProofTestHandler {
	private Logger logger = Logger.getLogger(ProofTestHandler.class);
	
	 private LegacyAsset legacyAssetManager;
	 private AssetManager assetManager;
	 private LegacyAssetType assetTypeManager;
	 private PersistenceManager persistenceManager;
	 private EventManager eventManager;
	 private PopulatorLog populatorLogManager;

	private EventSaver eventSaver;
	
	public ProofTestHandlerImpl(EntityManager em) {
		this.legacyAssetManager = new LegacyAssetManager(em);
		this.assetManager = new AssetManagerImpl(em);
		this.legacyAssetManager = new LegacyAssetManager(em);
		this.persistenceManager = new PersistenceManagerImpl(em);
		this.eventManager = new EventManagerImpl(em);
		this.populatorLogManager = new PopulatorLogManager(em);
		this.eventSaver = new ManagerBackedEventSaver(new LegacyAssetManager(em),
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
	public Map<String, Event> multiProofTestUpload(File proofTestFile, ProofTestType type, Long tenantId, Long userId, Long ownerId, Long inspectionBookId) throws FileProcessingException {
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
		User user = persistenceManager.find(User.class, userId, tenantId);
		BaseOrg customer = persistenceManager.find(BaseOrg.class, ownerId, tenantId);
		EventBook book = persistenceManager.find(EventBook.class, inspectionBookId, tenantId);
		
		return createOrUpdateProofTest(fileData, user, customer, book, false);
	}
	
	public Map<String, Event> inspectionServiceUpload(FileDataContainer fileData, User performedBy) throws FileProcessingException  {
		return createOrUpdateProofTest(fileData, performedBy, null, null, true);
	}
	
	/*
	 * @returns		A map of Serial Numbers to Inspections.  A null inspection means that processing failed for that Serial Number
	 */
	private Map<String, Event> createOrUpdateProofTest(FileDataContainer fileData, User performedBy, BaseOrg customer, EventBook book, boolean assetOverridesPerformedBy) throws FileProcessingException {
		Map<String, Event> inspectionMap = new HashMap<String, Event>();
		
		logger.info("Started processing of file [" + fileData.getFileName() + "]");
		
		Tenant tenant = performedBy.getTenant();
		PrimaryOrg primaryOrg = performedBy.getOwner().getPrimaryOrg();
		
		
		
		
		// sending a null customer will lookup assets with no customer (rather then assets for any customer)
		Long customerId = (customer != null) ? customer.getId() : null;
		
		//if our customer is null, then let's see if we're supposed to resolve a customer by name from the FileDataContainer
		if(customerId == null && fileData.isResolveCustomer()) {
			// lets resolve a customer from the file data container.  This will also create a customer if resolution fails and createCustomer is set
			
			customer = findOrCreateCustomer(primaryOrg, performedBy, fileData.getCustomerName(), fileData.isCreateCustomer());
						
			// If the customer is still null then, we were unalbe to find or create a customer.  Let's assume we we're supposed to use no customer
			if(customer == null) {
				logger.warn("Unable to find or create customer.  Assuming no customer.");
				customerId = null;
			} else {
				customerId = customer.getId();
			}

		} 
		Date datePerformed = fileData.getDatePerformed();

		Asset asset;
		List<Event> events;
		Event event;
		// since proof tests may have multiple serial numbers, we'll need to do this process for each
		for (String serialNumber : fileData.getSerialNumbers()) {
			event = null;
			try {
				// find an asset for this tenant, serial and customer
				asset = findOrCreateAsset(primaryOrg, performedBy, serialNumber, customer, fileData);
			} catch (NonUniqueAssetException e) {
				writeLogMessage(tenant, "There are multiple Asset with serial number[" + serialNumber + "] in file [" + fileData.getFileName() + "]", false, null);
				inspectionMap.put(serialNumber, null);
				continue;
			}
			
			// if the asset is null, log it and move on to the next serial
			if (asset == null) {
				writeLogMessage(tenant, "Could not find/create Asset [" + serialNumber + "] referenced in file [" + fileData.getFileName() + "]", false, null);
				inspectionMap.put(serialNumber, null);
				continue;
			}
			
			/*
			 *  If the asset identifiedBy is set to override the performedBy (databridge upload uses this)
			 *  and the two are different, then set the performedBy to be the identifiedBy from the asset
			 */
			if (assetOverridesPerformedBy) {
				performedBy = asset.getIdentifiedBy();
			}
			
			// convert date to utc using the performed By time.
			Date datePerformedInUTC = DateHelper.convertToUTC(datePerformed, performedBy.getTimeZone());
			Date datePerformedRangeStartInUTC = DateHelper.convertToUTC(DateHelper.getBeginingOfDay(datePerformed), performedBy.getTimeZone());
			Date datePerformedRangeEndInUTC = DateHelper.convertToUTC(DateHelper.getEndOfDay(datePerformed), performedBy.getTimeZone());

			// if we find an asset then it's time to try and find an inspection inside the same day as given.
			events = eventManager.findEventsByDateAndAsset(datePerformedRangeStartInUTC, datePerformedRangeEndInUTC, asset, performedBy.getSecurityFilter());
			
			// now we need to find the inspection, supporting out ProofTestType, and does not already have a chart
			for (Event insp: events) {
				if(insp.getType().supports(fileData.getFileType()) && !chartImageExists(insp)) {
					// we have found our inspection, move on
					event = insp;
					break;
				}
			}
			
			// if we were unable to locate an inspection, then we'll need to create a new one
			if (event == null) {
				event = createInspection(tenant, performedBy, customer, asset, book, datePerformedInUTC, fileData);
			} else {
				try {
					// we have a valid inspection, now we can update it
					eventManager.updateEvent(event, performedBy.getId(), fileData, null);
					writeLogMessage(tenant, "Updated Inspection for Asset with serial [" + serialNumber + "] and date performed [" + event.getDate() + "]");
				} catch(Exception e) {
					// we don't want a failure in one inspection to cause the others to fail, so we will simply log these expections and move on
					writeLogMessage(tenant, "Failed to update Inspection for Asset with serial [" + serialNumber + "] and date performed [" + event.getDate() + "]", false, e);
					inspectionMap.put(serialNumber, null);
					continue;
				}
			}

			// update our map with the serial and inspection
			inspectionMap.put(serialNumber, event);
		}
		
		logger.info("Completed processing of file [" + fileData.getFileName() + "]");
		
		return inspectionMap;
	}
	
	private CustomerOrg findOrCreateCustomer(PrimaryOrg primaryOrg, User user, String customerName, boolean createCustomer) {
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
	
	private Asset findOrCreateAsset(PrimaryOrg primaryOrg, User user, String serial, BaseOrg customer, FileDataContainer fileData) throws NonUniqueAssetException {
		Long customerId = (customer != null) ?  customer.getId() : null;
		
		// we must have a valid serial number
		if(serial == null || serial.length() == 0) {
			return null;
		}
		
		// find an asset for this tenant, serial and customer
		Asset asset = assetManager.findAssetBySerialNumber(serial, primaryOrg.getTenant().getId(), customerId);
		
		if(asset == null) {
			if(!fileData.isCreateAsset()) {
				// if we're not supported to create new assets and we could not find one, return null
				return null;
			} else {
				// create asset is set, lets create a default
				asset = createAsset(primaryOrg, user, customer, serial, fileData.getExtraInfo());
			}
		}
		
		return asset;
	}

	private Asset createAsset(PrimaryOrg primaryOrg, User user, BaseOrg owner, String serialNumber, Map<String, String> assetOptions) {
		Asset asset = new Asset();
		
		asset.setTenant(primaryOrg.getTenant());
		asset.setSerialNumber(serialNumber);
		
		AssetType assetType = assetTypeManager.findDefaultAssetType(primaryOrg.getTenant().getId());
		asset.setType(assetType);
		
		asset.setIdentifiedBy(user);
		asset.setModifiedBy(user);
		
		if (owner != null) {
			asset.setOwner(owner);
		} else {
			// if the owner was null, it goes against the primary
			asset.setOwner(primaryOrg);
		}
		
		Date now = new Date();
		asset.setIdentified(now);
		asset.setCreated(now);
		asset.setModified(now);
		
		try {
			// now lets's try and resolve the infofield names from our assetoptions
			String infoFieldName, infoOptionName;
			InfoFieldBean infoField;
			InfoOptionBean infoOption;
			for(Map.Entry<String, String> optEntry: assetOptions.entrySet()) {
				infoFieldName = optEntry.getKey();
				infoOptionName = optEntry.getValue();
	
				// we'll use the fuzzy resolver to try and find the info field we're looking for
				infoField = FuzzyResolver.resolve(infoFieldName, asset.getType().getInfoFields(), "name", false);
				
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
				infoOption.setWeight(asset.getNextInfoOptionWeight());
				infoOption.setName(infoOptionName);
				
				// make sure the asset has an infoOption set ready to go
				if(asset.getInfoOptions() == null) {
					asset.setInfoOptions(new TreeSet<InfoOptionBean>());
				}
				
				// now we can set it on the asset
				asset.getInfoOptions().add(infoOption);
			}
			
		} catch(Exception e) {
			// these could be thrown out of the resolver since it uses reflection.  We'll just log these and move on
			logger.error("Unable to resolve info fields", e);
			
			// we should also null our info option list since we don't know what state it's in
			asset.setInfoOptions(null);
		}
		
		try {
			asset =  legacyAssetManager.create(asset, user);
		} catch( SubAssetUniquenessException e ) {
			logger.error( "received a subasset uniquness error this should not be possible form this type of update.", e );
			throw new RuntimeException( e );
		}

		String message = "Created Asset [" + asset.toString() + "] Owner [" + asset.getOwner().getName() + "]";
		writeLogMessage(primaryOrg.getTenant(), message);
		
		return asset;
	}
	
	private Event createInspection(Tenant tenant, User performedBy, BaseOrg owner, Asset asset, EventBook book, Date datePerformed, FileDataContainer fileData) {
		Event event = new Event();
		event.setTenant(tenant);
		
		if (owner != null) {
			/*
			 * if the assets owner is a division, we need to see if the resolved owner is the parent
			 * of the assets owner.  If that is the case, we preserve the division from the asset on the inspection.
			 * In all other cases we will use the resolved owner directly.
			 */
			if (asset.getOwner().isDivision() && asset.getOwner().getParent().equals(owner)) {
				event.setOwner(asset.getOwner());
			} else {
				event.setOwner(owner);
			}
			
		} else {
			// if the passed in owner is null, use the one from the asset
			// this can happen if the proof test had no customer set, meaning this asset is from the primary
			event.setOwner(asset.getOwner());
		}
		
		event.setAsset(asset);
		event.setAssetStatus(asset.getAssetStatus());
		event.setDate(datePerformed);
		event.setPerformedBy(performedBy);
		event.setBook(book);
		event.setComments(fileData.getComments());
		event.setAdvancedLocation(asset.getAdvancedLocation());
		
		// find the first inspection that for this asset that supports our file type
		EventType inspType = findSupportedEventTypeForAsset(fileData.getFileType(), asset);
		
		// if we were unable to find an inspection type, we cannot continue.
		if(inspType == null) {
			writeLogMessage(tenant, "Unable to find EventType for AssetType: [" + asset.getType().getId() + "], Proof Test Type: [" + fileData.getFileType().name() + "]", false, null);
			return null;
		}
		
		event.setType(inspType);
		event.setPrintable(inspType.isPrintable());
		
		
		
		// let's see if there are any inspection info fields that need to be set
		String infoFieldName, infoOptionName, resolvedInfoField;
		for(Map.Entry<String, String> optEntry: fileData.getExtraInfo().entrySet()) {
			infoFieldName = optEntry.getKey();
			infoOptionName = optEntry.getValue();
			
			// see if our inspection type supports this info field
			resolvedInfoField = FuzzyResolver.resolveString(infoFieldName, event.getType().getInfoFieldNames(), false);
			
			// if we've found one, let's update the inspection
			if(resolvedInfoField != null) {
				event.getInfoOptionMap().put(resolvedInfoField, infoOptionName);
			}
		}
		
		try {
			
			
			eventSaver.createEvent(
					new CreateInspectionParameterBuilder(event,performedBy.getId())
					.withProofTestFile(fileData).build());
			
			writeLogMessage(tenant, "Created Inspection for Asset with serial [" + asset.getSerialNumber() + "] and date performed [" + event.getDate() + "]");
		} catch(Exception e) {
			// we failed to create an inspection, log the failure
			writeLogMessage(tenant, "Failed to create Inspection for Asset with serial [" + asset.getSerialNumber() + "] and date performed [" + event.getDate() + "]", false, e);
			return null;
		}
		
		return event;
	}
	
	private EventType findSupportedEventTypeForAsset(ProofTestType proofTestType, Asset asset) {
		EventType type = null;
		
		// here we simply find the first inspection type that supports this asset type and proof test type
		for(EventType inspType: asset.getType().getEventTypes()) {
			if(inspType.supports(proofTestType)) {
				type = inspType;
				break;
			}
		}

		return type;
	}
	
	private boolean chartImageExists(Event event) {
		return PathHandler.getChartImageFile(event).exists();
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
