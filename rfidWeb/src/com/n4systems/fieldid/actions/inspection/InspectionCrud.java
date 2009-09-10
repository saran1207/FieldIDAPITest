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
import rfid.ejb.entity.UserBean;
import rfid.ejb.session.CommentTemp;
import rfid.ejb.session.LegacyProductSerial;
import rfid.ejb.session.User;

import com.n4systems.ejb.InspectionManager;
import com.n4systems.ejb.InspectionScheduleManager;
import com.n4systems.ejb.PersistenceManager;
import com.n4systems.ejb.ProductManager;
import com.n4systems.ejb.SafetyNetworkManager;
import com.n4systems.exceptions.FileAttachmentException;
import com.n4systems.exceptions.InvalidQueryException;
import com.n4systems.exceptions.ProcessingProofTestException;
import com.n4systems.fieldid.actions.exceptions.PersistenceException;
import com.n4systems.fieldid.actions.exceptions.ValidationException;
import com.n4systems.fieldid.actions.helpers.InspectionScheduleSuggestion;
import com.n4systems.fieldid.actions.helpers.MissingEntityException;
import com.n4systems.fieldid.actions.helpers.UploadFileSupport;
import com.n4systems.fieldid.actions.utils.OwnerPicker;
import com.n4systems.fieldid.viewhelpers.InspectionHelper;
import com.n4systems.fileprocessing.ProofTestType;
import com.n4systems.model.AbstractInspection;
import com.n4systems.model.AssociatedInspectionType;
import com.n4systems.model.Criteria;
import com.n4systems.model.CriteriaResult;
import com.n4systems.model.CriteriaSection;
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
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.reporting.PathHandler;
import com.n4systems.tools.FileDataContainer;
import com.n4systems.util.DateHelper;
import com.n4systems.util.ListingPair;
import com.opensymphony.xwork2.validator.annotations.CustomValidator;
import com.opensymphony.xwork2.validator.annotations.RequiredFieldValidator;
import com.opensymphony.xwork2.validator.annotations.RequiredStringValidator;
import com.opensymphony.xwork2.validator.annotations.ValidationParameter;

public class InspectionCrud extends UploadFileSupport {
	private static final long serialVersionUID = 1L;
	private static Logger logger = Logger.getLogger(InspectionCrud.class);

	private final InspectionManager inspectionManager;
	private final LegacyProductSerial legacyProductManager;
	private final User userManager;
	private final CommentTemp commentTemplateManager;
	private final SafetyNetworkManager safetyNetworkManager;
	protected final ProductManager productManager;
	private final InspectionScheduleManager inspectionScheduleManager;
	protected final InspectionHelper inspectionHelper;
	
	
	private InspectionGroup inspectionGroup;
	protected Product product;
	protected Inspection inspection;
	protected String nextInspectionDate;
	protected ProductStatusBean productStatus;
	protected List<CriteriaResult> criteriaResults;
	protected String inspectionDate;
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
	
	private List<SubInspection> subInspections;
	private List<ListingPair> inspectors;
	private List<ProductStatusBean> productStatuses;
	private List<ListingPair> commentTemplates;
	private List<ListingPair> inspectionBooks;
	private List<InspectionSchedule> availableSchedules;
	
	private OwnerPicker ownerPicker;
	

	private Map<AbstractInspection, Map<CriteriaSection, List<CriteriaResult>>> sections = new HashMap<AbstractInspection, Map<CriteriaSection, List<CriteriaResult>>>();
	private Map<AbstractInspection, List<CriteriaSection>> availableSections = new HashMap<AbstractInspection, List<CriteriaSection>>();

	private Map<String, String> encodedInfoOptionMap = new HashMap<String, String>(); 


	private List<CriteriaSection> currentCriteriaSections;

	protected FileDataContainer fileData = null;

	private String proofTestDirectory;
	private boolean newFile = false;

	public InspectionCrud(PersistenceManager persistenceManager, InspectionManager inspectionManager, User userManager, LegacyProductSerial legacyProductManager,
			CommentTemp commentTemplateManager, SafetyNetworkManager safetyNetworkManager, ProductManager productManager, InspectionScheduleManager inspectionScheduleManager) {
		super(persistenceManager);
		this.inspectionManager = inspectionManager;
		this.legacyProductManager = legacyProductManager;
		this.userManager = userManager;
		this.commentTemplateManager = commentTemplateManager;
		this.safetyNetworkManager = safetyNetworkManager;
		this.productManager = productManager;
		this.inspectionHelper = new InspectionHelper(persistenceManager);
		this.inspectionScheduleManager = inspectionScheduleManager;
	}

	@Override
	protected void initMemberFields() {
		inspection = new Inspection();
		inspection.setDate(new Date());
	}

	@Override
	protected void loadMemberFields(Long uniqueId) {
		inspection = inspectionManager.findAllFields(uniqueId, getSecurityFilter());

		if (inspection != null && !inspection.isActive()) {
			inspection = null;
		}

		if (inspection != null) {
			inspectionGroup = inspection.getGroup();
			inspectionScheduleOnInspection = (inspection.getSchedule() != null);
		}
	}
	
	@Override
	protected void postInit() {
		super.postInit();
		ownerPicker = new OwnerPicker(getLoaderFactory().createFilteredIdLoader(BaseOrg.class), inspection);
	}
	

	protected void testDependices() throws MissingEntityException {
		if (product == null) {
			addActionError(getText("error.noproduct"));
			throw new MissingEntityException();
		}

		if (inspection == null) {
			addActionError(getText("error.noinspection"));
			throw new MissingEntityException();
		}

		if (inspection.getType() == null) {
			addActionError(getText("error.inspectiontype"));
			throw new MissingEntityException();
		}
	}

	@SkipValidation
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
	public String doAdd() {
		testDependices();

		// set defaults.
		productStatus = product.getProductStatus();
		inspection.setOwner(product.getOwner());
		inspection.setLocation(product.getLocation());
		inspection.setDate(DateHelper.getTodayWithTime());
		setInspector(getSessionUser().getUniqueID());
		inspection.setPrintable(inspection.getType().isPrintable());
		setUpSupportedProofTestTypes();

		
		// get the next inspection date default.
		ProductTypeSchedule schedule = product.getType().getSchedule(inspection.getType(), product.getOwner());
		if (schedule != null) {
			Date nextDate = schedule.getNextDate(DateHelper.getToday());
			nextInspectionDate = convertDate(nextDate);
		}
		
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

		return SUCCESS;
	}

	private void suggestSchedule() {
		if (inspectionScheduleOnInspection == false && inspectionSchedule == null) {
			setScheduleId(new InspectionScheduleSuggestion(getAvailableSchedules()).getSuggestedScheduleId());
			scheduleSuggested = (inspectionSchedule != null);
			
		}
	}

	@SkipValidation
	public String doShowLinked() {
		// TODO: the display for this should be different than for a normal one.
		// inspector should be the company not the individual. other information
		// may need to be hidden.
		if (product == null) {
			addActionError(getText("error.noproduct"));
			return MISSING;
		}

		try {
			inspection = safetyNetworkManager.findLinkedProductInspection(product, uniqueID);

			testDependices();
		} catch (MissingEntityException e) {
			return MISSING;
		} catch (NullPointerException e) {
			return MISSING;
		} catch (InvalidQueryException e) {
			logger.error("Invalid Query while finding linked product inspections", e);
			return ERROR;
		}

		return SUCCESS;
	}

	@SkipValidation
	public String doShow() {
		try {
			product = inspection.getProduct();
			testDependices();
		} catch (NullPointerException e) {
			return MISSING;
		}

		inspectionGroup = inspection.getGroup();
		return SUCCESS;
	}

	@SkipValidation
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

	public String doSave() {
		try {
			testDependices();
		} catch (MissingEntityException e) {
			return MISSING;
		}

		try {
			// get the user to set modifiedBy's later
			UserBean modifiedBy = fetchCurrentUser();
			
			findInspectionBook();

			inspection.setInfoOptionMap(decodeMapKeys(encodedInfoOptionMap));

			inspection.setGroup(inspectionGroup);
			inspection.setTenant(getTenant());
			inspection.setProduct(product);
			inspection.setDate(convertDateTime(inspectionDate));

			// setup the next inspection date.
			Date nextInspection = null;
			if (nextInspectionDate != null && nextInspectionDate.length() > 0) {
				nextInspection = convertDate(nextInspectionDate);
			}

			processProofTestFile();
			
			if (inspection.isNew()) {
				// the criteriaResults from the form must be processed before setting them on the inspection
				inspectionHelper.processFormCriteriaResults(inspection, criteriaResults, modifiedBy);
				
				// ensure the form version is set from the inspection type on create
				inspection.setFormVersion(inspection.getType().getFormVersion());
				
				// it's save time
				inspection = inspectionManager.createInspection(inspection, productStatus, nextInspection, getSessionUser().getUniqueID(), fileData, getUploadedFiles());
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

	
	protected void findInspectionBook() throws ValidationException, PersistenceException {
		if (newInspectionBookTitle != null) {
			if (newInspectionBookTitle.trim().length() == 0) {
				addFieldError("newInspectionBookTitle", getText("error.inspection_book_title_required"));
				throw new ValidationException("not validated.");
			}
			InspectionBookCrud bookCrud = new InspectionBookCrud(persistenceManager, inspectionManager);
			try {
				bookCrud.prepare();
			} catch (Exception e) {
				addActionErrorText("error.new_saving_inspection_book");
				throw new PersistenceException("could not save.");
			}
			bookCrud.setOwner(getOwner());
			bookCrud.setName(newInspectionBookTitle);
			
			
			if (bookCrud.duplicateValueExists(newInspectionBookTitle)) {
				inspection.setBook(inspectionManager.findInspectionBook(newInspectionBookTitle.trim(), getSecurityFilter()));
			} else {
				if (bookCrud.doSave().equals(SUCCESS)) {
					inspection.setBook(bookCrud.getBook());
				} else {
					addActionErrorText("error.new_saving_inspection_book");
					throw new PersistenceException("could not save.");
				}
			}
			
			
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
			fileData = inspectionManager.createFileDataContainer(inspection, proofTest);
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

	@SkipValidation
	public String doDelete() {
		try {
			testDependices();
		} catch (MissingEntityException e) {
			return MISSING;
		}
		try {
			inspectionManager.retireInspection(inspection, getSessionUser().getUniqueID());
		} catch (Exception e) {
			addFlashError(getText("error.inspectiondeleting"));
			logger.error("inspection retire " + product.getSerialNumber(), e);
			return ERROR;
		}

		addFlashMessage(getText("message.inspectiondeleted"));
		return SUCCESS;
	}
	
	public BaseOrg getOwner() {
		return inspection.getOwner();
	}
	
	public void setOwner(BaseOrg owner) {
		inspection.setOwner(owner);
	}

	public String getLocation() {
		return inspection.getLocation();
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

	public void setLocation(String location) {
		inspection.setLocation(location);
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

	public String getNextInspectionDate() {
		return nextInspectionDate;
	}

	@CustomValidator(type = "n4systemsDateValidator", message = "", key = "error.mustbeadate")
	public void setNextInspectionDate(String nextInspectionDate) {
		this.nextInspectionDate = nextInspectionDate;
	}

	public List<ListingPair> getInspectors() {
		if (inspectors == null) {
			inspectors = userManager.getInspectorList(getSecurityFilter());
		}
		return inspectors;
	}

	public List<ListingPair> getUsers() {
		if (inspectors == null) {
			inspectors = userManager.getUserList(getSecurityFilter());
		}
		return inspectors;
	}

	public List<ProductStatusBean> getProductStatuses() {
		if (productStatuses == null) {
			productStatuses = legacyProductManager.getAllProductStatus(getTenantId());
		}
		return productStatuses;
	}

	public List<ListingPair> getCommentTemplates() {
		if (commentTemplates == null) {
			commentTemplates = commentTemplateManager.findCommentTemplatesLP(getTenantId());
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
		return (productStatus != null) ? productStatus.getUniqueID() : null;
	}

	public void setProductStatus(Long productStatus) {
		if (productStatus == null) {
			this.productStatus.setUniqueID(null);
		} else if (this.productStatus == null || !productStatus.equals(this.productStatus.getUniqueID())) {
			this.productStatus = legacyProductManager.findProductStatus(productStatus, getTenantId());
		}
	}

	public Long getInspector() {
		return (inspection.getInspector() != null) ? inspection.getInspector().getUniqueID() : null;
	}

	public void setInspector(Long inspector) {
		if (inspector == null) {
			inspection.setInspector(null);
		} else if (inspection.getInspector() == null || !inspector.equals(inspection.getInspector())) {
			inspection.setInspector(userManager.findUser(inspector, getTenantId()));
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

	public String getInspectionDate() {
		if (inspectionDate == null) {
			inspectionDate = convertDateTime(inspection.getDate());
		}
		return inspectionDate;
	}

	@RequiredStringValidator(message = "", key = "error.mustbeadate")
	@CustomValidator(type = "n4systemsDateValidator", message = "", key = "error.mustbeadate", parameters = { @ValidationParameter(name = "usingTime", value = "true") })
	public void setInspectionDate(String inspectionDate) {
		this.inspectionDate = inspectionDate;
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
			inspectionBooks = inspectionManager.findAvailableInspectionBooksLP(getSecurityFilter(), (uniqueID != null), getOwner());
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

	public List<CriteriaSection> getAvailableSections(AbstractInspection inspection) {
		if (availableSections.get(inspection) == null) {
			availableSections.put(inspection, new ArrayList<CriteriaSection>());
			getVisibleResults(inspection);

			if (!inspection.getType().getSections().isEmpty()) {
				for (CriteriaSection section : inspection.getType().getSections()) {
					if (!sections.get(inspection).isEmpty()) {
						if (sections.get(inspection).containsKey(section)) {
							availableSections.get(inspection).add(section);
						}
					} else if (inspection.isNew()) {
						if (!section.isRetired()) {
							availableSections.get(inspection).add(section);
						}
					}
				}
			}
		}
		currentCriteriaSections = availableSections.get(inspection);
		return availableSections.get(inspection);
	}

	public Map<CriteriaSection, List<CriteriaResult>> getVisibleResults(AbstractInspection inspection) {
		if (sections.get(inspection) == null) {
			sections.put(inspection, new HashMap<CriteriaSection, List<CriteriaResult>>());
			if (!inspection.getType().getSections().isEmpty() && !inspection.getResults().isEmpty()) {
				for (CriteriaSection section : inspection.getType().getSections()) {
					List<CriteriaResult> results = new ArrayList<CriteriaResult>();
					for (Criteria criteria : section.getCriteria()) {
						for (CriteriaResult criteriaResult : inspection.getResults()) {
							if (criteriaResult.getCriteria().equals(criteria)) {
								results.add(criteriaResult);
							}
						}
					}
					if (!results.isEmpty()) {
						sections.get(inspection).put(section, results);
					}
				}
			}
		}

		return sections.get(inspection);

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

	public List<CriteriaSection> getCurrentCriteriaSections() {
		return currentCriteriaSections;
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
			availableSchedules = inspectionScheduleManager.getSchedulesInTimeFrame(product, inspection.getType(), inspection.getDate());
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
		return ownerPicker.getOwnerId();
	}

	public void setOwnerId(Long id) {
		ownerPicker.setOwnerId(id);
	}

	

}
