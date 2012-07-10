package com.n4systems.ejb.legacy;

import java.io.IOException;
import java.util.List;

import com.n4systems.model.AbstractEvent;
import com.n4systems.model.Asset;
import com.n4systems.model.AssetType;
import com.n4systems.model.AssetTypeGroup;
import com.n4systems.model.Event;
import com.n4systems.model.EventBook;
import com.n4systems.model.EventGroup;
import com.n4systems.model.EventSchedule;
import com.n4systems.model.FileAttachment;
import com.n4systems.model.Project;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.orgs.CustomerOrg;
import com.n4systems.model.orgs.DivisionOrg;
import com.n4systems.model.orgs.InternalOrg;
import com.n4systems.model.orgs.PrimaryOrg;
import com.n4systems.model.safetynetwork.OrgConnection;
import com.n4systems.model.tenant.SetupDataLastModDates;
import com.n4systems.model.user.User;
import com.n4systems.webservice.dto.CustomerOrgServiceDTO;
import com.n4systems.webservice.dto.DivisionOrgServiceDTO;
import com.n4systems.webservice.dto.InspectionBookServiceDTO;
import com.n4systems.webservice.dto.InspectionImageServiceDTO;
import com.n4systems.webservice.dto.InspectionScheduleServiceDTO;
import com.n4systems.webservice.dto.InspectionServiceDTO;
import com.n4systems.webservice.dto.InternalOrgServiceDTO;
import com.n4systems.webservice.dto.JobServiceDTO;
import com.n4systems.webservice.dto.ProductServiceDTO;
import com.n4systems.webservice.dto.ProductTypeGroupServiceDTO;
import com.n4systems.webservice.dto.ProductTypeServiceDTO;
import com.n4systems.webservice.dto.SetupDataLastModDatesServiceDTO;
import com.n4systems.webservice.dto.TenantServiceDTO;
import com.n4systems.webservice.dto.UserServiceDTO;
import com.n4systems.webservice.dto.VendorServiceDTO;

public interface ServiceDTOBeanConverter {

	public InspectionServiceDTO convert(Event event);

	public List<InspectionServiceDTO> convert(EventGroup eventGroup);

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
