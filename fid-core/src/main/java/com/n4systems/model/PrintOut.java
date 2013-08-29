package com.n4systems.model;

import com.n4systems.model.api.NamedEntity;
import com.n4systems.model.parents.EntityWithTenant;
import com.n4systems.reporting.PathHandler;

import javax.persistence.*;

@Entity
@Table(name = "printouts")
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

	@Enumerated(EnumType.STRING)
	@Column(nullable=false)
	private PrintOutType type;

    @Column(name="withsubevents")
	private boolean withSubEvents = false;
	
	public PrintOut() {
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
		return pdfTemplate + ".jpg";
	}
	
	public String getFullPdfImage() {
		return pdfTemplate + ".pdf";
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

	public boolean isThumbPreviewFileThere(){
		return PathHandler.getPreviewThumb(this).exists();
	}
	
	public boolean isPrintOutFileThere(){
		return  PathHandler.getPreviewImage(this).exists();
	}
	
	public boolean isCustom() {
		return custom;
	}

	public void setCustom(boolean custom) {
		this.custom = custom;
	}

	public boolean isWithSubEvents() {
		return withSubEvents;
	}

	public void setWithSubEvents(boolean withSubEvents) {
		this.withSubEvents = withSubEvents;
	}

}
