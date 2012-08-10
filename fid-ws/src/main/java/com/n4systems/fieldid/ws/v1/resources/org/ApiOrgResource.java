package com.n4systems.fieldid.ws.v1.resources.org;

import javax.ws.rs.Path;

import com.n4systems.fieldid.service.amazon.S3Service;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.n4systems.fieldid.ws.v1.resources.SetupDataResource;
import com.n4systems.model.AddressInfo;
import com.n4systems.model.orgs.BaseOrg;

@Component
@Path("organization")
public class ApiOrgResource extends SetupDataResource<ApiOrg, BaseOrg> {
	private static Logger logger = Logger.getLogger(ApiOrgResource.class);

    @Autowired
    private S3Service s3Service;

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
        byte[] image = null;
        try {
            image = s3Service.downloadCustomerLogo(baseOrg.getId());
        } catch(Exception e) {
            logger.warn("Unable to load organization image at: " + baseOrg.getId(), e);
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
