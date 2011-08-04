package com.n4systems.webservice.server;

import java.util.List;

import com.n4systems.webservice.assetdownload.AssetIdListResponse;
import com.n4systems.webservice.assetdownload.AssetListResponse;
import com.n4systems.webservice.assetdownload.AssetRequest;
import com.n4systems.webservice.assetdownload.AssetSearchRequest;
import com.n4systems.webservice.dto.AssetImageServiceDTO;
import com.n4systems.webservice.dto.AuthenticationRequest;
import com.n4systems.webservice.dto.AuthenticationResponse;
import com.n4systems.webservice.dto.CompletedJobScheduleRequest;
import com.n4systems.webservice.dto.CustomerOrgListResponse;
import com.n4systems.webservice.dto.DivisionOrgListResponse;
import com.n4systems.webservice.dto.EmployeeListResponse;
import com.n4systems.webservice.dto.InspectionBookListResponse;
import com.n4systems.webservice.dto.InspectionImageServiceDTO;
import com.n4systems.webservice.dto.InspectionServiceDTO;
import com.n4systems.webservice.dto.InternalOrgListResponse;
import com.n4systems.webservice.dto.JobListResponse;
import com.n4systems.webservice.dto.MobileUpdateInfo;
import com.n4systems.webservice.dto.PaginatedRequestInformation;
import com.n4systems.webservice.dto.PaginatedUpdateRequestInfo;
import com.n4systems.webservice.dto.ProductServiceDTO;
import com.n4systems.webservice.dto.ProductTypeGroupListResponse;
import com.n4systems.webservice.dto.ProductTypeListResponse;
import com.n4systems.webservice.dto.RequestInformation;
import com.n4systems.webservice.dto.RequestResponse;
import com.n4systems.webservice.dto.SetupDataLastModDatesServiceDTO;
import com.n4systems.webservice.dto.VendorListResponse;
import com.n4systems.webservice.dto.findinspection.FindInspectionRequestInformation;
import com.n4systems.webservice.dto.findinspection.FindInspectionResponse;
import com.n4systems.webservice.dto.findproduct.FindProductRequestInformation;
import com.n4systems.webservice.dto.findproduct.FindProductResponse;
import com.n4systems.webservice.dto.hello.HelloRequest;
import com.n4systems.webservice.dto.hello.HelloResponse;
import com.n4systems.webservice.dto.inspectionschedule.InspectionScheduleRequest;
import com.n4systems.webservice.dto.limitedproductupdate.LimitedProductUpdateRequest;
import com.n4systems.webservice.dto.limitedproductupdate.UpdateProductByCustomerRequest;
import com.n4systems.webservice.exceptions.InspectionException;
import com.n4systems.webservice.exceptions.ProductException;
import com.n4systems.webservice.exceptions.ServiceException;
import com.n4systems.webservice.predefinedlocation.PredefinedLocationListResponse;

@SuppressWarnings("deprecation")
public interface DataService {

	public HelloResponse hello(HelloRequest helloRequest) throws ServiceException;
	public AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest) throws ServiceException;
	public ProductTypeListResponse getAllProductTypes(PaginatedRequestInformation paginatedRequestInformation) throws ServiceException;
	public InspectionBookListResponse getAllInspectionBooks(PaginatedRequestInformation paginatedRequestInformation) throws ServiceException;
	public ProductTypeGroupListResponse getAllProductTypeGroups(PaginatedRequestInformation paginatedRequestInformation) throws ServiceException;
	public EmployeeListResponse getAllEmployees(PaginatedRequestInformation paginatedRequestInformation) throws ServiceException;
	public PredefinedLocationListResponse getAllPredefinedLocations(PaginatedRequestInformation paginatedRequestInformation) throws ServiceException;
	
	public RequestResponse createProduct( RequestInformation requestInformation, ProductServiceDTO productDTO ) throws ServiceException;
	public RequestResponse updateProduct( RequestInformation requestInformation, ProductServiceDTO productDTO ) throws ServiceException;
	public RequestResponse limitedProductUpdate(LimitedProductUpdateRequest request) throws ServiceException;		
	public RequestResponse updateProductByCustomer(UpdateProductByCustomerRequest request) throws ServiceException;		
	public RequestResponse createInspections( RequestInformation requestInformation, List<InspectionServiceDTO> inspectionDTOs ) throws ServiceException, ProductException, InspectionException;
	public RequestResponse createInspectionImage(RequestInformation requestInformation, InspectionImageServiceDTO inspectionImageServiceDTO ) throws ServiceException, ProductException, InspectionException;
	public RequestResponse updateAssetImage(RequestInformation requestInformation, AssetImageServiceDTO assetImageServiceDTO) throws ServiceException;
	
	public FindProductResponse findProduct(FindProductRequestInformation requestInformation) throws ServiceException;
	public FindInspectionResponse findInspection(FindInspectionRequestInformation requestInformation) throws ServiceException;
	public JobListResponse getAllJobs(PaginatedUpdateRequestInfo request) throws ServiceException;
	public SetupDataLastModDatesServiceDTO getSetupDataLastModDates(RequestInformation requestInformation) throws ServiceException;
	public MobileUpdateInfo getMobileUpdateInfo(String currentVersion) throws ServiceException;
	public CustomerOrgListResponse getAllCustomerOrgs(PaginatedRequestInformation requestInformation) throws ServiceException;	
	public DivisionOrgListResponse getAllDivisionOrgs(PaginatedRequestInformation requestInformation) throws ServiceException;
	public InternalOrgListResponse getAllInternalOrgs(PaginatedRequestInformation requestInformation) throws ServiceException;
	public VendorListResponse getAllVendors(PaginatedRequestInformation requestInformation) throws ServiceException;
	
	public RequestResponse createInspectionSchedule(InspectionScheduleRequest request) throws ServiceException;
	public RequestResponse updateInspectionSchedule(InspectionScheduleRequest request) throws ServiceException;
	public RequestResponse removeInspectionSchedule(InspectionScheduleRequest request) throws ServiceException;
	public RequestResponse createCompletedJobSchedule(CompletedJobScheduleRequest request) throws ServiceException;
	
	public AssetIdListResponse getAssetIds(AssetSearchRequest search) throws ServiceException;
	public AssetListResponse getAssets(AssetRequest request) throws ServiceException;
}
