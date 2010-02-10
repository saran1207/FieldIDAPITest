package com.n4systems.model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.CollectionOfElements;

import rfid.ejb.entity.ProductStatusBean;

import com.n4systems.model.api.HasFileAttachments;
import com.n4systems.model.parents.EntityWithTenant;
import com.n4systems.model.security.NetworkAccessLevel;
import com.n4systems.model.security.SecurityLevel;
import com.n4systems.util.StringUtils;

@Entity
@Table(name = "inspections")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class AbstractInspection extends EntityWithTenant implements HasFileAttachments {
	private static final long serialVersionUID = 1L;

	@Column( length=2500 )
	private String comments;
	
	@ManyToOne(fetch=FetchType.EAGER, optional = false)
	private InspectionType type;
	
	@ManyToOne(fetch=FetchType.LAZY, optional = false)
	private Product product;
	
	@ManyToOne(optional = true)
	@JoinColumn(name="productstatus_id")
	private ProductStatusBean productStatus;
	
	@OneToMany(fetch=FetchType.LAZY, mappedBy = "inspection", cascade=CascadeType.ALL)
	private Set<CriteriaResult> results = new HashSet<CriteriaResult>();
	
	@OneToMany(fetch=FetchType.LAZY, cascade=CascadeType.ALL)
	private Set<FileAttachment> attachments = new HashSet<FileAttachment>();

	@CollectionOfElements(fetch=FetchType.LAZY)
    private Map<String, String> infoOptionMap = new HashMap<String, String>();
	
	@Column(nullable=false)
	private long formVersion;
	
	private String mobileGUID;
	
	@NetworkAccessLevel(SecurityLevel.ALLOWED)
	public String getMobileGUID() {
		return mobileGUID;
	}
	public void setMobileGUID(String mobileGUID) {
		this.mobileGUID = mobileGUID;
	}

	public AbstractInspection() {}

	public AbstractInspection(Tenant tenant) {
		super(tenant);
	}
	
	@Override
    public String toString() {
		String resultString = new String();
		for (CriteriaResult result: getResults()) {
			resultString += "\n" + result;
		}
		
	    return	"id: " + getId() +
	    		"\nTenant: " + getTenant() + 
	    		"\nType: " + getType() +
	    		"\nForm Ver: " + getFormVersion() +
	    		"\nProduct: " + getProduct() + 
	    		"\nResults: " + StringUtils.indent(resultString, 1);
    }

	@NetworkAccessLevel(SecurityLevel.LOCAL)
	public boolean isEditable() {
		return (formVersion == type.getFormVersion());
	}
	
	@NetworkAccessLevel(SecurityLevel.ALLOWED)
	public InspectionType getType() {
		return type;
	}

	public void setType(InspectionType type) {
		this.type = type;
	}

	@NetworkAccessLevel(SecurityLevel.ALLOWED)
	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	@NetworkAccessLevel(SecurityLevel.ALLOWED)
	public ProductStatusBean getProductStatus() {
		return productStatus;
	}

	public void setProductStatus(ProductStatusBean productStatus) {
		this.productStatus = productStatus;
	}

	@NetworkAccessLevel(SecurityLevel.DIRECT)
	public Set<CriteriaResult> getResults() {
		return results;
	}

	public void setResults(Set<CriteriaResult> results) {
		this.results = results;
	}

	@NetworkAccessLevel(SecurityLevel.MANY_AWAY)
	public Set<FileAttachment> getAttachments() {
		return attachments;
	}

	public void setAttachments(Set<FileAttachment> attachments) {
		this.attachments = attachments;
	}
	
	@NetworkAccessLevel(SecurityLevel.MANY_AWAY)
	public Map<String, String> getInfoOptionMap() {
		return infoOptionMap;
	}

	public void setInfoOptionMap(Map<String, String> infoOptionMap) {
		this.infoOptionMap = infoOptionMap;
	}

	@NetworkAccessLevel(SecurityLevel.MANY_AWAY)
	public String getComments() {
		return comments;
	}

	public void setComments( String comments ) {
		this.comments = comments;
	}

	@NetworkAccessLevel(SecurityLevel.ALLOWED)
	public long getFormVersion() {
    	return formVersion;
    }

	public void setFormVersion(long formVersion) {
    	this.formVersion = formVersion;
    }
	
	public Set<FileAttachment> getImageAttachments() {
		Set<FileAttachment> imageAttachments = new HashSet<FileAttachment>();
		for (FileAttachment fileAttachment : attachments) {
			if (fileAttachment.isImage()) {
				imageAttachments.add(fileAttachment);
			}
		}
		return imageAttachments;
	}
	
}
