package com.n4systems.fieldid.ws.v1.resources.eventschedule;

import java.util.Date;
import java.util.List;

public class ApiTriggerEvent {
	private String name;
	private Date date;
	private String performedBy;
	private String criteria;
	private List<byte[]> images;
	private int imageTotal;
	
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
	
	public List<byte[]> getImages() {
		return images;
	}

	public void setImages(List<byte[]> images) {
		this.images = images;
		
		if(this.images != null)
			this.imageTotal = this.images.size();
	}
	
	public int getImageTotal() {
		return imageTotal;
	}

	public void setImageTotal(int imageTotal) {
		this.imageTotal = imageTotal;
	}
}
