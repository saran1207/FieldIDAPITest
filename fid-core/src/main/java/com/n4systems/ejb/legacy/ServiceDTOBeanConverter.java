package com.n4systems.ejb.legacy;

import com.n4systems.model.*;
import com.n4systems.model.orgs.*;
import com.n4systems.model.safetynetwork.OrgConnection;
import com.n4systems.model.tenant.SetupDataLastModDates;
import com.n4systems.model.user.User;
import com.n4systems.webservice.dto.*;

import java.io.IOException;

public interface ServiceDTOBeanConverter {

	public InspectionServiceDTO convert(Event event);

	public Asset convert(ProductServiceDTO productServiceDTO, Asset targetProduct, long tenantId);

	public ProductServiceDTO convert(Asset product);

	public ProductTypeServiceDTO convert_new(AssetType assetType);

	public TenantServiceDTO convert(PrimaryOrg tenant);

	public UserServiceDTO convert(User user);

	public InspectionBookServiceDTO convert(EventBook eventBook);

	public ProductTypeGroupServiceDTO convert(AssetTypeGroup assetTypeGroup);

	public JobServiceDTO convert(Project job);

	public SetupDataLastModDatesServiceDTO convert(SetupDataLastModDates setupModDates);

	public CustomerOrgServiceDTO convert(CustomerOrg customerOrg);

	public DivisionOrgServiceDTO convert(DivisionOrg divisionOrg);

	public InternalOrgServiceDTO convert(InternalOrg internalOrg);

	public VendorServiceDTO convert(OrgConnection orgConnections);

	public BaseOrg convert(long ownerId, long tenantId);

	public Event convert(InspectionScheduleServiceDTO inspectionScheduleServiceDTO, long tenantId);

	public FileAttachment convert(AbstractEvent event, InspectionImageServiceDTO inspectionImageServiceDTO, User performedBy) throws IOException;

	public Event convert(InspectionServiceDTO inspectionServiceDTO, EventSchedule schedule, Long tenantId) throws IOException;

	public User convert(UserServiceDTO user);

}
