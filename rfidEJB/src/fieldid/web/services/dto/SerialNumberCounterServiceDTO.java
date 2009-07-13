package fieldid.web.services.dto;

import java.util.Date;


public class SerialNumberCounterServiceDTO extends LegacyBaseServiceDTO {
	
	
	
	private long r_Manufacture;
	private String decimalFormat;
	private long daysToReset;
	private Date lastReset;
	
	
	
	/**
	 * use tenantId instead
	 * Deprecated since version 4
	 * 
	 */
	@Deprecated
	public long getR_Manufacture() {
		return r_Manufacture;
	}
	
	/**
	 * use tenantId instead
	 * Deprecated since version 4
	 * 
	 */
	@Deprecated
	public void setR_Manufacture(long manufacture) {
		r_Manufacture = manufacture;
	}
	
	public String getDecimalFormat() {
		return decimalFormat;
	}
	public void setDecimalFormat(String decimalFormat) {
		this.decimalFormat = decimalFormat;
	}
	public long getDaysToReset() {
		return daysToReset;
	}
	public void setDaysToReset(long daysToReset) {
		this.daysToReset = daysToReset;
	}
	public Date getLastReset() {
		return lastReset;
	}
	public void setLastReset(Date lastReset) {
		this.lastReset = lastReset;
	}
	
}
