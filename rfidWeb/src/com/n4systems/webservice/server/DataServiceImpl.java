package com.n4systems.webservice.server;


import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.naming.NamingException;

import org.apache.log4j.Logger;

import rfid.ejb.entity.ProductStatusBean;
import rfid.ejb.entity.UserBean;
import rfid.ejb.session.LegacyProductSerial;
import rfid.ejb.session.LegacyProductType;
import rfid.ejb.session.PopulatorLog;
import rfid.ejb.session.ServiceDTOBeanConverter;
import rfid.ejb.session.User;
import rfid.util.PopulatorLogger;

import com.n4systems.ejb.CustomerManager;
import com.n4systems.ejb.InspectionManager;
import com.n4systems.ejb.InspectionScheduleManager;
import com.n4systems.ejb.PersistenceManager;
import com.n4systems.ejb.ProductManager;
import com.n4systems.exceptions.FindProductFailure;
import com.n4systems.exceptions.InvalidQueryException;
import com.n4systems.exceptions.InvalidTransactionGUIDException;
import com.n4systems.exceptions.TransactionAlreadyProcessedException;
import com.n4systems.model.AutoAttributeCriteria;
import com.n4systems.model.AutoAttributeDefinition;
import com.n4systems.model.Inspection;
import com.n4systems.model.InspectionBook;
import com.n4systems.model.InspectionGroup;
import com.n4systems.model.InspectionSchedule;
import com.n4systems.model.InspectionType;
import com.n4systems.model.Product;
import com.n4systems.model.ProductType;
import com.n4systems.model.ProductTypeGroup;
import com.n4systems.model.Project;
import com.n4systems.model.StateSet;
import com.n4systems.model.SubProduct;
import com.n4systems.model.Tenant;
import com.n4systems.model.orgs.CustomerOrg;
import com.n4systems.model.orgs.CustomerOrgPaginatedLoader;
import com.n4systems.model.orgs.DivisionOrg;
import com.n4systems.model.orgs.DivisionOrgPaginatedLoader;
import com.n4systems.model.orgs.PrimaryOrg;
import com.n4systems.model.orgs.SecondaryOrg;
import com.n4systems.model.orgs.SecondaryOrgPaginatedLoader;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.model.security.TenantOnlySecurityFilter;
import com.n4systems.model.tenant.SetupDataLastModDates;
import com.n4systems.persistence.loaders.LoaderFactory;
import com.n4systems.services.SetupDataLastModUpdateService;
import com.n4systems.services.TenantCache;
import com.n4systems.tools.Pager;
import com.n4systems.util.ConfigContext;
import com.n4systems.util.ConfigEntry;
import com.n4systems.util.ServiceLocator;
import com.n4systems.util.TransactionSupervisor;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereParameter;
import com.n4systems.util.persistence.WhereParameter.Comparator;
import com.n4systems.webservice.dto.AbstractInspectionServiceDTO;
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
import com.n4systems.webservice.dto.ResponseStatus;
import com.n4systems.webservice.dto.SecondaryOrgListResponse;
import com.n4systems.webservice.dto.SetupDataLastModDatesServiceDTO;
import com.n4systems.webservice.dto.StateSetListResponse;
import com.n4systems.webservice.dto.SubInspectionServiceDTO;
import com.n4systems.webservice.dto.SubProductMapServiceDTO;
import com.n4systems.webservice.dto.TransactionLogServiceDTO;
import com.n4systems.webservice.dto.UserListResponse;
import com.n4systems.webservice.dto.UserServiceDTO;
import com.n4systems.webservice.dto.WSJobSearchCriteria;
import com.n4systems.webservice.dto.WSSearchCritiera;
import com.n4systems.webservice.dto.AuthenticationRequest.LoginType;
import com.n4systems.webservice.exceptions.InspectionException;
import com.n4systems.webservice.exceptions.ProductException;
import com.n4systems.webservice.exceptions.ServiceException;

public class DataServiceImpl implements DataService {

	private static Logger logger = Logger.getLogger(DataServiceImpl.class);

	private static int FIRST_PAGE = 1;
	
	public AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest) throws ServiceException {
		
		try {
			
			logAuthenticationAttempt(authenticationRequest);
			
			WebServiceAuthenticator authenticator = new WebServiceAuthenticator(authenticationRequest);
			
			return authenticator.authenticate();
			
		} catch( Exception e ) {
			logger.error("exception occured while authenticating the web service.", e);
			throw new ServiceException();
		}
	}
	
	private void logAuthenticationAttempt(AuthenticationRequest authenticationRequest) {
		String loginType = "username and password";
		if (authenticationRequest.getLoginType() == LoginType.SECURITY) {
			loginType = "security rfid number";
		}
		
		logger.info("Webservice authenication attempt using "+loginType+". "+
				"Handheld version "+authenticationRequest.getMajorVersion()+"."+authenticationRequest.getMinorVersion()+". "+
				"Tenant ID: "+authenticationRequest.getTenantName()+" and User ID: "+authenticationRequest.getUserId());
	}
	
	public List<TransactionLogServiceDTO> getRecentTransactions(RequestInformation requestInformation, Long lastRevision) throws ServiceException {		
		return new ArrayList<TransactionLogServiceDTO>();
	}
	
	public InspectionTypeListResponse getAllInspectionTypes(PaginatedRequestInformation paginatedRequestInformation) throws ServiceException {
		
		try {
			int RESULTS_PER_PAGE = ConfigContext.getCurrentContext().getInteger( ConfigEntry.MOBLIE_PAGESIZE_SETUPDATA ).intValue();
		
			InspectionTypeListResponse response = new InspectionTypeListResponse();
			
			PersistenceManager persistenceManager = ServiceLocator.getPersistenceManager();
			ServiceDTOBeanConverter converter = ServiceLocator.getServiceDTOBeanConverter();
			
			SecurityFilter securityFilter = new TenantOnlySecurityFilter(paginatedRequestInformation.getTenantId());
			QueryBuilder<InspectionType> queryBuilder = new QueryBuilder<InspectionType>(InspectionType.class, securityFilter);
			queryBuilder.setSimpleSelect();
			queryBuilder.addPostFetchPaths("sections", "infoFieldNames");
			
			List<InspectionType> inspectionTypes = null;
				inspectionTypes = persistenceManager.findAll( queryBuilder );
			
			for (InspectionType inspectionType : inspectionTypes) {
				response.getInspectionTypes().add( converter.convert(inspectionType) );
			}
			
			response.setCurrentPage(1);
			response.setRecordsPerPage(RESULTS_PER_PAGE);
			response.setStatus(ResponseStatus.OK);
			response.setTotalPages(1);
			
			return response;
		
		} catch( Exception e ) {
			logger.error( "exception occured while lookup the list of inspection types.", e );
			throw new ServiceException();
		}
	}
	
	public StateSetListResponse getAllStateSets(PaginatedRequestInformation paginatedRequestInformation) throws ServiceException {
		try {
			int RESULTS_PER_PAGE = ConfigContext.getCurrentContext().getInteger( ConfigEntry.MOBLIE_PAGESIZE_SETUPDATA ).intValue();
			StateSetListResponse response = new StateSetListResponse();
			
			PersistenceManager persistenceManager = ServiceLocator.getPersistenceManager();
			ServiceDTOBeanConverter converter = ServiceLocator.getServiceDTOBeanConverter();
			
			SecurityFilter securityFilter = new TenantOnlySecurityFilter(paginatedRequestInformation.getTenantId());
			QueryBuilder<StateSet> queryBuilder = new QueryBuilder<StateSet>(StateSet.class, securityFilter);
			queryBuilder.setSimpleSelect();
	
			List<StateSet> stateSets = null;
				stateSets = persistenceManager.findAll( queryBuilder );
			
			for (StateSet stateSet : stateSets) {
				if( !stateSet.isRetired() ) {
					response.getStateSets().add( converter.convert(stateSet) );
				}
			}
			
			response.setCurrentPage(1);
			response.setRecordsPerPage(RESULTS_PER_PAGE);
			response.setStatus(ResponseStatus.OK);
			response.setTotalPages(1);
			
			return response;
		
		} catch( Exception e ) {
			logger.error( "exception occured while lookup the list of state sets.", e );
			throw new ServiceException();
		}
	}
	
	public InspectionBookListResponse getAllInspectionBooks(PaginatedRequestInformation paginatedRequestInformation) throws ServiceException {
		try {
			int RESULTS_PER_PAGE = ConfigContext.getCurrentContext().getInteger( ConfigEntry.MOBLIE_PAGESIZE_SETUPDATA ).intValue();
			
			InspectionBookListResponse response = new InspectionBookListResponse();
			
			PersistenceManager persistenceManager = ServiceLocator.getPersistenceManager();
			ServiceDTOBeanConverter converter = ServiceLocator.getServiceDTOBeanConverter();
			
			SecurityFilter securityFilter = new TenantOnlySecurityFilter(paginatedRequestInformation.getTenantId());
			QueryBuilder<InspectionBook> queryBuilder = new QueryBuilder<InspectionBook>(InspectionBook.class, securityFilter);
			queryBuilder.setSimpleSelect();
	
			List<InspectionBook> inspectionBooks = null;
			inspectionBooks = persistenceManager.findAll( queryBuilder );
			
			for (InspectionBook inspectionBook : inspectionBooks) {
				response.getInspectionBooks().add( converter.convert(inspectionBook) );
			}
			
			response.setCurrentPage(1);
			response.setRecordsPerPage(RESULTS_PER_PAGE);
			response.setStatus(ResponseStatus.OK);
			response.setTotalPages(1);
			
			return response;
		} catch (InvalidQueryException e) {	
			throw new ServiceException();
		} catch( Exception e ) {
			throw new ServiceException();
		}
	}

	public ProductTypeListResponse getAllProductTypes(PaginatedRequestInformation paginatedRequestInformation) throws ServiceException {
		try {
			int RESULTS_PER_PAGE = ConfigContext.getCurrentContext().getInteger( ConfigEntry.MOBLIE_PAGESIZE_SETUPDATA ).intValue();
	
			ProductTypeListResponse response = new ProductTypeListResponse();
	
			LegacyProductType productTypeManager = ServiceLocator.getProductType();
			ServiceDTOBeanConverter converter = ServiceLocator.getServiceDTOBeanConverter();
			
			List<ProductType> productTypes = productTypeManager.getProductTypesForTenant( paginatedRequestInformation.getTenantId() );
			
			for (ProductType productType : productTypes) {
				response.getProductTypes().add( converter.convert_new(productType) );
			}
			
			response.setCurrentPage(1);
			response.setRecordsPerPage(RESULTS_PER_PAGE);
			response.setStatus(ResponseStatus.OK);
			response.setTotalPages(1);
			
			return response;
		} catch( Exception e ) {
			logger.error( "exception occured while lookup the list of product types.", e );
			throw new ServiceException();
		}
	}
	
	public ProductTypeGroupListResponse getAllProductTypeGroups(PaginatedRequestInformation paginatedRequestInformation) throws ServiceException {
		try {
			int RESULTS_PER_PAGE = ConfigContext.getCurrentContext().getInteger(ConfigEntry.MOBLIE_PAGESIZE_SETUPDATA).intValue();
			
			ProductTypeGroupListResponse response = new ProductTypeGroupListResponse();
			
			PersistenceManager persistenceManager = ServiceLocator.getPersistenceManager();
			ServiceDTOBeanConverter converter = ServiceLocator.getServiceDTOBeanConverter();
			
			SecurityFilter securityFilter = new TenantOnlySecurityFilter(paginatedRequestInformation.getTenantId());
			QueryBuilder<ProductTypeGroup> queryBuilder = new QueryBuilder<ProductTypeGroup>(ProductTypeGroup.class, securityFilter);
			queryBuilder.setSimpleSelect();
			
			List<ProductTypeGroup> productTypeGroups = persistenceManager.findAll(queryBuilder);
			
			for (ProductTypeGroup productTypeGroup : productTypeGroups) {
				response.getProductTypeGroups().add(converter.convert(productTypeGroup));
			}
			
			response.setCurrentPage(1);
			response.setRecordsPerPage(RESULTS_PER_PAGE);
			response.setStatus(ResponseStatus.OK);
			response.setTotalPages(1);
			
			return response;
			
		} catch (Exception e) {
			logger.error("exception occured while looking up product type groups", e);
			throw new ServiceException();
		}
	}
	
	public JobListResponse getAllJobs(PaginatedUpdateRequestInfo request) throws ServiceException {
		try {
			JobListResponse response = new JobListResponse();
						
			ServiceDTOBeanConverter converter = ServiceLocator.getServiceDTOBeanConverter();
			PersistenceManager persistenceManager = ServiceLocator.getPersistenceManager();
			
			int jobsPerPage = ConfigContext.getCurrentContext().getInteger(ConfigEntry.MOBLIE_PAGESIZE_SETUPDATA);
			int currentPage = request.getPageNumber().intValue();
			
			Date modified = converter.convertStringToDate(request.getModified());
			
			SecurityFilter securityFilter = new TenantOnlySecurityFilter(request.getTenantId());
			QueryBuilder<Project> jobBuilder = new QueryBuilder<Project>(Project.class, securityFilter);
			jobBuilder.addWhere(Comparator.EQ, "eventjob", "eventJob", true);
			jobBuilder.addWhere(Comparator.EQ, "open", "open",true);
			jobBuilder.addWhere(Comparator.EQ, "retired", "retired", false);
			if (modified != null)
			{
				jobBuilder.addWhere(Comparator.EQ, "modified", "modified", modified);
			}
			jobBuilder.addFetch("resources");
			
			// for postgres to paginate correctly.
			jobBuilder.addOrder("id");
			
			if (currentPage != PaginatedRequestInformation.INFORMATION_PAGE) {
				Pager<Project> jobPage = persistenceManager.findAllPaged(jobBuilder, currentPage, jobsPerPage);
				response.setTotalPages((int)jobPage.getTotalPages());
				
				for (Project job : jobPage.getList()) {
					response.getJobs().add(converter.convert(job));
				}
			} else {
				//response.setTotalPages(persistenceManager.countAllPages(Project.class, jobsPerPage, securityFilter));
			}
			
			response.setRecordsPerPage(jobsPerPage);
			response.setStatus(ResponseStatus.OK);
			
			return response;
			
		} catch (Exception e) {
			logger.error("exception while pulling down jobs", e);
			throw new ServiceException();
		}
	}
	
	public UserListResponse getAllUsers(PaginatedRequestInformation request) throws ServiceException {
		try {
			logger.info("Finding Users: Tenant [" + request.getTenantId() + "] Page [" + request.getPageNumber() + "]");
			
			UserListResponse response = new UserListResponse();
			
			ServiceDTOBeanConverter converter = ServiceLocator.getServiceDTOBeanConverter();
			PersistenceManager persistenceManager = ServiceLocator.getPersistenceManager();
			
			int usersPerPage = ConfigContext.getCurrentContext().getInteger(ConfigEntry.MOBLIE_PAGESIZE_SETUPDATA);
			int currentPage = request.getPageNumber().intValue();
			
			SecurityFilter securityFilter = new TenantOnlySecurityFilter(request.getTenantId());
			QueryBuilder<UserBean> userBuilder = new QueryBuilder<UserBean>(UserBean.class, securityFilter);
			// This is for postgres to ensure paging works
			userBuilder.addOrder("uniqueID");
			
			if (currentPage != PaginatedRequestInformation.INFORMATION_PAGE) {
				Pager<UserBean> userPage = persistenceManager.findAllPaged(userBuilder, currentPage, usersPerPage);
				response.setTotalPages((int)userPage.getTotalPages());
				
				for(UserBean user: userPage.getList()) {
					response.getUsers().add(converter.convert(user));
				}
			} else {
				response.setTotalPages(persistenceManager.countAllPages(UserBean.class, usersPerPage, securityFilter));
				response.setCurrentPage(currentPage);
			}
			
			response.setRecordsPerPage(usersPerPage);
			response.setStatus(ResponseStatus.OK);
			
			logger.info("Returning Users: Tenant [" + request.getTenantId() + 
					"] Page [" + response.getCurrentPage() + "/" + response.getTotalPages() + 
					"] Users [" + response.getUsers().size() + 
					"] PageSize [" + response.getRecordsPerPage() + 
					"] Status [" +response.getStatus().name() + "]");
			
			return response;
			
		} catch (Exception e) {
			logger.error( "failed while processing users", e );
			throw new ServiceException();			
		}
	}
	
	public JobSiteListResponse getAllJobSites(PaginatedRequestInformation paginatedRequestInformation) throws ServiceException {
		return new JobSiteListResponse();	
	}
	
	public AutoAttributeCriteriaListResponse getAutoAttributeCriteria(PaginatedRequestInformation paginatedRequestInformation) throws ServiceException {
		try {
			int RESULTS_PER_PAGE = ConfigContext.getCurrentContext().getInteger( ConfigEntry.MOBLIE_PAGESIZE_SETUPDATA ).intValue();
			int currentPage = paginatedRequestInformation.getPageNumber().intValue();
			
			ServiceDTOBeanConverter converter = ServiceLocator.getServiceDTOBeanConverter();
			PersistenceManager persistenceManager = ServiceLocator.getPersistenceManager();

			SecurityFilter securityFilter = new TenantOnlySecurityFilter(paginatedRequestInformation.getTenantId());
			QueryBuilder<AutoAttributeCriteria> queryBuilder = new QueryBuilder<AutoAttributeCriteria>(AutoAttributeCriteria.class, securityFilter);
			queryBuilder.addPostFetchPaths("productType", "inputs", "outputs");
			// this is so postgres can paginate correctly.
			queryBuilder.addOrder("id");
			
			Pager<AutoAttributeCriteria> pager = persistenceManager.findAllPaged(queryBuilder, currentPage, RESULTS_PER_PAGE);
			
			AutoAttributeCriteriaListResponse response = new AutoAttributeCriteriaListResponse();
			
			for (AutoAttributeCriteria autoAttributeCriteria : pager.getList()) {
				response.getAutoAttributeCriteria().add( converter.convert(autoAttributeCriteria) );
			}
			
			response.setCurrentPage(currentPage);
			response.setRecordsPerPage(RESULTS_PER_PAGE);
			response.setStatus(ResponseStatus.OK);
			response.setTotalPages( (int)pager.getTotalPages() );
			
			return response;
			
		} catch (Exception e) {
			logger.error("Exception occured while lookup auto attribute criteria", e);
			throw new ServiceException();
		}
	}
	
	public AutoAttributeDefinitionListResponse getAutoAttributeDefinition(PaginatedRequestInformation paginatedRequestInformation) throws ServiceException {
		try {
			int RESULTS_PER_PAGE = ConfigContext.getCurrentContext().getInteger( ConfigEntry.MOBLIE_PAGESIZE_SETUPDATA ).intValue();
			int currentPage = paginatedRequestInformation.getPageNumber().intValue();
			
			ServiceDTOBeanConverter converter = ServiceLocator.getServiceDTOBeanConverter();
			PersistenceManager persistenceManager = ServiceLocator.getPersistenceManager();

			SecurityFilter securityFilter = new TenantOnlySecurityFilter(paginatedRequestInformation.getTenantId());
			QueryBuilder<AutoAttributeDefinition> queryBuilder = new QueryBuilder<AutoAttributeDefinition>(AutoAttributeDefinition.class, securityFilter);
			queryBuilder.addFetch("criteria");
			queryBuilder.addPostFetchPaths("outputs");
			// for postgres to paginate correctly.
			queryBuilder.addOrder("id");
			
			Pager<AutoAttributeDefinition> pager = persistenceManager.findAllPaged(queryBuilder, currentPage, RESULTS_PER_PAGE);
			
			AutoAttributeDefinitionListResponse response = new AutoAttributeDefinitionListResponse();
			
			for (AutoAttributeDefinition autoAttributeDefinition : pager.getList()) {
				response.getAutoAttributeDefinitions().add( converter.convert(autoAttributeDefinition) );
			}
						
			response.setCurrentPage(currentPage);
			response.setRecordsPerPage(RESULTS_PER_PAGE);
			response.setStatus(ResponseStatus.OK);
			response.setTotalPages( (int)pager.getTotalPages() );
			
			return response;
			
		} catch (Exception e) {
			logger.error("Exception occured while lookup auto attribute definition", e);
			throw new ServiceException();
		}		
	}
	
	public CustomerOrgListResponse getAllCustomerOrgs(PaginatedRequestInformation requestInformation) throws ServiceException {
		try {
			int RESULTS_PER_PAGE = ConfigContext.getCurrentContext().getInteger( ConfigEntry.MOBLIE_PAGESIZE_SETUPDATA ).intValue();
			int currentPage = requestInformation.getPageNumber().intValue();			
			ServiceDTOBeanConverter converter = ServiceLocator.getServiceDTOBeanConverter();
									
			LoaderFactory loaderFactory = new LoaderFactory(new TenantOnlySecurityFilter(requestInformation.getTenantId()));
			CustomerOrgPaginatedLoader loader = loaderFactory.createCustomerOrgPaginatedLoader();
			loader.setPage(currentPage);
			loader.setPageSize(RESULTS_PER_PAGE);
			
			Pager<CustomerOrg> pager = loader.load();
			
			CustomerOrgListResponse response = new CustomerOrgListResponse();
			
			for (CustomerOrg customerOrg : pager.getList()) {
				response.getCustomers().add( converter.convert(customerOrg) );
			}
						
			response.setCurrentPage(currentPage);
			response.setRecordsPerPage(RESULTS_PER_PAGE);
			response.setStatus(ResponseStatus.OK);
			response.setTotalPages((int)pager.getTotalPages());
			
			return response;
		} catch (Exception e) {
			logger.error("Exception occured while looking up customer orgs", e);
			throw new ServiceException();
		}
	}
	
	public DivisionOrgListResponse getAllDivisionOrgs(PaginatedRequestInformation requestInformation) throws ServiceException {
		try {
			int RESULTS_PER_PAGE = ConfigContext.getCurrentContext().getInteger( ConfigEntry.MOBLIE_PAGESIZE_SETUPDATA ).intValue();
			int currentPage = requestInformation.getPageNumber().intValue();			
			ServiceDTOBeanConverter converter = ServiceLocator.getServiceDTOBeanConverter();
									
			LoaderFactory loaderFactory = new LoaderFactory(new TenantOnlySecurityFilter(requestInformation.getTenantId()));
			DivisionOrgPaginatedLoader loader = loaderFactory.createDivisionOrgPaginatedLoader();
			loader.setPage(currentPage);
			loader.setPageSize(RESULTS_PER_PAGE);
			
			Pager<DivisionOrg> pager = loader.load();
			
			DivisionOrgListResponse response = new DivisionOrgListResponse();
			
			for (DivisionOrg divisionOrg : pager.getList()) {
				response.getDivisions().add( converter.convert(divisionOrg) );
			}
						
			response.setCurrentPage(currentPage);
			response.setRecordsPerPage(RESULTS_PER_PAGE);
			response.setStatus(ResponseStatus.OK);
			response.setTotalPages((int)pager.getTotalPages());
			
			return response;
		} catch (Exception e) {
			logger.error("Exception occured while looking up division orgs", e);
			throw new ServiceException();
		}
	}
	
	public SecondaryOrgListResponse getAllSecondaryOrgs(PaginatedRequestInformation requestInformation)	throws ServiceException {
		try {
			int RESULTS_PER_PAGE = ConfigContext.getCurrentContext().getInteger( ConfigEntry.MOBLIE_PAGESIZE_SETUPDATA ).intValue();
			int currentPage = requestInformation.getPageNumber().intValue();			
			ServiceDTOBeanConverter converter = ServiceLocator.getServiceDTOBeanConverter();
									
			LoaderFactory loaderFactory = new LoaderFactory(new TenantOnlySecurityFilter(requestInformation.getTenantId()));
			SecondaryOrgPaginatedLoader loader = loaderFactory.createSecondaryOrgPaginatedLoader();
			loader.setPage(currentPage);
			loader.setPageSize(RESULTS_PER_PAGE);
			
			Pager<SecondaryOrg> pager = loader.load();
			
			SecondaryOrgListResponse response = new SecondaryOrgListResponse();
			
			for (SecondaryOrg secondaryOrg : pager.getList()) {
				response.getSecondaryOrgs().add( converter.convert(secondaryOrg) );
			}
						
			response.setCurrentPage(currentPage);
			response.setRecordsPerPage(RESULTS_PER_PAGE);
			response.setStatus(ResponseStatus.OK);
			response.setTotalPages((int)pager.getTotalPages());
			
			return response;
		} catch (Exception e) {
			logger.error("Exception occured while looking up secondary orgs", e);
			throw new ServiceException();
		}
	}
	
	
	public RequestResponse updateProduct( RequestInformation requestInformation, ProductServiceDTO productDTO ) throws ServiceException {
		try {
			RequestResponse response = new RequestResponse();
			response.setStatus(ResponseStatus.OK);

			ServiceDTOBeanConverter converter = ServiceLocator.getServiceDTOBeanConverter();
			ProductManager productManager = ServiceLocator.getProductManager();
			
			Product existingProduct;
			SecurityFilter filter = new TenantOnlySecurityFilter( requestInformation.getTenantId() );
			
			if( productDTO.isCreatedOnMobile() ) {
				existingProduct = productManager.findProductByGUID( productDTO.getMobileGuid(), filter );
			} else {
				existingProduct = productManager.findProductAllFields( productDTO.getId(), filter );
				
			}
			
			if( existingProduct == null ) {
				logger.error( "can not load product to edit" );
				throw new ServiceException();
			}
			
			Product product = converter.convert(productDTO, existingProduct, requestInformation.getTenantId());
			ServiceLocator.getProductSerialManager().update(product);
			
			return response;
		} catch (Exception e) {
			logger.error( "failed while processing product", e );
			throw new ServiceException("Problem updating product");
		}
	}
	
	public RequestResponse createProduct( RequestInformation requestInformation, ProductServiceDTO productDTO ) throws ServiceException {
		Product product = null;
		RequestResponse response = new RequestResponse();
		response.setStatus(ResponseStatus.OK);
		
		testTransactionId( requestInformation );
		
		try {
			PopulatorLogger populatorLogger = PopulatorLogger.getInstance();
			LegacyProductSerial productManager = ServiceLocator.getProductSerialManager();
			
			if( isTransactionCompleted( requestInformation ) ) { 
				return response; 
			}
			
			// convert from the service dto
			product = convertNewProduct( requestInformation.getTenantId(), productDTO );
			
			// create the product with attached sub product transactionally
			product = productManager.createProductWithServiceTransaction( requestInformation.getMobileGuid(), product );

			// create any new subproducts (this is not currently used by mobile (sub products come up attached to inspections))
			if (productDTO.getSubProducts() != null && productDTO.getSubProducts().size() > 0) {
				List<SubProduct> subProducts = lookupOrCreateSubProducts(requestInformation.getTenantId(), productDTO.getSubProducts(), product);			
				if (subProducts.size() > 0) {
					/*
					 * Note: the list of SubProducts on Product is marked as @Transient however productManager.update 
					 * has special handling code to persist it anyway.  and yes it does suck ...  
					 */
					product.getSubProducts().addAll(subProducts);
					productManager.update(product);
				}
			}
			
			logSuccessfulProductCreate( requestInformation.getTenantId(), populatorLogger, product );
		} catch ( TransactionAlreadyProcessedException e ) {
			logger.info( "transaction already processed for product  " + product.getSerialNumber() );
			return response;
		} catch (Exception e) {
			logger.error( "failed while processing product", e );
			throw new ServiceException("Problem creating product");
		}
		return response;
	}
	
	/**
	 * Creates a customer and attached divisions from a CustomerServiceDTO.  If the customer or divisions already existed (defined by the rules 
	 * in {@link CustomerManager#findOrCreateCustomer()}), this method will do nothing.
	 */
	public RequestResponse createCustomer(RequestInformation requestInformation, CustomerServiceDTO customerDTO) throws ServiceException {
		// TODO: CUSTOMER_REFACTOR: DataService fix create customer (or don't it's only used by the importer)
//		RequestResponse response = new RequestResponse();
//		response.setStatus(ResponseStatus.OK);
//		
//		try {
//			CustomerManager customerManager = ServiceLocator.getCustomerManager();
//	
//			LegacySecurityFilter filter = createFilterFromRequest(requestInformation);
//
//			Customer customer = customerManager.findOrCreateCustomer(customerDTO.getName(), customerDTO.getCustomerId(), requestInformation.getTenantId(), filter);
//				
//			for (DivisionServiceDTO divisionService: customerDTO.getDivisions()) {
//				customerManager.findOrCreateDivision(divisionService.getName(), customer.getId(), filter);
//			}
//			
//		} catch (Exception e) {
//			logger.error("Failed while creating Customer and Divisions", e);
//			throw new ServiceException("Unable to create Customer");
//		}
//		
//		return response;
		return null;
	}
	
	/**
	 * Creates a user from a UserServiceDTO.  If the user already exists, this method will do nothing.
	 */
	public RequestResponse createUser(RequestInformation requestInformation, UserServiceDTO userDTO) throws ServiceException {
		// TODO: make this transactional
		RequestResponse response = new RequestResponse();
		response.setStatus(ResponseStatus.OK);
		
		String userId = userDTO.getUserId();
		try {
			User userManager = ServiceLocator.getUser();

			Tenant tenant = getTenantCache().findTenant(requestInformation.getTenantId());
			PrimaryOrg primaryOrg = getTenantCache().findPrimaryOrg(tenant.getId());
			
			// if the userid is unique, we can assume it does not exist
			if (userManager.userIdIsUnique(requestInformation.getTenantId(), userId)) {
				// set the basic information
				UserBean user = ServiceLocator.getServiceDTOBeanConverter().convert(userDTO);
				user.setTenant(tenant);
				user.setOwner(primaryOrg);
				
				// make sure the id is cleared
				user.setUniqueID(null);
				
				// we don't want people actually loging into these accounts so we'll set the password to a random UUID
				// and set them inactive
				user.assignPassword(UUID.randomUUID().toString());
				user.setActive(false);
				user.setTimeZoneID("United States:New York - New York");
				user.setEmailAddress(ConfigContext.getCurrentContext().getString(ConfigEntry.FIELDID_ADMINISTRATOR_EMAIL));
				
				// since we don't have a first name/last name, we'll have to just use the userId
				user.setFirstName(user.getUserID());
				
				userManager.createUser(user);
			}

		} catch (Exception e) {
			logger.error("Failed while creating User", e);
			throw new ServiceException("Unable to create User");
		}
		
		return response;
	}
	
	protected TenantCache getTenantCache() {
		return TenantCache.getInstance();
	}

	private SecurityFilter createFilterFromRequest(RequestInformation requestInformation) {
		return new TenantOnlySecurityFilter(requestInformation.getTenantId());
	}
	
	private void testTransactionId( RequestInformation requestInformation ) throws ServiceException {
		if( !requestInformation.hasValidTransactionId() ) {
			logger.error( "transaction Id is invalid for create product " );
			throw new ServiceException("Invalid transaction Id");
		}
	}

	private Product convertNewProduct( Long tenantId, ProductServiceDTO productDTO ) throws Exception {
		Product product = new Product();
		ServiceDTOBeanConverter converter = ServiceLocator.getServiceDTOBeanConverter();
		
		product = converter.convert( productDTO, product, tenantId );
		
		return product;
	}
	
	private Product createProduct(ProductServiceDTO productDTO, Long tenantId) throws Exception {
		PopulatorLogger populatorLogger = PopulatorLogger.getInstance();
		LegacyProductSerial productManager = ServiceLocator.getProductSerialManager();
		
		Product product = convertNewProduct( tenantId, productDTO );
		
	
		product = productManager.create(product);
	

		logSuccessfulProductCreate( tenantId, populatorLogger, product );
		
		return product;
	}

	private void logSuccessfulProductCreate( Long tenantId, PopulatorLogger populatorLogger, Product product ) {
		populatorLogger.logMessage(tenantId, "Successfully created product with serial number "+product.getSerialNumber(), PopulatorLog.logType.mobile, PopulatorLog.logStatus.success);
	}

	
	
	
	
	public RequestResponse createInspections( RequestInformation requestInformation, List<InspectionServiceDTO> inspectionDTOs ) throws ServiceException, ProductException, InspectionException {
		RequestResponse response = new RequestResponse();
		response.setStatus(ResponseStatus.OK);
		
		testTransactionId( requestInformation );
				
		try {
			if( isTransactionCompleted( requestInformation ) ) { 
				return response; 
			}
			
			
			PopulatorLogger populatorLogger = PopulatorLogger.getInstance();
			ServiceDTOBeanConverter converter = ServiceLocator.getServiceDTOBeanConverter();
			InspectionManager inspectionManager = ServiceLocator.getInspectionManager();
			LegacyProductSerial productManager = ServiceLocator.getProductSerialManager();
			InspectionScheduleManager scheduleManager = ServiceLocator.getInspectionScheduleManager();
			
		
			Long tenantId = requestInformation.getTenantId();
			
			List<Inspection> inspections = new ArrayList<Inspection>();
			Map<Inspection, ProductStatusBean> productStatus = new HashMap<Inspection, ProductStatusBean>();
			Map<Inspection, Date> nextInspectionDates = new HashMap<Inspection, Date>();
			Map<Inspection, InspectionSchedule> inspectionSchedules = new HashMap<Inspection, InspectionSchedule>();
			Product product = null;
			for (InspectionServiceDTO inspectionServiceDTO : inspectionDTOs) {
				product = findOrTagProduct( tenantId, inspectionServiceDTO );				
				inspectionServiceDTO.setProductId( product.getId() );
				
				// lets look up or create all newly attached sub products and attach to product
				List<SubProduct> subProducts = lookupOrCreateSubProducts(tenantId, inspectionServiceDTO.getNewSubProducts(), product);
				if (subProducts.size() > 0) {
					/*
					 * Note: the list of SubProducts on Product is marked as @Transient however productManager.update 
					 * has special handling code to persist it anyway.  and yes it does suck ...  
					 */
					product.getSubProducts().addAll(subProducts);
					productManager.update(product);
				}
								
				// we also need to get the product for any sub-inspections
				if (inspectionServiceDTO.getSubInspections() != null) {
					Product subProduct = null;
					for (SubInspectionServiceDTO subInspection : inspectionServiceDTO.getSubInspections()) {
						subProduct = findOrTagProduct( tenantId, subInspection );
						subInspection.setProductId( subProduct.getId() );
					}
				}
				
				Inspection inspection = converter.convert(inspectionServiceDTO, tenantId);
				inspections.add( inspection );
				productStatus.put(inspection, converter.convertProductStatus(inspectionServiceDTO));
				nextInspectionDates.put(inspection, converter.convertNextDate(inspectionServiceDTO));
				inspectionSchedules.put(inspection, converter.convertInspectionSchedule(inspectionServiceDTO));				
			}	
		
			List<Inspection> savedInspections = null;
			
			try {
				savedInspections = inspectionManager.createInspections( requestInformation.getMobileGuid(), inspections, productStatus, nextInspectionDates);
				logger.info( "save inspections on product " + product.getSerialNumber() );
				populatorLogger.logMessage(tenantId, "Created inspection for product with serial number "+product.getSerialNumber(), PopulatorLog.logType.mobile, PopulatorLog.logStatus.success);
			} catch ( TransactionAlreadyProcessedException e) {
				// if the transaction is already complete just return success.
				logger.info( "transaction already processed for inspections on product  " + product.getSerialNumber() );
			} catch ( Exception e ) {
				logger.error( "failed to save inspections", e );
				throw new InspectionException("Failed to save inspections");
			}
			
			if (savedInspections != null) {
				for (Inspection savedInspection : savedInspections) {
					InspectionSchedule schedule = inspectionSchedules.get(savedInspection);
					try {
						if (schedule != null) {
							schedule.completed(savedInspection);
							scheduleManager.update(schedule);
						}
					} catch (Exception e) {
						logger.error("failed to attach schedule to inspection", e);
						populatorLogger.logMessage(tenantId, "Could not attach inspection schedule to inspection on product with serial number "+savedInspection.getProduct().getSerialNumber(), PopulatorLog.logType.mobile, PopulatorLog.logStatus.error);
						// We allow the inspection to still go through even if this happens
					}
				}
			}
			
			return response;
		
		} catch( InspectionException e ) {
			throw e;
		} catch( FindProductFailure e ) {
			throw new ProductException("Could not find product");
		} catch (Exception e) {
			logger.error( "failed while processing inspections", e );
			throw new ServiceException("Problem processing inspections");
		}
	}
	
	private List<SubProduct> lookupOrCreateSubProducts(Long tenantId, List<SubProductMapServiceDTO> subProductMaps, Product masterProduct) throws Exception {
		
		List<SubProduct> subProducts = new ArrayList<SubProduct>();
		
		if (subProductMaps == null) return subProducts;
		
		ProductManager productManager = ServiceLocator.getProductManager();
		PersistenceManager persistenceManager = ServiceLocator.getPersistenceManager();
		
		for (SubProductMapServiceDTO subProductMap : subProductMaps) {
			ProductServiceDTO subProductDTO = subProductMap.getNewProduct();
			
			Product product = productManager.findProductByGUID(subProductDTO.getMobileGuid(), new TenantOnlySecurityFilter( tenantId ) );
			
			// Try by id
			if (product == null && subProductDTO.getId() != null && subProductDTO.getId() > 0) {
				product = persistenceManager.find(Product.class, subProductDTO.getId());
			}				
			
			// Create product 
			if (product == null) {
				product = createProduct(subProductDTO, tenantId);
			}
			
			SubProduct subProduct = new SubProduct();
			subProduct.setLabel(subProductMap.getName());
			subProduct.setProduct(product);
			subProduct.setMasterProduct(masterProduct);
			
			subProducts.add(subProduct);
		}
		
		return subProducts;
	}
	
	/**
	 * The logic here is:  if the product serial guid is not set, lookup by products unique id, if not found throw error.  
	 * If it is set, try looking up by guid, if not found then tag (create) this product.  
	 */
	private Product findOrTagProduct( Long tenantId, AbstractInspectionServiceDTO inspectionServiceDTO ) throws FindProductFailure {
		
		Product product = null;
		if( inspectionServiceDTO.productIdExists() ) {
			try {
				product = ServiceLocator.getProductManager().findProduct( inspectionServiceDTO.getProductId(), new TenantOnlySecurityFilter( tenantId ) );
				product = ServiceLocator.getProductManager().fillInSubProductsOnProduct(product);
			} catch( Exception e ) {
				logger.error( "looking up product with product id " + inspectionServiceDTO.getProductId(), e );
			}
			
		} else if( inspectionServiceDTO.productMobileGuidExists() ) {
			// Try looking up by GUID
			try {
				product = ServiceLocator.getProductManager().findProductByGUID( inspectionServiceDTO.getProductMobileGuid(), new TenantOnlySecurityFilter( tenantId ) );
			} catch (Exception e) {
				logger.error("Looking up product serial by GUID = "+inspectionServiceDTO.getProductMobileGuid(), e);
			}
			
			// If still null, lets tag it
			if( product == null && inspectionServiceDTO.getProduct() != null ) {
				logger.info( "using tag product from inside create inspection" );
				try {
					product = createProduct(inspectionServiceDTO.getProduct(), tenantId);
				} catch (Exception e) {
					logger.error("Tagging product",e);
				}
				
			}
		}
		
		if( product == null ) {
			throw new FindProductFailure( "Could not find product." );
		}
		
		return product;
	}
	
	/**
	 * Deprecated in version 1.20; remove in version after
	 */
	@Deprecated 
	public InspectionListResponse getInspectionsForCustomerDivision(PaginatedRequestInformation requestInformation, List<Long> customerIds, List<Long> divisionIds) throws ServiceException {
		WSSearchCritiera searchCriteria = new WSSearchCritiera();
		searchCriteria.setCustomerIds(customerIds);
		searchCriteria.setDivisionIds(divisionIds);
		
		return getInspections(requestInformation, searchCriteria);
	}
	
	public InspectionListResponse getInspections(PaginatedRequestInformation requestInformation, WSSearchCritiera searchCriteria) throws ServiceException {
		try {
			logger.info("Finding Inspections: Tenant [" + requestInformation.getTenantId() + "] Page [" + requestInformation.getPageNumber() + "]");
			
			InspectionListResponse response = new InspectionListResponse();
			
			ServiceDTOBeanConverter converter = ServiceLocator.getServiceDTOBeanConverter();
			InspectionManager inspectionManager = ServiceLocator.getInspectionManager();
			
			int INSPECTIONS_PER_PAGE = ConfigContext.getCurrentContext().getInteger(ConfigEntry.MOBILE_PAGESIZE_INSPECTIONS);
			int CURRENT_PAGE = requestInformation.getPageNumber().intValue();
	
			Pager<InspectionGroup> inspectionGroups = null;			
			if (CURRENT_PAGE != PaginatedRequestInformation.INFORMATION_PAGE) {
				inspectionGroups = inspectionManager.findNewestInspections( searchCriteria, new TenantOnlySecurityFilter(requestInformation.getTenantId()), CURRENT_PAGE, INSPECTIONS_PER_PAGE );
				for( InspectionGroup inspectionGroup : inspectionGroups.getList() ) {
					response.getInspections().addAll(converter.convert(inspectionGroup));
				}
			} else {
				inspectionGroups = inspectionManager.findNewestInspections( searchCriteria, new TenantOnlySecurityFilter(requestInformation.getTenantId()), FIRST_PAGE, INSPECTIONS_PER_PAGE );
			}
			
			response.setCurrentPage(CURRENT_PAGE);
			response.setRecordsPerPage(INSPECTIONS_PER_PAGE);
			response.setStatus(ResponseStatus.OK);
			response.setTotalPages( (int)inspectionGroups.getTotalPages() );
			
			logger.info("Returning Inspections: Tenant [" + requestInformation.getTenantId() + 
					"] Page [" + response.getCurrentPage() + "/" + response.getTotalPages() + 
					"] Inspections [" + response.getInspections().size() + 
					"] PageSize [" + response.getRecordsPerPage() + 
					"] Status [" +response.getStatus().name() + "]");
			
			return response;
		} catch (Exception e) {
			logger.error( "failed while processing inspections", e );
			throw new ServiceException();
		}		
	}
	
	public ProductListResponse getProducts(PaginatedRequestInformation requestInformation, WSSearchCritiera searchCriteria) throws ServiceException {
		try {
			logger.info("Finding Products: Tenant [" + requestInformation.getTenantId() + "] Page [" + requestInformation.getPageNumber() + "]");
			
			ProductListResponse response = new ProductListResponse();
			
			ServiceDTOBeanConverter converter = ServiceLocator.getServiceDTOBeanConverter();
			PersistenceManager persistenceManager = ServiceLocator.getPersistenceManager();
			
			int PRODUCTS_PER_PAGE = ConfigContext.getCurrentContext().getInteger(ConfigEntry.MOBILE_PAGESIZE_PRODUCTS);
			int CURRENT_PAGE = requestInformation.getPageNumber().intValue();
			
			SecurityFilter securityFilter = new TenantOnlySecurityFilter(requestInformation.getTenantId());
			QueryBuilder<Product> queryBuilder = new QueryBuilder<Product>(Product.class, securityFilter);
			queryBuilder.setSimpleSelect();
			
			if (searchCriteria.getCustomerIds() != null && searchCriteria.getCustomerIds().size() > 0) {
				queryBuilder.addWhere(WhereParameter.Comparator.IN, "customerIds", "owner.customer_id", searchCriteria.getCustomerIds());
			}
			
			if (searchCriteria.getDivisionIds() != null && searchCriteria.getDivisionIds().size() > 0) {
				queryBuilder.addWhere(WhereParameter.Comparator.IN, "divisionIds", "owner.division_id", searchCriteria.getDivisionIds());
			}
			
			if (searchCriteria.getJobSiteIds() != null && searchCriteria.getJobSiteIds().size() > 0) {
				queryBuilder.addWhere(WhereParameter.Comparator.IN, "jobSiteIds", "jobSite.id", searchCriteria.getJobSiteIds());
			}

			Date createDate = converter.convertStringToDate(searchCriteria.getCreateDate()); 
			if (createDate != null) {
				queryBuilder.addWhere(WhereParameter.Comparator.GE, "modified", "modified", createDate);
			}
			
			// This is for postgres to ensure paging works
			queryBuilder.addOrder("id");
			
			
			Pager<Product> productPage = null;
			if (CURRENT_PAGE != PaginatedRequestInformation.INFORMATION_PAGE) {
				productPage = persistenceManager.findAllPaged(queryBuilder, CURRENT_PAGE, PRODUCTS_PER_PAGE);
				
				for(Product product : productPage.getList()) {
					response.getProducts().add( converter.convert(product) );
				}
			} else {
				productPage = persistenceManager.findAllPaged(queryBuilder, FIRST_PAGE, PRODUCTS_PER_PAGE);
			}
			
			
			response.setCurrentPage(CURRENT_PAGE);
			response.setRecordsPerPage(PRODUCTS_PER_PAGE);
			response.setStatus(ResponseStatus.OK);
			response.setTotalPages( (int)productPage.getTotalPages() );
			
			logger.info("Returning Products: Tenant [" + requestInformation.getTenantId() + 
					"] Page [" + response.getCurrentPage() + "/" + response.getTotalPages() + 
					"] Products [" + response.getProducts().size() + 
					"] PageSize [" + response.getRecordsPerPage() + 
					"] Status [" +response.getStatus().name() + "]");
			
			return response;
		} catch (Exception e) {
			logger.error( "failed while processing products", e );
			throw new ServiceException();			
		}
	}
	
	public ProductListResponse getProductsByJob(PaginatedRequestInformation requestInformation, WSJobSearchCriteria searchCriteria) throws ServiceException {
		try {
			logger.info("Finding Products by Jobs: Tenant [" + requestInformation.getTenantId() + "] Page [" + requestInformation.getPageNumber() + "]");
			
			ProductListResponse response = new ProductListResponse();
			
			ServiceDTOBeanConverter converter = ServiceLocator.getServiceDTOBeanConverter();
			PersistenceManager persistenceManager = ServiceLocator.getPersistenceManager();
			
			int PRODUCTS_PER_PAGE = ConfigContext.getCurrentContext().getInteger(ConfigEntry.MOBILE_PAGESIZE_PRODUCTS);
			int CURRENT_PAGE = requestInformation.getPageNumber().intValue();
			
			SecurityFilter securityFilter = new TenantOnlySecurityFilter(requestInformation.getTenantId());
			QueryBuilder<InspectionSchedule> queryBuilder = new QueryBuilder<InspectionSchedule>(InspectionSchedule.class, securityFilter);
					
			if (searchCriteria.getJobIds() != null && searchCriteria.getJobIds().size() > 0) {
				queryBuilder.addWhere(WhereParameter.Comparator.IN, "project_id", "project.id", searchCriteria.getJobIds());
			}
			
			Date createDate = converter.convertStringToDate(searchCriteria.getCreateDate()); 
			if (createDate != null) {
				queryBuilder.addWhere(WhereParameter.Comparator.GE, "project_modified", "project.modified", createDate);
			}
			
			// This is for postgres to ensure paging works
			queryBuilder.addOrder("id");
			
			
			
			Pager<InspectionSchedule> schedulePage = null;
			if (CURRENT_PAGE != PaginatedRequestInformation.INFORMATION_PAGE) {
				schedulePage = persistenceManager.findAllPaged(queryBuilder, CURRENT_PAGE, PRODUCTS_PER_PAGE);
				
				for(InspectionSchedule schedule : schedulePage.getList()) {
					
					response.getProducts().add( converter.convert(schedule.getProduct()) );
					
					if (schedule.getProduct().getSubProducts() != null) {
						for (SubProduct subProduct : schedule.getProduct().getSubProducts()) {
							response.getProducts().add( converter.convert(subProduct.getProduct()) );
						}							
					}
				}
			} else {
				schedulePage = persistenceManager.findAllPaged(queryBuilder, FIRST_PAGE, PRODUCTS_PER_PAGE);				
			}
			
			response.setCurrentPage(CURRENT_PAGE);
			response.setRecordsPerPage(PRODUCTS_PER_PAGE);
			response.setStatus(ResponseStatus.OK);
			response.setTotalPages( (int)schedulePage.getTotalPages() );
			
			logger.info("Returning Products By Job: Tenant [" + requestInformation.getTenantId() + 
					"] Page [" + response.getCurrentPage() + "/" + response.getTotalPages() + 
					"] Products [" + response.getProducts().size() + 
					"] PageSize [" + response.getRecordsPerPage() + 
					"] Status [" +response.getStatus().name() + "]");
			
			return response;
			
		} catch (Exception e) {
			logger.error( "failed while getting products by job", e );
			throw new ServiceException();						
		}
	}
	
	public InspectionListResponse getInspectionsByJob(PaginatedRequestInformation requestInformation, WSJobSearchCriteria searchCriteria) throws ServiceException {
		try {
			logger.info("Finding Inspections By Job: Tenant [" + requestInformation.getTenantId() + "] Page [" + requestInformation.getPageNumber() + "]");
			
			InspectionListResponse response = new InspectionListResponse();
			
			ServiceDTOBeanConverter converter = ServiceLocator.getServiceDTOBeanConverter();
			InspectionManager inspectionManager = ServiceLocator.getInspectionManager();
			
			int INSPECTIONS_PER_PAGE = ConfigContext.getCurrentContext().getInteger(ConfigEntry.MOBILE_PAGESIZE_INSPECTIONS);
			int CURRENT_PAGE = requestInformation.getPageNumber().intValue();
	
			Pager<InspectionGroup> inspectionGroups = null;			
			if (CURRENT_PAGE != PaginatedRequestInformation.INFORMATION_PAGE) {
				inspectionGroups = inspectionManager.findNewestInspections( searchCriteria, new TenantOnlySecurityFilter(requestInformation.getTenantId()), CURRENT_PAGE, INSPECTIONS_PER_PAGE );
				for( InspectionGroup inspectionGroup : inspectionGroups.getList() ) {
					response.getInspections().addAll(converter.convert(inspectionGroup));
				}
			} else {
				inspectionGroups = inspectionManager.findNewestInspections( searchCriteria, new TenantOnlySecurityFilter(requestInformation.getTenantId()), FIRST_PAGE, INSPECTIONS_PER_PAGE );
			}
			
			response.setCurrentPage(CURRENT_PAGE);
			response.setRecordsPerPage(INSPECTIONS_PER_PAGE);
			response.setStatus(ResponseStatus.OK);
			response.setTotalPages( (int)inspectionGroups.getTotalPages() );
			
			logger.info("Returning Inspections By Job: Tenant [" + requestInformation.getTenantId() + 
					"] Page [" + response.getCurrentPage() + "/" + response.getTotalPages() + 
					"] Inspections [" + response.getInspections().size() + 
					"] PageSize [" + response.getRecordsPerPage() + 
					"] Status [" +response.getStatus().name() + "]");
			
			return response;
		} catch (Exception e) {
			logger.error( "failed while pulling down inspections by job", e );
			throw new ServiceException();
		}			
	}
	
	private boolean isTransactionCompleted( RequestInformation requestInformation ) throws NamingException, InvalidTransactionGUIDException {
		return new TransactionSupervisor( ServiceLocator.getPersistenceManager() ).isTransactionCompleted( requestInformation.getMobileGuid(), requestInformation.getTenantId() );	
	}
	
	public SetupDataLastModDatesServiceDTO getSetupDataLastModDates(RequestInformation requestInformation) throws ServiceException {
		ServiceDTOBeanConverter converter = ServiceLocator.getServiceDTOBeanConverter();
		
		SetupDataLastModDates setupLastModDates = SetupDataLastModUpdateService.getInstance().getModDates(requestInformation.getTenantId());
		SetupDataLastModDatesServiceDTO setupLastModDatesDTO = converter.convert(setupLastModDates);
		
		return setupLastModDatesDTO;
	}
	
	public MobileUpdateInfo getMobileUpdateInfo(String currentVersion) throws ServiceException {
		MobileUpdateInfo mobileUpdateInfo = new MobileUpdateInfo();
		
		mobileUpdateInfo.setMajorVersion(ConfigContext.getCurrentContext().getInteger(ConfigEntry.CURRENT_MOBILE_MAJOR_VERSION));
		mobileUpdateInfo.setMinorVersion(ConfigContext.getCurrentContext().getInteger(ConfigEntry.CURRENT_MOBILE_MINOR_VERSION));
		mobileUpdateInfo.setBuildVersion(ConfigContext.getCurrentContext().getInteger(ConfigEntry.CURRENT_MOBILE_BUILD_VERSION));
		mobileUpdateInfo.setFileName(ConfigContext.getCurrentContext().getString(ConfigEntry.CURRENT_MOBILE_FILE_NAME));
		
		return mobileUpdateInfo;
	}

}
