package com.n4systems.fieldid.viewhelpers;

import java.util.Date;

import com.n4systems.model.Inspection;
import com.n4systems.model.SavedReport;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.reporting.ReportDefiner;
import com.n4systems.util.DateHelper;
import com.n4systems.util.persistence.search.SortTerm;


public class InspectionSearchContainer extends SearchContainer implements ReportDefiner {
	private static final long serialVersionUID = 1L;
	private static final String[] joinColumns = {"book", "product.shopOrder.order", "product.productStatus", "product.identifiedBy"};
	
	private Long savedReportId;
	private boolean savedReportModified;
	private String rfidNumber;
	private String serialNumber;
	private String orderNumber;
	private String purchaseOrder;
	private String referenceNumber;
	private String location;
	private BaseOrg owner;
	private Long productTypeId;
	private Long productStatusId;
	private Long assignedUserId;
	private Long inspectionTypeGroupId;
	private Long inspectorId;
	private Long inspectionBookId;
	private Long jobId;
	private Date fromDate;
	private Date toDate;
	
	public InspectionSearchContainer(SecurityFilter filter) {
		super(Inspection.class, "id", filter, joinColumns);
	}
	
	public InspectionSearchContainer(SecurityFilter filter, SavedReport report) {
		this(filter);
		setFromSavedReport(report);
	}

	@Override
	protected void evalSearchTerms() {
		addStringTerm("product.rfidNumber", rfidNumber);
		addWildcardTerm("product.serialNumber", serialNumber);
		addStringTerm("product.shopOrder.order.orderNumber", orderNumber);
		addStringTerm("product.purchaseOrder", purchaseOrder);
		addStringTerm("product.customerRefNumber", referenceNumber);
		addWildcardTerm("location", location);
		addSimpleTerm("product.type.id", productTypeId);
		addSimpleTerm("product.productStatus.uniqueID", productStatusId);
		addSimpleTerm("product.assignedUser.uniqueID", assignedUserId);
		addSimpleTerm("type.group.id", inspectionTypeGroupId);
		addSimpleTerm("inspector.uniqueID", inspectorId);
		addSimpleTerm("schedule.project.id", jobId);
		
		// when inspectionBookId is 0, we search for inspections not in a book
		if(inspectionBookId != null && inspectionBookId == 0) {
			addNullTerm("book.id");
		} else {
			addSimpleTerm("book.id", inspectionBookId);
		}
		
		addDateRangeTerm("date", fromDate, toDate);
	}
	
	@Override
	protected void evalSearchFilters() {
		addOwnerFilter(getOwner());
	}
	
	/**
	 * @return This container converted to a {@link SavedReport}
	 */
	public SavedReport toSavedReport() {
		SavedReport report =  new SavedReport();
		toSavedReport(report);
		
		return report;
	}
	
	/**
	 * Sets the {@link SavedReport} fields from this container
	 * @param report	The report to set fields in
	 */
	public void toSavedReport(SavedReport report) {
		report.setSortColumn(getSortColumn());
		report.setSortDirection(getSortDirection());
		report.setColumns(getSelectedColumns());
		
		report.getCriteria().clear();
		
		report.setInCriteria(SavedReport.PURCHASE_ORDER_NUMBER, getPurchaseOrder());
		report.setInCriteria(SavedReport.ORDER_NUMBER, getOrderNumber());
		report.setInCriteria(SavedReport.RFID_NUMBER, getRfidNumber());
		report.setInCriteria(SavedReport.SERIAL_NUMBER, getSerialNumber());
		report.setInCriteria(SavedReport.OWNER_ID, getOwnerId());
		report.setInCriteria(SavedReport.REFERENCE_NUMBER, getReferenceNumber());
		report.setInCriteria(SavedReport.INSPECTION_BOOK, getInspectionBook());
		report.setInCriteria(SavedReport.INSPECTION_TYPE_GROUP, getInspectionTypeGroup());
		report.setInCriteria(SavedReport.INSPECTOR, getInspector());
		report.setInCriteria(SavedReport.PRODUCT_STATUS, getProductStatus());
		report.setInCriteria(SavedReport.PRODUCT_TYPE, getProductType());
		report.setInCriteria(SavedReport.ASSINGED_USER, getAssingedUser());
		report.setInCriteria(SavedReport.JOB_ID, getJob());
		report.setInCriteria(SavedReport.LOCATION, location);

		if (getFromDate() != null) {
			report.setInCriteria(SavedReport.FROM_DATE, DateHelper.date2String(SavedReport.DATE_FORMAT, getFromDate()));
		}
		
		if (getToDate() != null) {
			report.setInCriteria(SavedReport.TO_DATE, DateHelper.date2String(SavedReport.DATE_FORMAT, getToDate()));
		}
	}
	
	/**
	 * Sets the fields of this container from a {@link SavedReport}
	 * @param report	The SavedReport to set fields from
	 */
	public void setFromSavedReport(SavedReport report) {
		setSelectedColumns(report.getColumns());
		setSortColumn(report.getSortColumn());
		setSortDirection(report.getSortDirection());
		
		setPurchaseOrder(report.getStringCriteria(SavedReport.PURCHASE_ORDER_NUMBER));
		setOrderNumber(report.getStringCriteria(SavedReport.ORDER_NUMBER));
		setRfidNumber(report.getStringCriteria(SavedReport.RFID_NUMBER));
		setSerialNumber(report.getStringCriteria(SavedReport.SERIAL_NUMBER));
		setReferenceNumber(report.getStringCriteria(SavedReport.REFERENCE_NUMBER));
		setLocation(report.getStringCriteria(SavedReport.LOCATION));
		//FIXME:  this must load differently.
		//setOwner(report.getLongCriteria(SavedReport.OWNER_ID));
		setInspectionBook(report.getLongCriteria(SavedReport.INSPECTION_BOOK));
		setInspectionTypeGroup(report.getLongCriteria(SavedReport.INSPECTION_TYPE_GROUP));
		setInspector(report.getLongCriteria(SavedReport.INSPECTOR));
		setAssingedUser(report.getLongCriteria(SavedReport.ASSINGED_USER));
		setProductStatus(report.getLongCriteria(SavedReport.PRODUCT_STATUS));
		setProductType(report.getLongCriteria(SavedReport.PRODUCT_TYPE));
		setJob(report.getLongCriteria(SavedReport.JOB_ID));
		
		setFromDate(DateHelper.string2Date(SavedReport.DATE_FORMAT, report.getCriteria().get(SavedReport.FROM_DATE)));
		setToDate(DateHelper.string2Date(SavedReport.DATE_FORMAT, report.getCriteria().get(SavedReport.TO_DATE)));
		
		setSavedReportId(report.getId());
		setSavedReportModified(false);
	}
	
	@Override
	protected String defaultSortColumn() {
		return "date";
	}
	
	@Override
	protected SortTerm.Direction defaultSortDirection() {
		return SortTerm.Direction.DESC;
	}
	
	public String getRfidNumber() {
		return rfidNumber;
	}

	public void setRfidNumber(String rfidNumber) {
		this.rfidNumber = rfidNumber;
	}

	public String getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}
	
	
	public String getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}

	public Long getProductType() {
		return productTypeId;
	}

	public void setProductType(Long productTypeId) {
		this.productTypeId = productTypeId;
	}
	
	public Long getProductStatus() {
		return productStatusId;
	}
	
	public void setProductStatus(Long productStatusId) {
		this.productStatusId = productStatusId;
	}

	public Long getInspectionTypeGroup() {
		return inspectionTypeGroupId;
	}

	public void setInspectionTypeGroup(Long inspectionTypeGroupId) {
		this.inspectionTypeGroupId = inspectionTypeGroupId;
	}

	public Long getInspector() {
		return inspectorId;
	}

	public void setInspector(Long inspectorId) {
		this.inspectorId = inspectorId;
	}

	public Long getInspectionBook() {
		return inspectionBookId;
	}

	public void setInspectionBook(Long inspectionBookId) {
		this.inspectionBookId = inspectionBookId;
	}

	public String getPurchaseOrder() {
		return purchaseOrder;
	}

	public void setPurchaseOrder(String purchaseOrder) {
		this.purchaseOrder = purchaseOrder;
	}

	public Date getFromDate() {
		return fromDate;
	}

	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}

	public Date getToDate() {
		return toDate;
	}

	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}
	
	public Long getAssingedUser() {
		return assignedUserId;
	}
	
	public void setAssingedUser(Long assignedUserId) {
		this.assignedUserId = assignedUserId;
	}

	public boolean isFromSavedReport() {
		return savedReportId != null;
	}
	
	public Long getSavedReportId() {
		return savedReportId;
	}

	public void setSavedReportId(Long savedReportId) {
		this.savedReportId = savedReportId;
	}

	public boolean isSavedReportModified() {
		return savedReportModified;
	}

	public void setSavedReportModified(boolean savedReportModified) {
		this.savedReportModified = savedReportModified;
	}

	public Long getJob() {
		return jobId;
	}

	public void setJob(Long jobId) {
		this.jobId = jobId;
	}

	public String getReferenceNumber() {
		return referenceNumber;
	}

	public void setReferenceNumber(String referenceNumber) {
		this.referenceNumber = referenceNumber;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public BaseOrg getOwner() {
		return owner;
	}

	public void setOwner(BaseOrg owner) {
		this.owner = owner;
	}

	public Long getOwnerId() {
		return (owner != null) ? owner.getId() : null;
	}
	
}
