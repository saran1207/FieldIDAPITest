package com.n4systems.util;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.n4systems.fieldid.tools.reports.ColumnDefinition;
import com.n4systems.fieldid.tools.reports.TableStructure;
import com.n4systems.model.Inspection;
import com.n4systems.model.api.Archivable.EntityState;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereParameter;

/**
 * Use {@link InspectionSearchContainer}
 */
@Deprecated
public class ReportCriteria extends AbstractSearchCriteria<Inspection> {
	private QueryBuilder<Inspection> queryBuilder;

	private String reportType;
	
	// XXX - the following two should be changed to work operate directly on the report builder
	// see note on setupOrderBy below
	private String direction;
	private String orderBy;
	
	public ReportCriteria(SecurityFilter filter, TableStructure reportColumns) {
		super(filter, reportColumns);
		queryBuilder = new QueryBuilder<Inspection>(Inspection.class, getSecurityFilter());
		
		// we don't want deleted inspections or archived.
		queryBuilder.addWhere(WhereParameter.Comparator.EQ, "inspection_retired", "state", EntityState.ACTIVE );
		
	}

	public String getRfidNumber() {
		return (String)queryBuilder.getWhereParameterValue("rfidNumber");
	}

	public void setRfidNumber(String rfidNumber) {
		queryBuilder.addWhere(WhereParameter.Comparator.EQ, "rfidNumber", getColumnMapping(ColumnDefinition.RfidNumber), blankValue(rfidNumber), WhereParameter.IGNORE_CASE);
	}

	public String getSerialNumber() {
		return (String)queryBuilder.getWhereParameterValue("serialNumber");
	}

	public void setSerialNumber(String serialNumber) {
		queryBuilder.addWhere(WhereParameter.Comparator.LIKE, "serialNumber", getColumnMapping(ColumnDefinition.SerialNumber), blankValue(serialNumber), WhereParameter.WILDCARD_RIGHT | WhereParameter.IGNORE_CASE);
	}

	public Long getCustomer() {
		return (Long)queryBuilder.getWhereParameterValue("customer");
	}

	public void setCustomer(Long customer) {
		queryBuilder.addWhere(WhereParameter.Comparator.EQ, "customer", "owner.customer_id", customer);
	}
	
	public Long getJobSite() {
		return (Long)queryBuilder.getWhereParameterValue("jobSite");
	}

	public void setJobSite(Long jobSite) {
		queryBuilder.addWhere(WhereParameter.Comparator.EQ, "jobSite", "jobSite.id", jobSite);
	}
	

	public Long getDivision() {
		return (Long)queryBuilder.getWhereParameterValue("division");
	}

	public void setDivision(Long division) {
		queryBuilder.addWhere(WhereParameter.Comparator.EQ, "division", "owner.division_id", division);
	}

	public String getOrderNumber() {
		return (String)queryBuilder.getWhereParameterValue("orderNumber");
	}

	public void setOrderNumber(String orderNumber) {
		queryBuilder.addWhere(WhereParameter.Comparator.LIKE, "orderNumber", getColumnMapping(ColumnDefinition.OrderNumber), blankValue(orderNumber), WhereParameter.WILDCARD_RIGHT | WhereParameter.IGNORE_CASE);
	}

	public Long getProductType() {
		return (Long)queryBuilder.getWhereParameterValue("productType");
	}

	public void setProductType(Long productType) {
		queryBuilder.addWhere(WhereParameter.Comparator.EQ, "productType", "product.type.id", productType);
	}
	
	public Long getProductStatus() {
		return (Long)queryBuilder.getWhereParameterValue("productStatus");
	}
	
	public void setProductStatus(Long productStatus) {
		queryBuilder.addWhere(WhereParameter.Comparator.EQ, "productStatus", "product.productStatus.uniqueID", productStatus);
	}

	public Long getInspectionTypeGroup() {
		return (Long)queryBuilder.getWhereParameterValue("inspectionTypeGroup");
	}

	public void setInspectionTypeGroup(Long inspectionType) {
		queryBuilder.addWhere(WhereParameter.Comparator.EQ, "inspectionTypeGroup", "type.group.id", inspectionType );
	}

	public Long getInspector() {
		return (Long)queryBuilder.getWhereParameterValue("inspector");
	}

	public void setInspector(Long inspector) {
		queryBuilder.addWhere(WhereParameter.Comparator.EQ, "inspector", "inspector.uniqueID", inspector);
	}

	public Long getInspectionBook() {
		return (Long)queryBuilder.getWhereParameterValue("inspectionBook");
	}

	public void setInspectionBook(Long inspectionBook) {
		//this handles the special case of inspections not in a book
		if(inspectionBook != null && inspectionBook == 0) {
			queryBuilder.addWhere(new WhereParameter<Long>(WhereParameter.Comparator.NULL, "inspectionBook", "book.id"));
		} else {
			queryBuilder.addWhere(WhereParameter.Comparator.EQ, "inspectionBook", "book.id", inspectionBook);
		}
	}

	public String getPurchaseOrder() {
		return (String)queryBuilder.getWhereParameterValue("purchaseOrder");
	}

	public void setPurchaseOrder(String purchaseOrder) {
		queryBuilder.addWhere(WhereParameter.Comparator.EQ, "purchaseOrder", getColumnMapping(ColumnDefinition.purchaseOrder), blankValue(purchaseOrder), WhereParameter.IGNORE_CASE);
	}

	public Date getFromDate() {
		return (Date)queryBuilder.getWhereParameterValue("fromDate");
	}

	public void setFromDate(Date fromDate) {
		queryBuilder.addWhere(WhereParameter.Comparator.GE, "fromDate", getColumnMapping(ColumnDefinition.InspectionDate), fromDate);
	}

	public Date getToDate() {
		return (Date)queryBuilder.getWhereParameterValue("toDate");
	}

	public void setToDate(Date toDate) {
		queryBuilder.addWhere(WhereParameter.Comparator.LE, "toDate", getColumnMapping(ColumnDefinition.InspectionDate), DateHelper.getEndOfDay(toDate));
	}
	
	public Long getAssingedUser() {
		return (Long)queryBuilder.getWhereParameterValue("assignedUser");
	}
	
	public void setAssingedUser(Long assignedUser) {
		queryBuilder.addWhere(WhereParameter.Comparator.EQ, "assignedUser", getColumnMapping(ColumnDefinition.AssignedUser), assignedUser);
	}
	
	public String getOrderBy() {
		return orderBy;
	}

	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}

	public String getDirection() {
		return direction;
	}

	public void setDirection(String direction) {
		this.direction = direction;
	}

	public String getReportType() {
		return reportType;
	}

	public void setReportType(String reportType) {
		this.reportType = reportType;
	}
	
	private void setupOrderBy() {
		String orderColumn;
		
		try {
			
			orderColumn = getColumnMapping(ColumnDefinition.valueOf(orderBy));
			
			// XXX - I don't really like this up/down/off thing, need something more concrete
			if(orderColumn != null && direction != null && !direction.equals("off")) {

				boolean ascending = true;
				if (direction.equals("up")) {
					ascending = false;
				}
				
				queryBuilder.setOrder(orderColumn, ascending);
					
			} else {
				// if the direction isn't set or set to off clear all orderby and use default
				queryBuilder.getOrderArguments().clear();
				queryBuilder.addOrder("owner.customer_id", "product.id");
			}
		} catch (Exception e) {
			// do nothing this will catch the exception of the enum valueOf on a
			// null or non existent type.
			queryBuilder.getOrderArguments().clear();
			queryBuilder.addOrder("owner.customer_id", "product.id");
		}
	}
	
	@Override
	public QueryBuilder<Inspection> getCountQueryBuilder() {
		return queryBuilder.setCountSelect();
	}

	@Override
	public QueryBuilder<Inspection> getSearchQueryBuilder() {
		setupOrderBy();
		
		queryBuilder.setSimpleSelect();
		queryBuilder.getJoinArguments().clear();
		queryBuilder.addLeftJoin("product.productStatus", null);
		queryBuilder.addLeftJoin("product.shopOrder", null);
		queryBuilder.addLeftJoin("division", null);
		queryBuilder.getPostFetchPaths().clear();
		queryBuilder.addPostFetchPaths("product.infoOptions");

		return queryBuilder;
	}

	@Override
	public QueryBuilder<Inspection> getIdQueryBuilder() {
		queryBuilder.setSimpleSelect("id");
		queryBuilder.getJoinArguments().clear();
		queryBuilder.getOrderArguments().clear();
		return queryBuilder;
	}

	@Override
	protected Map<ColumnDefinition, String> initColumnMap() {
		Map<ColumnDefinition, String> colMap = new HashMap<ColumnDefinition, String>();
		
		colMap.put(ColumnDefinition.Productname, 	"product.shopOrder.order.description");
		colMap.put(ColumnDefinition.EndUserName ,	"customer.name");
		colMap.put(ColumnDefinition.InspectionDate,	"date");
		colMap.put(ColumnDefinition.ProductStatus,	"product.productStatus.name");
		colMap.put(ColumnDefinition.InspectionType,	"type.name");
		colMap.put(ColumnDefinition.Division,		"division.name");
		colMap.put(ColumnDefinition.SerialNumber,	"product.serialNumber");
		colMap.put(ColumnDefinition.ReelID,			"product.serialNumber");
		colMap.put(ColumnDefinition.Description,	"product.description");
		colMap.put(ColumnDefinition.Result,			"status");
		colMap.put(ColumnDefinition.Location,		"location");
		colMap.put(ColumnDefinition.ProductType,	"product.type.name");
		colMap.put(ColumnDefinition.Charge,			"charge");
		colMap.put(ColumnDefinition.Comments,		"comments");
		colMap.put(ColumnDefinition.purchaseOrder,	"product.purchaseOrder");
		colMap.put(ColumnDefinition.OrderNumber,	"product.shopOrder.order.orderNumber");
		colMap.put(ColumnDefinition.RfidNumber,		"product.rfidNumber");
		colMap.put(ColumnDefinition.PartNumber,		"product.getProductExtensionValue(\"partnumber\")");
		colMap.put(ColumnDefinition.AssignedUser,   "product.assignedUser.id");
		
		return colMap;
	}

}
