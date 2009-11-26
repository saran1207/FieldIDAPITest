package com.n4systems.webservice.dto;

import com.n4systems.util.GUIDHelper;

public class MobileDTOHelper {
	
	public static boolean isValidServerId( Long id ) {
		return ( id != null && id > 0 );
	}
	
	public static boolean isNullGUID( String guid ) {
		return GUIDHelper.isNullGUID( guid );
	}	

}
