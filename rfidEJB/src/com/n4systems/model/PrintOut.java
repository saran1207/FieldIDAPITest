package com.n4systems.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.n4systems.model.api.NamedEntity;
import com.n4systems.model.parents.EntityWithTenant;

@Entity
@Table(name = "printouts")
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
public class PrintOut extends EntityWithTenant implements NamedEntity {

	private static final long serialVersionUID = 1L;

	public enum PrintOutType {
		CERT(), OBSERVATION();
		
		public String getName() {
			return name();
		}
	}

	@Column(length = 100, nullable = false)
	private String name;
	
	@Column(nullable = false)
	private String description;

	private boolean custom = false;

	@Column(nullable = false)
	private String pdfTemplate;

	// TODO: REMOVE_ME
//	@ManyToOne(fetch = FetchType.EAGER)
//	private Tenant tenant;

	@Enumerated(EnumType.STRING)
	@Column(nullable=false)
	private PrintOutType type;
	
	private boolean withSubInspections = false;
	
	public PrintOut() {
		super();
	}
	
	@Override
	protected void onCreate() {
		super.onCreate();
		trimName();
	}
	
	@Override
	protected void onUpdate() {
		super.onUpdate();
		trimName();
	}
	

	private void trimName() {
		name = (name != null) ? name.trim() : null;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getThumbNailImage() {
		return pdfTemplate + "_thumb.jpg";
	}

	public String getFullImage() {
		return pdfTemplate + "_preview.jpg";
	}

	public String getPdfTemplate() {
		return pdfTemplate;
	}

	public void setPdfTemplate(String pdfTemplate) {
		this.pdfTemplate = pdfTemplate;
	}

	public PrintOutType getType() {
		return type;
	}

	public void setType(PrintOutType type) {
		this.type = type;
	}

	// TODO: REMOVE_ME
//	public Tenant getTenant() {
//		return tenant;
//	}
//
//	public void setTenant(Tenant tenant) {
//		this.tenant = tenant;
//	}

	public boolean isCustom() {
		return custom;
	}

	public void setCustom(boolean custom) {
		this.custom = custom;
	}

	public boolean isWithSubInspections() {
		return withSubInspections;
	}

	public void setWithSubInspections(boolean withSubInspections) {
		this.withSubInspections = withSubInspections;
	}

}
