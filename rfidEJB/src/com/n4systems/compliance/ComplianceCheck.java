package com.n4systems.compliance;

import java.io.Serializable;

public class ComplianceCheck implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private Long outOfCompliance;
	
	private Long totalAssets;

	public ComplianceCheck(Long outOfCompliance, Long totalAssets) {
		super();
		this.outOfCompliance = outOfCompliance;
		this.totalAssets = totalAssets;
	}

	public Long getOutOfCompliance() {
		return outOfCompliance;
	}

	public void setOutOfCompliance(Long outOfCompliance) {
		this.outOfCompliance = outOfCompliance;
	}

	public Long getTotalAssets() {
		return totalAssets;
	}

	public void setTotalAssets(Long totalAssets) {
		this.totalAssets = totalAssets;
	}
	
	
	public boolean hasComplianceValue() {
		return ( totalAssets > 0L );
	}
	
	public boolean isFullyCompliant() {
		return( outOfCompliance == 0L && hasComplianceValue() ); 
	}
	
	/**
	 * 
	 * @return percentage compliant floored, so 99.9999% will come as 99%
	 */
	public Float percentageCompliant() {
		if( totalAssets == 0L ) {
			return 0F;
		} else {
			double percentageCompliant = ( ( totalAssets - outOfCompliance ) / totalAssets.floatValue() );			
			return new Float(Math.floor(percentageCompliant*100)/100);
		}
	}
	
}
