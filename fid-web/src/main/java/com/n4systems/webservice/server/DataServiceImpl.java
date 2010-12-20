package com.n4systems.webservice.server;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.naming.NamingException;

import org.apache.log4j.Logger;

import rfid.util.PopulatorLogger;

import com.n4systems.ejb.AssetManager;
import com.n4systems.ejb.EventScheduleManager;
import com.n4systems.ejb.OrderManager;
import com.n4systems.ejb.PersistenceManager;
import com.n4systems.ejb.legacy.LegacyAsset;
import com.n4systems.ejb.legacy.LegacyAssetType;
import com.n4systems.ejb.legacy.PopulatorLog;
import com.n4systems.ejb.legacy.ServiceDTOBeanConverter;
import com.n4systems.exceptions.FindAssetFailure;
import com.n4systems.exceptions.InvalidQueryException;
import com.n4systems.exceptions.InvalidScheduleStateException;
import com.n4systems.exceptions.InvalidTransactionGUIDException;
import com.n4systems.exceptions.SubAssetUniquenessException;
import com.n4systems.exceptions.TransactionAlreadyProcessedException;
import com.n4systems.fieldid.permissions.SerializableSecurityGuard;
import com.n4systems.fieldid.permissions.SystemSecurityGuard;
import com.n4systems.handlers.creator.EventPersistenceFactory;
import com.n4systems.handlers.creator.EventsInAGroupCreator;
import com.n4systems.handlers.creator.events.factory.ProductionEventPersistenceFactory;
import com.n4systems.model.AbstractEvent;
import com.n4systems.model.Asset;
import com.n4systems.model.AssetType;
import com.n4systems.model.AssetTypeGroup;
import com.n4systems.model.Event;
import com.n4systems.model.EventBook;
import com.n4systems.model.EventSchedule;
import com.n4systems.model.EventType;
import com.n4systems.model.ExtendedFeature;
import com.n4systems.model.FileAttachment;
import com.n4systems.model.LineItem;
import com.n4systems.model.Project;
import com.n4systems.model.SubAsset;
import com.n4systems.model.SubEvent;
import com.n4systems.model.asset.AssetByMobileGuidLoader;
import com.n4systems.model.asset.AssetSubAssetsLoader;
import com.n4systems.model.asset.SmartSearchLoader;
import com.n4systems.model.event.EventAttachmentSaver;
import com.n4systems.model.event.EventByMobileGuidLoader;
import com.n4systems.model.event.EventBySubEventLoader;
import com.n4systems.model.event.NewestEventsForAssetIdLoader;
import com.n4systems.model.eventschedule.EventScheduleByGuidOrIdLoader;
import com.n4systems.model.eventschedule.EventScheduleSaver;
import com.n4systems.model.location.Location;
import com.n4systems.model.orgs.CustomerOrg;
import com.n4systems.model.orgs.CustomerOrgWithArchivedPaginatedLoader;
import com.n4systems.model.orgs.DivisionOrg;
import com.n4systems.model.orgs.DivisionOrgPaginatedLoader;
import com.n4systems.model.orgs.InternalOrg;
import com.n4systems.model.orgs.PrimaryOrg;
import com.n4systems.model.orgs.PrimaryOrgByTenantLoader;
import com.n4systems.model.orgs.SecondaryOrg;
import com.n4systems.model.orgs.SecondaryOrgPaginatedLoader;
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
import com.n4systems.servicedto.converts.DtoToModelConverterFactory;
import com.n4systems.servicedto.converts.EmployeeServiceDTOConverter;
import com.n4systems.servicedto.converts.InspectionServiceDTOConverter;
import com.n4systems.servicedto.converts.LocationConverter;
import com.n4systems.servicedto.converts.LocationServiceToContainerConverter;
import com.n4systems.servicedto.converts.ProductServiceDTOConverter;
import com.n4systems.servicedto.converts.util.DtoDateConverter;
import com.n4systems.services.TenantFinder;
import com.n4systems.services.asset.AssetSaveService;
import com.n4systems.tools.Pager;
import com.n4systems.util.ConfigContext;
import com.n4systems.util.ConfigEntry;
import com.n4systems.util.ServiceLocator;
import com.n4systems.util.StringUtils;
import com.n4systems.util.TransactionSupervisor;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereParameter.Comparator;
import com.n4systems.webservice.ModelToServiceConverterFactory;
import com.n4systems.webservice.RequestHandlerFactory;
import com.n4systems.webservice.assetdownload.AssetIdListResponse;
import com.n4systems.webservice.assetdownload.AssetListResponse;
import com.n4systems.webservice.assetdownload.AssetRequest;
import com.n4systems.webservice.assetdownload.AssetSearchRequest;
import com.n4systems.webservice.dto.AbstractInspectionServiceDTO;
import com.n4systems.webservice.dto.AuthenticationRequest;
import com.n4systems.webservice.dto.AuthenticationRequest.LoginType;
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
import com.n4systems.webservice.dto.ProductLookupable;
import com.n4systems.webservice.dto.ProductServiceDTO;
import com.n4systems.webservice.dto.ProductTypeGroupListResponse;
import com.n4systems.webservice.dto.ProductTypeListResponse;
import com.n4systems.webservice.dto.RequestInformation;
import com.n4systems.webservice.dto.RequestResponse;
import com.n4systems.webservice.dto.ResponseStatus;
import com.n4systems.webservice.dto.SetupDataLastModDatesServiceDTO;
import com.n4systems.webservice.dto.SubInspectionServiceDTO;
import com.n4systems.webservice.dto.SubProductMapServiceDTO;
import com.n4systems.webservice.dto.VendorListResponse;
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

@SuppressWarnings("deprecation")
public class DataServiceImpl implements DataService {
	private static Logger logger = Logger.getLogger(DataServiceImpl.class);

	public DataServiceImpl() {
	}

	private RequestHandlerFactory createResponseHandlerFactory(RequestInformation request) {
		LoaderFactory loaderFactory = createLoaderFactory(request);
		ModelToServiceConverterFactory converterFactory = new ModelToServiceConverterFactory(loaderFactory, ServiceLocator.getServiceDTOBeanConverter());
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

			PersistenceManager persistenceManager = ServiceLocator.getPersistenceManager();
			ServiceDTOBeanConverter converter = ServiceLocator.getServiceDTOBeanConverter();

			SecurityFilter securityFilter = new TenantOnlySecurityFilter(paginatedRequestInformation.getTenantId());
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

			LegacyAssetType assetTypeManager = ServiceLocator.getAssetType();
			ServiceDTOBeanConverter converter = ServiceLocator.getServiceDTOBeanConverter();

			List<AssetType> assetTypes = assetTypeManager.getAssetTypesForTenant(paginatedRequestInformation.getTenantId());

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

			PersistenceManager persistenceManager = ServiceLocator.getPersistenceManager();
			ServiceDTOBeanConverter converter = ServiceLocator.getServiceDTOBeanConverter();

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

			ServiceDTOBeanConverter converter = ServiceLocator.getServiceDTOBeanConverter();
			PersistenceManager persistenceManager = ServiceLocator.getPersistenceManager();

			int currentPage = request.getPageNumber().intValue();

			Date modified = DtoDateConverter.convertStringToDate(request.getModified());

			SecurityFilter securityFilter = new TenantOnlySecurityFilter(request.getTenantId());
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
			ServiceDTOBeanConverter converter = ServiceLocator.getServiceDTOBeanConverter();

			LoaderFactory loaderFactory = new LoaderFactory(new TenantOnlySecurityFilter(requestInformation.getTenantId()));
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
			ServiceDTOBeanConverter converter = ServiceLocator.getServiceDTOBeanConverter();

			LoaderFactory loaderFactory = new LoaderFactory(new TenantOnlySecurityFilter(requestInformation.getTenantId()));
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
			ServiceDTOBeanConverter converter = ServiceLocator.getServiceDTOBeanConverter();

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
			ServiceDTOBeanConverter converter = ServiceLocator.getServiceDTOBeanConverter();

			LoaderFactory loaderFactory = new LoaderFactory(new TenantOnlySecurityFilter(requestInformation.getTenantId()));
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
				primaryLoader.setTenantId(requestInformation.getTenantId());
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

		PersistenceManager persistenceManager = ServiceLocator.getPersistenceManager();
		try {
			ProductLookupInformation lookupInformation = request.getProductLookupInformation();

			Asset asset = lookupProduct(lookupInformation, request.getTenantId());

			User user = null;
			if (request.modifiedByIdExists()) {
				user = persistenceManager.find(User.class, request.getModifiedById());
			}

			LocationConverter locationConverter = new LocationServiceToContainerConverter(createLoaderFactory(request));
			locationConverter.convert(request, asset);

			AssetSaveService saver = new AssetSaveService(ServiceLocator.getLegacyAssetManager(), user);
			saver.setAsset(asset).update();

		} catch (Exception e) {
			logger.error("Exception occured while doing a limited asset update");
			throw new ServiceException();
		}

		return new RequestResponse();
	}

	@Override
	public RequestResponse updateProductByCustomer(UpdateProductByCustomerRequest request) throws ServiceException {

		try {
			ProductLookupInformation lookupInformation = request.getProductLookupInformation();

			Asset asset = lookupProduct(lookupInformation, request.getTenantId());

			ServiceDTOBeanConverter converter = ServiceLocator.getServiceDTOBeanConverter();
			PersistenceManager persistenceManager = ServiceLocator.getPersistenceManager();
			asset.setOwner(converter.convert(request.getOwnerId(), request.getTenantId()));

			User user = null;

			if (request.modifiedByIdExists()) {
				user = persistenceManager.find(User.class, request.getModifiedById());
			}

			asset.setAdvancedLocation(Location.onlyFreeformLocation(request.getLocation()));
			asset.setCustomerRefNumber(request.getCustomerRefNumber());
			asset.setPurchaseOrder(request.getPurchaseOrder());

			AssetSaveService saver = new AssetSaveService(ServiceLocator.getLegacyAssetManager(), user);
			saver.setAsset(asset).update();

		} catch (Exception e) {
			logger.error("Exception occured while doing asset update by customer");
			throw new ServiceException();
		}

		return new RequestResponse();
	}

	@Override
	public RequestResponse createInspectionSchedule(InspectionScheduleRequest request) throws ServiceException {

		try {

			ServiceDTOBeanConverter converter = ServiceLocator.getServiceDTOBeanConverter();
			EventSchedule eventSchedule = converter.convert(request.getScheduleService(), request.getTenantId());

			new InspectionScheduleCreateHandler(new AssetByMobileGuidLoader(new TenantOnlySecurityFilter(request.getTenantId())), new FilteredIdLoader<Asset>(
					new TenantOnlySecurityFilter(request.getTenantId()), Asset.class), new FilteredIdLoader<EventType>(new TenantOnlySecurityFilter(
					request.getTenantId()), EventType.class), new EventScheduleSaver())
					.createNewInspectionSchedule(eventSchedule, request.getScheduleService());

		} catch (Exception e) {
			logger.error("Exception occured while saving inspection schedule");
			throw new ServiceException();
		}

		return new RequestResponse();
	}

	@Override
	public RequestResponse updateInspectionSchedule(InspectionScheduleRequest request) throws ServiceException {

		try {

			new InspectionScheduleUpdateHandler(new EventScheduleByGuidOrIdLoader(new TenantOnlySecurityFilter(request.getTenantId())),
					new EventScheduleSaver()).updateInspectionSchedule(request.getScheduleService());

		} catch (Exception e) {
			logger.error("Exception occured while updating inspection schedule");
			throw new ServiceException();
		}

		return new RequestResponse();

	}

	@Override
	public RequestResponse removeInspectionSchedule(InspectionScheduleRequest request) throws ServiceException {

		try {

			new InspectionScheduleUpdateHandler(new EventScheduleByGuidOrIdLoader(new TenantOnlySecurityFilter(request.getTenantId())),
					new EventScheduleSaver()).removeInspectionSchedule(request.getScheduleService());

		} catch (Exception e) {
			logger.error("Exception occured while removing inspection schedule");
			throw new ServiceException();
		}

		return new RequestResponse();

	}

	private Asset lookupProduct(ProductLookupable productLookupableDto, Long tenantId) {
		AssetManager assetManager = ServiceLocator.getAssetManager();

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

			OrderManager orderManager = ServiceLocator.getOrderManager();

			long tenantId = requestInformation.getTenantId();
			Asset existingAsset = lookupProduct(productDTO, tenantId);

			if (existingAsset == null) {
				logger.error("can not load asset to edit");
				throw new ServiceException();
			}

			productDTO = fixModifyByFromOldVersionsOfMobile(productDTO);
			productDTO.unsetIdentifedById();

			ProductServiceDTOConverter converter = createProductServiceDTOConverter(tenantId);

			Asset asset = converter.convert(productDTO, existingAsset);

			updateShopOrderOnProduct(asset, productDTO, orderManager, tenantId);

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

	private void updateShopOrderOnProduct(Asset asset, ProductServiceDTO productDTO, OrderManager orderManager, Long tenantId) {
		PrimaryOrg primaryOrg = getTenantFinder().findPrimaryOrg(tenantId);

		// Update the shop order only if the tenant does not have Integration
		// and the order number has changed
		// Integration tenants cannot add/update order information from mobile
		if (!primaryOrg.hasExtendedFeature(ExtendedFeature.Integration) && StringUtils.isNotEmpty(productDTO.getOrderNumber())) {
			LineItem currentProductOrder = asset.getShopOrder();
			if (currentProductOrder == null || !currentProductOrder.getOrder().getOrderNumber().equalsIgnoreCase(productDTO.getOrderNumber().trim())) {
				asset.setShopOrder(orderManager.createNonIntegrationShopOrder(productDTO.getOrderNumber(), tenantId));
			}
		}
	}

	@Override
	public RequestResponse createProduct(RequestInformation requestInformation, ProductServiceDTO productDTO) throws ServiceException {
		Asset asset = null;
		RequestResponse response = new RequestResponse();
		response.setStatus(ResponseStatus.OK);

		testTransactionId(requestInformation);

		try {
			PopulatorLogger populatorLogger = PopulatorLogger.getInstance();
			LegacyAsset productManager = ServiceLocator.getLegacyAssetManager();

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
				Asset linkedAsset = networkLoader.setSerialNumber(productDTO.getSerialNumber()).setRfidNumber(productDTO.getRfidNumber())
						.setRefNumber(productDTO.getCustomerRefNumber()).setVendorOrgId(productDTO.getVendorId()).load();
				asset.setLinkedAsset(linkedAsset);
			}

			// create the asset with attached sub asset transactionally
			asset = productManager.createAssetWithServiceTransaction(requestInformation.getMobileGuid(), asset, asset.getModifiedBy());

			// create any new subproducts (this is not currently used by mobile
			// (sub products come up attached to inspections))
			if (productDTO.getSubProducts() != null && productDTO.getSubProducts().size() > 0) {
				List<SubAsset> subAssets = lookupOrCreateSubProducts(requestInformation.getTenantId(), productDTO.getSubProducts(), asset,
						requestInformation.getVersionNumber());
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
			logger.info("transaction already processed for asset  " + asset.getSerialNumber());
			return response;
		} catch (Exception e) {
			logger.error("failed while processing asset", e);
			throw new ServiceException("Problem creating asset");
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
		OrderManager orderManager = ServiceLocator.getOrderManager();

		ProductServiceDTOConverter converter = createProductServiceDTOConverter(tenantId);
		asset = converter.convert(productDTO, asset);

		updateShopOrderOnProduct(asset, productDTO, orderManager, tenantId);

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
		LegacyAsset productManager = ServiceLocator.getLegacyAssetManager();

		Asset asset = convertNewProduct(tenantId, productDTO);

		asset = productManager.create(asset, asset.getIdentifiedBy());

		logSuccessfulProductCreate(tenantId, populatorLogger, asset);

		return asset;
	}

	private void logSuccessfulProductCreate(Long tenantId, PopulatorLogger populatorLogger, Asset asset) {
		populatorLogger.logMessage(tenantId, "Successfully created asset with serial number " + asset.getSerialNumber(), PopulatorLog.logType.mobile,
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

			Long tenantId = requestInformation.getTenantId();

			PopulatorLogger populatorLogger = PopulatorLogger.getInstance();

			InspectionServiceDTOConverter converter = createInspectionServiceDTOConverter(tenantId);

			LegacyAsset productManager = ServiceLocator.getLegacyAssetManager();
			EventScheduleManager scheduleManager = ServiceLocator.getEventScheduleManager();

			List<Event> events = new ArrayList<Event>();
			// Map<Inspection, AssetStatus> assetStatus = new
			// HashMap<Inspection, AssetStatus>();
			Map<Event, Date> nextInspectionDates = new HashMap<Event, Date>();
			Map<Event, EventSchedule> inspectionSchedules = new HashMap<Event, EventSchedule>();
			Asset asset = null;
			EventScheduleByGuidOrIdLoader scheduleLoader = new EventScheduleByGuidOrIdLoader(new TenantOnlySecurityFilter(tenantId));
			for (InspectionServiceDTO inspectionServiceDTO : inspectionDTOs) {
				asset = findOrTagProduct(tenantId, inspectionServiceDTO);
				inspectionServiceDTO.setProductId(asset.getId());

				// lets look up or create all newly attached sub products and
				// attach to asset
				List<SubAsset> subAssets = lookupOrCreateSubProducts(tenantId, inspectionServiceDTO.getNewSubProducts(), asset,
						requestInformation.getVersionNumber());
				updateSubProducts(productManager, tenantId, asset, inspectionServiceDTO, subAssets);

				// we also need to get the asset for any sub-inspections
				if (inspectionServiceDTO.getSubInspections() != null) {
					Asset subProduct = null;
					for (SubInspectionServiceDTO subInspection : inspectionServiceDTO.getSubInspections()) {
						subProduct = findOrTagProduct(tenantId, subInspection);
						subInspection.setProductId(subProduct.getId());
					}
				}

				Event event = converter.convert(inspectionServiceDTO);
				events.add(event);
				nextInspectionDates.put(event, DtoDateConverter.convertStringToDate(inspectionServiceDTO.getNextDate()));
				inspectionSchedules.put(event, loadScheduleFromInspectionDto(scheduleLoader, inspectionServiceDTO));
			}

			List<Event> savedEvents = null;

			try {
				EventPersistenceFactory eventPersistenceFactory = new ProductionEventPersistenceFactory();

				EventsInAGroupCreator eventsInAGroupCreator = eventPersistenceFactory.createEventsInAGroupCreator();

				savedEvents = eventsInAGroupCreator.create(requestInformation.getMobileGuid(), events, nextInspectionDates);
				logger.info("save inspections on asset " + asset.getSerialNumber());
				populatorLogger.logMessage(tenantId, "Created inspection for asset with serial number " + asset.getSerialNumber(), PopulatorLog.logType.mobile,
						PopulatorLog.logStatus.success);
			} catch (TransactionAlreadyProcessedException e) {
				// if the transaction is already complete just return success.
				logger.info("transaction already processed for inspections on asset  " + asset.getSerialNumber());
			} catch (Exception e) {
				logger.error("failed to save inspections", e);
				throw new InspectionException("Failed to save inspections");
			}

			if (savedEvents != null) {
				for (Event savedEvent : savedEvents) {
					for (Entry<Event, EventSchedule> scheduleEntry : inspectionSchedules.entrySet()) {
						if (savedEvent.equals(scheduleEntry.getKey())) {
							EventSchedule schedule = scheduleEntry.getValue();
							try {
								if (schedule != null) {
									schedule.completed(scheduleEntry.getKey());
									scheduleManager.update(schedule);
								}
							} catch (InvalidScheduleStateException e) {
								logger.warn("the state of the schedule is not valid to be completed.");
								populatorLogger.logMessage(tenantId, "Could not attach inspection schedule to inspection on asset with serial number "
										+ savedEvent.getAsset().getSerialNumber(), PopulatorLog.logType.mobile, PopulatorLog.logStatus.error);
							} catch (Exception e) {
								logger.error("failed to attach schedule to inspection", e);
								populatorLogger.logMessage(tenantId, "Could not attach inspection schedule to inspection on asset with serial number "
										+ savedEvent.getAsset().getSerialNumber(), PopulatorLog.logType.mobile, PopulatorLog.logStatus.error);
								// We allow the inspection to still go through
								// even if this happens
							}

							break;
						}
					}
				}
			}

			return response;

		} catch (InspectionException e) {
			throw e;
		} catch (FindAssetFailure e) {
			throw new ProductException("Could not find asset");
		} catch (Exception e) {
			logger.error("failed while processing inspections", e);
			throw new ServiceException("Problem processing inspections");
		}
	}

	private EventSchedule loadScheduleFromInspectionDto(EventScheduleByGuidOrIdLoader scheduleLoader, InspectionServiceDTO inspectionServiceDTO) {
		return scheduleLoader.setId(inspectionServiceDTO.getInspectionScheduleId()).setMobileGuid(inspectionServiceDTO.getInspectionScheduleMobileGuid())
				.load();
	}

	private void updateSubProducts(LegacyAsset productManager, Long tenantId, Asset asset, InspectionServiceDTO inspectionServiceDTO, List<SubAsset> subAssets)
			throws SubAssetUniquenessException {

		new UpdateSubProducts(productManager, tenantId, asset, inspectionServiceDTO, subAssets, ServiceLocator.getAssetManager()).run();

	}

	@Override
	public RequestResponse createInspectionImage(RequestInformation requestInformation, InspectionImageServiceDTO inspectionImageServiceDTO)
			throws ServiceException, InspectionException {
		RequestResponse response = new RequestResponse();

		Long tenantId = requestInformation.getTenantId();

		ServiceDTOBeanConverter converter = ServiceLocator.getServiceDTOBeanConverter();
		PersistenceManager persistenceManager = ServiceLocator.getPersistenceManager();

		try {

			User performedBy = persistenceManager.find(User.class, inspectionImageServiceDTO.getPerformedById());

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
		}

		return response;
	}

	private List<SubAsset> lookupOrCreateSubProducts(Long tenantId, List<SubProductMapServiceDTO> subProductMaps, Asset masterAsset, long apiVersion)
			throws Exception {

		List<SubAsset> subAssets = new ArrayList<SubAsset>();

		if (subProductMaps == null)
			return subAssets;

		AssetManager assetManager = ServiceLocator.getAssetManager();
		PersistenceManager persistenceManager = ServiceLocator.getPersistenceManager();

		for (SubProductMapServiceDTO subProductMap : subProductMaps) {
			ProductServiceDTO subProductDTO = subProductMap.getNewProduct();

			Asset asset = assetManager.findAssetByGUID(subProductDTO.getMobileGuid(), new TenantOnlySecurityFilter(tenantId));

			// Try by id
			if (asset == null && subProductDTO.getId() != null && subProductDTO.getId() > 0) {
				asset = persistenceManager.find(Asset.class, subProductDTO.getId());
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
	private Asset findOrTagProduct(Long tenantId, AbstractInspectionServiceDTO inspectionServiceDTO) throws FindAssetFailure {

		Asset asset = null;
		if (inspectionServiceDTO.productIdExists()) {
			try {
				asset = ServiceLocator.getAssetManager().findAsset(inspectionServiceDTO.getProductId(), new TenantOnlySecurityFilter(tenantId));
				asset = ServiceLocator.getAssetManager().fillInSubAssetsOnAsset(asset);
			} catch (Exception e) {
				logger.error("looking up asset with asset id " + inspectionServiceDTO.getProductId(), e);
			}

		} else if (inspectionServiceDTO.productMobileGuidExists()) {
			// Try looking up by GUID
			try {
				asset = ServiceLocator.getAssetManager().findAssetByGUID(inspectionServiceDTO.getProductMobileGuid(), new TenantOnlySecurityFilter(tenantId));
			} catch (Exception e) {
				logger.error("Looking up asset serial by GUID = " + inspectionServiceDTO.getProductMobileGuid(), e);
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
			ServiceDTOBeanConverter converter = ServiceLocator.getServiceDTOBeanConverter();
			SecurityFilter securityFilter = new TenantOnlySecurityFilter(requestInformation.getTenantId());
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
			ServiceDTOBeanConverter converter = ServiceLocator.getServiceDTOBeanConverter();
			TenantOnlySecurityFilter filter = new TenantOnlySecurityFilter(requestInformation.getTenantId());
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
		ServiceDTOBeanConverter converter = ServiceLocator.getServiceDTOBeanConverter();

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
					new EventScheduleSaver(), new FilteredIdLoader<Project>(filter, Project.class));
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
