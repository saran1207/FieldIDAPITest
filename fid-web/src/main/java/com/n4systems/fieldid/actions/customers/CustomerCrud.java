package com.n4systems.fieldid.actions.customers;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.fieldid.actions.api.AbstractCrud;
import com.n4systems.fieldid.permissions.UserPermissionFilter;
import com.n4systems.fieldid.service.amazon.S3Service;
import com.n4systems.fieldid.service.org.PlaceService;
import com.n4systems.model.AddressInfo;
import com.n4systems.model.Contact;
import com.n4systems.model.api.Listable;
import com.n4systems.model.downloadlink.DownloadLink;
import com.n4systems.model.orgs.*;
import com.n4systems.model.orgs.customer.CustomerOrgArchiver;
import com.n4systems.model.orgs.customer.CustomerOrgListLoader;
import com.n4systems.model.user.User;
import com.n4systems.model.user.UserPaginatedLoader;
import com.n4systems.model.user.UserSaver;
import com.n4systems.reporting.PathHandler;
import com.n4systems.security.Permissions;
import com.n4systems.tools.Pager;
import com.n4systems.util.ListHelper;
import com.n4systems.util.ListingPair;
import com.opensymphony.xwork2.validator.annotations.*;
import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.springframework.beans.factory.annotation.Autowired;
import rfid.web.helper.Constants;

import java.io.File;
import java.net.URL;
import java.util.List;

@Deprecated
@Validation
@UserPermissionFilter(userRequiresOneOf={Permissions.MANAGE_END_USERS})
public class CustomerCrud extends AbstractCrud {
	private static final long serialVersionUID = 1L;
	private static final int CRUD_RESULTS_PER_PAGE = 20;
	private static Logger logger = Logger.getLogger(CustomerCrud.class);

    private static final String CUSTOMER_OWNS_ASSETS = "This Job Site is still listed as the Owner to one or more Assets.  You must change the Owner of these Assets before Archiving!";
    private static final String CUSTOMER_ARCHIVE_WARNING = "Are you sure you want to archive this? All associated users will be removed, which cannot be undone by unarchiving this.";

	private final OrgSaver saver;
	
	private boolean archivedOnly;
	
	private CustomerOrg customer;

	private Pager<CustomerOrg> customerPage;
	private String nameFilter;
	private String idFilter;
	private Long orgFilter;
	private List<ListingPair> internalOrgList;
	private DownloadLink downloadLink;
	private String reportName;
	
	private File logo;
	private String logoImageDirectory;
	private boolean removeImage = false;
	private boolean newImage = false;
    private boolean customerOwnsAssets = false;

    @Autowired
    private S3Service s3Service;

    //Do these warnings matter, or are they here simply because we're shoehorning Struts 2 into Spring 4.2??
    @Autowired
    private PlaceService placeService;
	
	public CustomerCrud(PersistenceManager persistenceManager) {
		super(persistenceManager);
		this.saver = new OrgSaver();
	}
	
	@Override
	protected void initMemberFields() {
		customer = new CustomerOrg();
		customer.setAddressInfo(new AddressInfo());
		customer.setTenant(getTenant());
		customer.setParent(getSessionUserOwner().getInternalOrg());
	}

	@Override
	protected void loadMemberFields(Long uniqueId) {
		customer = getLoaderFactory().createEntityByIdLoader(CustomerOrg.class).setId(uniqueId).setPostFetchFields("createdBy", "modifiedBy").load();
		if (customer.getAddressInfo() == null) {
			customer.setAddressInfo(new AddressInfo());
		}
		if (customer.getContact() == null) {
			customer.setContact(new Contact());
		}
	}

	@SkipValidation
	public String doList() {
		archivedOnly = false;
		setPageType("customer", "list");
		return SUCCESS;
	}

	@SkipValidation
	public String doListArchived() {
		archivedOnly = true;
		setPageType("customer", "list_archived");
		return SUCCESS;
	}

	private String logoImageExists() {
		if (customer.getId() == null) {
			return null;
		}

        if (!s3Service.customerLogoExists(customer.getId())) {
            return null;
        }

		URL logoUrl = s3Service.getCustomerLogoURL(customer.getId());
		return logoUrl.toString();
	}
	
	@SkipValidation
	public String doLoadEdit() {
		if (customer == null) {
			addActionError("Customer not found");
			return ERROR;
		}

		logoImageDirectory = logoImageExists();
		
		return INPUT;
	}
	
	@SkipValidation
	public String doEditQuickSetupWizard() {
		if (customer == null) {
			addActionError("Customer not found");
			return ERROR;
		}

		return INPUT;
	}
	
	@SkipValidation
	public String doSaveQuickSetupWizard() {
		if(customer.getName() != null && !customer.getName().isEmpty()) {
			return doSave();
		} else {
			return SUCCESS;
			}
	}
	
	@SkipValidation
	public String doShow() {
		if (customer == null || customer.getId() == null) {
			addActionError("Customer not found");
			return ERROR;
		}

		logoImageDirectory = logoImageExists();

		return SUCCESS;
	}

	@SkipValidation
	public String doArchive() {
		String result = setCustomerActive(false);
		if (SUCCESS.equals(result)) addFlashMessage("Archive successful");
		return result;
	}
	
	@SkipValidation
	public String doUnarchive() {
		String result = setCustomerActive(true);
		if (SUCCESS.equals(result)) addFlashMessage("Unarchive successful");
		return result;
	}

	private String setCustomerActive(boolean active) {

		if (customer == null) {
			addFlashError("Customer not found");
			return ERROR;
		}

		// if the address info was created by our loadMemberFields, 
		// we need to nullify it or it'll screw with the delete process
		if (customer.getAddressInfo().isNew()) {
			customer.setAddressInfo(null);
		}

		try {
			
			CustomerOrgArchiver archiver = new CustomerOrgArchiver();
			archiver.archiveCustomer(customer, saver, new UserSaver(), getLoaderFactory(), getSecurityFilter(), active);
			
		} catch (Exception e) {
			logger.error("Failed updating customer", e);
			addFlashErrorText("error.updatingcustomer");
			return ERROR;
		}
		
		return SUCCESS;
	}

	public String doSave() {
		try {
			if (customer.getTenant() == null) {
				customer.setTenant(getTenant());
			}
            customer.touch();
			saver.saveOrUpdate(customer);
            processImage();
			addFlashMessage(getText("message.saved"));
			uniqueID = customer.getId();
		} catch (Exception e) {
			logger.error(getText("error.savingcustomer"), e);
			addActionError(getText("error.savingcustomer"));
			return ERROR;
		}
		
		return SUCCESS;
	}
	
	private void processImage() {
		if (removeImage) {
            s3Service.removeCustomerLogo(customer.getId());
		}
		if (newImage == true && logoImageDirectory != null && logoImageDirectory.length() != 0) {
            File uploadedImage = new File(PathHandler.getTempRoot(), logoImageDirectory);
            s3Service.uploadCustomerLogo(uploadedImage, customer.getId());
            uploadedImage.delete();
		}
	}
	
	
	@SkipValidation
	public String doShowImportExport() {
		return SUCCESS;
	}
	
	@SkipValidation
	public String doExport() {
		try {
			reportName = getText("label.export_file.customer");
			downloadLink = getDownloadCoordinator().generateCustomerExport(reportName, getDownloadLinkUrl(), createCustomerOrgListLoader(), getSecurityFilter());
		} catch (RuntimeException e) {
			logger.error("Unable to execute customer export", e);
			addFlashMessage(getText("error.export_failed.customer"));
			return ERROR;
		}
		return SUCCESS;
	}
	
	protected CustomerOrgListLoader createCustomerOrgListLoader() {
		return getLoaderFactory().createCustomerOrgListLoader().withoutLinkedOrgs().setPostFetchFields("modifiedBy", "createdBy");
	}
	
	public Pager<CustomerOrg> getPage() {
		
		
		if (customerPage == null) {
			CustomerOrgPaginatedLoader loader = getLoaderFactory().createCustomerOrgPaginatedLoader();
			loader.setPostFetchFields("modifiedBy", "createdBy");
			loader.setPage(getCurrentPage()).setPageSize(CRUD_RESULTS_PER_PAGE);
			loader.setNameFilter(nameFilter);
			loader.setIdFilter(idFilter);
			loader.setOrgFilter(orgFilter);
			loader.setArchivedOnly(archivedOnly);
			try{
			customerPage = loader.load();
			} catch (Exception e) {
				logger.error("error loading customers", e);
			}
		}
		
		return customerPage;
	}

	public int getDivisionCount() {
		DivisionOrgsForCustomerOrgLoader loader = new DivisionOrgsForCustomerOrgLoader(getSecurityFilter());
		return loader.parent(customer).load().size();
	}
	
	public long getUserCount() {
		Pager<User> page = new UserPaginatedLoader(getSecurityFilter())
		.withCustomer(customer)
		.setPage(1)
		.setPageSize(Constants.PAGE_SIZE)
		.load();
		
		return page.getTotalResults();
	}
	
	public String getCustomerName() {
		return customer.getName();
	}

	@RequiredStringValidator(type = ValidatorType.FIELD, message = "", key = "error.customernamerequired")
	public void setCustomerName(String customerName) {
		customer.setName(customerName);
	}

	public String getCustomerId() {
		return customer.getCode();
	}

	@RequiredStringValidator(type = ValidatorType.FIELD, message = "", key = "error.customeridrequired")
	public void setCustomerId(String customerId) {
		customer.setCode(customerId);
	}

	public String getAccountManagerEmail() {
		return customer.getContact().getEmail();
	}

    @CustomValidator(type = "rfcEmailValidator", message = "", key = "error.emailformat")
    public void setAccountManagerEmail(String accountManagerEmail) {
		customer.getContact().setEmail(accountManagerEmail);
	}

	public String getContactName() {
		return customer.getContact().getName();
	}

	public void setContactName(String contactName) {
		customer.getContact().setName(contactName);
	}

	public AddressInfo getAddressInfo() {
		return customer.getAddressInfo();
	}

	public void setAddressInfo(AddressInfo addressInfo) {
		customer.setAddressInfo(addressInfo);
	}

	public void setParentOrgId(Long parent) {
		customer.setParent((InternalOrg)getLoaderFactory().createFilteredIdLoader(BaseOrg.class).setId(parent).load());
	}
	
	public Long getParentOrgId() {
		return customer.getParent().getId();
	}
	
	public String getNameFilter() {
		return nameFilter;
	}

	public void setNameFilter(String nameFilter) {
		this.nameFilter = nameFilter;
	}
	
	public List<ListingPair> getParentOrgs() {
		if( internalOrgList == null ) {
			List<Listable<Long>> orgListables = getLoaderFactory().createInternalOrgListableLoader().load();
			internalOrgList = ListHelper.longListableToListingPair(orgListables);
		}
		return internalOrgList;
	}
	
	public CustomerOrg getCustomer() {
		return customer;
	}
	
	public String getFilterAction() {
		if (archivedOnly) {
			return "archivedCustomerList";
		} else {
			return "customerList";
		}
	}
	
	public void setArchivedOnly(boolean archivedOnly) {
		this.archivedOnly = archivedOnly;
	}

	public String getIdFilter() {
		return idFilter;
	}

	public void setIdFilter(String idFilter) {
		this.idFilter = idFilter;
	}

	public Long getOrgFilter() {
		return orgFilter;
	}

	public void setOrgFilter(Long orgFilter) {
		this.orgFilter = orgFilter;
	}

	public DownloadLink getDownloadLink() {
		return downloadLink;
	}

	public String getReportName() {
		return reportName;
	}
	
	public String getCustomerNotes() {
		return customer.getNotes();
	}

	@StringLengthFieldValidator(type = ValidatorType.FIELD, message = "", key = "error.notes_length", maxLength = "1000")
	public void setCustomerNotes(String notes) {
		customer.setNotes(notes);
	}

	public File getLogo() {
		return logo;
	}
	
	@CustomValidator(type = "fileSizeValidator", message = "", key = "errors.file_too_large", parameters = { @ValidationParameter(name = "fileSize", value = "524288") })
	public void setLogo(File logo) {
		this.logo = logo;
	}

	public String getLogoImageDirectory() {
		return logoImageDirectory;
	}

	public void setLogoImageDirectory(String logoImageDirectory) {
		this.logoImageDirectory = logoImageDirectory;
	}

	public boolean isRemoveImage() {
		return removeImage;
	}

	public void setRemoveImage(boolean removeImage) {
		this.removeImage = removeImage;
	}

	public boolean isNewImage() {
		return newImage;
	}

	public void setNewImage(boolean newImage) {
		this.newImage = newImage;
	}

    public String getAppropriateJSValue(Long customerId) {
        Long assetCount = placeService.getAssetCount(customerId);
        if(assetCount != null && assetCount > 0) {
            return "alert('" + CUSTOMER_OWNS_ASSETS + "'); return false;";
        } else {
            return "return confirm('" + CUSTOMER_ARCHIVE_WARNING + "');";
        }
    }
}
