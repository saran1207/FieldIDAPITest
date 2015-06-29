package com.n4systems.fieldid.api.mobile.resources.eventschedule;

import java.util.Date;
import java.util.List;

public class ApiTriggerEvent {
	private String name;
	private Date date;
	private String performedBy;
	private String criteria;
    private List<String> imageComments;
	private List<byte[]> images;
	private List<String> imagePaths;
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

	public List<String> getImagePaths() {
		return imagePaths;
	}

	public void setImagePaths(List<String> imagePaths) {
		this.imagePaths = imagePaths;
	}

	public int getImageTotal() {
		return imageTotal;
	}

	public void setImageTotal(int imageTotal) {
		this.imageTotal = imageTotal;
	}

    public List<String> getImageComments() {
        return imageComments;
    }

    public void setImageComments(List<String> imageComments) {
        this.imageComments = imageComments;
    }
}
