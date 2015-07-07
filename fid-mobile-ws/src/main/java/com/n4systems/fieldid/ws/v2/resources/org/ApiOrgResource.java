package com.n4systems.fieldid.ws.v2.resources.org;

import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.n4systems.fieldid.service.amazon.S3Service;
import com.n4systems.fieldid.ws.v2.resources.SetupDataResource;
import com.n4systems.fieldid.ws.v2.resources.model.DateParam;
import com.n4systems.fieldid.ws.v2.resources.model.ListResponse;
import com.n4systems.model.AddressInfo;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereClauseFactory;
import com.n4systems.util.persistence.WhereParameter;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@Path("organization")
public class ApiOrgResource extends SetupDataResource<ApiOrg, BaseOrg> {

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

	/*
	Returns list of all customer logos and their corresponding org id.  This is used by mobile
	as a speed improvement to the synchronization process.
	 */
	@GET
	@Path("images")
	@Consumes(MediaType.TEXT_PLAIN)
	@Produces(MediaType.APPLICATION_JSON)
	@Transactional(readOnly = true)
	public ListResponse<ApiOrgImage> findAllOrgImages(@QueryParam("after") DateParam after) {
		/*
		Note: Rather than loading orgs first and fetching images, we fetch ALL customer logo images, parse the id
		from the filename and then load the org.  This is much faster because we do not know if the org has a logo
		image until we try and fetch it from S3.  Given 1000 orgs, that would be 1000 GET requests, most of which will 404
		since the ratio of org images to orgs is very low.
		 */
		List<S3ObjectSummary> s3Images = s3Service.getAllCustomerLogos();
		List<ApiOrgImage> orgImages = new ArrayList<>();

		// parse the id from the org
		Pattern p = Pattern.compile("^.*/" + S3Service.CUSTOMER_FILE_PREFIX + "(\\d+)\\." + S3Service.CUSTOMER_FILE_EXT + "$");
		for (S3ObjectSummary image: s3Images) {
			Matcher m = p.matcher(image.getKey());
			if (m.matches()) {
				/*
				We still need to attempt to load the org as it may be archived (the images are not removed when an org is archived).
				We can apply date filtering at the same time
				*/
				long orgId = Long.parseLong(m.group(1));
				QueryBuilder<BaseOrg> query = createTenantSecurityBuilder(BaseOrg.class).addWhere(WhereClauseFactory.create("id", orgId));
				if (after != null) {
					query.addWhere(WhereClauseFactory.create(WhereParameter.Comparator.GT, "modified", after));
				}

				if (persistenceService.exists(query)) {
					orgImages.add(new ApiOrgImage(orgId, s3Service.getCustomerLogoURL(orgId)));
				}
			}
		}
		ListResponse<ApiOrgImage> response = new ListResponse<>(orgImages, 0, 100, orgImages.size());
		return response;
	}

	private String convertAddress(AddressInfo addressInfo) {
		return addressInfo == null ? null : StringUtils.join(Arrays.asList(addressInfo.getStreetAddress(), addressInfo.getCity(), addressInfo.getState(), addressInfo.getZip()), ", ");
	}
}
