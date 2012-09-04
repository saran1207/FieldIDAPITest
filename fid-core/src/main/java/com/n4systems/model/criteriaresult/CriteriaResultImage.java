package com.n4systems.model.criteriaresult;


import com.n4systems.model.BaseEntity;
import com.n4systems.model.CriteriaResult;

import javax.persistence.*;

@Entity
@Table(name = "criteriaresult_images")
public class CriteriaResultImage extends BaseEntity {

	@ManyToOne(optional = false)
	@JoinColumn(name="criteriaresult_id")
	private CriteriaResult criteriaResult;

	@Column(name = "file_name", nullable = false)
	private String fileName;

	@Column(nullable = true)
	private String comments;

	@Transient
	private byte[] imageData;

	@Transient
	private String contentType;

	public CriteriaResult getCriteriaResult() {
		return criteriaResult;
	}

	public void setCriteriaResult(CriteriaResult criteriaResult) {
		this.criteriaResult = criteriaResult;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public byte[] getImageData() {
		return imageData;
	}

	public void setImageData(byte[] imageData) {
		this.imageData = imageData;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
}
