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

import rfid.ejb.entity.UserBean;

import com.n4systems.model.api.Archivable;
import com.n4systems.model.api.HasOwner;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.security.SecurityDefiner;
import com.n4systems.util.DateHelper;
import com.n4systems.util.StringUtils;

@Entity
@Table(name = "inspectionsmaster")
@PrimaryKeyJoinColumn(name="inspection_id")
public class Inspection extends AbstractInspection implements Comparable<Inspection>, HasOwner, Archivable {
	private static final long serialVersionUID = 1L;
	
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
	private UserBean inspector;

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
	private Status status = Status.NA;
		
	@Enumerated(EnumType.STRING)
	private EntityState state = EntityState.ACTIVE;
	
	@OneToOne(mappedBy="inspection")
	private InspectionSchedule schedule;
	
	public Inspection() {
		super();
	}
	
	public Inspection(Tenant tenant) {
		super(tenant);
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public Date getDate() {
		return date;
	}
	public Date getDateInUserTime(TimeZone timeZone) {
		return DateHelper.convertToUserTimeZone(date, timeZone);
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public boolean isPrintable() {
		return printable;
	}

	public void setPrintable(boolean printable) {
		this.printable = printable;
	}

	public UserBean getInspector() {
		return inspector;
	}

	public void setInspector(UserBean inspector) {
		this.inspector = inspector;
	}

	public InspectionGroup getGroup() {
		return group;
	}

	public void setGroup(InspectionGroup group) {
		this.group = group;
	}

	public InspectionBook getBook() {
		return book;
	}

	public void setBook(InspectionBook book) {
		this.book = book;
	}

	public BaseOrg getOwner() {
		return owner;
	}
	
	public void setOwner(BaseOrg owner) {
		this.owner = owner;
	}

	public ProofTestInfo getProofTestInfo() {
		return proofTestInfo;
	}

	public void setProofTestInfo(ProofTestInfo proofTestInfo) {
		this.proofTestInfo = proofTestInfo;
	}

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
	
	public boolean isRetired() {
		return state == EntityState.RETIRED;
	}

	public boolean isActive() {
		return state == EntityState.ACTIVE;
	}
	
	public boolean isArchived() {
		return state == EntityState.ARCHIVED;
	}
	
	public boolean hasAnyPrintOuts() {
		return (printable && getType().getGroup().hasPrintOut()) || getType().getGroup().hasObservationPrintOut();
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
	    		"\nInspector: " + getInspector() + 
	    		"\nStatus: " + getStatus() + 
	    		"\nSubInspections: " + StringUtils.indent(subInspectionString, 1);
    }

	public InspectionSchedule getSchedule() {
		return schedule;
	}
}
