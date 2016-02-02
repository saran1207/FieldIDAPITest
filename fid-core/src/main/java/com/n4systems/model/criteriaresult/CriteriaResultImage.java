package com.n4systems.model.criteriaresult;


import com.n4systems.model.BaseEntity;
import com.n4systems.model.CriteriaResult;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "criteriaresult_images")
public class CriteriaResultImage extends BaseEntity {

	private String mobileGUID;

	@ManyToOne(optional = false)
	@JoinColumn(name="criteriaresult_id")
	private CriteriaResult criteriaResult;

	@Column(name = "file_name", nullable = false)
	private String fileName;

	@Column(nullable = true)
	private String comments;

    @Column(name = "md5sum")
    private String md5sum;

	@Transient
	private String contentType;

    @Transient
    private String tempFileName;

	@Override
	protected void onCreate() {
		super.onCreate();
		adjustAssetForSave();
	}

	@Override
	protected void onUpdate() {
		super.onUpdate();
		adjustAssetForSave();
	}

	private void adjustAssetForSave() {
		ensureMobileGuidIsSet();
	}

	public void ensureMobileGuidIsSet() {
		if (getMobileGUID() == null || getMobileGUID().length() == 0) {
			setMobileGUID(UUID.randomUUID().toString());
		}
	}

	public String getMobileGUID() {
		return mobileGUID;
	}

	public void setMobileGUID(String mobileGUID) {
		this.mobileGUID = mobileGUID;
	}

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

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

    public String getTempFileName() {
        return tempFileName;
    }

    public void setTempFileName(String tempFileName) {
        this.tempFileName = tempFileName;
    }

    public String getMd5sum() {
        return md5sum;
    }

    public void setMd5sum(String md5sum) {
        this.md5sum = md5sum;
    }

}
