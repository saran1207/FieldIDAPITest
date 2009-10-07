package com.n4systems.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import rfid.ejb.entity.InfoFieldBean;
import rfid.ejb.entity.InfoOptionBean;

import com.n4systems.model.api.HasFileAttachments;
import com.n4systems.model.api.Listable;
import com.n4systems.model.api.NamedEntity;
import com.n4systems.model.api.Saveable;
import com.n4systems.model.api.SecurityEnhanced;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.parents.ArchivableEntityWithTenant;
import com.n4systems.model.security.EntitySecurityEnhancer;
import com.n4systems.model.security.NetworkAccessLevel;
import com.n4systems.model.security.SecurityLevel;


@Entity
@Table(name = "producttypes")
public class ProductType extends ArchivableEntityWithTenant implements NamedEntity, HasFileAttachments, Listable<Long>, Saveable, SecurityEnhanced<ProductType> {
	private static final long serialVersionUID = 1L;
	private static final String descVariableDefault = "";
	private static final String descVariableStart = "{";
	private static final String descVariableEnd = "}";
	private static final String descVariableRegex = ".*\\" + descVariableStart + "(.+?)" + "\\" + descVariableEnd + ".*";
	public static final String DEFAULT_ITEM_NUMBER = "*";
	
	@Column(nullable=false)
	private String name;
	
	@Column(length=2047)
	private String warnings;
	
	@Column(length=2047)
	private String instructions;

	private String cautionUrl;
	private String imageName;
	private String descriptionTemplate;
	
	@Column(length=2000)
	private String manufactureCertificateText;	
	private boolean hasManufactureCertificate;

	@OneToMany(cascade = CascadeType.REMOVE, fetch = FetchType.EAGER, mappedBy = "productType")
	private Set<ProductTypeSchedule> schedules = new HashSet<ProductTypeSchedule>();

	@OneToMany(mappedBy = "productInfo", targetEntity = InfoFieldBean.class, cascade = CascadeType.ALL)
	@OrderBy("weight, name ASC")
	private Collection<InfoFieldBean> infoFields = new ArrayList<InfoFieldBean>();
	
	@OneToOne( mappedBy="productType", targetEntity = AutoAttributeCriteria.class, fetch = FetchType.LAZY)
	private AutoAttributeCriteria autoAttributeCriteria;
	
	@OneToMany(fetch=FetchType.LAZY, cascade=CascadeType.ALL)
	private Set<FileAttachment> attachments = new HashSet<FileAttachment>();

	@ManyToMany(fetch = FetchType.LAZY)
	private Set<ProductType> subTypes = new HashSet<ProductType>();
	
	private String archivedName;
	
	@OneToMany(mappedBy="productType")
	private Set<AssociatedInspectionType> inspectionTypes;
	
	@ManyToOne(fetch=FetchType.EAGER)
	private ProductTypeGroup group;
		
	public ProductType() {
		this(null);
	}
	
	public ProductType(String name) {
		super();
		this.name = name;
	}
	
	private void trimName() {
		this.name = (name != null) ? name.trim() : null;
	}
	
	protected void onCreate() {
		super.onCreate();
		trimName();
	}
	
	protected void onUpdate() {
		super.onUpdate();
		trimName();
	}
	
	@Deprecated
	@NetworkAccessLevel(SecurityLevel.ALLOWED)
	public Long getUniqueID() {
		return getId();
	}
	
	@NetworkAccessLevel(SecurityLevel.MANY_AWAY)
	public String getDescriptionTemplate() {
		return descriptionTemplate;
	}

	public void setDescriptionTemplate(String descriptionTemplate) {
		this.descriptionTemplate = descriptionTemplate;
	}

	public void setName(String name) {
		this.name = name;
	}

	@NetworkAccessLevel(SecurityLevel.MANY_AWAY)
	public String getName() {
		return name;
	}
	
	@Deprecated
	@NetworkAccessLevel(SecurityLevel.MANY_AWAY)
	public String getProductType() {
		return getName();
	}
	
	@Deprecated
	public void setProductType(String name) {
		setName(name);
	}

	public void setWarnings(String safetyLocationPath) {
		this.warnings = safetyLocationPath;
	}

	@NetworkAccessLevel(SecurityLevel.DIRECT)
	public String getWarnings() {
		return warnings;
	}

	public void setCautionUrl(String externalURL) {
		this.cautionUrl = externalURL;
	}

	@NetworkAccessLevel(SecurityLevel.DIRECT)
	public String getCautionUrl() {
		return cautionUrl;
	}
	
	@Deprecated
	@NetworkAccessLevel(SecurityLevel.DIRECT)
	public String getCautions() {
		return getCautionUrl();
	}
	
	@Deprecated
	public void setCautions(String cautionUrl) {
		setCautionUrl(cautionUrl);
	}

	public void setInstructions(String instructions) {
		this.instructions = instructions;
	}

	@NetworkAccessLevel(SecurityLevel.DIRECT)
	public String getInstructions() {
		return instructions;
	}

	@Deprecated
	@NetworkAccessLevel(SecurityLevel.ALLOWED)
	public Set<InspectionType> getInspectionTypes() {
		Set<InspectionType> types = new HashSet<InspectionType>();
		for (AssociatedInspectionType inspectionType : inspectionTypes) {
			types.add(inspectionType.getInspectionType());
		}
		return types;
	}

	@NetworkAccessLevel(SecurityLevel.ALLOWED)
	public Collection<InfoFieldBean> getInfoFields() {
		return infoFields;
	}

	public void setInfoFields(Collection<InfoFieldBean> infoFields) {
		this.infoFields = infoFields;
	}

	public void associateFields() {
		if(infoFields != null) {
			for (InfoFieldBean infoFieldBean : infoFields) {
				infoFieldBean.setProductInfo(this);
			}
		}
	}

	@NetworkAccessLevel(SecurityLevel.MANY_AWAY)
	public boolean isHasManufactureCertificate() {
		return hasManufactureCertificate;
	}

	public void setHasManufactureCertificate(boolean hasManufactureCertificate) {
		this.hasManufactureCertificate = hasManufactureCertificate;
	}

	@NetworkAccessLevel(SecurityLevel.MANY_AWAY)
	public String getManufactureCertificateText() {
		return manufactureCertificateText;
	}

	public void setManufactureCertificateText(String manufactureCertificateText) {
		this.manufactureCertificateText = manufactureCertificateText;
	}

	@NetworkAccessLevel(SecurityLevel.LOCAL)
	public AutoAttributeCriteria getAutoAttributeCriteria() {
		return autoAttributeCriteria;
	}

	public void setAutoAttributeCriteria(AutoAttributeCriteria autoAttributeCriteria) {
		this.autoAttributeCriteria = autoAttributeCriteria;
	}
	
	@NetworkAccessLevel(SecurityLevel.LOCAL)
	public boolean hasCriteria() {
		return autoAttributeCriteria != null;
	}
	
	@NetworkAccessLevel(SecurityLevel.ALLOWED)
	public Collection<InfoFieldBean> getAvailableInfoFields() {
		ArrayList<InfoFieldBean> availableFields = new ArrayList<InfoFieldBean>(); 
		for( InfoFieldBean infoField : infoFields ) {
			if( !infoField.isRetired() ) {
				availableFields.add( infoField );
			}
		}
		
		return availableFields;
	}
	
	/*
	 * prepareDescription is used to create a product description based of the 
	 * ProductType descriptionTemplate and the Product's info options.
	 * 
	 * This method is complex because it has been highly optimized.  Please ensure any
	 * refactors conform to the same standard of performance as it is used heavily by reporting
	 */
	public String prepareDescription(Collection<InfoOptionBean> infoOptions) {
		if( descriptionTemplate == null ) { 
			return "";
		}
		// if the template does not have a starting bracket, then it has no variables.  Just return the unmodified template
		if(descriptionTemplate.indexOf(descVariableStart) == -1) {
			return descriptionTemplate;
		}
		
		// now we can create a HashMap of info field names to info option names.
		// this will be used in variable substitution below.  The map is initialized to its final size for performance. 
		Map<String, String> optionMap = new HashMap<String, String>(infoOptions.size());
		for(InfoOptionBean option: infoOptions) {
			optionMap.put(option.getInfoField().getName(), option.getName());
		}
		
		// index of the last closing bracket in the template 
		int lastBracket = descriptionTemplate.lastIndexOf(descVariableEnd);
		int currentIdx = 0;
		
		String part, field;
		StringBuilder desc = new StringBuilder();
		while(currentIdx < lastBracket) {
			//this isolates the current non-variable part
			part = descriptionTemplate.substring(currentIdx, descriptionTemplate.indexOf(descVariableStart, currentIdx));
			currentIdx += part.length() + 1;
			desc.append(part);

			//this isolates the field name
			field = descriptionTemplate.substring(currentIdx, descriptionTemplate.indexOf(descVariableEnd, currentIdx));
			currentIdx += field.length() + 1;
			
			// if the field name exists in our map, then substitute it in, otherwise use the default
			if(optionMap.containsKey(field)) {
				desc.append(optionMap.get(field));
			} else {
				desc.append(descVariableDefault);
			}
		}
		// get the final part after the last closing bracket
		desc.append(descriptionTemplate.substring(lastBracket + 1));
		
		return desc.toString();
	}
	
	@NetworkAccessLevel(SecurityLevel.LOCAL)
	public boolean isDescriptionTemplateValid() {
		List<String> fieldNames = new ArrayList<String>();
		for(InfoFieldBean field: infoFields) {
			fieldNames.add(field.getName());
		}
		
		return isDescriptionTemplateValid(descriptionTemplate, fieldNames);
	}
	
	// XXX - we should pull this to a util/handler class at some point
	@NetworkAccessLevel(SecurityLevel.LOCAL)
	public static boolean isDescriptionTemplateValid(String descriptionTemplate, Collection<String> infoFieldNames) {
		return (getInvalidDescriptionTemplateVariables(descriptionTemplate, infoFieldNames).size() > 0);
	}
	
	// XXX - we should pull this to a util/handler class at some point
	@NetworkAccessLevel(SecurityLevel.LOCAL)
	public static List<String> getInvalidDescriptionTemplateVariables(String descriptionTemplate, Collection<String> infoFieldNames) {
		String description = "" + descriptionTemplate;
		
		for(String fieldName: infoFieldNames) {
			// we'll just replace them with nothing since all we care about is if there are invalid variables
			description = description.replace(descVariableStart + fieldName + descVariableEnd, "");
		}
		
		Matcher varMatcher = Pattern.compile(descVariableRegex).matcher(description);
		
		List<String> invalidVariables = new ArrayList<String>();
		if(varMatcher.matches()) {
			for(int i = 1; i <= varMatcher.groupCount(); i++) {
				invalidVariables.add(varMatcher.group(i));
			}
		}
		
		return invalidVariables;
	}

	@NetworkAccessLevel(SecurityLevel.ALLOWED)
	public Set<ProductTypeSchedule> getSchedules() {
		return schedules;
	}

	public void setSchedules(Set<ProductTypeSchedule> schedules) {
		this.schedules = schedules;
	}
	
	/**
	 * Finds the ProductTypeSchedule for the given inspection type and owner, looking for schedules 
	 * up the owners {@link BaseOrg#getParent() parent} chain until it finds one or reaches the 
	 * PrimaryOrg.
	 * @param type	Inspection type
	 * @param owner	Owner
	 * @return		ProductTypeSchedule or null if no schedule was found
	 */
	@NetworkAccessLevel(SecurityLevel.ALLOWED)
	public ProductTypeSchedule getSchedule(InspectionType type, BaseOrg owner) {
		ProductTypeSchedule scheduleForOrg = null;
		
		if(type == null || owner == null) {
			// the null owner check is important here since it is a stopping case for the recursion
			return null;
		}
		
		for (ProductTypeSchedule schedule: schedules) {
			if (schedule.getInspectionType().equals(type) && schedule.getOwner().equals(owner)) {
				scheduleForOrg = schedule;
				break;
			}				
		}
		
		// if we didn't find a schedule, recurse up the owner's parent chain
		if (scheduleForOrg == null) {
			scheduleForOrg = getSchedule(type, owner.getParent());
		}
		
		return scheduleForOrg;
	}
	
	@NetworkAccessLevel(SecurityLevel.DIRECT)
	public Date getSuggestedNextInspectionDate(Date fromDate, InspectionType type, BaseOrg owner) {
		Date returnDate = fromDate;
		
		ProductTypeSchedule schedule = getSchedule(type, owner);
		if(schedule != null) {
			returnDate = schedule.getNextDate(fromDate);
		}
		
		return returnDate;
	}

	@NetworkAccessLevel(SecurityLevel.DIRECT)
	public Set<FileAttachment> getAttachments() {
		return attachments;
	}

	public void setAttachments( Set<FileAttachment> attachments ) {
		this.attachments = attachments;
	}

	@NetworkAccessLevel(SecurityLevel.DIRECT)
	public String getImageName() {
		return imageName;
	}

	public void setImageName( String imageName ) {
		this.imageName = imageName;
	}
	
	@NetworkAccessLevel(SecurityLevel.DIRECT)
	public boolean hasImage() {
		return (imageName != null);
	}
	
	@NetworkAccessLevel(SecurityLevel.ALLOWED)
	public Set<ProductType> getSubTypes() {
		return subTypes;
	}

	public void setSubTypes(Set<ProductType> subTypes) {
		this.subTypes = subTypes;
	}
	
	@NetworkAccessLevel(SecurityLevel.MANY_AWAY)
	public String getDisplayName() {
		return name;
	}
	
	@NetworkAccessLevel(SecurityLevel.ALLOWED)
	public boolean isMaster() {
		return !subTypes.isEmpty();
	}
	
	public void archivedName(String prefix) {
		archivedName = name;
		if (prefix != null) {
			name = prefix + " ";
		}
		name += UUID.randomUUID().toString();
	}

	@NetworkAccessLevel(SecurityLevel.LOCAL)
	public String getArchivedName() {
		return archivedName;
	}

	@NetworkAccessLevel(SecurityLevel.ALLOWED)
	public ProductTypeGroup getGroup() {
		return group;
	}

	public void setGroup(ProductTypeGroup group) {
		this.group = group;
	}

	public ProductType enhance(SecurityLevel level) {
		return EntitySecurityEnhancer.enhanceEntity(this, level);
	}
	
}
