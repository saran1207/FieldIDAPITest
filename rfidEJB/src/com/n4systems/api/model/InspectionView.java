package com.n4systems.api.model;

import java.util.Date;

import com.n4systems.api.validation.validators.AssociatedInspectionTypeValidator;
import com.n4systems.api.validation.validators.DateValidator;
import com.n4systems.api.validation.validators.FullNameUserValidator;
import com.n4systems.api.validation.validators.InspectionStatusValidator;
import com.n4systems.api.validation.validators.NotNullValidator;
import com.n4systems.api.validation.validators.OwnerExistsValidator;
import com.n4systems.api.validation.validators.ProductIdentifierValidator;
import com.n4systems.api.validation.validators.ProductStatusExistsValidator;
import com.n4systems.api.validation.validators.YNValidator;
import com.n4systems.exporting.beanutils.ExportField;
import com.n4systems.exporting.beanutils.OwnerSerializationHandler;

public class InspectionView extends ExternalModelView {
	private static final long serialVersionUID = 1L;

	@ExportField(title = "Asset Identifier", order = 100, validators = { NotNullValidator.class, ProductIdentifierValidator.class, AssociatedInspectionTypeValidator.class })
	private String identifier;

	@ExportField(title = "Date Performed", order = 200, validators = { NotNullValidator.class, DateValidator.class })
	private Object inspectionDate;

	@ExportField(title = "Inspection Result (Pass, Fail, N/A)", order = 300, validators = { NotNullValidator.class, InspectionStatusValidator.class })
	private String status;

	@ExportField(title = "", order = 400, handler = OwnerSerializationHandler.class, validators = { NotNullValidator.class, OwnerExistsValidator.class })
	private final String[] owners = new String[3];

	@ExportField(title = "Performed By", order = 500, validators = { NotNullValidator.class, FullNameUserValidator.class })
	private String performedBy;

	@ExportField(title = "Inspection Book", order = 600)
	private String inspectionBook;

	@ExportField(title = "Printable (Y/N)", order = 700, validators = { YNValidator.class })
	private String printable;

	@ExportField(title = "Product Status", order = 800, validators = { ProductStatusExistsValidator.class })
	private String productStatus;

	@ExportField(title = "Next Inspection Date", order = 900, validators = { DateValidator.class })
	private Object nextInspectionDate;

	@ExportField(title = "Location", order = 1000)
	private String location;	
	
	@ExportField(title = "Comments", order = 1100)
	private String comments;

	public InspectionView() {}

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public Object getInspectionDate() {
		return inspectionDate;
	}

	public Date getInspectionDateAsDate() {
		if (inspectionDate == null) {
			return null;
		} else if (!(inspectionDate instanceof Date)) {
			throw new ClassCastException("inspectionDate should have been instance of java.lang.Date but was " + inspectionDate.getClass().getName());
		}

		return (Date)inspectionDate;
	}
	
	public void setInspectionDate(Object inspectionDate) {
		this.inspectionDate = inspectionDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getPerformedBy() {
		return performedBy;
	}

	public void setPerformedBy(String performedBy) {
		this.performedBy = performedBy;
	}

	public String getInspectionBook() {
		return inspectionBook;
	}

	public void setInspectionBook(String inspectionBook) {
		this.inspectionBook = inspectionBook;
	}

	public String getPrintable() {
		return printable;
	}

	public void setPrintable(String printable) {
		this.printable = printable;
	}

	public void setPrintable(boolean printable) {
		this.printable = (printable) ? "Y" : "N";
	}
	
	public Boolean isPrintable() {
		if (printable == null) {
			return null;
		}
		return (printable.equalsIgnoreCase("Y"));
	}
	
	public String getProductStatus() {
		return productStatus;
	}

	public void setProductStatus(String productStatus) {
		this.productStatus = productStatus;
	}

	public Object getNextInspectionDate() {
		return nextInspectionDate;
	}

	public Date getNextInspectionDateAsDate() {
		if (nextInspectionDate == null) {
			return null;
		} else if (!(nextInspectionDate instanceof Date)) {
			throw new ClassCastException("nextInspectionDate should have been instance of java.lang.Date but was " + nextInspectionDate.getClass().getName());
		}
		return (Date)nextInspectionDate;
	}
	
	public void setNextInspectionDate(Object nextInspectionDate) {
		this.nextInspectionDate = nextInspectionDate;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public String[] getOwners() {
		return owners;
	}

	public void setOrganization(String organization) {
		owners[OwnerSerializationHandler.OWNER_ORGANIZATION] = organization;
	}
	
	public String getOrganization() {
		return owners[OwnerSerializationHandler.OWNER_ORGANIZATION];
	}
	
	public void setCustomer(String customer) {
		owners[OwnerSerializationHandler.OWNER_CUSTOMER] = customer;
	}
	
	public String getCustomer() {
		return owners[OwnerSerializationHandler.OWNER_CUSTOMER];
	}
	
	public void setDivision(String division) {
		owners[OwnerSerializationHandler.OWNER_DIVISION] = division;
	}
	
	public String getDivision() {
		return owners[OwnerSerializationHandler.OWNER_DIVISION];
	}
	
	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	@Override
	public String getGlobalId() {
		return null;
	}

	@Override
	public void setGlobalId(String globalId) {}

}
