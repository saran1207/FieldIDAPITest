package com.n4systems.fieldid.actions.inspection;

import java.util.List;

import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.validation.SkipValidation;

import rfid.web.helper.Constants;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.exceptions.InvalidQueryException;
import com.n4systems.fieldid.actions.api.AbstractCrud;
import com.n4systems.fieldid.actions.helpers.MissingEntityException;
import com.n4systems.fieldid.actions.utils.OwnerPicker;
import com.n4systems.fieldid.validators.HasDuplicateValueValidator;
import com.n4systems.model.Inspection;
import com.n4systems.model.InspectionBook;
import com.n4systems.model.inspectionbook.InspectionBookListLoader;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.security.OpenSecurityFilter;
import com.n4systems.tools.Pager;
import com.n4systems.util.ListingPair;
import com.n4systems.util.persistence.QueryBuilder;
import com.opensymphony.xwork2.validator.annotations.CustomValidator;
import com.opensymphony.xwork2.validator.annotations.RequiredFieldValidator;
import com.opensymphony.xwork2.validator.annotations.RequiredStringValidator;

public class InspectionBookCrud extends AbstractCrud implements HasDuplicateValueValidator {

	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(InspectionBookCrud.class);


	private InspectionBook book;

	private boolean withClosed;
	private List<ListingPair> books;

	private Pager<InspectionBook> page;

	private OwnerPicker ownerPicker;
	
	public InspectionBookCrud(PersistenceManager persistenceManager) {
		super(persistenceManager);
	}

	@Override
	protected void initMemberFields() {
		book = new InspectionBook();
	}

	@Override
	protected void loadMemberFields(Long uniqueId) {
		book = persistenceManager.find(InspectionBook.class, uniqueId, getTenant());
	}
	
	

	@Override
	protected void postInit() {
		super.postInit();
		ownerPicker = new OwnerPicker(getLoaderFactory().createFilteredIdLoader(BaseOrg.class), book);
	}

	@SkipValidation
	public String doList() {
		QueryBuilder<InspectionBook> queryBuilder = new QueryBuilder<InspectionBook>(InspectionBook.class, new OpenSecurityFilter());

		queryBuilder.applyFilter(getSecurityFilter());
		queryBuilder.addOrder("name");
		try {
			page = persistenceManager.findAllPaged(queryBuilder, getCurrentPage(), Constants.PAGE_SIZE);
			return SUCCESS;
		} catch (InvalidQueryException iqe) {
			addActionErrorText("error.failedtoloadinspectionbooks");
			logger.error("couldn't load the list of inspection docs", iqe);
			return ERROR;
		}
	}

	@SkipValidation
	public String doLPList() {
		try {
			InspectionBookListLoader loader = new InspectionBookListLoader(getSecurityFilter());
			loader.setOpenBooksOnly(!withClosed);
			loader.setOwner(getOwner());
			books = loader.loadListingPair();
			return SUCCESS;
		} catch (Exception e) {
			addActionErrorText("error.failedtoloadinspectionbooks");
			logger.error("couldn't load the list of inspection docs", e);
			return ERROR;
		}
	}

	private void testDependencies() throws MissingEntityException {
		if (book == null) {
			addActionErrorText("error.noinspectionbook");
			throw new MissingEntityException();
		}
	}

	@SkipValidation
	public String doAdd() {
		try {
			testDependencies();
		} catch (MissingEntityException e) {
			return MISSING;
		}
		return SUCCESS;
	}

	@SkipValidation
	public String doEdit() {
		try {
			testDependencies();
		} catch (MissingEntityException e) {
			return MISSING;
		}
		return SUCCESS;
	}

	public String doSave() {
		try {
			testDependencies();
		} catch (MissingEntityException e) {
			return MISSING;
		}

		book.setTenant(getTenant());
		try {
			if (book.getId() == null) {
				uniqueID = persistenceManager.save(book, getSessionUser().getUniqueID());
			} else {
				book = persistenceManager.update(book, getSessionUser().getUniqueID());
			}
			addFlashMessageText("message.inspectionbooksaved");
		} catch (Exception e) {
			logger.error("failed to save inspection book", e);
			addActionErrorText("error.inspectionbooksavefailed");
			return ERROR;
		}

		return SUCCESS;
	}

	@SkipValidation
	public String doOpen() {
		try {
			testDependencies();
		} catch (MissingEntityException e) {
			return MISSING;
		}

		book.setOpen(true);

		try {
			book = persistenceManager.update(book, getSessionUser().getUniqueID());
			addFlashMessage(getText("message.inspectionbooksaved"));
		} catch (Exception e) {
			logger.error("failed to save inspection book", e);
			addActionError(getText("error.inspectionbooksavefailed"));
			return ERROR;
		}
		return SUCCESS;
	}

	@SkipValidation
	public String doClose() {
		try {
			testDependencies();
		} catch (MissingEntityException e) {
			return MISSING;
		}

		book.setOpen(false);

		try {
			book = persistenceManager.update(book, getSessionUser().getUniqueID());
			addFlashMessageText("message.inspectionbooksaved");
		} catch (Exception e) {
			logger.error("failed to save inspection book", e);
			addActionErrorText("error.inspectionbooksavefailed");
			return ERROR;
		}
		return SUCCESS;
	}

	@SkipValidation
	public String doDelete() {
		try {
			testDependencies();
		} catch (MissingEntityException e) {
			return MISSING;
		}

		QueryBuilder<Inspection> builder = new QueryBuilder<Inspection>(Inspection.class, new OpenSecurityFilter());

		builder.setCountSelect();
		builder.addSimpleWhere("book", book);
		try {
			if (persistenceManager.findCount(builder) > 0) {
				addActionErrorText("error.inspectionbookinuse");
				return ERROR;
			}

			persistenceManager.delete(book);
			addFlashMessageText("message.inspectionbookdeleted");
		} catch (Exception e) {
			logger.error("failed to save inspection book", e);
			addActionErrorText("error.inspectionbookdeletefailed");
			return ERROR;
		}
		return SUCCESS;
	}

	public InspectionBook getBook() {
		return book;
	}

	public Pager<InspectionBook> getPage() {
		return page;
	}
	
	public String getName() {
		return book.getName();
	}

	public boolean isOpen() {
		return book.isOpen();
	}

	@RequiredStringValidator(message = "", key = "error.titlerequired")
	@CustomValidator(type = "uniqueValue", message = "", key = "error.titleunique")
	public void setName(String name) {
		book.setName((name != null) ? name.trim() : name);
	}

	public void setOpen(boolean open) {
		book.setOpen(open);
	}
	
	public boolean duplicateValueExists(String formValue) {
		return !persistenceManager.uniqueNameAvailableWithCustomer(InspectionBook.class, formValue, uniqueID, getTenantId(), getOwnerId());
	}

	public void setWithClosed(boolean withClosed) {
		this.withClosed = withClosed;
	}

	public List<ListingPair> getBooks() {
		return books;
	}

	public Long getOwnerId() {
		return ownerPicker.getOwnerId();
	}

	public void setOwnerId(Long id) {
		ownerPicker.setOwnerId(id);
	}
	
	@RequiredFieldValidator(message="", key="error.owner_required")
	public BaseOrg getOwner() {
		return ownerPicker.getOwner();
	}
}
