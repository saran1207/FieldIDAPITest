package com.n4systems.webservice.server;

import com.n4systems.webservice.assetdownload.AssetIdListResponse;
import com.n4systems.webservice.assetdownload.AssetListResponse;
import com.n4systems.webservice.assetdownload.AssetRequest;
import com.n4systems.webservice.assetdownload.AssetSearchRequest;
import com.n4systems.webservice.dto.*;
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

import java.util.List;

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
