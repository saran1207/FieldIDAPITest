package com.n4systems.model;

import com.n4systems.model.api.Listable;
import com.n4systems.model.api.NamedEntity;
import com.n4systems.model.api.Retirable;
import com.n4systems.model.parents.EntityWithOwner;
import com.n4systems.model.user.User;
import com.n4systems.util.DateHelper;
import org.hibernate.annotations.IndexColumn;

import javax.persistence.*;
import java.util.*;

@Entity
@Table( name="projects" )
public class Project extends EntityWithOwner implements NamedEntity, Listable<Long>, Retirable {

	private static final long serialVersionUID = 1L;
	
	@Column( nullable=false, length=255 )
	private String projectID;
	
	private boolean eventJob = true;
	
	@Column( nullable=false, length=255 )
	private String name;
	
	@Column(nullable=true, length=2000)
	private String description;
	
	@Column(nullable=true, length=2000)
	private String workPerformed;
		
	@Column(nullable=true, length=255)
	private String poNumber;
    
    @Column( nullable=false, length=255 )
    private String status;
	
	@Column(nullable=false)
	private boolean open = true;
    
	@Temporal(TemporalType.TIMESTAMP)
    private Date started;

	@Temporal(TemporalType.TIMESTAMP)
    private Date estimatedCompletion;

	@Temporal(TemporalType.TIMESTAMP)
    private Date actualCompletion;
	
    private String duration; 

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "projects_assets", joinColumns = @JoinColumn(name="projects_id"), inverseJoinColumns = @JoinColumn(name="asset_id"))
    @IndexColumn(name="orderidx")
    private List<Asset> assets = new ArrayList<Asset>();
    
    @OneToMany(fetch=FetchType.LAZY, cascade=CascadeType.ALL)
    private List<FileAttachment> notes = new ArrayList<FileAttachment>();
    
    //@OneToMany(fetch=FetchType.LAZY, cascade=CascadeType.ALL, mappedBy="project")
    //private Set<EventSchedule> schedules = new HashSet<EventSchedule>();

    @OneToMany(fetch=FetchType.LAZY, cascade=CascadeType.ALL, mappedBy="project")
    private Set<Event> events = new HashSet<Event>();
    
    @ManyToMany(fetch=FetchType.LAZY, cascade=CascadeType.ALL)
    private Set<User> resources = new HashSet<User>();
    
    private boolean retired;
    
	public Project() {}
	
	@Override
	protected void onCreate() {
		super.onCreate();
		trimNames();
	}
	
	@Override
	protected void onUpdate() {
		super.onUpdate();
		trimNames();
	}
	

	private void trimNames() {
		name = (name != null) ? name.trim() : null;
		projectID = (projectID != null) ? projectID.trim() : null;
	}

	public String getProjectID() {
		return projectID;
	}

	public void setProjectID( String projectID ) {
		this.projectID = projectID;
	}

	public String getName() {
		return name;
	}

	public void setName( String name ) {
		this.name = name;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus( String status ) {
		this.status = status;
	}

	public Date getStarted() {
		return started;
	}
	
	public Date getStartedInUserTime(TimeZone timeZone) {
		return DateHelper.convertToUserTimeZone(started, timeZone);
	}

	public void setStarted( Date started ) {
		this.started = started;
	}

	public Date getEstimatedCompletion() {
		return estimatedCompletion;
	}

	public Date getEstimatedCompletionInUserTime(TimeZone timeZone) {
		return DateHelper.convertToUserTimeZone(estimatedCompletion, timeZone);
	}
	
	public void setEstimatedCompletion( Date estimatedCompletion ) {
		this.estimatedCompletion = estimatedCompletion;
	}

	public Date getActualCompletion() {
		return actualCompletion;
	}
	
	public Date getActualCompletionInUserTime(TimeZone timeZone) {
		return DateHelper.convertToUserTimeZone(actualCompletion, timeZone);
	}
	
	public void setActualCompletion( Date actualCompletion ) {
		this.actualCompletion = actualCompletion;
	}

	public String getDuration() {
		return duration;
	}

	public void setDuration( String duration ) {
		this.duration = duration;
	}

	public String getDisplayName() {
		return name;
	}

	public List<Asset> getAssets() {
		return assets;
	}

	public void setAssets( List<Asset> assets ) {
		this.assets = assets;
	}

	public List<FileAttachment> getNotes() {
		return notes;
	}

	public void setNotes( List<FileAttachment> notes ) {
		this.notes = notes;
	}

	public boolean isRetired() {
		return retired;
	}

	public void setRetired( boolean retired ) {
		this.retired = retired;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getWorkPerformed() {
		return workPerformed;
	}

	public void setWorkPerformed(String workPerformed) {
		this.workPerformed = workPerformed;
	}

	public String getPoNumber() {
		return poNumber;
	}

	public void setPoNumber(String poNumber) {
		this.poNumber = poNumber;
	}

	public boolean isOpen() {
		return open;
	}

	public void setOpen(boolean open) {
		this.open = open;
	}

	public boolean isEventJob() {
		return eventJob;
	}

	public void setEventJob(boolean eventJob) {
		this.eventJob = eventJob;
	}

	public Set<EventSchedule> getSchedules() {
        throw new UnsupportedOperationException("EventSchedules no longer exist");
    }

	public void setSchedules(Set<EventSchedule> schedules) {
        throw new UnsupportedOperationException("EventSchedules no longer exist");
    }

	public Set<User> getResources() {
		return resources;
	}

	public void setResources(Set<User> resources) {
		this.resources = resources;
	}

    public Set<Event> getEvents() {
        return events;
    }

    public void setEvents(Set<Event> events) {
        this.events = events;
    }
}
