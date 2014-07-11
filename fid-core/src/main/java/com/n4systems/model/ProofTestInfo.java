package com.n4systems.model;

import com.n4systems.fileprocessing.ProofTestType;
import org.apache.commons.lang.ArrayUtils;
import org.hibernate.FetchMode;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.Type;

import java.io.Serializable;
import java.util.UUID;

import javax.persistence.*;

@Embeddable
public class ProofTestInfo implements Serializable {
	private static final long serialVersionUID = 2L;

	/*
	 *   XXX - Peak Load and Test Duration were strings on the old InspectionDoc.
	 *   My feeling is they should be moved to a Number (float or double) and the unit stored separately
	 *   - mf
	 */
    @Enumerated(EnumType.STRING)
    private ProofTestType proofTestType;
	private String peakLoad;
	private String duration;
	private String peakLoadDuration;
    private String proofTestFileName;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    private Character[] proofTestData;

	public String getPeakLoad() {
		return peakLoad;
	}

	public void setPeakLoad(String peakLoad) {
		this.peakLoad = peakLoad;
	}

	public String getDuration() {
		return duration;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}
	
	public ProofTestType getProofTestType() {
		return proofTestType;
	}

	public void setProofTestType(ProofTestType proofTestType) {
		this.proofTestType = proofTestType;
	}
	
	public boolean hasProofTestFile() {
		return (proofTestType != null);
	}

	public String getPeakLoadDuration() {
		return peakLoadDuration;
	}

	public void setPeakLoadDuration(String peakLoadDuration) {
		this.peakLoadDuration = peakLoadDuration;
	}

    public Character[] getProofTestData() {
        return proofTestData;
    }

    public void setProofTestData(String fileData) {
        if(fileData != null){
            this.proofTestData = ArrayUtils.toObject(fileData.toCharArray());
        }
        else {
            this.proofTestData = null;
        }

    }

    public void setProofTestData(Character[] fileData) {
        if(fileData != null){
            this.proofTestData = fileData.clone();
        }
        else {
            this.proofTestData = null;
        }
    }

    public String getProofTestFileName() {
        return proofTestFileName;
    }

    public void setProofTestFileName(String fileName) {
        this.proofTestFileName = fileName;
    }
	
}
