package com.n4systems.api.model;

import com.n4systems.api.validation.validators.*;
import com.n4systems.exporting.beanutils.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

public class EventView extends ExternalModelView {
	private static final long serialVersionUID = 1L;

	@SerializableField(title = "Asset Identifier", order = 100, validators = { NotNullValidator.class, EventAssetIdentifierValidator.class })
	private String identifier;

    @SerializableField(title = "Due Date", order = 150, handler = DateSerializationHandler.class, validators = {DateValidator.class })
    private Object dueDate;

	@SerializableField(title = "Date Performed", order = 200, validators = { NotNullValidator.class, DateValidator.class })
	private Object datePerformed;

	@SerializableField(title = "Event Result (Pass, Fail, N/A)", order = 300, validators = { NotNullValidator.class, EventResultValidator.class })
	private String status;

	@SerializableField(title = "", order = 400, handler = OwnerSerializationHandler.class, validators = { NotNullValidator.class, OwnerExistsValidator.class })
	private final String[] owners = new String[3];

    @SerializableField(title = "", order = 450, handler = NewOwnerSerializationHandler.class, validators = { NotNullValidator.class, OwnerExistsValidator.class })
    private final String[] newOwners = new String[3];

	@SerializableField(title = "Performed By", order = 500, validators = { NotNullValidator.class, FullNameUserValidator.class })
	private String performedBy;

	@SerializableField(title = "Event Book", order = 600)
	private String eventBook;

	@SerializableField(title = "Printable (Y/N)", order = 700, validators = { YNValidator.class })
	private String printable;

	@SerializableField(title = "Asset Status", order = 800, validators = { AssetStatusExistsValidator.class })
	private String assetStatus;

    @SerializableField(title = "Event Status", order = 850, validators = { EventStatusExistsValidator.class })
    private String eventStatus;

    @SerializableField(title = "Next Event Date", order = 900, handler = DateSerializationHandler.class, validators = { DateValidator.class })
	private Object nextEventDate;

	@SerializableField(title = "Location", order = 1000, validators = {LocationValidator.class})
	private String location;	
	
	@SerializableField(title = "Comments", order = 1100)
	private String comments;

    @SerializableField(title = "Action Notes", order = 1200)
    private String notes;

	@SerializableField(title = "Criteria", order = 1300, handler=CriteriaResultSerializationHandler.class, validators = {CriteriaResultValidator.class})
	private Collection<CriteriaResultView> criteriaResults = new ArrayList<CriteriaResultView>();

	public EventView() {}

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public Object getDatePerformed() {
		return datePerformed;
	}

	public Date getDatePerformedAsDate() {
		if (datePerformed == null) {
			return null;
		} else if (!(datePerformed instanceof Date)) {
			throw new ClassCastException("datePerformed should have been instance of java.lang.Date but was " + datePerformed.getClass().getName());
		}

		return (Date)datePerformed;
	}
	
	public void setDatePerformed(Object datePerformed) {
		this.datePerformed = datePerformed;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getPerformedBy() {
		return performedBy;
	}

	public void setPerformedBy(String performedBy) {
		this.performedBy = performedBy;
	}

	public String getEventBook() {
		return eventBook;
	}

	public void setEventBook(String eventBook) {
		this.eventBook = eventBook;
	}

	public String getPrintable() {
		return printable;
	}

	public void setPrintable(String printable) {
		this.printable = printable;
	}

	public void setPrintable(boolean printable) {
		this.printable = (printable) ? "Y" : "N";
	}
	
	public Boolean isPrintable() {
		if (printable == null) {
			return null;
		}
		return (printable.equalsIgnoreCase("Y"));
	}
	
	public String getAssetStatus() {
		return assetStatus;
	}

	public void setAssetStatus(String assetStatus) {
		this.assetStatus = assetStatus;
	}

    public String getEventStatus() {
        return eventStatus;
    }

    public void setEventStatus(String eventStatus) {
        this.eventStatus = eventStatus;
    }

    public Object getNextEventDate() {
		return nextEventDate;
	}

	public Date getNextEventDateAsDate() {
		if (nextEventDate == null) {
			return null;
		} else if (!(nextEventDate instanceof Date)) {
			throw new ClassCastException("nextEventDate should have been instance of java.lang.Date but was " + nextEventDate.getClass().getName());
		}
		return (Date) nextEventDate;
	}
	
	public void setNextEventDate(Object nextEventDate) {
		this.nextEventDate = nextEventDate;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public String[] getOwners() {
		return owners;
	}

    public String getOwnerString() {
        return String.format("%s %s %s", getOrganization(), getCustomer(), getDivision());
    }

	public void setOrganization(String organization) {
		owners[OwnerSerializationHandler.ORGANIZATION_INDEX] = organization;
	}
	
	public String getOrganization() {
		return owners[OwnerSerializationHandler.ORGANIZATION_INDEX];
	}
	
	public void setCustomer(String customer) {
		owners[OwnerSerializationHandler.CUSTOMER_ID] = customer;
	}
	
	public String getCustomer() {
		return owners[OwnerSerializationHandler.CUSTOMER_ID];
	}
	
	public void setDivision(String division) {
		owners[OwnerSerializationHandler.DIVISION_INDEX] = division;
	}
	
	public String getDivision() {
		return owners[OwnerSerializationHandler.DIVISION_INDEX];
	}

    public String[] getNewOwners() {
        return newOwners;
    }

    public String getNewOwnerString() {
        return String.format("%s %s %s", getOrganization(), getCustomer(), getDivision());
    }

    public void setNewOrganization(String organization) {
        newOwners[NewOwnerSerializationHandler.ORGANIZATION_INDEX] = organization;
    }

    public String getNewOrganization() {
        return newOwners[NewOwnerSerializationHandler.ORGANIZATION_INDEX];
    }

    public void setNewCustomer(String customer) {
        newOwners[NewOwnerSerializationHandler.CUSTOMER_ID] = customer;
    }

    public String getNewCustomer() {
        return newOwners[NewOwnerSerializationHandler.CUSTOMER_ID];
    }

    public void setNewDivision(String division) {
        newOwners[NewOwnerSerializationHandler.DIVISION_INDEX] = division;
    }

    public String getNewDivision() {
        return newOwners[NewOwnerSerializationHandler.DIVISION_INDEX];
    }
	
	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	@Override
	public String getGlobalId() {   // DD : hmmm...why is this null? can they be edited on import?
		return null;
	}

	@Override
	public void setGlobalId(String globalId) {}

	public void setCriteriaResults(Collection<CriteriaResultView> criteriaResults) {
		this.criteriaResults = criteriaResults;
	}

	public Collection<CriteriaResultView> getCriteriaResults() {
		return criteriaResults;
	}

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Object getDueDate() {
        return dueDate;
    }

    public Date getDueDateAsDate() {
        if (dueDate == null) {
            return null;
        } else if (!(dueDate instanceof Date)) {
            throw new ClassCastException("dueDate should have been instance of java.lang.Date but was " + dueDate.getClass().getName());
        }
        return (Date) dueDate;
    }

    public void setDueDate(Object dueDate) {
        this.dueDate = dueDate;
    }
}
