package com.n4systems.model;

import com.n4systems.fileprocessing.ProofTestType;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Embeddable
public class ProofTestInfo implements Serializable {
	private static final long serialVersionUID = 1L;

	@Enumerated(EnumType.STRING)
	private ProofTestType proofTestType;
	
	/*
	 *   XXX - Peak Load and Test Duration were strings on the old InspectionDoc.
	 *   My feeling is they should be moved to a Number (float or double) and the unit stored separately
	 *   - mf
	 */
	private String peakLoad;	
	private String duration;
	private String peakLoadDuration;
	
	public ProofTestInfo() {}

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

	
}
