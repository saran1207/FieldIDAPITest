package fieldid.web.services.dto;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * This was created to accomodate the LegacyBaseServiceDTO vs. the new BaseServiceDTO 
 * and allow for the overlapping fields to be in one class
 * @author Jesse Miller
 *
 */
public abstract class AbstractBaseServiceDTO {

	public static final Long DEFAULT_DTO_VERSION = com.n4systems.webservice.dto.AbstractBaseServiceDTO.DEFAULT_DTO_VERSION; 
	
	public static final Long CURRENT_DTO_VERSION = com.n4systems.webservice.dto.AbstractBaseServiceDTO.CURRENT_DTO_VERSION;

	private static SimpleDateFormat DF = new SimpleDateFormat("MM/dd/yy hh:mm:ss a");
	
	protected String dtoVersion;
	protected String tenantId;
	
	public String getDtoVersion() {
		return dtoVersion;
	}

	public void setDtoVersion(String dtoVersion) {
		this.dtoVersion = dtoVersion;
	}
	
	public Long dtoVersion() {
		if( dtoVersion == null ) {
			return DEFAULT_DTO_VERSION; 
		}
		return convertToLong( dtoVersion );
		
	}

	public String getTenantId() {
		return tenantId;
	}
	public Long getTenantIdLong() {
		return convertToLong( tenantId );
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}
	public void setTenantId(Long tenantId) {
		this.tenantId = tenantId.toString();
	}
	
	public Long convertToLong( String longValue ) {
		try {
			return Long.parseLong( longValue );
		} catch( Exception e ) {
			return null;
		}
	}

	public static Date stringToDate(String originalDate){
		
		
		Date dateConvert = null;		
		try {
			dateConvert = DF.parse(originalDate);
		} catch (ParseException e) {
			// do nothing, return null
		} catch (NullPointerException e) {
			// do nothing, return null
		}
		
		 
		return dateConvert;
	}		
	
	public static String dateToString(Date originalDate){
		
		
		String dateConvert = null;		
		try {
			dateConvert = DF.format( originalDate );
		} catch (NullPointerException e) {
			// do nothing, return null
		}
		
		 
		return dateConvert;
	}


}
