package com.n4systems.webservice.server;

import com.n4systems.ejb.AssetManager;
import com.n4systems.ejb.OrderManager;
import com.n4systems.ejb.PersistenceManager;
import com.n4systems.ejb.legacy.LegacyAsset;
import com.n4systems.ejb.legacy.LegacyAssetType;
import com.n4systems.ejb.legacy.PopulatorLog;
import com.n4systems.ejb.legacy.ServiceDTOBeanConverter;
import com.n4systems.exceptions.*;
import com.n4systems.fieldid.context.ThreadLocalUserContext;
import com.n4systems.fieldid.context.UserContext;
import com.n4systems.fieldid.permissions.SerializableSecurityGuard;
import com.n4systems.fieldid.permissions.SystemSecurityGuard;
import com.n4systems.handlers.creator.EventPersistenceFactory;
import com.n4systems.handlers.creator.EventsInAGroupCreator;
import com.n4systems.handlers.creator.events.factory.ProductionEventPersistenceFactory;
import com.n4systems.model.*;
import com.n4systems.model.asset.AssetByMobileGuidLoader;
import com.n4systems.model.asset.AssetImageFileSaver;
import com.n4systems.model.asset.AssetSubAssetsLoader;
import com.n4systems.model.asset.SmartSearchLoader;
import com.n4systems.model.event.*;
import com.n4systems.model.eventschedule.EventScheduleByGuidOrIdLoader;
import com.n4systems.model.orgs.*;
import com.n4systems.model.safetynetwork.OrgConnection;
import com.n4systems.model.safetynetwork.SafetyNetworkBackgroundSearchLoader;
import com.n4systems.model.safetynetwork.TenantWideVendorOrgConnPaginatedLoader;
import com.n4systems.model.security.OrgOnlySecurityFilter;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.model.security.TenantOnlySecurityFilter;
import com.n4systems.model.tenant.SetupDataLastModDates;
import com.n4systems.model.tenant.SetupDataLastModDatesLoader;
import com.n4systems.model.user.EmployeePaginatedLoader;
import com.n4systems.model.user.User;
import com.n4systems.persistence.loaders.FilteredIdLoader;
import com.n4systems.persistence.loaders.LoaderFactory;
import com.n4systems.servicedto.converts.*;
import com.n4systems.servicedto.converts.util.DtoDateConverter;
import com.n4systems.services.TenantFinder;
import com.n4systems.services.asset.AssetSaveService;
import com.n4systems.tools.Pager;
import com.n4systems.util.*;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereParameter.Comparator;
import com.n4systems.webservice.ModelToServiceConverterFactory;
import com.n4systems.webservice.RequestHandlerFactory;
import com.n4systems.webservice.assetdownload.AssetIdListResponse;
import com.n4systems.webservice.assetdownload.AssetListResponse;
import com.n4systems.webservice.assetdownload.AssetRequest;
import com.n4systems.webservice.assetdownload.AssetSearchRequest;
import com.n4systems.webservice.dto.*;
import com.n4systems.webservice.dto.AuthenticationRequest.LoginType;
import com.n4systems.webservice.dto.findinspection.FindInspectionRequestInformation;
import com.n4systems.webservice.dto.findinspection.FindInspectionResponse;
import com.n4systems.webservice.dto.findproduct.FindProductRequestInformation;
import com.n4systems.webservice.dto.findproduct.FindProductResponse;
import com.n4systems.webservice.dto.hello.HelloRequest;
import com.n4systems.webservice.dto.hello.HelloResponse;
import com.n4systems.webservice.dto.inspectionschedule.InspectionScheduleRequest;
import com.n4systems.webservice.dto.limitedproductupdate.LimitedProductUpdateRequest;
import com.n4systems.webservice.dto.limitedproductupdate.ProductLookupInformation;
import com.n4systems.webservice.dto.limitedproductupdate.UpdateProductByCustomerRequest;
import com.n4systems.webservice.exceptions.InspectionException;
import com.n4systems.webservice.exceptions.ProductException;
import com.n4systems.webservice.exceptions.ServiceException;
import com.n4systems.webservice.predefinedlocation.PredefinedLocationListResponse;
import com.n4systems.webservice.server.handlers.CompletedScheduleCreator;
import com.n4systems.webservice.server.handlers.HelloHandler;
import com.n4systems.webservice.server.handlers.RealTimeAssetLookupHandler;
import com.n4systems.webservice.server.handlers.RealTimeInspectionLookupHandler;
import org.apache.log4j.Logger;
import rfid.util.PopulatorLogger;

import javax.naming.NamingException;
import java.util.*;

@SuppressWarnings("deprecation")
public class DataServiceImpl implements DataService {
	private static Logger logger = Logger.getLogger(DataServiceImpl.class);

	public DataServiceImpl() {
	}

	private RequestHandlerFactory createResponseHandlerFactory(RequestInformation request) {
		LoaderFactory loaderFactory = createLoaderFactory(request);
		ModelToServiceConverterFactory converterFactory = new ModelToServiceConverterFactory(loaderFactory, WsServiceLocator.getServiceDTOBeanConverter(request.getTenantId()));
		RequestHandlerFactory handlerFactory = new RequestHandlerFactory(ConfigContext.getCurrentContext(), loaderFactory, converterFactory);
		return handlerFactory;
	}

	private LoaderFactory createLoaderFactory(RequestInformation request) {
		return new LoaderFactory(new TenantOnlySecurityFilter(request.getTenantId()));
	}

	@Override
	public HelloResponse hello(HelloRequest helloRequest) throws ServiceException {
		try {
			HelloHandler helloHandler = new HelloHandler();
			return helloHandler.sayHello(helloRequest);
		} catch (Exception e) {
			logger.error("exception occured while saying hello", e);
			throw new ServiceException();
		}
	}

	@Override
	public AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest) throws ServiceException {
		try {
			logAuthenticationAttempt(authenticationRequest);

			WebServiceAuthenticator authenticator = new WebServiceAuthenticator(authenticationRequest);

			return authenticator.authenticate();
		} catch (Exception e) {
			logger.error("exception occured while authenticating the web service.", e);
			throw new ServiceException();
		}
	}

	private void logAuthenticationAttempt(AuthenticationRequest authenticationRequest) {
		String loginType = "username and password";
		if (authenticationRequest.getLoginType() == LoginType.SECURITY) {
			loginType = "security rfid number";
		}

		logger.info("Webservice authenication attempt using " + loginType + ". " + "Handheld version " + authenticationRequest.getMajorVersion() + "."
				+ authenticationRequest.getMinorVersion() + ". " + "Tenant ID: " + authenticationRequest.getTenantName() + " and User ID: "
				+ authenticationRequest.getUserId());
	}

	@Override
	public InspectionBookListResponse getAllInspectionBooks(PaginatedRequestInformation paginatedRequestInformation) throws ServiceException {
		try {
			InspectionBookListResponse response = new InspectionBookListResponse();

			long tenantId = paginatedRequestInformation.getTenantId();
			PersistenceManager persistenceManager = WsServiceLocator.getPersistenceManager(tenantId);
			ServiceDTOBeanConverter converter = WsServiceLocator.getServiceDTOBeanConverter(tenantId);

			SecurityFilter securityFilter = new TenantOnlySecurityFilter(tenantId);
			QueryBuilder<EventBook> queryBuilder = new QueryBuilder<EventBook>(EventBook.class, securityFilter);
			queryBuilder.setSimpleSelect();

			List<EventBook> eventBooks = null;
			eventBooks = persistenceManager.findAll(queryBuilder);

			for (EventBook eventBook : eventBooks) {
				response.getInspectionBooks().add(converter.convert(eventBook));
			}

			response.setCurrentPage(1);
			response.setRecordsPerPage(getSetupDataPageSize());
			response.setStatus(ResponseStatus.OK);
			response.setTotalPages(1);

			return response;
		} catch (InvalidQueryException e) {
			throw new ServiceException();
		} catch (Exception e) {
			throw new ServiceException();
		}
	}

	@Override
	public ProductTypeListResponse getAllProductTypes(PaginatedRequestInformation paginatedRequestInformation) throws ServiceException {
		try {
			ProductTypeListResponse response = new ProductTypeListResponse();

			long tenantId = paginatedRequestInformation.getTenantId();
			LegacyAssetType assetTypeManager = WsServiceLocator.getAssetType(tenantId);
			ServiceDTOBeanConverter converter = WsServiceLocator.getServiceDTOBeanConverter(tenantId);

			List<AssetType> assetTypes = assetTypeManager.getAssetTypesForTenant(tenantId);

			for (AssetType assetType : assetTypes) {
				response.getProductTypes().add(converter.convert_new(assetType));
			}

			response.setCurrentPage(1);
			response.setRecordsPerPage(getSetupDataPageSize());
			response.setStatus(ResponseStatus.OK);
			response.setTotalPages(1);

			return response;
		} catch (Exception e) {
			logger.error("exception occured while lookup the list of asset types.", e);
			throw new ServiceException();
		}
	}

	@Override
	public ProductTypeGroupListResponse getAllProductTypeGroups(PaginatedRequestInformation paginatedRequestInformation) throws ServiceException {
		try {
			ProductTypeGroupListResponse response = new ProductTypeGroupListResponse();

			Long tenantId = paginatedRequestInformation.getTenantId();
			PersistenceManager persistenceManager = WsServiceLocator.getPersistenceManager(tenantId);
			ServiceDTOBeanConverter converter = WsServiceLocator.getServiceDTOBeanConverter(tenantId);

			SecurityFilter securityFilter = new TenantOnlySecurityFilter(paginatedRequestInformation.getTenantId());
			QueryBuilder<AssetTypeGroup> queryBuilder = new QueryBuilder<AssetTypeGroup>(AssetTypeGroup.class, securityFilter);
			queryBuilder.setSimpleSelect();

			List<AssetTypeGroup> assetTypeGroups = persistenceManager.findAll(queryBuilder);

			for (AssetTypeGroup assetTypeGroup : assetTypeGroups) {
				response.getProductTypeGroups().add(converter.convert(assetTypeGroup));
			}

			response.setCurrentPage(1);
			response.setRecordsPerPage(getSetupDataPageSize());
			response.setStatus(ResponseStatus.OK);
			response.setTotalPages(1);

			return response;

		} catch (Exception e) {
			logger.error("exception occured while looking up asset type groups", e);
			throw new ServiceException();
		}
	}

	@Override
	public JobListResponse getAllJobs(PaginatedUpdateRequestInfo request) throws ServiceException {
		try {
			JobListResponse response = new JobListResponse();

			long tenantId = request.getTenantId();
			ServiceDTOBeanConverter converter = WsServiceLocator.getServiceDTOBeanConverter(tenantId);
			PersistenceManager persistenceManager = WsServiceLocator.getPersistenceManager(tenantId);

			int currentPage = request.getPageNumber().intValue();

			Date modified = DtoDateConverter.convertStringToDate(request.getModified());

			SecurityFilter securityFilter = new TenantOnlySecurityFilter(tenantId);
			QueryBuilder<Project> jobBuilder = new QueryBuilder<Project>(Project.class, securityFilter);
			jobBuilder.addWhere(Comparator.EQ, "eventjob", "eventJob", true);
			jobBuilder.addWhere(Comparator.EQ, "open", "open", true);
			jobBuilder.addWhere(Comparator.EQ, "retired", "retired", false);
			if (modified != null) {
				jobBuilder.addWhere(Comparator.EQ, "modified", "modified", modified);
			}
			jobBuilder.addFetch("resources");

			// for postgres to paginate correctly.
			jobBuilder.addOrder("id");

			if (currentPage != PaginatedRequestInformation.INFORMATION_PAGE) {
				Pager<Project> jobPage = persistenceManager.findAllPaged(jobBuilder, currentPage, getSetupDataPageSize());
				response.setTotalPages((int) jobPage.getTotalPages());

				for (Project job : jobPage.getList()) {
					response.getJobs().add(converter.convert(job));
				}
			} else {
				// response.setTotalPages(persistenceManager.countAllPages(Project.class,
				// jobsPerPage, securityFilter));
			}

			response.setRecordsPerPage(getSetupDataPageSize());
			response.setStatus(ResponseStatus.OK);

			return response;

		} catch (Exception e) {
			logger.error("exception while pulling down jobs", e);
			throw new ServiceException();
		}
	}

	@Override
	public EmployeeListResponse getAllEmployees(PaginatedRequestInformation requestInformation) throws ServiceException {
		try {

			int currentPage = requestInformation.getPageNumber().intValue();

			LoaderFactory loaderFactory = new LoaderFactory(new TenantOnlySecurityFilter(requestInformation.getTenantId()));
			EmployeePaginatedLoader loader = loaderFactory.createEmployeePaginatedLoader();
			loader.setPageSize(getSetupDataPageSize());
			loader.setPage(currentPage);

			Pager<User> pager = loader.load();

			EmployeeListResponse response = new EmployeeListResponse(pager, getSetupDataPageSize());
			response.setStatus(ResponseStatus.OK);

			for (User user : pager.getList()) {
				response.getEmployees().add(new EmployeeServiceDTOConverter().convert(user));
			}

			return response;

		} catch (Exception e) {
			logger.error("failed while downloading all employee lists", e);
			throw new ServiceException();
		}
	}

	@Override
	public PredefinedLocationListResponse getAllPredefinedLocations(PaginatedRequestInformation request) throws ServiceException {
		PredefinedLocationListResponse response = createResponseHandlerFactory(request).createAllPredefinedLocationsRequestHandler().getResponse(request);
		return response;
	}

	@Override
	public CustomerOrgListResponse getAllCustomerOrgs(PaginatedRequestInformation requestInformation) throws ServiceException {
		try {
			int currentPage = requestInformation.getPageNumber().intValue();
			long tenantId = requestInformation.getTenantId();
			LoaderFactory loaderFactory = new LoaderFactory(new TenantOnlySecurityFilter(tenantId));
			ServiceDTOBeanConverter converter = WsServiceLocator.getServiceDTOBeanConverter(tenantId);

			CustomerOrgWithArchivedPaginatedLoader loader = loaderFactory.createCustomerOrgWithArchivedPaginatedLoader();
			loader.setPageSize(getSetupDataPageSize());
			loader.setPage(currentPage);
			loader.withArchivedState();

			Pager<CustomerOrg> pager = loader.load();

			CustomerOrgListResponse response = new CustomerOrgListResponse(pager, getSetupDataPageSize());
			response.setStatus(ResponseStatus.OK);

			for (CustomerOrg customerOrg : pager.getList()) {
				response.getCustomers().add(converter.convert(customerOrg));
			}

			return response;
		} catch (Exception e) {
			logger.error("Exception occured while looking up customer orgs", e);
			throw new ServiceException();
		}
	}

	@Override
	public DivisionOrgListResponse getAllDivisionOrgs(PaginatedRequestInformation requestInformation) throws ServiceException {
		try {
			int currentPage = requestInformation.getPageNumber().intValue();
			long tenantId = requestInformation.getTenantId();
			ServiceDTOBeanConverter converter = WsServiceLocator.getServiceDTOBeanConverter(tenantId);

			LoaderFactory loaderFactory = new LoaderFactory(new TenantOnlySecurityFilter(tenantId));
			DivisionOrgPaginatedLoader loader = loaderFactory.createDivisionOrgPaginatedLoader();
			loader.setPageSize(getSetupDataPageSize());
			loader.setPage(currentPage);
			loader.withArchivedState();

			Pager<DivisionOrg> pager = loader.load();

			DivisionOrgListResponse response = new DivisionOrgListResponse(pager, getSetupDataPageSize());
			response.setStatus(ResponseStatus.OK);

			for (DivisionOrg divisionOrg : pager.getList()) {
				response.getDivisions().add(converter.convert(divisionOrg));
			}

			return response;
		} catch (Exception e) {
			logger.error("Exception occured while looking up division orgs", e);
			throw new ServiceException();
		}
	}

	@Override
	public VendorListResponse getAllVendors(PaginatedRequestInformation requestInformation) throws ServiceException {
		try {
			ServiceDTOBeanConverter converter = WsServiceLocator.getServiceDTOBeanConverter(requestInformation.getTenantId());

			LoaderFactory loaderFactory = new LoaderFactory(new TenantOnlySecurityFilter(requestInformation.getTenantId()));
			TenantWideVendorOrgConnPaginatedLoader connectionLoader = loaderFactory.createTenantWideVendorOrgConnPaginatedLoader();
			connectionLoader.setPageSize(getSetupDataPageSize());
			connectionLoader.setPage(requestInformation.getPageNumber().intValue());

			Pager<OrgConnection> pager = connectionLoader.load();

			VendorListResponse response = new VendorListResponse(pager, getSetupDataPageSize());

			for (OrgConnection orgConnection : pager.getList()) {
				response.getVendors().add(converter.convert(orgConnection));
			}

			return response;
		} catch (Exception e) {
			logger.error("Exception occured while looking up vendors", e);
			throw new ServiceException();
		}
	}

	@Override
	public InternalOrgListResponse getAllInternalOrgs(PaginatedRequestInformation requestInformation) throws ServiceException {
		try {
			int currentPage = requestInformation.getPageNumber().intValue();
			long tenantId = requestInformation.getTenantId();
			ServiceDTOBeanConverter converter = WsServiceLocator.getServiceDTOBeanConverter(tenantId);
			LoaderFactory loaderFactory = new LoaderFactory(new TenantOnlySecurityFilter(tenantId));
			SecondaryOrgPaginatedLoader secondaryLoader = loaderFactory.createSecondaryOrgPaginatedLoader();
			secondaryLoader.setPageSize(getSetupDataPageSize());
			secondaryLoader.setPage(currentPage);

			Pager<SecondaryOrg> pager = secondaryLoader.load();

			InternalOrgListResponse response = new InternalOrgListResponse(pager, getSetupDataPageSize());
			response.setStatus(ResponseStatus.OK);

			for (InternalOrg internalOrg : pager.getList()) {
				response.getInternalOrgs().add(converter.convert(internalOrg));
			}

			// Adjust to throw in the primary org
			if (response.getTotalPages() == 0) {
				response.setTotalPages(1);
			}

			if (response.getCurrentPage() >= response.getTotalPages()) {
				PrimaryOrgByTenantLoader primaryLoader = loaderFactory.createPrimaryOrgByTenantLoader();
				primaryLoader.setTenantId(tenantId);
				PrimaryOrg primaryOrg = primaryLoader.load();

				response.getInternalOrgs().add(converter.convert((InternalOrg) primaryOrg));
			}

			return response;
		} catch (Exception e) {
			logger.error("Exception occured while looking up secondary orgs", e);
			throw new ServiceException();
		}
	}

	@Override
	public RequestResponse limitedProductUpdate(LimitedProductUpdateRequest request) throws ServiceException {
		Long tenantId = request.getTenantId();
		PersistenceManager persistenceManager = WsServiceLocator.getPersistenceManager(tenantId );
		ProductLookupInformation lookupInformation = request.getProductLookupInformation();
		
		UserContext uc = ThreadLocalUserContext.getInstance();
		User user = null;
		try {
			if (request.modifiedByIdExists()) {
				user = persistenceManager.find(User.class, request.getModifiedById());
			}
			uc.setCurrentUser(user);

			Asset asset = lookupProduct(lookupInformation, request.getTenantId());

			LocationConverter locationConverter = new LocationServiceToContainerConverter(createLoaderFactory(request));
			locationConverter.convert(request, asset);

			AssetSaveService saver = new AssetSaveService(ServiceLocator.getLegacyAssetManager(), user);
			saver.setAsset(asset).update();

		} catch (Exception e) {
			logger.error("Exception occured while doing a limited asset update", e);
			throw new ServiceException();
		} finally {
			uc.setCurrentUser(null);
		}

		return new RequestResponse();
	}

	@Override
	public RequestResponse updateProductByCustomer(UpdateProductByCustomerRequest request) throws ServiceException {
		UserContext uc = ThreadLocalUserContext.getInstance();
		try {
			long tenantId = request.getTenantId();
			ServiceDTOBeanConverter converter = WsServiceLocator.getServiceDTOBeanConverter(tenantId);
			PersistenceManager persistenceManager = WsServiceLocator.getPersistenceManager(tenantId);
			
			User user = null;
			if (request.modifiedByIdExists()) {
				user = persistenceManager.find(User.class, request.getModifiedById());
			}
			uc.setCurrentUser(user);
			
			ProductLookupInformation lookupInformation = request.getProductLookupInformation();

			Asset asset = lookupProduct(lookupInformation, request.getTenantId());
			asset.setOwner(converter.convert(request.getOwnerId(), request.getTenantId()));
			
			LocationConverter locationConverter = new LocationServiceToContainerConverter(createLoaderFactory(request));
			locationConverter.convert(request, asset);

			asset.setCustomerRefNumber(request.getCustomerRefNumber());
			asset.setPurchaseOrder(request.getPurchaseOrder());

			AssetSaveService saver = new AssetSaveService(ServiceLocator.getLegacyAssetManager(), user);
			saver.setAsset(asset).update();

		} catch (Exception e) {
			logger.error("Exception occured while doing asset update by customer", e);
			throw new ServiceException();
		} finally {
			uc.setCurrentUser(null);
		}

		return new RequestResponse();
	}

	@Override
	public RequestResponse createInspectionSchedule(InspectionScheduleRequest request) throws ServiceException {
		try {
			ServiceDTOBeanConverter converter = WsServiceLocator.getServiceDTOBeanConverter(request.getTenantId());
			Event openEvent = converter.convert(request.getScheduleService(), request.getTenantId());

			TenantOnlySecurityFilter securityFilter = new TenantOnlySecurityFilter(request.getTenantId());
			securityFilter.setShowArchived(true);
			
			new InspectionScheduleCreateHandler(new AssetByMobileGuidLoader(securityFilter), 
					new FilteredIdLoader<Asset>(securityFilter, Asset.class), 
					new FilteredIdLoader<EventType>(securityFilter, EventType.class), 
					new SimpleEventSaver())
					.createNewInspectionSchedule(openEvent, request.getScheduleService());

		} catch (Exception e) {
			logger.error("Exception occured while saving inspection schedule", e);
			throw new ServiceException();
		}

		return new RequestResponse();
	}

	@Override
	public RequestResponse updateInspectionSchedule(InspectionScheduleRequest request) throws ServiceException {

		try {
			new InspectionScheduleUpdateHandler(new EventScheduleByGuidOrIdLoader(new TenantOnlySecurityFilter(request.getTenantId())),
					new SimpleEventSaver()).updateInspectionSchedule(request.getScheduleService());

		} catch (Exception e) {
			logger.error("Exception occured while updating inspection schedule", e);
			throw new ServiceException();
		}

		return new RequestResponse();

	}

	@Override
	public RequestResponse removeInspectionSchedule(InspectionScheduleRequest request) throws ServiceException {

		try {

			new InspectionScheduleUpdateHandler(new EventScheduleByGuidOrIdLoader(new TenantOnlySecurityFilter(request.getTenantId())),
					new SimpleEventSaver()).removeInspectionSchedule(request.getScheduleService());

		} catch (Exception e) {
			logger.error("Exception occured while removing inspection schedule", e);
			throw new ServiceException();
		}

		return new RequestResponse();

	}

	private Asset lookupProduct(ProductLookupable productLookupableDto, Long tenantId) {
		AssetManager assetManager = WsServiceLocator.getAssetManager(tenantId);

		SecurityFilter filter = new TenantOnlySecurityFilter(tenantId);

		Asset asset = null;

		if (productLookupableDto.isCreatedOnMobile()) {
			asset = assetManager.findAssetByGUID(productLookupableDto.getMobileGuid(), filter);
		} else {
			asset = assetManager.findAssetAllFields(productLookupableDto.getId(), filter);
		}

		return asset;
	}

	@Override
	public RequestResponse updateProduct(RequestInformation requestInformation, ProductServiceDTO productDTO) throws ServiceException {
		try {
			RequestResponse response = new RequestResponse();
			response.setStatus(ResponseStatus.OK);

			long tenantId = requestInformation.getTenantId();
			OrderManager orderManager = WsServiceLocator.getOrderManager(tenantId);
			Asset existingAsset = lookupProduct(productDTO, tenantId);

			if (existingAsset == null) {
				logger.error("can not load asset to edit");
				throw new ServiceException();
			}

			productDTO = fixModifyByFromOldVersionsOfMobile(productDTO);
			productDTO.unsetIdentifedById();

			ProductServiceDTOConverter converter = createProductServiceDTOConverter(tenantId);

			Asset asset = converter.convert(productDTO, existingAsset);

			// on edit, the identified by user is populated with the modified
			// user
			ServiceLocator.getLegacyAssetManager().update(asset, asset.getModifiedBy());

			return response;
		} catch (Exception e) {
			logger.error("failed while processing asset", e);
			throw new ServiceException("Problem updating asset");
		}
	}

	private SystemSecurityGuard createSecurityGuard(long tenantId) {
		SystemSecurityGuard systemSecurityGuard = new SerializableSecurityGuard(getTenantFinder().findTenant(tenantId));
		return systemSecurityGuard;
	}

	@Override
	public RequestResponse createProduct(RequestInformation requestInformation, ProductServiceDTO productDTO) throws ServiceException {
		Asset asset = null;
		RequestResponse response = new RequestResponse();
		response.setStatus(ResponseStatus.OK);

		testTransactionId(requestInformation);

		UserContext uc = ThreadLocalUserContext.getInstance();
		try {
			if (productDTO.identifiedByExists()) {
				EntityByIdIncludingArchivedLoader<User> userLoader = createLoaderFactory(requestInformation).createEntityByIdLoader(User.class);
				uc.setCurrentUser(userLoader.setId(productDTO.getIdentifiedById()).load());
			}
			
			PopulatorLogger populatorLogger = PopulatorLogger.getInstance();
			LegacyAsset productManager = WsServiceLocator.getLegacyAssetManager(requestInformation.getTenantId());

			if (isTransactionCompleted(requestInformation)) {
				return response;
			}

			// convert from the service dto
			asset = convertNewProduct(requestInformation.getTenantId(), productDTO);

			// Check if we need to try and register this asset on the safety
			// network
			if (productDTO.vendorIdExists()) {
				LoaderFactory loaderFactory = new LoaderFactory(new OrgOnlySecurityFilter(asset.getOwner().getInternalOrg()));
				SafetyNetworkBackgroundSearchLoader networkLoader = loaderFactory.createSafetyNetworkBackgroundSearchLoader();
				Asset linkedAsset = networkLoader.setIdentifier(productDTO.getSerialNumber()).setRfidNumber(productDTO.getRfidNumber())
						.setRefNumber(productDTO.getCustomerRefNumber()).setVendorOrgId(productDTO.getVendorId()).load();
				asset.setLinkedAsset(linkedAsset);
			}

			// create the asset with attached sub asset transactionally
			asset = productManager.createAssetWithServiceTransaction(requestInformation.getMobileGuid(), asset, asset.getModifiedBy());

			// createAssetWithServiceTransaction no longer auto-schedules as of 2011.2
			WsServiceLocator.getEventScheduleManager(requestInformation.getTenantId()).autoSchedule(asset);
			
			// create any new subproducts (this is not currently used by mobile
			// (sub products come up attached to inspections))
			if (productDTO.getSubProducts() != null && productDTO.getSubProducts().size() > 0) {
				List<SubAsset> subAssets = lookupOrCreateSubProducts(requestInformation.getTenantId(), productDTO.getSubProducts(), asset);
				if (subAssets.size() > 0) {
					/*
					 * Note: the list of SubProducts on Asset is marked as
					 * @Transient however productManager.update has special
					 * handling code to persist it anyway. and yes it does suck
					 * ...
					 */
					asset.getSubAssets().addAll(subAssets);
					productManager.update(asset, asset.getModifiedBy());
				}
			}

			logSuccessfulProductCreate(requestInformation.getTenantId(), populatorLogger, asset);
		} catch (TransactionAlreadyProcessedException e) {
			logger.info("transaction already processed for asset  " + asset.getIdentifier());
			return response;
		} catch (Exception e) {
			logger.error("failed while processing asset", e);
			throw new ServiceException("Problem creating asset");
		} finally {
			uc.setCurrentUser(null);
		}
		return response;
	}

	protected TenantFinder getTenantFinder() {
		return TenantFinder.getInstance();
	}

	private void testTransactionId(RequestInformation requestInformation) throws ServiceException {
		if (!requestInformation.hasValidTransactionId()) {
			logger.error("transaction Id is invalid for create asset ");
			throw new ServiceException("Invalid transaction Id");
		}
	}

	private Asset convertNewProduct(Long tenantId, ProductServiceDTO productDTO) throws Exception {
		productDTO = fixModifyByFromOldVersionsOfMobile(productDTO);

		Asset asset = new Asset();
		OrderManager orderManager = WsServiceLocator.getOrderManager(tenantId);

		ProductServiceDTOConverter converter = createProductServiceDTOConverter(tenantId);
		asset = converter.convert(productDTO, asset);

		return asset;
	}

	private ProductServiceDTOConverter createProductServiceDTOConverter(Long tenantId) {
		ProductServiceDTOConverter converter = DtoToModelConverterFactory.createFactory(createSecurityGuard(tenantId)).createProductConverter();
		return converter;
	}

	private InspectionServiceDTOConverter createInspectionServiceDTOConverter(Long tenantId) {
		InspectionServiceDTOConverter converter = DtoToModelConverterFactory.createFactory(createSecurityGuard(tenantId)).createInspectionConverter();
		return converter;
	}

	private ProductServiceDTO fixModifyByFromOldVersionsOfMobile(ProductServiceDTO productDTO) {
		if (!productDTO.modifiedByIdExists()) {
			productDTO.setModifiedById(productDTO.getIdentifiedById());
		}

		return productDTO;
	}

	private Asset createProduct(ProductServiceDTO productDTO, Long tenantId) throws Exception {
		PopulatorLogger populatorLogger = PopulatorLogger.getInstance();
		LegacyAsset productManager = WsServiceLocator.getLegacyAssetManager(tenantId);

		Asset asset = convertNewProduct(tenantId, productDTO);

		asset = productManager.create(asset, asset.getIdentifiedBy());

		logSuccessfulProductCreate(tenantId, populatorLogger, asset);

		return asset;
	}

	private void logSuccessfulProductCreate(Long tenantId, PopulatorLogger populatorLogger, Asset asset) {
		populatorLogger.logMessage(tenantId, "Successfully created asset with identifier " + asset.getIdentifier(), PopulatorLog.logType.mobile,
				PopulatorLog.logStatus.success);
	}

	@Override
	public RequestResponse createInspections(RequestInformation requestInformation, List<InspectionServiceDTO> inspectionDTOs) throws ServiceException,
			ProductException, InspectionException {
		RequestResponse response = new RequestResponse();
		response.setStatus(ResponseStatus.OK);

		testTransactionId(requestInformation);

		try {
			if (isTransactionCompleted(requestInformation)) {
				return response;
			}

			
			UserContext userContext = ThreadLocalUserContext.getInstance();
			try {
				// The performedBy user will be the same for all events in this group.
				EntityByIdIncludingArchivedLoader<User> userLoader = createLoaderFactory(requestInformation).createEntityByIdLoader(User.class);
				userContext.setCurrentUser(userLoader.setId(inspectionDTOs.get(0).getPerformedById()).load());
			
				Long tenantId = requestInformation.getTenantId();
				PopulatorLogger populatorLogger = PopulatorLogger.getInstance();
				InspectionServiceDTOConverter converter = createInspectionServiceDTOConverter(tenantId);
				LegacyAsset productManager = WsServiceLocator.getLegacyAssetManager(tenantId);

				List<Event> events = new ArrayList<Event>();
				Map<Event, Date> nextInspectionDates = new HashMap<Event, Date>();
				Asset asset = null;

				EventScheduleByGuidOrIdLoader scheduleLoader = new EventScheduleByGuidOrIdLoader(new TenantOnlySecurityFilter(tenantId));
				for (InspectionServiceDTO inspectionServiceDTO : inspectionDTOs) {
					asset = findOrCreateAsset(tenantId, inspectionServiceDTO);
					inspectionServiceDTO.setProductId(asset.getId());

					// lets look up or create all newly attached sub products and
					// attach to asset
					List<SubAsset> subAssets = lookupOrCreateSubProducts(tenantId, inspectionServiceDTO.getNewSubProducts(), asset);
					updateSubProducts(productManager, tenantId, asset, inspectionServiceDTO, subAssets);

					// we also need to get the asset for any sub-inspections
					if (inspectionServiceDTO.getSubInspections() != null) {
						Asset subProduct = null;
						for (SubInspectionServiceDTO subInspection : inspectionServiceDTO.getSubInspections()) {
							subProduct = findOrCreateAsset(tenantId, subInspection);
							subInspection.setProductId(subProduct.getId());
						}
					}

                    Event schedule = loadScheduleFromInspectionDto(scheduleLoader, inspectionServiceDTO);

					Event event = converter.convert(inspectionServiceDTO, schedule);

					events.add(event);
					nextInspectionDates.put(event, DtoDateConverter.convertStringToDate(inspectionServiceDTO.getNextDate()));
				}
	
				try {
					EventPersistenceFactory eventPersistenceFactory = new ProductionEventPersistenceFactory();
	
					EventsInAGroupCreator eventsInAGroupCreator = eventPersistenceFactory.createEventsInAGroupCreator();
	
					eventsInAGroupCreator.create(requestInformation.getMobileGuid(), events, nextInspectionDates);
					logger.info("save inspections on asset " + asset.getIdentifier());
					populatorLogger.logMessage(tenantId, "Created inspection for asset with identifier " + asset.getIdentifier(), PopulatorLog.logType.mobile, PopulatorLog.logStatus.success);
				} catch (TransactionAlreadyProcessedException e) {
					logger.info("Transaction already processed for inspections on asset  " + asset.getIdentifier());
				} catch (Exception e) {
					logger.error("failed to save inspections", e);
					throw new InspectionException("Failed to save inspections");
				}

				return response;
			} finally {
				userContext.setCurrentUser(null);
			}
		} catch (InspectionException e) {
			throw e;
		} catch (FindAssetFailure e) {
			throw new ProductException("Could not find asset");
		} catch (Exception e) {
			logger.error("failed while processing inspections", e);
			throw new ServiceException("Problem processing inspections");
		}
	}

	private Event loadScheduleFromInspectionDto(EventScheduleByGuidOrIdLoader scheduleLoader, InspectionServiceDTO inspectionServiceDTO) {
		return scheduleLoader.setId(inspectionServiceDTO.getInspectionScheduleId()).setMobileGuid(inspectionServiceDTO.getInspectionScheduleMobileGuid())
				.load();
	}

	private void updateSubProducts(LegacyAsset productManager, Long tenantId, Asset asset, InspectionServiceDTO inspectionServiceDTO, List<SubAsset> subAssets)
			throws SubAssetUniquenessException {

		new UpdateSubProducts(productManager, tenantId, asset, inspectionServiceDTO, subAssets, WsServiceLocator.getAssetManager(tenantId)).run();

	}

	@Override
	public RequestResponse createInspectionImage(RequestInformation requestInformation, InspectionImageServiceDTO inspectionImageServiceDTO)
			throws ServiceException, InspectionException {
		RequestResponse response = new RequestResponse();

		Long tenantId = requestInformation.getTenantId();

		ServiceDTOBeanConverter converter = WsServiceLocator.getServiceDTOBeanConverter(tenantId);
		PersistenceManager persistenceManager = WsServiceLocator.getPersistenceManager(tenantId);

		UserContext uc = ThreadLocalUserContext.getInstance();
		try {

			User performedBy = persistenceManager.find(User.class, inspectionImageServiceDTO.getPerformedById());
			uc.setCurrentUser(performedBy);
			
			EventAttachmentSaver attachmentSaver = new EventAttachmentSaver();
			attachmentSaver.setData(inspectionImageServiceDTO.getImage().getImage());

			// 1. find out inspection using inspection Mobile Guid
			AbstractEvent targetEvent;
			if (inspectionImageServiceDTO.isFromSubInspection()) {
				EventByMobileGuidLoader<SubEvent> loader = new EventByMobileGuidLoader<SubEvent>(new TenantOnlySecurityFilter(tenantId), SubEvent.class);
				SubEvent subEvent = loader.setMobileGuid(inspectionImageServiceDTO.getInspectionMobileGuid()).load();

				EventBySubEventLoader masterEventLoader = new EventBySubEventLoader();
				masterEventLoader.setSubEvent(subEvent);
				Event masterEvent = masterEventLoader.load();

				attachmentSaver.setEvent(masterEvent);
				attachmentSaver.setSubEvent(subEvent);

				targetEvent = subEvent;
			} else {
				EventByMobileGuidLoader<Event> loader = new EventByMobileGuidLoader<Event>(new TenantOnlySecurityFilter(tenantId), Event.class);
				Event event = loader.setMobileGuid(inspectionImageServiceDTO.getInspectionMobileGuid()).load();

				attachmentSaver.setEvent(event);

				targetEvent = event;
			}

			FileAttachment newFileAttachment = converter.convert(targetEvent, inspectionImageServiceDTO, performedBy);
			attachmentSaver.save(newFileAttachment);

		} catch (Exception e) {
			logger.error("failed while processing inspection image", e);
			throw new ServiceException("Problem processing inspection image");
		} finally {
			uc.setCurrentUser(null);
		}

		return response;
	}
	
	@Override
	public RequestResponse updateAssetImage(RequestInformation requestInformation, AssetImageServiceDTO assetImageServiceDTO)
			throws ServiceException	{
		RequestResponse response = new RequestResponse();		
		Long tenantId = requestInformation.getTenantId();
		PersistenceManager persistenceManager = WsServiceLocator.getPersistenceManager(tenantId);
		UserContext uc = ThreadLocalUserContext.getInstance();
		
		try {
			User modifiedBy = persistenceManager.find(User.class, assetImageServiceDTO.getModifiedById());
			uc.setCurrentUser(modifiedBy);
			
			AssetByMobileGuidLoader loader = new AssetByMobileGuidLoader(new TenantOnlySecurityFilter(tenantId));
			Asset asset = loader.setMobileGuid(assetImageServiceDTO.getAssetMobileGuid()).load();
			
			AssetImageFileSaver assetImageFileSaver = new AssetImageFileSaver(asset, asset.getImageName());
			assetImageFileSaver.setData(assetImageServiceDTO.getImage().getImage());
			assetImageFileSaver.save();
			logger.info("Asset Image updated for " + asset.getIdentifier() + " by " + modifiedBy.getDisplayName());
			
		} catch(Exception e) {
			logger.error("failed while processing asset image", e);
			throw new ServiceException("Problem processing inspection image");
		}
		finally {
			uc.setCurrentUser(null);
		}
		
		return response;
	}

	private List<SubAsset> lookupOrCreateSubProducts(Long tenantId, List<SubProductMapServiceDTO> subProductMaps, Asset masterAsset)
			throws Exception {

		List<SubAsset> subAssets = new ArrayList<SubAsset>();

		if (subProductMaps == null)
			return subAssets;

		AssetManager assetManager = WsServiceLocator.getAssetManager(tenantId);
		PersistenceManager persistenceManager = WsServiceLocator.getPersistenceManager(tenantId);
		SecurityFilter filter = new TenantOnlySecurityFilter(tenantId);
		
		for (SubProductMapServiceDTO subProductMap : subProductMaps) {
			Asset asset = assetManager.findAssetByGUID(subProductMap.getSubAssetGuid(), filter);
			
			// the following lookups are legacy as of 1.26
			if (asset == null) {
				ProductServiceDTO subProductDTO = subProductMap.getNewProduct();

				asset = assetManager.findAssetByGUID(subProductDTO.getMobileGuid(), filter);
				
				if (asset == null && subProductDTO.getId() != null && subProductDTO.getId() > 0) {
					asset = persistenceManager.find(Asset.class, subProductDTO.getId());
				}
			}
			
			if (asset != null) {
				SubAsset subAsset = new SubAsset();
				subAsset.setLabel(subProductMap.getName());
				subAsset.setAsset(asset);
				subAsset.setMasterAsset(masterAsset);

				subAssets.add(subAsset);
			}
		}

		return subAssets;
	}

	/**
	 * The logic here is: if the asset serial guid is not set, lookup by
	 * products unique id, if not found throw error. If it is set, try looking
	 * up by guid, if not found then tag (create) this asset.
	 */
	private Asset findOrCreateAsset(Long tenantId, AbstractInspectionServiceDTO inspectionServiceDTO) throws FindAssetFailure {

		Asset asset = null;
		
		TenantOnlySecurityFilter securityFilter = new TenantOnlySecurityFilter(tenantId);
		securityFilter.setShowArchived(true);
		
		if (inspectionServiceDTO.productIdExists()) {
			try {
				asset = WsServiceLocator.getAssetManager(tenantId).findAsset(inspectionServiceDTO.getProductId(), securityFilter);
				asset = WsServiceLocator.getAssetManager(tenantId).fillInSubAssetsOnAsset(asset);
			} catch (Exception e) {
				logger.error("looking up asset with asset id " + inspectionServiceDTO.getProductId(), e);
			}

		} else if (inspectionServiceDTO.productMobileGuidExists()) {
			// Try looking up by GUID
			try {
				asset = WsServiceLocator.getAssetManager(tenantId).findAssetByGUID(inspectionServiceDTO.getProductMobileGuid(), securityFilter);
			} catch (Exception e) {
				logger.error("Looking up asset by GUID = " + inspectionServiceDTO.getProductMobileGuid(), e);
			}

			// If still null, lets tag it
			if (asset == null && inspectionServiceDTO.getProduct() != null) {
				logger.info("using tag asset from inside create inspection");
				try {
					asset = createProduct(inspectionServiceDTO.getProduct(), tenantId);
				} catch (Exception e) {
					logger.error("Tagging asset", e);
				}

			}
		}

		if (asset == null) {
			throw new FindAssetFailure("Could not find asset.");
		}

		return asset;
	}

	@Override
	public FindProductResponse findProduct(FindProductRequestInformation requestInformation) throws ServiceException {
		try {
			long tenantId = requestInformation.getTenantId();
			ServiceDTOBeanConverter converter = WsServiceLocator.getServiceDTOBeanConverter(tenantId);
			SecurityFilter securityFilter = new TenantOnlySecurityFilter(tenantId);
			SmartSearchLoader smartSearchLoader = new SmartSearchLoader(securityFilter);
			AssetSubAssetsLoader subAssetLoader = new AssetSubAssetsLoader(securityFilter);
			RealTimeAssetLookupHandler realTimeAssetLookupHandler = new RealTimeAssetLookupHandler(smartSearchLoader, subAssetLoader);

			List<Asset> assets = realTimeAssetLookupHandler.setSearchText(requestInformation.getSearchText()).setModified(requestInformation.getModified())
					.lookup();

			FindProductResponse response = new FindProductResponse();

			for (Asset asset : assets) {
				response.getProducts().add(converter.convert(asset));
			}

			return response;
		} catch (Exception e) {
			logger.error("failed while finding asset for handheld", e);
			throw new ServiceException();
		}
	}

	@Override
	public FindInspectionResponse findInspection(FindInspectionRequestInformation requestInformation) throws ServiceException {
		try {
			long tenantId = requestInformation.getTenantId();
			ServiceDTOBeanConverter converter = WsServiceLocator.getServiceDTOBeanConverter(tenantId);
			TenantOnlySecurityFilter filter = new TenantOnlySecurityFilter(tenantId);
			NewestEventsForAssetIdLoader loader = new NewestEventsForAssetIdLoader(filter);
			RealTimeInspectionLookupHandler lookupHandler = new RealTimeInspectionLookupHandler(loader);

			List<Event> events = lookupHandler.setAssetId(requestInformation.getProductId()).setLastEventDate(requestInformation.getLastInspectionDate())
					.lookup();

			FindInspectionResponse response = new FindInspectionResponse();
			for (Event event : events) {
				response.getInspections().add(converter.convert(event));
			}

			return response;
		} catch (Exception e) {
			logger.error("failed while finding inspection for a given asset id", e);
			throw new ServiceException();
		}
	}

	@Override
	public AssetIdListResponse getAssetIds(AssetSearchRequest request) throws ServiceException {
		AssetIdListResponse response = createResponseHandlerFactory(request).createAssetIdSearchRequestHandler().getResponse(request);
		return response;
	}

	@Override
	public AssetListResponse getAssets(AssetRequest request) throws ServiceException {
		AssetListResponse response = createResponseHandlerFactory(request).createGetAssetRequestHandler().getResponse(request);
		return response;
	}

	private boolean isTransactionCompleted(RequestInformation requestInformation) throws NamingException, InvalidTransactionGUIDException {
		return new TransactionSupervisor(ServiceLocator.getPersistenceManager()).isTransactionCompleted(requestInformation.getMobileGuid(),
				requestInformation.getTenantId());
	}

	@Override
	public SetupDataLastModDatesServiceDTO getSetupDataLastModDates(RequestInformation requestInformation) throws ServiceException {
		ServiceDTOBeanConverter converter = WsServiceLocator.getServiceDTOBeanConverter(requestInformation.getTenantId());

		SetupDataLastModDatesLoader loader = createLoaderFactory(requestInformation).createSetupDataLastModDatesLoader();

		SetupDataLastModDates setupLastModDates = loader.load();
		SetupDataLastModDatesServiceDTO setupLastModDatesDTO = converter.convert(setupLastModDates);

		return setupLastModDatesDTO;
	}

	@Override
	public MobileUpdateInfo getMobileUpdateInfo(String currentVersion) throws ServiceException {
		MobileUpdateInfo mobileUpdateInfo = new MobileUpdateInfo();

		mobileUpdateInfo.setMajorVersion(ConfigContext.getCurrentContext().getInteger(ConfigEntry.CURRENT_MOBILE_MAJOR_VERSION));
		mobileUpdateInfo.setMinorVersion(ConfigContext.getCurrentContext().getInteger(ConfigEntry.CURRENT_MOBILE_MINOR_VERSION));
		mobileUpdateInfo.setBuildVersion(ConfigContext.getCurrentContext().getInteger(ConfigEntry.CURRENT_MOBILE_BUILD_VERSION));
		mobileUpdateInfo.setFileName(ConfigContext.getCurrentContext().getString(ConfigEntry.CURRENT_MOBILE_FILE_NAME));

		return mobileUpdateInfo;
	}

	@Override
	public RequestResponse createCompletedJobSchedule(CompletedJobScheduleRequest request) throws ServiceException {
		try {
			Date convertedNextDate = DtoDateConverter.convertStringToDate(request.getNextDate());
			TenantOnlySecurityFilter filter = new TenantOnlySecurityFilter(request.getTenantId());

			CompletedScheduleCreator scheduleCreator = new CompletedScheduleCreator(new EventByMobileGuidLoader<Event>(filter, Event.class),
                    new SimpleEventSaver(), new FilteredIdLoader<Project>(filter, Project.class));
			scheduleCreator.create(request.getInspectionMobileGuid(), convertedNextDate, request.getJobId());
		} catch (InspectionNotFoundException e) {
			logger.error("could not find inspection for completed job schedule", e);
			throw new ServiceException("Could not find inspection");
		} catch (Exception e) {
			logger.error("problem creating completed job schedule", e);
			throw new ServiceException();
		}

		return new RequestResponse();
	}

	private int getSetupDataPageSize() {
		return ConfigContext.getCurrentContext().getInteger(ConfigEntry.MOBLIE_PAGESIZE_SETUPDATA).intValue();
	}

}
