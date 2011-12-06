package com.n4systems.fieldid.ws.v1.resources.org;

import java.io.File;

import javax.ws.rs.Path;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.n4systems.fieldid.ws.v1.resources.SetupDataResource;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.reporting.PathHandler;

@Component
@Path("organization")
public class ApiOrgResource extends SetupDataResource<ApiOrg, BaseOrg> {
	private static Logger logger = Logger.getLogger(ApiOrgResource.class);
	
	public ApiOrgResource() {
		super(BaseOrg.class);
	}

	@Override
	protected ApiOrg convertEntityToApiModel(BaseOrg baseOrg) {
		ApiOrg apiOrg = new ApiOrg();
		apiOrg.setSid(baseOrg.getId());
		apiOrg.setModified(baseOrg.getModified());
		apiOrg.setActive(baseOrg.isActive());
		apiOrg.setName(baseOrg.getName());
		apiOrg.setImage(loadOrgImage(baseOrg));
		
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

}
