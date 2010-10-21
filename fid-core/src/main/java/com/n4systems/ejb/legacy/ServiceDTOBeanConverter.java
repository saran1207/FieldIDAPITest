package com.n4systems.ejb.legacy;

import java.io.IOException;
import java.util.List;

import com.n4systems.model.Asset;
import com.n4systems.model.AssetType;
import rfid.ejb.entity.AssetStatus;

import com.n4systems.model.AbstractInspection;
import com.n4systems.model.AutoAttributeCriteria;
import com.n4systems.model.AutoAttributeDefinition;
import com.n4systems.model.FileAttachment;
import com.n4systems.model.Inspection;
import com.n4systems.model.InspectionBook;
import com.n4systems.model.InspectionGroup;
import com.n4systems.model.InspectionSchedule;
import com.n4systems.model.InspectionType;
import com.n4systems.model.AssetTypeGroup;
import com.n4systems.model.Project;
import com.n4systems.model.StateSet;
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
import com.n4systems.webservice.dto.InspectionScheduleServiceDTO;
import com.n4systems.webservice.dto.InspectionTypeServiceDTO;
import com.n4systems.webservice.dto.InternalOrgServiceDTO;
import com.n4systems.webservice.dto.JobServiceDTO;
import com.n4systems.webservice.dto.ProductServiceDTO;
import com.n4systems.webservice.dto.ProductTypeGroupServiceDTO;
import com.n4systems.webservice.dto.ProductTypeServiceDTO;
import com.n4systems.webservice.dto.SetupDataLastModDatesServiceDTO;
import com.n4systems.webservice.dto.StateSetServiceDTO;
import com.n4systems.webservice.dto.TenantServiceDTO;
import com.n4systems.webservice.dto.VendorServiceDTO;

import fieldid.web.services.dto.ProductStatusServiceDTO;

public interface ServiceDTOBeanConverter {

	public com.n4systems.webservice.dto.InspectionServiceDTO convert(Inspection inspection);
	public List<com.n4systems.webservice.dto.InspectionServiceDTO> convert(InspectionGroup inspectionGroup);
	public Asset convert( ProductServiceDTO productServiceDTO, Asset targetProduct, long tenantId );
	public ProductServiceDTO convert(Asset product);
	public InspectionTypeServiceDTO convert( InspectionType inspectionType );
	public StateSetServiceDTO convert( StateSet stateSet );
	public com.n4systems.webservice.dto.AutoAttributeCriteriaServiceDTO convert( AutoAttributeCriteria criteria );
	public com.n4systems.webservice.dto.AutoAttributeDefinitionServiceDTO convert( AutoAttributeDefinition definition );
	public ProductStatusServiceDTO convert( AssetStatus assetStatus);
	public ProductTypeServiceDTO convert_new( AssetType assetType);
	public TenantServiceDTO convert(PrimaryOrg tenant);
	public com.n4systems.webservice.dto.UserServiceDTO convert(User user);
	public InspectionBookServiceDTO convert(InspectionBook inspectionBook);
	public ProductTypeGroupServiceDTO convert(AssetTypeGroup assetTypeGroup);
	public JobServiceDTO convert(Project job);
	public SetupDataLastModDatesServiceDTO convert(SetupDataLastModDates setupModDates);
	public CustomerOrgServiceDTO convert(CustomerOrg customerOrg);
	public DivisionOrgServiceDTO convert(DivisionOrg divisionOrg);
	public InternalOrgServiceDTO convert(InternalOrg internalOrg);
	public VendorServiceDTO convert(OrgConnection orgConnections);
	
	
	public BaseOrg convert(long ownerId, long tenantId);
	
	
	public InspectionSchedule convert(InspectionScheduleServiceDTO inspectionScheduleServiceDTO, long tenantId);
	public FileAttachment convert( AbstractInspection inspection, com.n4systems.webservice.dto.InspectionImageServiceDTO inspectionImageServiceDTO, User performedBy) throws IOException;
	public Inspection convert( com.n4systems.webservice.dto.InspectionServiceDTO inspectionServiceDTO, Long tenantId ) throws IOException;
	public User convert(com.n4systems.webservice.dto.UserServiceDTO user);
	
}
