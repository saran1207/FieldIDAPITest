package com.n4systems.fieldid.ws.v2.resources.event;

import java.net.URL;
import java.util.Date;
import java.util.List;

public class ApiTriggerEvent {
	private String name;
	private Date date;
	private String performedBy;
	private String criteria;
    private List<String> imageComments;
	private List<URL> imageUrls;
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
	
	public String getPerformedBy() {
		return performedBy;
	}

	public void setPerformedBy(String performedBy) {
		this.performedBy = performedBy;
	}
	
	public String getCriteria() {
		return criteria;
	}

	public void setCriteria(String criteria) {
		this.criteria = criteria;
	}

    public List<String> getImageComments() {
        return imageComments;
    }

    public void setImageComments(List<String> imageComments) {
        this.imageComments = imageComments;
    }

	public List<URL> getImageUrls() {
		return imageUrls;
	}

	public void setImageUrls(List<URL> imageUrls) {
		this.imageUrls = imageUrls;
	}
}
