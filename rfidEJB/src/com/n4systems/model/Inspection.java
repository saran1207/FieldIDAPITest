package com.n4systems.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.IndexColumn;

import com.n4systems.model.api.Archivable;
import com.n4systems.model.api.Exportable;
import com.n4systems.model.api.HasOwner;
import com.n4systems.model.api.NetworkEntity;
import com.n4systems.model.inspection.AssignedToUpdate;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.security.EntitySecurityEnhancer;
import com.n4systems.model.security.NetworkAccessLevel;
import com.n4systems.model.security.SafetyNetworkSecurityCache;
import com.n4systems.model.security.SecurityDefiner;
import com.n4systems.model.security.SecurityLevel;
import com.n4systems.model.user.User;
import com.n4systems.reporting.InspectionReportType;
import com.n4systems.util.DateHelper;
import com.n4systems.util.StringUtils;

@Entity
@Table(name = "inspectionsmaster")
@PrimaryKeyJoinColumn(name="inspection_id")
public class Inspection extends AbstractInspection implements Comparable<Inspection>, HasOwner, Archivable, NetworkEntity<Inspection>, Exportable {
	private static final long serialVersionUID = 1L;
	public static final String[] ALL_FIELD_PATHS = { "modifiedBy.userID", "type.sections", "type.supportedProofTests", "type.infoFieldNames", "attachments", "results", "product", "product.infoOptions", "infoOptionMap", "subInspections" };
	
	public static final SecurityDefiner createSecurityDefiner() {
		return new SecurityDefiner("tenant.id", "product.owner", null, "state");
	}
	
	private String location;
	
	@Column(nullable=false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date date;
	
	@Column(nullable=false)
	private boolean printable;

	@ManyToOne(fetch=FetchType.EAGER, optional = false)
	private User performedBy;

	@ManyToOne(fetch=FetchType.EAGER, optional = false)
	private InspectionGroup group;
	
	@ManyToOne(fetch=FetchType.EAGER)
	private InspectionBook book;
	
	@ManyToOne(fetch=FetchType.EAGER, optional=false)
	@JoinColumn(name="owner_id", nullable = false)
	private BaseOrg owner;
	
	private ProofTestInfo proofTestInfo;
	
	@OneToMany(fetch=FetchType.LAZY, cascade=CascadeType.ALL)
	@IndexColumn(name="orderidx")
	private List<SubInspection> subInspections = new ArrayList<SubInspection>();
	
	@Enumerated(EnumType.STRING)
	@Column(nullable=false)
	private Status status = Status.NA;
		
	@Enumerated(EnumType.STRING)
	private EntityState state = EntityState.ACTIVE;
	
	@OneToOne(mappedBy="inspection")
	private InspectionSchedule schedule;
	
	private AssignedToUpdate assignedTo;
	
	public Inspection() {
		super();
	}
	
	public Inspection(Tenant tenant) {
		super(tenant);
	}

	@NetworkAccessLevel(SecurityLevel.DIRECT)
	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	@NetworkAccessLevel(SecurityLevel.MANY_AWAY)
	public Date getDate() {
		return date;
	}
	
	@NetworkAccessLevel(SecurityLevel.MANY_AWAY)
	public Date getDateInUserTime(TimeZone timeZone) {
		return DateHelper.convertToUserTimeZone(date, timeZone);
	}

	public void setDate(Date date) {
		this.date = date;
	}

	@NetworkAccessLevel(SecurityLevel.ALLOWED)
	public boolean isPrintable() {
		return printable;
	}

	public void setPrintable(boolean printable) {
		this.printable = printable;
	}

	@NetworkAccessLevel(SecurityLevel.ALLOWED)
	public User getPerformedBy() {
		return performedBy;
	}
	

	public void setPerformedBy(User performedBy) {
		this.performedBy = performedBy;
	}

	@NetworkAccessLevel(SecurityLevel.MANY_AWAY)
	public InspectionGroup getGroup() {
		return group;
	}
	

	public void setGroup(InspectionGroup group) {
		this.group = group;
	}

	@NetworkAccessLevel(SecurityLevel.ALLOWED)
	public InspectionBook getBook() {
		return book;
	}

	public void setBook(InspectionBook book) {
		this.book = book;
	}

	@NetworkAccessLevel(SecurityLevel.ALLOWED)
	public BaseOrg getOwner() {
		return owner;
	}
	
	public void setOwner(BaseOrg owner) {
		this.owner = owner;
	}

	@NetworkAccessLevel(SecurityLevel.MANY_AWAY)
	public ProofTestInfo getProofTestInfo() {
		return proofTestInfo;
	}

	public void setProofTestInfo(ProofTestInfo proofTestInfo) {
		this.proofTestInfo = proofTestInfo;
	}

	@NetworkAccessLevel(SecurityLevel.MANY_AWAY)
	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}
	
	public int compareTo( Inspection inspection ) {
		if( inspection == null ) { 
			return -1;
		}
		int compare = date.compareTo( inspection.getDate() );
		
		return ( compare == 0 ) ? getCreated().compareTo( inspection.getCreated() ) : compare;
	}

	@NetworkAccessLevel(SecurityLevel.MANY_AWAY)
	public List<SubInspection> getSubInspections() {
		return subInspections;
	}

	public void setSubInspections(List<SubInspection> subInspections) {
		this.subInspections = subInspections;
	}

	public void activateEntity() {
		state = EntityState.ACTIVE;
	}

	public void archiveEntity() {
		state = EntityState.ARCHIVED;
	}

	@NetworkAccessLevel(SecurityLevel.ALLOWED)
	public EntityState getEntityState() {
		return state;
	}

	public void retireEntity() {
		state = EntityState.RETIRED;
	}
	
	public void setRetired( boolean retired ) {
		if( retired ) {
			retireEntity();
		} else  {
			activateEntity();
		}
	}
	
	@NetworkAccessLevel(SecurityLevel.ALLOWED)
	public boolean isRetired() {
		return state == EntityState.RETIRED;
	}

	@NetworkAccessLevel(SecurityLevel.ALLOWED)
	public boolean isActive() {
		return state == EntityState.ACTIVE;
	}
	
	@NetworkAccessLevel(SecurityLevel.ALLOWED)
	public boolean isArchived() {
		return state == EntityState.ARCHIVED;
	}
	
	@NetworkAccessLevel(SecurityLevel.ALLOWED)
	public boolean isPrintableForReportType(InspectionReportType reportType) { 
		if (!printable) {
			return false;
		}
		
		PrintOut printOut = getType().getGroup().getPrintOutForReportType(reportType);
		return (printOut != null);
	}

	@NetworkAccessLevel(SecurityLevel.ALLOWED)
	public boolean isInspectionCertPrintable() { 
		return isPrintableForReportType(InspectionReportType.INSPECTION_CERT);
	}

	@NetworkAccessLevel(SecurityLevel.ALLOWED)
	public boolean isObservationCertPrintable() { 
		return isPrintableForReportType(InspectionReportType.OBSERVATION_CERT);
	}
	
	@NetworkAccessLevel(SecurityLevel.ALLOWED)
	public boolean isAnyCertPrintable() {
		boolean isAnyPrintable = false;
		for (InspectionReportType reportType: InspectionReportType.values()) {
			if (isPrintableForReportType(reportType)) {
				isAnyPrintable = true;
				break;
			}
		}
		return isAnyPrintable;
	}
	
	@Override
    public String toString() {
		String subInspectionString = new String();
		for (SubInspection subInspection: getSubInspections()) {
			subInspectionString += "\n" + subInspection;
		}
		
	    return	super.toString() + 
	    		"\nState: " + state + 
	    		"\nDate: " + date +
	    		"\nOwner: " + getOwner() +
	    		"\nBook: " + getBook() +
	    		"\nPerformed By: " + getPerformedBy() + 
	    		"\nStatus: " + getStatus() + 
	    		"\nSubInspections: " + StringUtils.indent(subInspectionString, 1);
    }

	@NetworkAccessLevel(SecurityLevel.MANY_AWAY)
	public InspectionSchedule getSchedule() {
		return schedule;
	}
	
	@NetworkAccessLevel(SecurityLevel.ALLOWED)
	public SecurityLevel getSecurityLevel(BaseOrg fromOrg) {
		return SafetyNetworkSecurityCache.getSecurityLevel(fromOrg, getOwner());
	}
	
	public Inspection enhance(SecurityLevel level) {
		Inspection enhanced = EntitySecurityEnhancer.enhanceEntity(this, level);
		enhanced.setBook(enhance(book, level));
		enhanced.setPerformedBy(enhance(performedBy, level));
		enhanced.setGroup(enhance(group, level));
		enhanced.setType(enhance(getType(), level));
		enhanced.setProduct(enhance(getProduct(), level));
		enhanced.setOwner(enhance(getOwner(), level));
		
		List<SubInspection> enhancedSubInspections = new ArrayList<SubInspection>();
		for (SubInspection subInspection: getSubInspections()) {
			enhancedSubInspections.add(subInspection.enhance(level));
		}
		enhanced.setSubInspections(enhancedSubInspections);
		
		return enhanced;
	}

	// Inspection are never exported
	@Override
	public String getGlobalId() {
		return null;
	}

	@Override
	public void setGlobalId(String globalId) {}

	public AssignedToUpdate getAssignedTo() {
		return assignedTo;
	}

	public void setAssignedTo(AssignedToUpdate assignedTo) {
		this.assignedTo = assignedTo;
	}
	
	public boolean hasAssignToUpdate() {
		return assignedTo != null && assignedTo.isAssignmentApplyed();
	}

	@Override
	protected void onCreate() {
		super.onCreate();
		normalizeAssignmentForPersistence();
	}

	private void normalizeAssignmentForPersistence() {
		if (assignedTo == null)
			assignedTo = AssignedToUpdate.ignoreAssignment();
	}

	@Override
	protected void onUpdate() {
		super.onUpdate();
		normalizeAssignmentForPersistence();
	}
	
	
}
