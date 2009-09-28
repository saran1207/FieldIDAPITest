package com.n4systems.webservice.server;

import java.util.List;

import com.n4systems.webservice.dto.AuthenticationRequest;
import com.n4systems.webservice.dto.AuthenticationResponse;
import com.n4systems.webservice.dto.AutoAttributeCriteriaListResponse;
import com.n4systems.webservice.dto.AutoAttributeDefinitionListResponse;
import com.n4systems.webservice.dto.CustomerOrgListResponse;
import com.n4systems.webservice.dto.CustomerServiceDTO;
import com.n4systems.webservice.dto.DivisionOrgListResponse;
import com.n4systems.webservice.dto.InspectionBookListResponse;
import com.n4systems.webservice.dto.InspectionListResponse;
import com.n4systems.webservice.dto.InspectionServiceDTO;
import com.n4systems.webservice.dto.InspectionTypeListResponse;
import com.n4systems.webservice.dto.InternalOrgListResponse;
import com.n4systems.webservice.dto.JobListResponse;
import com.n4systems.webservice.dto.JobSiteListResponse;
import com.n4systems.webservice.dto.MobileUpdateInfo;
import com.n4systems.webservice.dto.PaginatedRequestInformation;
import com.n4systems.webservice.dto.PaginatedUpdateRequestInfo;
import com.n4systems.webservice.dto.ProductListResponse;
import com.n4systems.webservice.dto.ProductServiceDTO;
import com.n4systems.webservice.dto.ProductTypeGroupListResponse;
import com.n4systems.webservice.dto.ProductTypeListResponse;
import com.n4systems.webservice.dto.RequestInformation;
import com.n4systems.webservice.dto.RequestResponse;
import com.n4systems.webservice.dto.SetupDataLastModDatesServiceDTO;
import com.n4systems.webservice.dto.StateSetListResponse;
import com.n4systems.webservice.dto.TransactionLogServiceDTO;
import com.n4systems.webservice.dto.UserListResponse;
import com.n4systems.webservice.dto.UserServiceDTO;
import com.n4systems.webservice.dto.VendorListResponse;
import com.n4systems.webservice.dto.WSJobSearchCriteria;
import com.n4systems.webservice.dto.WSSearchCritiera;
import com.n4systems.webservice.exceptions.InspectionException;
import com.n4systems.webservice.exceptions.ProductException;
import com.n4systems.webservice.exceptions.ServiceException;

public interface DataService {

	public AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest) throws ServiceException;
	public List<TransactionLogServiceDTO> getRecentTransactions(RequestInformation requestInformation, Long lastRevision) throws ServiceException;
	public InspectionTypeListResponse getAllInspectionTypes(PaginatedRequestInformation paginatedRequestInformation) throws ServiceException;
	public StateSetListResponse getAllStateSets(PaginatedRequestInformation paginatedRequestInformation) throws ServiceException;
	public ProductTypeListResponse getAllProductTypes(PaginatedRequestInformation paginatedRequestInformation) throws ServiceException;
	public InspectionBookListResponse getAllInspectionBooks(PaginatedRequestInformation paginatedRequestInformation) throws ServiceException;
	public JobSiteListResponse getAllJobSites(PaginatedRequestInformation paginatedRequestInformation) throws ServiceException;
	public AutoAttributeDefinitionListResponse getAutoAttributeDefinition(PaginatedRequestInformation paginatedRequestInformation) throws ServiceException;
	public AutoAttributeCriteriaListResponse getAutoAttributeCriteria(PaginatedRequestInformation paginatedRequestInformation) throws ServiceException;
	public ProductTypeGroupListResponse getAllProductTypeGroups(PaginatedRequestInformation paginatedRequestInformation) throws ServiceException;
	public UserListResponse getAllUsers(PaginatedRequestInformation paginatedRequestInformation) throws ServiceException;
	
	public RequestResponse createProduct( RequestInformation requestInformation, ProductServiceDTO productDTO ) throws ServiceException;
	public RequestResponse updateProduct( RequestInformation requestInformation, ProductServiceDTO productDTO ) throws ServiceException;
	public RequestResponse createInspections( RequestInformation requestInformation, List<InspectionServiceDTO> inspectionDTOs ) throws ServiceException, ProductException, InspectionException;
	public RequestResponse createCustomer(RequestInformation requestInformation, CustomerServiceDTO customerDTO) throws ServiceException;
	public RequestResponse createUser(RequestInformation requestInformation, UserServiceDTO customerDTO) throws ServiceException;
	
	public ProductListResponse getProducts(PaginatedRequestInformation requestInformation, WSSearchCritiera searchCriteria) throws ServiceException;
	public ProductListResponse getProductsByJob(PaginatedRequestInformation requestInformation, WSJobSearchCriteria searchCriteria) throws ServiceException;
	public InspectionListResponse getInspections(PaginatedRequestInformation requestInformation, WSSearchCritiera searchCriteria) throws ServiceException;
	public InspectionListResponse getInspectionsByJob(PaginatedRequestInformation requestInformation, WSJobSearchCriteria searchCriteria) throws ServiceException;
	public JobListResponse getAllJobs(PaginatedUpdateRequestInfo request) throws ServiceException;
	public SetupDataLastModDatesServiceDTO getSetupDataLastModDates(RequestInformation requestInformation) throws ServiceException;
	public MobileUpdateInfo getMobileUpdateInfo(String currentVersion) throws ServiceException;
	public CustomerOrgListResponse getAllCustomerOrgs(PaginatedRequestInformation requestInformation) throws ServiceException;	
	public DivisionOrgListResponse getAllDivisionOrgs(PaginatedRequestInformation requestInformation) throws ServiceException;
	public InternalOrgListResponse getAllInternalOrgs(PaginatedRequestInformation requestInformation) throws ServiceException;
	public VendorListResponse getAllVendors(PaginatedRequestInformation requestInformation) throws ServiceException;
}
