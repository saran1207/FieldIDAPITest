package com.n4systems.fieldid.ws.v1.resources.offlineprofile;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.n4systems.fieldid.service.asset.AssetService;
import com.n4systems.fieldid.service.offlineprofile.OfflineProfileService;
import com.n4systems.fieldid.ws.v1.exceptions.NotFoundException;
import com.n4systems.fieldid.ws.v1.resources.ApiResource;
import com.n4systems.model.Asset;
import com.n4systems.model.offlineprofile.OfflineProfile;
import com.n4systems.model.orgs.BaseOrg;

@Path("/offlineProfile")
@Component
public class ApiOfflineProfileResource extends ApiResource<ApiOfflineProfile, OfflineProfile>  {
	
	@Autowired private OfflineProfileService offlineProfileService;
	@Autowired private AssetService assetService;
	
	@PUT
	@Path("asset/{assetId}")
	@Consumes(MediaType.TEXT_PLAIN)
	@Transactional
	public void addAsset(@PathParam("assetId") String assetId) {
		OfflineProfile profile = offlineProfileService.findOrCreate(getCurrentUser());
		
		// do a check to ensure the asset exists before adding it
		Asset asset = assetService.findByMobileId(assetId);
		if (asset == null) {
			throw new NotFoundException("Asset", assetId);
		}
		
		profile.getAssets().add(assetId);
		offlineProfileService.update(profile);
	}
	
	@DELETE
	@Path("asset/{assetId}")
	@Consumes(MediaType.TEXT_PLAIN)
	@Transactional
	public void removeAsset(@PathParam("assetId") String assetId) {
		OfflineProfile profile = offlineProfileService.find(getCurrentUser());
		if (profile == null) {
			return;
		}
		
		if (profile.getAssets().remove(assetId)) {
			offlineProfileService.update(profile);
		}
	}
	
	@PUT
	@Path("organization/{orgId}")
	@Consumes(MediaType.TEXT_PLAIN)
	@Transactional
	public void addOrganization(@PathParam("orgId") Long orgId) {
		OfflineProfile profile = offlineProfileService.findOrCreate(getCurrentUser());
		
		// check to see that the org exsists before adding it
		BaseOrg org = persistenceService.find(BaseOrg.class, orgId);
		if (org == null) {
			throw new NotFoundException("Organization", orgId);
		}
		
		profile.getOrganizations().add(orgId);
		offlineProfileService.update(profile);
	}
	
	@DELETE
	@Path("organization/{orgId}")
	@Consumes(MediaType.TEXT_PLAIN)
	@Transactional
	public void removeOrganization(@PathParam("orgId") Long orgId) {
		OfflineProfile profile = offlineProfileService.find(getCurrentUser());
		if (profile == null) {
			return;
		}
		
		if (profile.getOrganizations().remove(orgId)) {
			offlineProfileService.update(profile);
		}
	}

	@Override
	public ApiOfflineProfile convertEntityToApiModel(OfflineProfile profile) {
		ApiOfflineProfile apiProfile = new ApiOfflineProfile();
		apiProfile.setUserId(profile.getUser().getId());
		apiProfile.setSyncDuration(profile.getSyncDuration());
		apiProfile.getAssets().addAll(profile.getAssets());
		apiProfile.getOrganizations().addAll(profile.getOrganizations());
		return apiProfile;
	}
	
}
