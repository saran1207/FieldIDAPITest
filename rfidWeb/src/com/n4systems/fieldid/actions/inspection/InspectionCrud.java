package com.n4systems.fieldid.actions.inspection;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.validation.SkipValidation;

import rfid.ejb.entity.ProductStatusBean;

import com.n4systems.ejb.InspectionManager;
import com.n4systems.ejb.InspectionScheduleManager;
import com.n4systems.ejb.PersistenceManager;
import com.n4systems.ejb.ProductManager;
import com.n4systems.ejb.impl.InspectionScheduleBundle;
import com.n4systems.ejb.impl.ScheduleInTimeFrameLoader;
import com.n4systems.ejb.legacy.LegacyProductSerial;
import com.n4systems.ejb.legacy.UserManager;
import com.n4systems.ejb.parameters.CreateInspectionParameterBuilder;
import com.n4systems.exceptions.FileAttachmentException;
import com.n4systems.exceptions.MissingEntityException;
import com.n4systems.exceptions.ProcessingProofTestException;
import com.n4systems.fieldid.actions.exceptions.PersistenceException;
import com.n4systems.fieldid.actions.exceptions.ValidationException;
import com.n4systems.fieldid.actions.helpers.InspectionScheduleSuggestion;
import com.n4systems.fieldid.actions.helpers.UploadFileSupport;
import com.n4systems.fieldid.actions.inspection.viewmodel.ScheduleToWebInspectionScheduleConverter;
import com.n4systems.fieldid.actions.inspection.viewmodel.WebInspectionScheduleToInspectionScheduleBundleConverter;
import com.n4systems.fieldid.actions.inspection.viewmodel.WebModifiedableInspection;
import com.n4systems.fieldid.actions.utils.OwnerPicker;
import com.n4systems.fieldid.permissions.UserPermissionFilter;
import com.n4systems.fieldid.security.NetworkAwareAction;
import com.n4systems.fieldid.security.SafetyNetworkAware;
import com.n4systems.fieldid.utils.StrutsListHelper;
import com.n4systems.fieldid.viewhelpers.InspectionHelper;
import com.n4systems.fileprocessing.ProofTestType;
import com.n4systems.handlers.creator.inspections.factory.ProductionInspectionPersistenceFactory;
import com.n4systems.model.AssociatedInspectionType;
import com.n4systems.model.CriteriaResult;
import com.n4systems.model.Deficiency;
import com.n4systems.model.Inspection;
import com.n4systems.model.InspectionBook;
import com.n4systems.model.InspectionGroup;
import com.n4systems.model.InspectionSchedule;
import com.n4systems.model.InspectionType;
import com.n4systems.model.Product;
import com.n4systems.model.ProductTypeSchedule;
import com.n4systems.model.ProofTestInfo;
import com.n4systems.model.Recommendation;
import com.n4systems.model.Status;
import com.n4systems.model.SubInspection;
import com.n4systems.model.api.Listable;
import com.n4systems.model.inspectionbook.InspectionBookByNameLoader;
import com.n4systems.model.inspectionbook.InspectionBookListLoader;
import com.n4systems.model.inspectionbook.InspectionBookSaver;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.user.User;
import com.n4systems.reporting.PathHandler;
import com.n4systems.security.Permissions;
import com.n4systems.tools.FileDataContainer;
import com.n4systems.util.DateHelper;
import com.n4systems.util.ListHelper;
import com.n4systems.util.ListingPair;
import com.opensymphony.xwork2.validator.annotations.RequiredFieldValidator;
import com.opensymphony.xwork2.validator.annotations.VisitorFieldValidator;


public class InspectionCrud extends UploadFileSupport implements SafetyNetworkAware {
	private static final long serialVersionUID = 1L;
	private static Logger logger = Logger.getLogger(InspectionCrud.class);

	private final InspectionManager inspectionManager;
	private final LegacyProductSerial legacyProductManager;
	private final UserManager userManager;
	protected final ProductManager productManager;
	private final InspectionScheduleManager inspectionScheduleManager;
	protected final ProductionInspectionPersistenceFactory inspectionPersistenceFactory;
	protected final InspectionHelper inspectionHelper;
	protected final InspectionFormHelper inspectionFormHelper; 
	
	
	private InspectionGroup inspectionGroup;
	protected Product product;
	protected Inspection inspection;
	
	protected List<CriteriaResult> criteriaResults;
	protected String charge;
	protected ProofTestType proofTestType;
	protected File proofTest;// The actual file
	protected String peakLoad;
	protected String testDuration;
	protected String peakLoadDuration;
	protected InspectionSchedule inspectionSchedule;
	protected Long inspectionScheduleId;
	private boolean inspectionScheduleOnInspection = false;
	private boolean scheduleSuggested = false;
	private String newInspectionBookTitle;
	private boolean allowNetworkResults = false;
	private List<SubInspection> subInspections;
	private List<ListingPair> examiners;
	private List<ProductStatusBean> productStatuses;
	private List<Listable<Long>> commentTemplates;
	private List<ListingPair> inspectionBooks;
	private List<InspectionSchedule> availableSchedules;
	private List<ListingPair> eventJobs;
	
	private WebModifiedableInspection modifiableInspection;

	private Map<String, String> encodedInfoOptionMap = new HashMap<String, String>(); 

	protected FileDataContainer fileData = null;

	private String proofTestDirectory;
	private boolean newFile = false;
	
	
	private List<WebInspectionSchedule> nextSchedules = new ArrayList<WebInspectionSchedule>();
	
	
	private ScheduleInTimeFrameLoader scheduleInTimeFrameLoader;
	
	public InspectionCrud(PersistenceManager persistenceManager, InspectionManager inspectionManager, UserManager userManager, LegacyProductSerial legacyProductManager,
			ProductManager productManager, InspectionScheduleManager inspectionScheduleManager) {
		super(persistenceManager);
		this.inspectionManager = inspectionManager;
		this.legacyProductManager = legacyProductManager;
		this.userManager = userManager;
		this.productManager = productManager;
		this.inspectionHelper = new InspectionHelper(persistenceManager);
		this.inspectionScheduleManager = inspectionScheduleManager;
		this.inspectionPersistenceFactory = new ProductionInspectionPersistenceFactory();
		this.inspectionFormHelper = new InspectionFormHelper();
		this.scheduleInTimeFrameLoader = new ScheduleInTimeFrameLoader(persistenceManager);

	}

	@Override
	protected void initMemberFields() {
		inspection = new Inspection();
		inspection.setDate(new Date());
	}

	@Override
	protected void loadMemberFields(Long uniqueId) {
		try {
			if (allowNetworkResults) {
				
				// if we're in a vendor context we need to look inspections for assigned products rather than registered products
				inspection = getLoaderFactory().createSafetyNetworkInspectionLoader(isInVendorContext()).setId(uniqueId).load();
				
			} else {
				inspection = inspectionManager.findAllFields(uniqueId, getSecurityFilter());
			}
			
			if (inspection != null && !inspection.isActive()) {
				inspection = null;
			}
	
			if (inspection != null) {
				inspectionGroup = inspection.getGroup();
				inspectionScheduleOnInspection = (inspection.getSchedule() != null);
			}
		} catch(RuntimeException e) {
			logger.error("Failed to load inspection", e);
			
		}
	}
	
	@Override
	protected void postInit() {
		super.postInit();
		modifiableInspection = new WebModifiedableInspection(new OwnerPicker(getLoaderFactory().createFilteredIdLoader(BaseOrg.class), inspection), getSessionUser().createUserDateConverter());
	}
	

	protected void testDependices() throws MissingEntityException {
		if (inspection == null) {
			addActionError(getText("error.noinspection"));
			throw new MissingEntityException();
		}
		
		if (product == null) {
			addActionError(getText("error.noproduct"));
			throw new MissingEntityException();
		}

		if (inspection.getType() == null) {
			addActionError(getText("error.inspectiontype"));
			throw new MissingEntityException();
		}
	}

	@SkipValidation
	@UserPermissionFilter(userRequiresOneOf={Permissions.CreateInspection})
	public String doQuickInspect() {

		if (product == null) {
			addActionError(getText("error.noproduct"));
			return MISSING;
		}

		if (inspection == null) {
			addActionError(getText("error.noinspection"));
			return MISSING;
		}
		
		if (getInspectionTypes().size() == 1) {
			setType(getInspectionTypes().get(0).getInspectionType().getId());
			if (inspection.getType().isMaster()) {
				return "oneFoundMaster";
			}
			return "oneFound";
		}

		return SUCCESS;
	}

	private List<AssociatedInspectionType> getInspectionTypes() {
		return getLoaderFactory().createAssociatedInspectionTypesLoader().setProductType(product.getType()).load();
	}
	
	@SkipValidation
	@UserPermissionFilter(userRequiresOneOf={Permissions.CreateInspection, Permissions.EditInspection})
	public String doSelect() {
		if (inspection == null) {
			addActionError(getText("error.noinspection"));
			return MISSING;
		}
		
		if (!inspection.isNew()) {
			setProductId(inspection.getProduct().getId());
		}
		
		if (product == null) {
			addActionError(getText("error.noproduct"));
			return MISSING;
		}

		if (inspection.getType().isMaster()) {
			return "master";
		}
		
		return "regular";
		
	}
	

	@SkipValidation
	@UserPermissionFilter(userRequiresOneOf={Permissions.CreateInspection})
	public String doAdd() {
		testDependices();

		// set defaults.
		inspection.setProductStatus(product.getProductStatus());
		inspection.setOwner(product.getOwner());
		inspection.setLocation(product.getLocation());
		inspection.setDate(DateHelper.getTodayWithTime());
		setPerformedBy(getSessionUser().getUniqueID());
		inspection.setPrintable(inspection.getType().isPrintable());
		setUpSupportedProofTestTypes();
		
		if (inspectionSchedule != null) {	
			inspectionSchedule.inProgress();
			try {
				persistenceManager.update(inspectionSchedule, getSessionUser().getId());
				addActionMessageText("message.scheduleinprogress");
			} catch (Exception e) {
				logger.warn("could not move schedule to in progress", e);
			}
		}
		
		suggestSchedule();

		autoSchedule();
		
		
		modifiableInspection.updateValuesToMatch(inspection);
		
		return SUCCESS;
	}

	private void autoSchedule() {
		ProductTypeSchedule schedule = product.getType().getSchedule(inspection.getType(), product.getOwner());
		if (schedule != null) {
			ScheduleToWebInspectionScheduleConverter converter = new ScheduleToWebInspectionScheduleConverter(getSessionUser().createUserDateConverter());
			WebInspectionSchedule nextSchedule = converter.convert(schedule, inspection.getDate());
			nextSchedule.setAutoScheduled(true);
			nextSchedules.add(nextSchedule);
		}
	}

	private void suggestSchedule() {
		if (inspectionScheduleOnInspection == false && inspectionSchedule == null) {
			setScheduleId(new InspectionScheduleSuggestion(getAvailableSchedules()).getSuggestedScheduleId());
			scheduleSuggested = (inspectionSchedule != null);
			
		}
	}

	@SkipValidation
	@NetworkAwareAction
	public String doShow() {
		product = inspection != null ? inspection.getProduct() : null;
		testDependices();

		inspectionGroup = inspection.getGroup();
		return SUCCESS;
	}
	
	@SkipValidation
	@UserPermissionFilter(userRequiresOneOf={Permissions.EditInspection})
	public String doEdit() {
		try {
			setProductId(inspection.getProduct().getId());
			testDependices();
		} catch (NullPointerException e) {
			return MISSING;
		}

		setAttachments(inspection.getAttachments());
		// Re-encode the inspection info option map so it displays properly
		encodeInfoOptionMapForUseInForm();
		

		if (inspection.getProofTestInfo() != null) {
			peakLoad = inspection.getProofTestInfo().getPeakLoad();
			testDuration = inspection.getProofTestInfo().getDuration();
			peakLoadDuration = inspection.getProofTestInfo().getPeakLoadDuration();
		}

		setUpSupportedProofTestTypes();
		
		modifiableInspection.updateValuesToMatch(inspection);
		return SUCCESS;
	}

	protected void encodeInfoOptionMapForUseInForm() {
		encodedInfoOptionMap = encodeMapKeys(inspection.getInfoOptionMap());
	}

	protected void setUpSupportedProofTestTypes() {
		if (inspection.getProofTestInfo() != null && inspection.getProofTestInfo().getProofTestType() != null) {
			proofTestType = inspection.getProofTestInfo().getProofTestType();
		} else if (!getInspectionType().getSupportedProofTests().isEmpty()) {
			proofTestType = getInspectionType().getSupportedProofTests().iterator().next();
		}
	}

	
	@UserPermissionFilter(userRequiresOneOf={Permissions.CreateInspection})
	public String doCreate() {
		return save();
	}
	
	@UserPermissionFilter(userRequiresOneOf={Permissions.EditInspection})
	public String doUpdate() {
		
		return save();
	}
	
	private String save() {
		try {
			testDependices();
		} catch (MissingEntityException e) {
			return MISSING;
		}

		try {
			// get the user to set modifiedBy's later
			User modifiedBy = fetchCurrentUser();
			
			findInspectionBook();
			
			modifiableInspection.pushValuesTo(inspection);

			inspection.setInfoOptionMap(decodeMapKeys(encodedInfoOptionMap));

			inspection.setGroup(inspectionGroup);
			inspection.setTenant(getTenant());
			inspection.setProduct(product);
			
			
			processProofTestFile();
			
			if (inspection.isNew()) {
				// the criteriaResults from the form must be processed before setting them on the inspection
				inspectionHelper.processFormCriteriaResults(inspection, criteriaResults, modifiedBy);
				
				// ensure the form version is set from the inspection type on create
				inspection.syncFormVersionWithType();
				
				CreateInspectionParameterBuilder createInspectionParameterBuilder = new CreateInspectionParameterBuilder(inspection, getSessionUserId())
						.withProofTestFile(fileData)
						.withUploadedImages(getUploadedFiles());
				
				createInspectionParameterBuilder.addSchedules(createInspectionScheduleBundles());
				
				inspection = inspectionPersistenceFactory.createInspectionCreator().create(createInspectionParameterBuilder.build());
				uniqueID = inspection.getId();
				
			} else {
				// only process criteria results if form editing is allowed
				if (inspection.isEditable()) {
					inspectionHelper.processFormCriteriaResults(inspection, criteriaResults, modifiedBy);
				}
				// when updating, we need to remove any files that should no longer be attached
				updateAttachmentList(inspection, modifiedBy);
				inspection = inspectionManager.updateInspection(inspection, getSessionUser().getUniqueID(), fileData, getUploadedFiles());
			}
			
			completeSchedule();
			
		} catch (ProcessingProofTestException e) {
			addActionErrorText("error.processingprooftest");
			return INPUT;
		} catch (FileAttachmentException e) {
			addActionErrorText("error.attachingfile");
			return INPUT;
		} catch (ValidationException e) {
			return INPUT;
		} catch (Exception e) {
			addActionErrorText("error.inspectionsavefailed");
			logger.error("inspection save failed serial number " + product.getSerialNumber(), e);
			return ERROR;
		}

		addFlashMessageText("message.inspectionsaved");
		return SUCCESS;
	}

	protected List<InspectionScheduleBundle> createInspectionScheduleBundles() {
		List<InspectionScheduleBundle> scheduleBundles = new ArrayList<InspectionScheduleBundle>();
		StrutsListHelper.clearNulls(nextSchedules);
		
		WebInspectionScheduleToInspectionScheduleBundleConverter converter = createWebInspectionScheduleToInspectionScheduleBundleConverter();
		
		for (WebInspectionSchedule nextSchedule : nextSchedules) {
			InspectionScheduleBundle bundle = converter.convert(nextSchedule, product);
			scheduleBundles.add(bundle );
		}
	
		
		return scheduleBundles;
	}

	private WebInspectionScheduleToInspectionScheduleBundleConverter createWebInspectionScheduleToInspectionScheduleBundleConverter() {
		WebInspectionScheduleToInspectionScheduleBundleConverter converter = new WebInspectionScheduleToInspectionScheduleBundleConverter(getLoaderFactory(), getSessionUser().createUserDateConverter());
		return converter;
	}

	protected void findInspectionBook() throws ValidationException, PersistenceException {
		if (newInspectionBookTitle != null) {
			if (newInspectionBookTitle.trim().length() == 0) {
				addFieldError("newInspectionBookTitle", getText("error.inspection_book_title_required"));
				throw new ValidationException("not validated.");
			}

			// TODO: change this to the InspectionBookFindOrCreateLoader
			InspectionBookByNameLoader bookLoader = new InspectionBookByNameLoader(getSecurityFilter());
			bookLoader.setName(newInspectionBookTitle);
			bookLoader.setOwner(getOwner());
			InspectionBook inspectionBook = bookLoader.load();
			
			if (inspectionBook == null) {
				inspectionBook = new InspectionBook();
				inspectionBook.setName(newInspectionBookTitle);
				inspectionBook.setOpen(true);
				inspectionBook.setOwner(getOwner());
				inspectionBook.setTenant(getTenant());
				
				InspectionBookSaver bookSaver = new InspectionBookSaver();
				bookSaver.save(inspectionBook);
			}
			
			inspection.setBook(inspectionBook);			
		}
	}
	
	private void completeSchedule() {
		if (inspectionScheduleOnInspection == false ) {
			try {
				if (inspectionScheduleId.equals(InspectionScheduleSuggestion.NEW_SCHEDULE)) {
					inspectionSchedule = new InspectionSchedule(inspection);
				} else if (inspectionSchedule != null) {
					inspectionSchedule.completed(inspection);
				}
				if (inspectionSchedule != null) {
					inspectionScheduleManager.update(inspectionSchedule);
					addFlashMessageText("message.schedulecompleted");
				}
			} catch (Exception e) {
				logger.error("could not complete the schedule", e);
				addFlashErrorText("error.completingschedule");
			}
		}
	}

	protected void processProofTestFile() throws ProcessingProofTestException {
		if (newFile == true && proofTestDirectory != null && proofTestDirectory.length() != 0) {
			File tmpDirectory = PathHandler.getTempRoot();
			proofTest = new File(tmpDirectory.getAbsolutePath() + '/' + proofTestDirectory);
		}

		if (proofTest != null && proofTestType != ProofTestType.OTHER) {
			inspection.setProofTestInfo(new ProofTestInfo());
			inspection.getProofTestInfo().setProofTestType(proofTestType);
			fileData = createFileDataContainer();
		} else if (proofTestType == ProofTestType.OTHER) {
			inspection.setProofTestInfo(new ProofTestInfo());
			inspection.getProofTestInfo().setProofTestType(proofTestType);
			fileData = new FileDataContainer();
			fileData.setFileType(proofTestType);
			fileData.setPeakLoad(peakLoad);
			fileData.setTestDuration(testDuration);
			fileData.setPeakLoadDuration(peakLoadDuration);
		}
	}

	private FileDataContainer createFileDataContainer() throws ProcessingProofTestException {

		FileDataContainer fileData;
		try {
			fileData = inspection.getProofTestInfo().getProofTestType().getFileProcessorInstance().processFile(proofTest);
		} catch (Exception e) {
			throw new ProcessingProofTestException(e);
		} finally {
			// clean up the temp proof test file
			proofTest.delete();
		}

		return fileData;
	}

	@SkipValidation
	@UserPermissionFilter(userRequiresOneOf={Permissions.EditInspection})
	public String doDelete() {
		try {
			testDependices();
		} catch (MissingEntityException e) {
			return MISSING;
		}
		try {
			inspectionManager.retireInspection(inspection, getSessionUser().getUniqueID());
		} catch (Exception e) {
			addFlashErrorText("error.inspectiondeleting");
			logger.error("inspection retire " + product.getSerialNumber(), e);
			return ERROR;
		}

		addFlashMessage(getText("message.inspectiondeleted"));
		return SUCCESS;
	}
	
	


	public Long getType() {
		return (inspection.getType() != null) ? inspection.getType().getId() : null;
	}

	public InspectionType getInspectionType() {
		return inspection.getType();
	}

	public boolean isPrintable() {
		return inspection.isPrintable();
	}


	public void setPrintable(boolean printable) {
		inspection.setPrintable(printable);
	}

	@RequiredFieldValidator(message="", key="error.noinspectiontype")
	public void setType(Long type) {
		if (type == null) {
			inspection.setType(null);
		} else if (inspection.getType() == null || !type.equals(inspection.getType().getId())) {
			inspection.setType(persistenceManager.find(InspectionType.class, type, getTenantId(), "sections", "supportedProofTests", "infoFieldNames"));
		}
	}

	public Long getInspectionGroupId() {
		return (inspectionGroup != null) ? inspectionGroup.getId() : null;
	}

	public void setInspectionGroupId(Long inspectionGroupId) {
		if (inspectionGroupId == null) {
			inspectionGroup = null;
		} else if (inspectionGroup == null || inspectionGroupId.equals(inspectionGroup.getId())) {
			inspectionGroup = persistenceManager.find(InspectionGroup.class, inspectionGroupId, getTenantId());
		}
	}

	public Product getProduct() {
		return product;
	}

	public Long getProductId() {
		return (product != null) ? product.getId() : null;
	}

	@RequiredFieldValidator(message="", key="error.noproduct")
	public void setProductId(Long productId) {
		if (productId == null) {
			product = null;
		} else if (product == null || !productId.equals(product.getId())) {
			product = productManager.findProduct(productId, getSecurityFilter(), "type.inspectionTypes", "infoOptions", "projects");
			product = productManager.fillInSubProductsOnProduct(product);
		}
	}

	public Inspection getInspection() {
		return inspection;
	}

	
	public List<ListingPair> getExaminers() {
		if (examiners == null) {
			examiners = userManager.getExaminers(getSecurityFilter());
		}
		return examiners;
	}

	public List<ListingPair> getUsers() {
		if (examiners == null) {
			examiners = userManager.getUserList(getSecurityFilter());
		}
		return examiners;
	}
	
	
	

	public List<ProductStatusBean> getProductStatuses() {
		if (productStatuses == null) {
			productStatuses = getLoaderFactory().createProductStatusListLoader().load();
		}
		return productStatuses;
	}

	public List<Listable<Long>> getCommentTemplates() {
		if (commentTemplates == null) {
			commentTemplates = getLoaderFactory().createCommentTemplateListableLoader().load();
		}
		return commentTemplates;
	}

	public String getComments() {
		return inspection.getComments();
	}
	
	public void setComments(String comments) {
		inspection.setComments(comments);
	}

	public Long getProductStatus() {
		return (inspection.getProductStatus() != null) ? inspection.getProductStatus().getUniqueID() : null;
	}

	public void setProductStatus(Long productStatus) {
		if (productStatus == null) {
			inspection.setProductStatus(null);
		} else if (inspection.getProductStatus() == null || !productStatus.equals(inspection.getProductStatus().getUniqueID())) {
			inspection.setProductStatus(legacyProductManager.findProductStatus(productStatus, getTenantId()));
		}
	}

	public Long getperformedBy() {
		return (inspection.getPerformedBy() != null) ? inspection.getPerformedBy().getId() : null;
	}

	public void setPerformedBy(Long performedBy) {
		if (performedBy == null) {
			inspection.setPerformedBy(null);
		} else if (inspection.getPerformedBy() == null || !performedBy.equals(inspection.getPerformedBy())) {
			inspection.setPerformedBy(userManager.findUser(performedBy, getTenantId()));
		}

	}

	public Long getBook() {
		return (inspection.getBook() != null) ? inspection.getBook().getId() : null;
	}

	public void setBook(Long book) {
		if (book == null) {
			inspection.setBook(null);
		} else if (inspection.getBook() == null || !book.equals(inspection.getBook())) {
			inspection.setBook(persistenceManager.find(InspectionBook.class, book, getTenantId()));
		}
	}
	
	public String getNewInspectionBookTitle() {
		return newInspectionBookTitle;
	}
	
	public void setNewInspectionBookTitle(String newInspectionBookTitle) {
		this.newInspectionBookTitle = newInspectionBookTitle;
	}

	public List<CriteriaResult> getCriteriaResults() {
		if (criteriaResults == null) {
			// criteria results need to be placed back in the order that they appear on the form
			// so that the states and button images line up correctly.
			criteriaResults = inspectionHelper.orderCriteriaResults(inspection);
		}

		return criteriaResults;
	}

	public void setCriteriaResults(List<CriteriaResult> results) {
		criteriaResults = results;
	}


	public String getProofTestType() {
		getProofTestTypeEnum();
		return (proofTestType != null) ? proofTestType.name() : null;
	}

	public ProofTestType getProofTestTypeEnum() {
		if (proofTestType == null) {
			proofTestType = (inspection.getProofTestInfo() != null) ? inspection.getProofTestInfo().getProofTestType() : null;
		}
		return proofTestType;
	}

	public void setProofTestType(String proofTestType) {
		this.proofTestType = ProofTestType.valueOf(proofTestType);
	}

	public List<ListingPair> getInspectionBooks() {
		if (inspectionBooks == null) {
			InspectionBookListLoader loader = new InspectionBookListLoader(getSecurityFilter());
			loader.setOpenBooksOnly((uniqueID == null));
			loader.setOwner(getOwner());
			inspectionBooks = loader.loadListingPair();
		}
		return inspectionBooks;
	}

	public String getResult() {
		return (inspection.getStatus() != null) ? inspection.getStatus().name() : Status.NA.name();
	}

	public void setResult(String result) {
		inspection.setStatus((result != null && result.trim().length() > 0) ? Status.valueOf(result) : Status.NA);
	}

	public List<Status> getResults() {
		return Arrays.asList(Status.values());
	}

	@SuppressWarnings("unchecked")
	public Map getInfoOptionMap() {
		return inspection.getInfoOptionMap();
	}

	@SuppressWarnings("unchecked")
	public void setInfoOptionMap(Map infoOptionMap) {
		inspection.setInfoOptionMap(infoOptionMap);
	}

	
	public File returnFile(String fileName) {
		return new File(fileName);
	}

	public String getPeakLoad() {
		return peakLoad;
	}

	public void setPeakLoad(String peakLoad) {
		this.peakLoad = peakLoad;
	}

	public String getTestDuration() {
		return testDuration;
	}

	public void setTestDuration(String testDuration) {
		this.testDuration = testDuration;
	}
	
	public String getPeakLoadDuration() {
		return peakLoadDuration;
	}

	public void setPeakLoadDuration(String peakLoadDuration) {
		this.peakLoadDuration = peakLoadDuration;
	}

	public Map<String, Boolean> getProofTestTypesUpload() {
		Map<String, Boolean> uploadFlags = new HashMap<String, Boolean>();
		for (ProofTestType proofTestType : inspection.getType().getSupportedProofTests()) {
			uploadFlags.put(proofTestType.name(), proofTestType.isUploadable());
		}

		return uploadFlags;
	}

	public String getProofTestDirectory() {
		return proofTestDirectory;
	}

	public void setProofTestDirectory(String proofTestDirectory) {
		this.proofTestDirectory = proofTestDirectory;
	}

	public boolean isNewFile() {
		return newFile;
	}

	public void setNewFile(boolean newFile) {
		this.newFile = newFile;
	}

	public boolean isParentProduct() {
		return true;
	}

	public List<SubInspection> getSubInspections() {
		if (subInspections == null) {
			if (!inspection.getSubInspections().isEmpty()) {
				Set<Long> ids = new HashSet<Long>();
				for (SubInspection subInspection : inspection.getSubInspections()) {
					ids.add(subInspection.getId());
				}
				subInspections = persistenceManager.findAll(SubInspection.class, ids, getTenant(), "product", "type.sections", "results", "attachments", "infoOptionMap");
			}
		}
		return subInspections;
	}	

	
	
	/** Finds a Recommendation on the criteriaResults for this criteriaId and recommendation index */
	public Recommendation findEditRecommendation(Long criteriaId, int recIndex) {
		// first let's grab the result for this Criteria
		CriteriaResult result = inspectionHelper.findResultInCriteriaResultsByCriteriaId(criteriaResults, criteriaId);
		
		// next we need the text of our Recommendation
		String recText = inspectionHelper.findCriteriaOnInspectionType(inspection, criteriaId).getRecommendations().get(recIndex);
		
		// now we need to hunt down a matching Recommendation from our criteria results (if it exists)
		return inspectionHelper.getObservationForText(result.getRecommendations(), recText);
	}
	
	/** Finds a Deficiency on the criteriaResults for this criteriaId and deficiency index */
	public Deficiency findEditDeficiency(Long criteriaId, int defIndex) {
		// first let's grab the result for this Criteria
		CriteriaResult result = inspectionHelper.findResultInCriteriaResultsByCriteriaId(criteriaResults, criteriaId);
		
		// next we need the text of our Deficiency
		String defText = inspectionHelper.findCriteriaOnInspectionType(inspection, criteriaId).getDeficiencies().get(defIndex);
		
		// now we need to hunt down a matching Deficiency from our criteria results (if it exists)
		return inspectionHelper.getObservationForText(result.getDeficiencies(), defText);
	}
	
	/** Finds a comment Recommendation on the criteriaResults for this criteriaId */
	public Recommendation findEditRecommendationComment(Long criteriaId) {
		CriteriaResult result = inspectionHelper.findResultInCriteriaResultsByCriteriaId(criteriaResults, criteriaId);
		return inspectionHelper.getCommentObservation(result.getRecommendations());
	}
	
	/** Finds a comment Deficiency on the criteriaResults for this criteriaId */
	public Deficiency findEditDeficiencyComment(Long criteriaId) {
		CriteriaResult result = inspectionHelper.findResultInCriteriaResultsByCriteriaId(criteriaResults, criteriaId);
		return inspectionHelper.getCommentObservation(result.getDeficiencies());
	}

	public int countRecommendations(int criteriaIndex) {
		if (criteriaResults == null || criteriaResults.isEmpty() || criteriaResults.get(criteriaIndex) == null) {
			return 0;
		}
		return inspectionHelper.countObservations(criteriaResults.get(criteriaIndex).getRecommendations());
	}
	
	public int countDeficiencies(int criteriaIndex) {
		if (criteriaResults == null || criteriaResults.isEmpty() || criteriaResults.get(criteriaIndex) == null) {
			return 0;
		}
		return inspectionHelper.countObservations(criteriaResults.get(criteriaIndex).getDeficiencies());
	}
	
	public Long getScheduleId() {
		return inspectionScheduleId;
	}

	public void setScheduleId(Long inspectionScheduleId) {
		if (inspectionScheduleId == null) {
			inspectionSchedule = null; 
		} else if ((!inspectionScheduleId.equals(InspectionScheduleSuggestion.NEW_SCHEDULE) && !inspectionScheduleId.equals(InspectionScheduleSuggestion.NO_SCHEDULE)) && 
				(this.inspectionScheduleId == null || !inspectionScheduleId.equals(this.inspectionScheduleId))) {
			// XXX should this lock to just the correct product and inspection type?
			inspectionSchedule = persistenceManager.find(InspectionSchedule.class, inspectionScheduleId, getTenantId());
		}
		this.inspectionScheduleId = inspectionScheduleId;
	}

	public List<InspectionSchedule> getAvailableSchedules() {
		if (availableSchedules == null) {
			
			availableSchedules = scheduleInTimeFrameLoader.getSchedulesInTimeFrame(product, inspection.getType(), inspection.getDate());
			if (inspectionSchedule != null && !availableSchedules.contains(inspectionSchedule)) {
				availableSchedules.add(0, inspectionSchedule);  
			}
		}
		return availableSchedules;
	}
	
	public List<ListingPair> getSchedules() {
		List<ListingPair> scheduleOptions = new ArrayList<ListingPair>();
		scheduleOptions.add(new ListingPair(InspectionScheduleSuggestion.NO_SCHEDULE, getText("label.notscheduled")));
		scheduleOptions.add(new ListingPair(InspectionScheduleSuggestion.NEW_SCHEDULE, getText("label.createanewscheduled")));
		for (InspectionSchedule schedule : getAvailableSchedules()) {
			scheduleOptions.add(new ListingPair(schedule.getId(), convertDate(schedule.getNextDate())));
		}
		
		return scheduleOptions;
	}
	
	public List<ListingPair> getJobs() {
		if (eventJobs == null) {
			List<Listable<Long>> eventJobListables = getLoaderFactory().createEventJobListableLoader().load();
			eventJobs = ListHelper.longListableToListingPair(eventJobListables);
		}
		return eventJobs;
	}
	
	public boolean isInspectionScheduleOnInspection() {
		return inspectionScheduleOnInspection;
	}

	public boolean isScheduleSuggested() {
		return scheduleSuggested;
	}
	
	public Map<String, String> getEncodedInfoOptionMap() {
		return encodedInfoOptionMap;
	}

	public void setEncodedInfoOptionMap(Map<String, String> encodedInfoOptionMap) {
		this.encodedInfoOptionMap = encodedInfoOptionMap;
	}

	public Long getOwnerId() {
		return modifiableInspection.getOwnerId();
	}

	
	public void setOwnerId(Long id) {
		modifiableInspection.setOwnerId(id);
	}

	@VisitorFieldValidator(message = "")
	public WebModifiedableInspection getModifiableInspection() {
		if (modifiableInspection == null) {
			throw new NullPointerException("action has not been initialized.");
		}
		return modifiableInspection;
	}
	
	public BaseOrg getOwner() {
		return modifiableInspection.getOwner();
	}
	
	public void setAllowNetworkResults(boolean allow) {
		this.allowNetworkResults = allow;
	}
	
	public boolean isLinkedInspection() {
		return !inspection.getTenant().equals(getTenant());
	}

	public InspectionFormHelper getInspectionFormHelper() {
		return inspectionFormHelper;
	}
	
	@SuppressWarnings("deprecation")
	public List<InspectionType> getEventTypes() {
		return new ArrayList<InspectionType>(product.getType().getInspectionTypes());
	}

	public List<WebInspectionSchedule> getNextSchedules() {
		return nextSchedules;
	}
	
}
