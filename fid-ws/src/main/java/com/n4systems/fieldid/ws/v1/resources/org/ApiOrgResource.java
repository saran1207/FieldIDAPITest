package com.n4systems.fieldid.ws.v1.resources.org;

import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.n4systems.fieldid.service.amazon.S3Service;
import com.n4systems.fieldid.ws.v1.resources.SetupDataResource;
import com.n4systems.fieldid.ws.v1.resources.eventhistory.ApiPlaceEventHistoryResource;
import com.n4systems.fieldid.ws.v1.resources.eventtype.ApiPlaceEventTypeResource;
import com.n4systems.fieldid.ws.v1.resources.model.DateParam;
import com.n4systems.fieldid.ws.v1.resources.model.ListResponse;
import com.n4systems.fieldid.ws.v1.resources.savedEvent.ApiSavedPlaceEventResource;
import com.n4systems.model.AddressInfo;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereClauseFactory;
import com.n4systems.util.persistence.WhereParameter;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Component
@Path("organization")
public class ApiOrgResource extends SetupDataResource<ApiOrg, BaseOrg> {
	private static Logger logger = Logger.getLogger(ApiOrgResource.class);

    @Autowired
    private S3Service s3Service;

    @Autowired
    private ApiPlaceEventHistoryResource eventHistoryResource;

    @Autowired
    private ApiPlaceEventTypeResource eventTypeResource;

    //TODO Need to make use of this.
    @Autowired
    private ApiSavedPlaceEventResource savedPlaceEventResource;

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

		if (versionLessThan(1, 8, 0)) {
			apiOrg.setImage(loadOrgImage(baseOrg));
		}
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

		//Add Contact Information (put together from Contact and AddressInfo)
		convertContactInformation(apiOrg, baseOrg);

        apiOrg.setEventHistory(eventHistoryResource.findAllEventHistory(baseOrg.getId()));
        apiOrg.setEventTypes(baseOrg.getEventTypes().stream().map(eventTypeResource::convertToApiPlaceEvent).collect(Collectors.toList()));
        apiOrg.setEvents(savedPlaceEventResource.findLastEventOfEachType(baseOrg.getId()));

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
		Pattern p = Pattern.compile("^.*/" + s3Service.CUSTOMER_FILE_PREFIX + "(\\d+)\\." + s3Service.CUSTOMER_FILE_EXT + "$");
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
					orgImages.add(new ApiOrgImage(orgId, image.getKey()));
				}
			}
		}
		ListResponse<ApiOrgImage> response = new ListResponse<>(orgImages, 0, 100, orgImages.size());
		return response;
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

	private void convertContactInformation(ApiOrg apiOrg, BaseOrg baseOrg) {
		//name
		apiOrg.getContactInformation().setName(baseOrg.getContact().getName());
		//email
		apiOrg.getContactInformation().setEmail(baseOrg.getContact().getEmail());
		//streetAddress
		apiOrg.getContactInformation().setStreetAddress(baseOrg.getAddressInfo().getStreetAddress());
		//city
		apiOrg.getContactInformation().setCity(baseOrg.getAddressInfo().getCity());
		//state
		apiOrg.getContactInformation().setState(baseOrg.getAddressInfo().getState());
		//country
		apiOrg.getContactInformation().setCountry(baseOrg.getAddressInfo().getCountry());
		//zip
		apiOrg.getContactInformation().setZip(baseOrg.getAddressInfo().getZip());
		//phone1
		apiOrg.getContactInformation().setPhone1(baseOrg.getAddressInfo().getPhone1());
		//phone2
		apiOrg.getContactInformation().setPhone2(baseOrg.getAddressInfo().getPhone2());
		//fax1
		apiOrg.getContactInformation().setFax1(baseOrg.getAddressInfo().getFax1());
	}

}
