package com.n4systems.webservice.dto;


public abstract class AbstractBaseServiceDTO {

	public static final long DEFAULT_DTO_VERSION = 1L; 	
	public static final long CURRENT_DTO_VERSION = 7L;
	
	protected long dtoVersion;
	protected long id;
	
	public AbstractBaseServiceDTO() {
		setDtoVersion(CURRENT_DTO_VERSION);
	}
	
	public Long getDtoVersion() {
		return dtoVersion;
	}
	public void setDtoVersion(Long dtoVersion) {
		this.dtoVersion = dtoVersion;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	public boolean isCreatedOnMobile() {
		return !MobileDTOHelper.isValidServerId( id );
	}
	
}
