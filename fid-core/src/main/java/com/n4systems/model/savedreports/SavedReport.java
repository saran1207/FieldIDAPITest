package com.n4systems.model.savedreports;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.MapKeyColumn;
import javax.persistence.Table;

import org.hibernate.annotations.IndexColumn;

import com.n4systems.model.api.HasUser;
import com.n4systems.model.api.Listable;
import com.n4systems.model.api.NamedEntity;
import com.n4systems.model.parents.EntityWithTenant;
import com.n4systems.model.security.SecurityDefiner;
import com.n4systems.model.user.User;

@Entity
@Table(name="savedreports")
public class SavedReport extends EntityWithTenant implements NamedEntity, Listable<Long>, HasUser {
	private static final long serialVersionUID = 1L;
	public static final String TO_DATE = "toDate";
	public static final String FROM_DATE = "fromDate";
	public static final String DATE_FORMAT = "yyyy-MM-dd";
	public static final String JOB_SITE = "jobSite";
	public static final String ASSET_TYPE = "assetType";
	public static final String ASSET_STATUS = "assetStatus";
	public static final String ASSIGNED_USER = "assignedUser";
	public static final String PERFORMED_BY = "performedBy";
	public static final String EVENT_TYPE_GROUP = "eventTypeGroup";
	public static final String EVENT_BOOK = "eventBook";
	public static final String SERIAL_NUMBER = "serialNumber";
	public static final String RFID_NUMBER = "rfidNumber";
	public static final String ORDER_NUMBER = "orderNumber";
	public static final String PURCHASE_ORDER_NUMBER = "purchaseOrderNumber";
	public static final String JOB_ID = "jobId";
	public static final String REFERENCE_NUMBER = "referenceNumber";
	public static final String LOCATION = "location";
	public static final String PREDEFINED_LOCATION_ID = "predefinedLocationId";
	public static final String OWNER_ID = "ownerId";
	public static final String ASSET_TYPE_GROUP = "assetTypeGroup";
	public static final String EVENT_STATUS = "status";
    public static final String EVENT_TYPE = "eventTypeId";
	public static SecurityDefiner createSecurityDefiner() {
		return new SecurityDefiner(SavedReport.class);
	}
	
	@Column(nullable=false, length=255)
	private String name;
	private String sharedByName;
	
	@Column(nullable=false)
	private String sortColumn;
	private String sortDirection;
	
	@ElementCollection(fetch= FetchType.EAGER)
	@IndexColumn(name="idx")
    @JoinTable(name="savedreports_columns", joinColumns = {@JoinColumn(name="savedreports_id")})
    @Column(name="element")
	private List<String> columns = new ArrayList<String>();
	
	@ManyToOne(optional=false)
	@JoinColumn(name="user_id", updatable=false)
	private User user;
	
	@ElementCollection(fetch=FetchType.EAGER)
    @JoinTable(name="savedreports_criteria", joinColumns = {@JoinColumn(name="savedreports_id")})
    @MapKeyColumn(name = "mapkey")
    @Column(name="element")
	private Map<String, String> criteria = new HashMap<String, String>();

	public SavedReport() {}
	
	@Override
	protected void onCreate() {
		super.onCreate();
		saveCleanUp();
	}

	private void saveCleanUp() {
		trimName();
		if (columns.isEmpty()) {
			throw new RuntimeException("No columns selected");
		}
	}
	
	@Override
	protected void onUpdate() {
		super.onUpdate();
		saveCleanUp();
	}
	

	private void trimName() {
		name = (name != null) ? name.trim() : null;
	}
	
	public void setInCriteria(String key, Object value) {
		if (value != null && value.toString().length() > 0) {
			criteria.put(key, value.toString());
		}
	}
	
	public String getStringCriteria(String key) {
		return criteria.get(key);
	}
	
	public Long getLongCriteria(String key) {
		Long value = null;
		try {
			value = Long.parseLong(getStringCriteria(key));
		} catch(NumberFormatException e) {}
		
		return value;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDisplayName() {
		return getName();
	}

	public List<String> getColumns() {
		return columns;
	}

	public void setColumns(List<String> columns) {
		this.columns.clear();
		this.columns.addAll(columns);
	}

	public String getSortColumn() {
		return sortColumn;
	}

	public void setSortColumn(String sortColumn) {
		this.sortColumn = sortColumn;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User owner) {
		this.user = owner;
	}

	public Map<String, String> getCriteria() {
		return criteria;
	}

	public void setCriteria(Map<String, String> criteria) {
		this.criteria = criteria;
	}

	public String getSortDirection() {
		return sortDirection;
	}

	public void setSortDirection(String sortDirection) {
		this.sortDirection = sortDirection;
	}

	public String getSharedByName() {
    	return sharedByName;
    }

	public void setSharedByName(String sharedByName) {
    	this.sharedByName = sharedByName;
    }

}
