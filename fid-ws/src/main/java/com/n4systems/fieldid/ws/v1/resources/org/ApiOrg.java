package com.n4systems.fieldid.ws.v1.resources.org;

import com.n4systems.fieldid.ws.v1.resources.eventhistory.ApiPlaceEventHistory;
import com.n4systems.fieldid.ws.v1.resources.eventtype.ApiEventType;
import com.n4systems.fieldid.ws.v1.resources.model.ApiReadonlyModel;
import com.n4systems.fieldid.ws.v1.resources.savedEvent.ApiSavedPlaceEvent;

import java.util.List;

public class ApiOrg extends ApiReadonlyModel {
	private String name;
	private ContactInformation contactInformation;
	private Long parentId;
	private Long secondaryId;
	private Long customerId;
	private Long divisionId;
	private byte[] image;
	private String address;
    private List<ApiPlaceEventHistory> eventHistory;
    private List<ApiEventType> eventTypes;
    private List<ApiSavedPlaceEvent> events;

	public ApiOrg() {
		contactInformation = new ContactInformation();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ContactInformation getContactInformation() {
		return contactInformation;
	}

	public void setContactInformation (ContactInformation contactInformation) {
		this.contactInformation = contactInformation;
	}

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public Long getSecondaryId() {
		return secondaryId;
	}

	public void setSecondaryId(Long secondaryId) {
		this.secondaryId = secondaryId;
	}

	public Long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}

	public Long getDivisionId() {
		return divisionId;
	}

	public void setDivisionId(Long divisionId) {
		this.divisionId = divisionId;
	}

	public byte[] getImage() {
		return image;
	}

	public void setImage(byte[] image) {
		this.image = image;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getAddress() {
		return address;
	}

    public List<ApiPlaceEventHistory> getEventHistory() {
        return eventHistory;
    }

    public void setEventHistory(List<ApiPlaceEventHistory> eventHistory) {
        this.eventHistory = eventHistory;
    }

    public List<ApiEventType> getEventTypes() {
        return eventTypes;
    }

    public void setEventTypes(List<ApiEventType> eventTypes) {
        this.eventTypes = eventTypes;
    }

    public List<ApiSavedPlaceEvent> getEvents() {
        return events;
    }

    public void setEvents(List<ApiSavedPlaceEvent> events) {
        this.events = events;
    }

}

class ContactInformation {
	public String name = "";
	public String email = "";
	private String streetAddress = "";
	private String city = "";
	private String state = "";
	private String country = "";
	private String zip = "";
	private String phone1 = "";
	private String phone2 = "";
	private String fax1 = "";

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getStreetAddress() {
		return streetAddress;
	}

	public void setStreetAddress(String streetAddress) {
		this.streetAddress = streetAddress;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getZip() {
		return zip;
	}

	public void setZip(String zip) {
		this.zip = zip;
	}

	public String getPhone1() {
		return phone1;
	}

	public void setPhone1(String phone1) {
		this.phone1 = phone1;
	}

	public String getPhone2() {
		return phone2;
	}

	public void setPhone2(String phone2) {
		this.phone2 = phone2;
	}

	public String getFax1() {
		return fax1;
	}

	public void setFax1(String fax1) {
		this.fax1 = fax1;
	}
}