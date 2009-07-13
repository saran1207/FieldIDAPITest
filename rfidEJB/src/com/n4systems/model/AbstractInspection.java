package com.n4systems.model;

import com.n4systems.model.api.HasFileAttachments;
import com.n4systems.model.parents.EntityWithTenant;
import com.n4systems.util.StringUtils;

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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.CollectionOfElements;

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
	
	@OneToMany(fetch=FetchType.LAZY, mappedBy = "inspection", cascade=CascadeType.ALL)
	private Set<CriteriaResult> results = new HashSet<CriteriaResult>();
	
	@OneToMany(fetch=FetchType.LAZY, cascade=CascadeType.ALL)
	private Set<FileAttachment> attachments = new HashSet<FileAttachment>();

	@CollectionOfElements(fetch=FetchType.LAZY)
    private Map<String, String> infoOptionMap = new HashMap<String, String>();
	
	@Column(nullable=false)
	private long formVersion;
	
	public AbstractInspection() {}

	public AbstractInspection(TenantOrganization tenant) {
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

	public boolean isEditable() {
		return (formVersion == type.getFormVersion());
	}
	
	public InspectionType getType() {
		return type;
	}

	public void setType(InspectionType type) {
		this.type = type;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public Set<CriteriaResult> getResults() {
		return results;
	}

	public void setResults(Set<CriteriaResult> results) {
		this.results = results;
	}

	public Set<FileAttachment> getAttachments() {
		return attachments;
	}

	public void setAttachments(Set<FileAttachment> attachments) {
		this.attachments = attachments;
	}
	
	public Map<String, String> getInfoOptionMap() {
		return infoOptionMap;
	}

	public void setInfoOptionMap(Map<String, String> infoOptionMap) {
		this.infoOptionMap = infoOptionMap;
	}

	public String getComments() {
		return comments;
	}

	public void setComments( String comments ) {
		this.comments = comments;
	}

	public long getFormVersion() {
    	return formVersion;
    }

	public void setFormVersion(long formVersion) {
    	this.formVersion = formVersion;
    }
	
}
