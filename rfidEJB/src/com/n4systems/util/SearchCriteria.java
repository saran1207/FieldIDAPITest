package com.n4systems.util;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.n4systems.fieldid.tools.reports.ColumnDefinition;
import com.n4systems.fieldid.tools.reports.TableStructure;
import com.n4systems.model.Product;
import com.n4systems.model.api.Archivable.EntityState;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereParameter;

/**
 * Use {@link ProductSearchContainer}
 */
@Deprecated
public class SearchCriteria extends AbstractSearchCriteria<Product> {
	private QueryBuilder<Product> queryBuilder;
	
	private String direction;
	private String orderBy;
	
	public SearchCriteria(SecurityFilter filter, TableStructure reportStructure) {
		super(filter, reportStructure);
		queryBuilder = new QueryBuilder<Product>(Product.class, getSecurityFilter());
		// we don't want deleted inspections
		queryBuilder.addWhere(WhereParameter.Comparator.EQ, "product_retired", "state", EntityState.ACTIVE );
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
		queryBuilder.addWhere(WhereParameter.Comparator.LIKE, "serialNumber", getColumnMapping(ColumnDefinition.SerialNumber), blankValue(serialNumber), WhereParameter.WILDCARD_RIGHT|WhereParameter.IGNORE_CASE);
	}
	
	
	public String getLocation() {
		return (String)queryBuilder.getWhereParameterValue("location");
	}

	public void setLocation(String location) {
		queryBuilder.addWhere(WhereParameter.Comparator.LIKE, "location", "location", blankValue(location), WhereParameter.WILDCARD_BOTH|WhereParameter.IGNORE_CASE );
	}

	public Long getCustomer() {
		return (Long)queryBuilder.getWhereParameterValue("customer");
	}

	public void setCustomer(Long customer) {
		queryBuilder.addWhere(WhereParameter.Comparator.EQ, "customer", "owner.id", customer);
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
		queryBuilder.addWhere(WhereParameter.Comparator.LIKE, "orderNumber", getColumnMapping(ColumnDefinition.OrderNumber), blankValue(orderNumber), WhereParameter.WILDCARD_RIGHT|WhereParameter.IGNORE_CASE);
	}

	public Long getProductType() {
		return (Long)queryBuilder.getWhereParameterValue("productType");
	}

	public void setProductType(Long productType) {
		queryBuilder.addWhere(WhereParameter.Comparator.EQ, "productType", "type.id", productType);
	}

	public Long getProductStatus() {
		return (Long)queryBuilder.getWhereParameterValue("productStatus");
	}

	public void setProductStatus(Long productStatus) {
		queryBuilder.addWhere(WhereParameter.Comparator.EQ, "productStatus", "productStatus.uniqueID", productStatus);
	}

	public Date getFromDate() {
		return (Date)queryBuilder.getWhereParameterValue("fromDate");
	}

	public void setFromDate(Date fromDate) {
		queryBuilder.addWhere(WhereParameter.Comparator.GE, "fromDate", getColumnMapping(ColumnDefinition.DateIdentified), fromDate);
	}

	public Date getToDate() {
		return (Date)queryBuilder.getWhereParameterValue("toDate");
	}

	public void setToDate(Date toDate) {
		queryBuilder.addWhere(WhereParameter.Comparator.LE, "toDate", getColumnMapping(ColumnDefinition.DateIdentified), DateHelper.getEndOfDay(toDate));
	}

	public String getReferenceNumber() {
		return (String)queryBuilder.getWhereParameterValue("referenceNumber");
	}

	public void setReferenceNumber(String referenceNumber) {
		queryBuilder.addWhere(WhereParameter.Comparator.LIKE, "referenceNumber", getColumnMapping(ColumnDefinition.CustomerReferenceNumber), blankValue(referenceNumber), WhereParameter.WILDCARD_RIGHT|WhereParameter.IGNORE_CASE);
	}
	
	public String getPurchaseOrder() {
		return (String)queryBuilder.getWhereParameterValue("purchaseOrder");
	}

	public void setPurchaseOrder(String purchaseOrder) {
		queryBuilder.addWhere(WhereParameter.Comparator.EQ, "purchaseOrder", getColumnMapping(ColumnDefinition.purchaseOrder), blankValue(purchaseOrder), WhereParameter.IGNORE_CASE);
	}
	
	public Long getJobSite() {
		return (Long)queryBuilder.getWhereParameterValue("jobSite");
	}
	
	public void setJobSite(Long jobSite) {
		queryBuilder.addWhere(WhereParameter.Comparator.EQ, "jobSite", "jobSite.id", jobSite);
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
				queryBuilder.setOrder("modified", false);
			}
		} catch (Exception e) {
			// do nothing this will catch the exception of the enum valueOf on a
			// null or non existent type.
			queryBuilder.setOrder("modified", false);
		}
	}
	
	@Override
	public QueryBuilder<Product> getCountQueryBuilder() {
		return queryBuilder.setCountSelect();
	}
	
	@Override
	public QueryBuilder<Product> getSearchQueryBuilder() {
		setupOrderBy();
		
		queryBuilder.setSimpleSelect();
		queryBuilder.getPostFetchPaths().clear();
		queryBuilder.addLeftJoin("shopOrder", null);
		queryBuilder.addLeftJoin("productStatus", null);
		return queryBuilder;
	}
	
	@Override
	public QueryBuilder<Product> getIdQueryBuilder() {
		
		queryBuilder.setSimpleSelect("id");
		queryBuilder.getJoinArguments().clear();
		queryBuilder.getOrderArguments().clear();
		
		return queryBuilder;
	}

	@Override
	protected Map<ColumnDefinition, String> initColumnMap() {
		Map<ColumnDefinition, String> colMap = new HashMap<ColumnDefinition, String>();
		
		colMap.put(ColumnDefinition.RfidNumber,					"rfidNumber");
		colMap.put(ColumnDefinition.SerialNumber,				"serialNumber");
		colMap.put(ColumnDefinition.ReelID,						"serialNumber");
		colMap.put(ColumnDefinition.OrderNumber,				"shopOrder.order.orderNumber");
		colMap.put(ColumnDefinition.purchaseOrder,				"purchaseOrder");
		colMap.put(ColumnDefinition.CustomerReferenceNumber,	"customerRefNumber");
		colMap.put(ColumnDefinition.Productname,				"shopOrder.description");
		colMap.put(ColumnDefinition.EndUserName,				"owner.id");
		colMap.put(ColumnDefinition.ProductType,				"type.name");
		colMap.put(ColumnDefinition.ProductStatus,				"productStatus.name");
		colMap.put(ColumnDefinition.ProductLastInspectionDate,	"lastInspectionDate");
		colMap.put(ColumnDefinition.Description,				"description");
		colMap.put(ColumnDefinition.DateCreated,				"created");
		colMap.put(ColumnDefinition.DateIdentified,             "identified");
		colMap.put(ColumnDefinition.AssignedUser,             	"assignedUser.id");
		
		return colMap;
	}
	
}
