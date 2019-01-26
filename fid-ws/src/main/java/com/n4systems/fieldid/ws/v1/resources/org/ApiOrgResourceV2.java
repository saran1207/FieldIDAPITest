package com.n4systems.fieldid.ws.v1.resources.org;

import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.n4systems.fieldid.service.amazon.S3Service;
import com.n4systems.fieldid.service.asset.AssetService;
import com.n4systems.fieldid.service.org.OrgService;
import com.n4systems.fieldid.ws.v1.resources.SetupDataResource;
import com.n4systems.fieldid.ws.v1.resources.eventhistory.ApiPlaceEventHistoryResource;
import com.n4systems.fieldid.ws.v1.resources.eventtype.ApiPlaceEventTypeResource;
import com.n4systems.fieldid.ws.v1.resources.model.DateParam;
import com.n4systems.fieldid.ws.v1.resources.model.ListResponse;
import com.n4systems.fieldid.ws.v1.resources.savedEvent.ApiSavedPlaceEventResource;
import com.n4systems.model.AddressInfo;
import com.n4systems.model.Contact;
import com.n4systems.model.api.Archivable;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.orgs.OrgIdTree;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereClauseFactory;
import com.n4systems.util.persistence.WhereParameter;
import com.newrelic.api.agent.Trace;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@Path("organizationV2")
public class ApiOrgResourceV2 extends SetupDataResource<ApiOrgV2, BaseOrg> {
    private static Logger logger = Logger.getLogger(ApiOrgResource.class);

    @Autowired
    private S3Service s3Service;

    @Autowired
    private ApiPlaceEventHistoryResource eventHistoryResource;

    @Autowired
    private ApiPlaceEventTypeResource eventTypeResource;

    @Autowired
    private AssetService assetService;

    //TODO Need to make use of this.
    @Autowired
    private ApiSavedPlaceEventResource savedPlaceEventResource;

    @Autowired
    private OrgService orgService;

    public ApiOrgResourceV2() {
        super(BaseOrg.class, true);
    }


    @Override
    protected QueryBuilder<BaseOrg> createFindAllBuilder(Date after) {
        QueryBuilder<BaseOrg> builder = createTenantSecurityBuilder(BaseOrg.class, true);
        builder.addWhere(WhereClauseFactory.create(WhereParameter.Comparator.NE, "state", Archivable.EntityState.RETIRED));
        if (after != null) {
            builder.addWhere(WhereClauseFactory.create(WhereParameter.Comparator.GT, "modified", after));
        }
        builder.addOrder("id");
        addTermsToBuilder(builder);
        return builder;
    }

    @Override
    protected ApiOrgV2 convertEntityToApiModel(BaseOrg baseOrg) {
        ApiOrgV2 apiOrg = new ApiOrgV2();
        apiOrg.setSid(baseOrg.getId());
        apiOrg.setModified(baseOrg.getModified());
        apiOrg.setActive(baseOrg.isActive());
        apiOrg.setName(baseOrg.getName());

        if (versionLessThan(1, 8, 0) && !versionEqualOrGreaterThan(2014, 1, 0)) {
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

        apiOrg.setAssetCount(assetService.getAssetCountByOrg(baseOrg.getId()));
        apiOrg.setOfflineAssetCount(assetService.getOfflineAssetCountByOrg(baseOrg.getId()));

        return apiOrg;
    }

    @GET
    @Path("visibleOrgsTree")
    @Produces(MediaType.APPLICATION_JSON)
    @Trace  (dispatcher=true)
    @Transactional(readOnly = true)
    public Response findVisibleOrgsTree() {
        setNewRelicWithAppInfoParameters();
        List<OrgIdTree> results = orgService.getIdVisibleOrgsIdTree();

        System.out.println("TEST");

        return Response.ok().entity(results).build();
    }

    @GET
    @Path("visibleOrgs")
    @Produces(MediaType.APPLICATION_JSON)
    @Trace  (dispatcher=true)
    @Transactional(readOnly = true)
    public Response findVisibleOrgIds() {
        setNewRelicWithAppInfoParameters();
        List<Long> results = orgService.getIdOfAllVisibleOrgs();

        return Response.ok().entity(results).build();
    }

    /*
    Returns list of all customer logos and their corresponding org id.  This is used by mobile
    as a speed improvement to the synchronization process.
     */
    @GET
    @Path("images")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    @Trace  (dispatcher=true)
    @Transactional(readOnly = true)
    public ListResponse<ApiOrgImage> findAllOrgImages(@QueryParam("after") DateParam after) {
        /*
        Note: Rather than loading orgs first and fetching images, we fetch ALL customer logo images, parse the id
        from the filename and then load the org.  This is much faster because we do not know if the org has a logo
        image until we try and fetch it from S3.  Given 1000 orgs, that would be 1000 GET requests, most of which will 404
        since the ratio of org images to orgs is very low.
         */
        setNewRelicWithAppInfoParameters();
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
                    orgImages.add(new ApiOrgImage(orgId, image.getKey()));
                }
            }
        }
        ListResponse<ApiOrgImage> response = new ListResponse<>(orgImages, 0, 100, orgImages.size());
        return response;
    }

    private byte[] loadOrgImage(BaseOrg baseOrg) {
        byte[] image = null;
        if(s3Service.customerLogoExists(baseOrg.getId())) {
            try {
                image = s3Service.downloadCustomerLogo(baseOrg.getId());
            } catch (Exception e) {
                logger.warn("Unable to load organization image at: " + baseOrg.getId(), e);
            }
            return image;
        } else {
            return new byte[0];
        }
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

    private void convertContactInformation(ApiOrgV2 apiOrg, BaseOrg baseOrg) {
        ContactInformationV2 contactInformation = apiOrg.getContactInformation();
        if (contactInformation == null) {
            contactInformation = new ContactInformationV2();
            apiOrg.setContactInformation(contactInformation);
        }

        final Contact contact = baseOrg.getContact();
        if (contact != null) {
            //name
            if (contact.getName() != null) {
                contactInformation.setName(contact.getName());
            }
            //email
            if (contact.getEmail() != null) {
                contactInformation.setEmail(contact.getEmail());
            }
        }

        final AddressInfo addressInfo = baseOrg.getAddressInfo();
        if (addressInfo != null) {
            //streetAddress
            if(addressInfo.getStreetAddress()!= null) {
                contactInformation.setStreetAddress(addressInfo.getStreetAddress());
            }
            //city
            if(addressInfo.getCity() != null) {
                contactInformation.setCity(addressInfo.getCity());
            }
            //state
            if(addressInfo.getState() != null) {
                contactInformation.setState(addressInfo.getState());
            }
            //country
            if(addressInfo.getCountry() != null) {
                contactInformation.setCountry(addressInfo.getCountry());
            }
            //zip
            if(addressInfo.getZip() != null) {
                contactInformation.setZip(addressInfo.getZip());
            }
            //phone1
            if(addressInfo.getPhone1() != null) {
                contactInformation.setPhone1(addressInfo.getPhone1());
            }
            //phone2
            if(addressInfo.getPhone2() != null) {
                contactInformation.setPhone2(addressInfo.getPhone2());
            }
            //fax1
            if(addressInfo.getFax1() != null) {
                contactInformation.setFax1(addressInfo.getFax1());
            }
        }
    }

}
