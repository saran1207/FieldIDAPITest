package com.n4systems.fieldid.ws.v2.resources.setupdata.org;

import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.n4systems.fieldid.service.amazon.S3Service;
import com.n4systems.fieldid.ws.v2.resources.setupdata.SetupDataResourceReadOnly;
import com.n4systems.model.AddressInfo;
import com.n4systems.model.orgs.BaseOrg;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.Path;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@Path("org")
public class ApiOrgResource extends SetupDataResourceReadOnly<ApiOrg, BaseOrg> {
	private static Logger logger = Logger.getLogger(ApiOrgResource.class);

    @Autowired
    private S3Service s3Service;

	public ApiOrgResource() {
		super(BaseOrg.class, true);
	}

	@Override
	protected List<ApiOrg> postConvertAllEntitiesToApiModels(List<ApiOrg> orgs) {
		/*
		Note: Rather than checking each org for an image, we fetch ALL customer logo images, parse the id
		from the filename and then match up the org.  This is much faster because we do not know if the org has a logo
		image until we try and fetch it from S3.  Given 1000 orgs, that would be 1000 GET requests, most of which will 404
		since the ratio of org images to orgs is very low.
		 */
		List<S3ObjectSummary> s3Images = s3Service.getAllCustomerLogos();

		// parse the id from the org
		Pattern p = Pattern.compile("^.*/" + S3Service.CUSTOMER_FILE_PREFIX + "(\\d+)\\." + S3Service.CUSTOMER_FILE_EXT + "$");
		for (S3ObjectSummary image: s3Images) {
			Matcher m = p.matcher(image.getKey());
			if (m.matches()) {
				long orgId = Long.parseLong(m.group(1));
				ApiOrg apiOrg = orgs.stream().filter(o -> o.getSid().equals(orgId)).findFirst().orElse(null);

				if (apiOrg != null) {
					try {
						apiOrg.setImage(s3Service.downloadCustomerLogo(orgId));
					} catch (IOException e) {
						logger.warn(e);
					}
				}
			}
		}
		return orgs;
	}

	@Override
	protected ApiOrg convertEntityToApiModel(BaseOrg baseOrg) {
		ApiOrg apiOrg = new ApiOrg();
		apiOrg.setSid(baseOrg.getId());
		apiOrg.setModified(baseOrg.getModified());
		apiOrg.setActive(baseOrg.isActive());
		apiOrg.setName(baseOrg.getName());
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

	private String convertAddress(AddressInfo addressInfo) {
		return addressInfo == null ? null : StringUtils.join(Arrays.asList(addressInfo.getStreetAddress(), addressInfo.getCity(), addressInfo.getState(), addressInfo.getZip()), ", ");
	}
}
