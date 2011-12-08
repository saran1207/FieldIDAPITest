package com.n4systems.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.persistence.*;

import rfid.ejb.entity.InfoFieldBean;
import rfid.ejb.entity.InfoOptionBean;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.n4systems.model.api.HasFileAttachments;
import com.n4systems.model.api.Listable;
import com.n4systems.model.api.NamedEntity;
import com.n4systems.model.api.Saveable;
import com.n4systems.model.api.SecurityEnhanced;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.parents.ArchivableEntityWithTenant;
import com.n4systems.model.security.AllowSafetyNetworkAccess;
import com.n4systems.model.security.EntitySecurityEnhancer;
import com.n4systems.model.security.SecurityLevel;


@Entity
@Table(name = "assettypes")
public class AssetType extends ArchivableEntityWithTenant implements NamedEntity, HasFileAttachments, Listable<Long>, Saveable, SecurityEnhanced<AssetType> {

	private static final long serialVersionUID = 1L;
	private static final String descVariableDefault = "";
	private static final String descVariableStart = "{";
	private static final String descVariableEnd = "}";
	private static final String descVariableRegex = ".*\\" + descVariableStart + "(.+?)" + "\\" + descVariableEnd + ".*";
	public static final String DEFAULT_ITEM_NUMBER = "*";
	
	private static final String PO_NUMBER = "PONumber";
	private static final String RFID = "RFID";
	private static final String REF_NUMBER = "RefNumber";
	private static final String ORDER_NUMBER = "OrderNumber";
	private static final String IDENTIFIER = "Identifier";
	
	private static Collection<? extends String> reservedFieldNames = Lists.newArrayList(PO_NUMBER, RFID, REF_NUMBER, ORDER_NUMBER, IDENTIFIER );
	
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

	@OneToMany(fetch = FetchType.EAGER, mappedBy = "assetType")
	private Set<AssetTypeSchedule> schedules = new HashSet<AssetTypeSchedule>();

	@OneToMany(mappedBy = "assetInfo", targetEntity = InfoFieldBean.class, cascade = CascadeType.ALL)
	@OrderBy("weight, name ASC")
	private Collection<InfoFieldBean> infoFields = new ArrayList<InfoFieldBean>();
	
	@OneToOne( mappedBy="assetType", targetEntity = AutoAttributeCriteria.class, fetch = FetchType.LAZY)
	private AutoAttributeCriteria autoAttributeCriteria;
	
	@OneToMany(fetch=FetchType.LAZY, cascade=CascadeType.ALL)
    @JoinTable(name="assettypes_fileattachments", joinColumns = @JoinColumn(name="assettype_id"), inverseJoinColumns = @JoinColumn(name="attachments_id"))
	private List<FileAttachment> attachments = new ArrayList<FileAttachment>();

	@ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name="assettypes_assettypes", joinColumns = @JoinColumn(name="assettype_id"), inverseJoinColumns = @JoinColumn(name="subtypes_id"))
	private Set<AssetType> subTypes = new HashSet<AssetType>();
	
	private String archivedName;
	
	@OneToMany(mappedBy="assetType")
	private Set<AssociatedEventType> eventTypes;
	
	
	
	@ManyToOne(fetch=FetchType.EAGER)
	private AssetTypeGroup group;

    private String identifierFormat;
    private String identifierLabel;
    private boolean identifierOverridden;
		
	public AssetType() {
		this(null);
	}
	
	public AssetType(String name) {
		super();
		this.name = name;
	}
	
	private void trimName() {
		this.name = (name != null) ? name.trim() : null;
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
	
	@Deprecated
	@AllowSafetyNetworkAccess
	public Long getUniqueID() {
		return getId();
	}
	
	@AllowSafetyNetworkAccess
	public String getDescriptionTemplate() {
		return descriptionTemplate;
	}

	public void setDescriptionTemplate(String descriptionTemplate) {
		this.descriptionTemplate = descriptionTemplate;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	@AllowSafetyNetworkAccess
	public String getName() {
		return name;
	}
	
	@Deprecated
	@AllowSafetyNetworkAccess
	public String getAssetType() {
		return getName();
	}
	
	@Deprecated
	public void setAssetType(String name) {
		setName(name);
	}

	public void setWarnings(String safetyLocationPath) {
		this.warnings = safetyLocationPath;
	}

	@AllowSafetyNetworkAccess
	public String getWarnings() {
		return warnings;
	}

	public void setCautionUrl(String externalURL) {
		this.cautionUrl = externalURL;
	}

	@AllowSafetyNetworkAccess
	public String getCautionUrl() {
		return cautionUrl;
	}
	
	@Deprecated
	@AllowSafetyNetworkAccess
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

	@AllowSafetyNetworkAccess
	public String getInstructions() {
		return instructions;
	}

	@Deprecated
	@AllowSafetyNetworkAccess
	public Set<EventType> getEventTypes() {
		Set<EventType> types = new HashSet<EventType>();
		for (AssociatedEventType eventType : eventTypes) {
			types.add(eventType.getEventType());
		}
		return types;
	}

	@AllowSafetyNetworkAccess
	public Collection<InfoFieldBean> getInfoFields() {
		return infoFields;
	}

	public void setInfoFields(Collection<InfoFieldBean> infoFields) {
		this.infoFields = infoFields;
	}

	public void associateFields() {
		if(infoFields != null) {
			for (InfoFieldBean infoFieldBean : infoFields) {
				infoFieldBean.setAssetInfo(this);
			}
		}
	}

	@AllowSafetyNetworkAccess
	public boolean isHasManufactureCertificate() {
		return hasManufactureCertificate;
	}

	public void setHasManufactureCertificate(boolean hasManufactureCertificate) {
		this.hasManufactureCertificate = hasManufactureCertificate;
	}

	@AllowSafetyNetworkAccess
	public String getManufactureCertificateText() {
		return manufactureCertificateText;
	}

	public void setManufactureCertificateText(String manufactureCertificateText) {
		this.manufactureCertificateText = manufactureCertificateText;
	}

	public AutoAttributeCriteria getAutoAttributeCriteria() {
		return autoAttributeCriteria;
	}

	public void setAutoAttributeCriteria(AutoAttributeCriteria autoAttributeCriteria) {
		this.autoAttributeCriteria = autoAttributeCriteria;
	}
	
	public boolean hasCriteria() {
		return autoAttributeCriteria != null;
	}
	
	@AllowSafetyNetworkAccess
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
	 * prepareDescription is used to create an asset description based of the
	 * AssetType descriptionTemplate and the Asset's info options.
	 * 
	 * This method is complex because it has been highly optimized.  Please ensure any
	 * refactors conform to the same standard of performance as it is used heavily by reporting
	 */
	public String prepareDescription(Asset asset, Collection<InfoOptionBean> infoOptions) {
		if( descriptionTemplate == null ) { 
			return "";
		}
		// if the template does not have a starting bracket, then it has no variables.  Just return the unmodified template
		if(descriptionTemplate.indexOf(descVariableStart) == -1) {
			return descriptionTemplate;
		}
		
		// now we can create a HashMap of info field names to info option names.
		// this will be used in variable substitution below.
		Map<String, String> valueMap = createValueMap(asset, infoOptions);
		
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
			if(valueMap.containsKey(field)) {
				desc.append(valueMap.get(field));
			} else {
				desc.append(descVariableDefault);
			}
		}
		// get the final part after the last closing bracket
		desc.append(descriptionTemplate.substring(lastBracket + 1));
		
		return desc.toString();
	}

	private Map<String, String> createValueMap(Asset asset, Collection<InfoOptionBean> infoOptions) {
		Map<String, String> valueMap = Maps.newHashMapWithExpectedSize(infoOptions.size());
		// NOTE : if the asset has an attribute that is the same as a reserved keyword (like "Identifier") then 
		// that one wins.   i.e. put the infoOption values in second so they will overwrite values from asset.  see WEB-2539		
		valueMap.put(RFID, asset.getRfidNumber());
		valueMap.put(REF_NUMBER, asset.getCustomerRefNumber());
		valueMap.put(ORDER_NUMBER, asset.getOrderNumber());
		valueMap.put(PO_NUMBER, asset.getPurchaseOrder());
		valueMap.put(IDENTIFIER, asset.getIdentifier());
		for(InfoOptionBean option: infoOptions) {
			valueMap.put(option.getInfoField().getName(), option.getName());
		}
		return valueMap;
	}
	
	public boolean isDescriptionTemplateValid() {
		List<String> fieldNames = new ArrayList<String>();
		for(InfoFieldBean field: infoFields) {
			fieldNames.add(field.getName());
		}		
		return isDescriptionTemplateValid(descriptionTemplate, fieldNames);
	}
	
	// XXX - we should pull this to a util/handler class at some point
	public static boolean isDescriptionTemplateValid(String descriptionTemplate, Collection<String> infoFieldNames) {
		return (getInvalidDescriptionTemplateVariables(descriptionTemplate, infoFieldNames).size() > 0);
	}
	
	// XXX - we should pull this to a util/handler class at some point
	public static List<String> getInvalidDescriptionTemplateVariables(String descriptionTemplate, Collection<String> infoFieldNames) {
		String description = "" + descriptionTemplate;

		ImmutableList<String> validFieldNames = new ImmutableList.Builder<String>().addAll(infoFieldNames).addAll(reservedFieldNames).build();
		for(String fieldName: validFieldNames) {
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

	@AllowSafetyNetworkAccess
	public Set<AssetTypeSchedule> getSchedules() {
		return schedules;
	}

	public void setSchedules(Set<AssetTypeSchedule> schedules) {
		this.schedules = schedules;
	}
	
	/**
	 * Finds the AssetTypeSchedule for the given event type and owner, looking for schedules
	 * up the owners {@link BaseOrg#getParent() parent} chain until it finds one or reaches the 
	 * PrimaryOrg.
	 * @param type	Event type
	 * @param owner	Owner
	 * @return		AssetTypeSchedule or null if no schedule was found
	 */
	@AllowSafetyNetworkAccess
	public AssetTypeSchedule getSchedule(EventType type, BaseOrg owner) {
		AssetTypeSchedule scheduleForOrg = null;
		
		if(type == null || owner == null) {
			// the null owner check is important here since it is a stopping case for the recursion
			return null;
		}
		
		for (AssetTypeSchedule schedule: schedules) {
			if (schedule.getEventType().equals(type) && schedule.getOwner().equals(owner)) {
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
	
	public AssetTypeSchedule getDefaultSchedule(EventType type) {
		for (AssetTypeSchedule schedule: schedules) {
			if (schedule.getEventType().equals(type) && schedule.getOwner().isPrimary()) {
				return  schedule;
			}				
		}
		return null;
	}
	
	@AllowSafetyNetworkAccess
	public Date getSuggestedNextEventDate(Date fromDate, EventType type, BaseOrg owner) {
		Date returnDate = fromDate;
		
		AssetTypeSchedule schedule = getSchedule(type, owner);
		if(schedule != null) {
			returnDate = schedule.getNextDate(fromDate);
		}
		
		return returnDate;
	}

	@Override
	@AllowSafetyNetworkAccess
	public List<FileAttachment> getAttachments() {
		return attachments;
	}

	@Override
	public void setAttachments( List<FileAttachment> attachments ) {
		this.attachments = attachments;
	}

	@AllowSafetyNetworkAccess
	public String getImageName() {
		return imageName;
	}

	public void setImageName( String imageName ) {
		this.imageName = imageName;
	}
	
	@AllowSafetyNetworkAccess
	public boolean hasImage() {
		return (imageName != null);
	}
	
	@AllowSafetyNetworkAccess
	public Set<AssetType> getSubTypes() {
		return subTypes;
	}

	public void setSubTypes(Set<AssetType> subTypes) {
		this.subTypes = subTypes;
	}
	
	@Override
	@AllowSafetyNetworkAccess
	public String getDisplayName() {
		return name;
	}
	
	@AllowSafetyNetworkAccess
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

	@AllowSafetyNetworkAccess
	public String getArchivedName() {
		return archivedName;
	}

	@AllowSafetyNetworkAccess
	public AssetTypeGroup getGroup() {
		return group;
	}

	public void setGroup(AssetTypeGroup group) {
		this.group = group;
	}

	@Override
	public AssetType enhance(SecurityLevel level) {
		return EntitySecurityEnhancer.enhanceEntity(this, level);
	}

    public Set<AssociatedEventType> getAssociatedEventTypes() {
        return eventTypes;
    }

    public String getIdentifierFormat() {
        return identifierFormat;
    }

    public void setIdentifierFormat(String identifierFormat) {
        this.identifierFormat = identifierFormat;
    }

    public String getIdentifierLabel() {
        return identifierLabel;
    }

    public void setIdentifierLabel(String identifierLabel) {
        this.identifierLabel = identifierLabel;
    }

    public boolean isIdentifierOverridden() {
        return identifierOverridden;
    }

    public void setIdentifierOverridden(boolean identifierOverridden) {
        this.identifierOverridden = identifierOverridden;
    }

    @Transient
    public List<EventType> getAllEventTypes() {
        List<EventType> allEventTypes = new ArrayList<EventType>();
        for (AssociatedEventType eventType : eventTypes) {
            allEventTypes.add(eventType.getEventType());
        }
        return allEventTypes;
    }

}
