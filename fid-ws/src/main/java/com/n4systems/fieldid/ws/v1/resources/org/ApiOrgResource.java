package com.n4systems.fieldid.ws.v1.resources.org;

import java.io.File;

import javax.ws.rs.Path;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.n4systems.fieldid.ws.v1.resources.SetupDataResource;
import com.n4systems.model.AddressInfo;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.reporting.PathHandler;

@Component
@Path("organization")
public class ApiOrgResource extends SetupDataResource<ApiOrg, BaseOrg> {
	private static Logger logger = Logger.getLogger(ApiOrgResource.class);
	
	public ApiOrgResource() {
		super(BaseOrg.class, true);
	}

	@Override
	protected ApiOrg convertEntityToApiModel(BaseOrg baseOrg) {
		ApiOrg apiOrg = new ApiOrg();
		apiOrg.setSid(baseOrg.getId());
		apiOrg.setModified(baseOrg.getModified());
		apiOrg.setActive(baseOrg.isActive());
		apiOrg.setName(baseOrg.getName());
		apiOrg.setImage(loadOrgImage(baseOrg));		
		apiOrg.setAddress(convertAddress(baseOrg.getAddressInfo()));
		
		if (baseOrg.getParent() != null) {
			apiOrg.setParentId(baseOrg.getParent().getId());
		}

		if (baseOrg.getSecondaryOrg() != null) {
			apiOrg.setSecondaryId(baseOrg.getSecondaryOrg().getId());
		}

		if (baseOrg.getCustomerOrg() != null) {
			apiOrg.setCustomerId(baseOrg.getCustomerOrg().getId());
		}

		if (baseOrg.getDivisionOrg() != null) {
			apiOrg.setDivisionId(baseOrg.getDivisionOrg().getId());
		}
		return apiOrg;
	}
	
	private byte[] loadOrgImage(BaseOrg baseOrg) {
		File imageFile = PathHandler.getOrgLogo(baseOrg);
		
		byte[] image = null;
		if (imageFile.exists()) {
			try {
				image = FileUtils.readFileToByteArray(imageFile);
			} catch(Exception e) {
				logger.warn("Unable to load organization image at: " + imageFile, e);
			}
		}
		return image;
	}

	private String convertAddress(AddressInfo addressInfo) {		
		if(addressInfo != null) {			
			StringBuilder builder = new StringBuilder();
			
			appendStringSafely(builder, addressInfo.getStreetAddress());
			appendStringSafely(builder, addressInfo.getCity());
			appendStringSafely(builder, addressInfo.getState());
			appendStringSafely(builder, addressInfo.getZip());
			
			return builder.toString();
		} else {
			return null;
		}
	}
	
	private void appendStringSafely(StringBuilder builder, String data) {
		if(data != null && data.length() != 0)
		{
			if(builder.length() > 0)
				builder.append(", ");
			
			builder.append(data);
		}
	}
}
