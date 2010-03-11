package com.n4systems.util;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import rfid.ejb.session.LegacyProductSerial;
import rfid.ejb.session.LegacyProductSerialManager;
import rfid.ejb.session.LegacyProductType;
import rfid.ejb.session.LegacyProductTypeManager;
import rfid.ejb.session.Option;
import rfid.ejb.session.OptionManager;
import rfid.ejb.session.PopulatorLog;
import rfid.ejb.session.PopulatorLogManager;
import rfid.ejb.session.ProductCodeMapping;
import rfid.ejb.session.ProductCodeMappingManager;
import rfid.ejb.session.SerialNumberCounter;
import rfid.ejb.session.SerialNumberCounterManager;
import rfid.ejb.session.ServiceDTOBeanConverter;
import rfid.ejb.session.ServiceDTOBeanConverterImpl;
import rfid.ejb.session.UnitOfMeasureManager;
import rfid.ejb.session.UnitOfMeasureManagerImpl;
import rfid.ejb.session.User;
import rfid.ejb.session.UserManager;

import com.n4systems.ejb.AutoAttributeManager;
import com.n4systems.ejb.AutoAttributeManagerImpl;
import com.n4systems.ejb.ConfigManager;
import com.n4systems.ejb.ConfigManagerImpl;
import com.n4systems.ejb.InspectionManager;
import com.n4systems.ejb.InspectionManagerImpl;
import com.n4systems.ejb.InspectionScheduleManager;
import com.n4systems.ejb.InspectionScheduleManagerImpl;
import com.n4systems.ejb.MailManager;
import com.n4systems.ejb.OrderManager;
import com.n4systems.ejb.OrderManagerImpl;
import com.n4systems.ejb.PersistenceManager;
import com.n4systems.ejb.PersistenceManagerImpl;
import com.n4systems.ejb.ProductManager;
import com.n4systems.ejb.ProductManagerImpl;
import com.n4systems.ejb.ProofTestHandler;
import com.n4systems.ejb.ProofTestHandlerImpl;
import com.n4systems.exceptions.EJBLookupException;
import com.n4systems.mail.MailManagerFactory;
import com.n4systems.notifiers.EmailNotifier;
import com.n4systems.notifiers.Notifier;

public class ServiceLocator {
	private static final String APP_PREFIX = "fieldid/";
	private static final String LOCAL_SUFFIX = "/local";
	
	private static Context context;
	private static Context getContext() throws NamingException {
		if(context == null) {
			context = new InitialContext();
		}
		return context;
	}
	
	/**
	 * Executes a named lookup of an ejb.
	 * @param managerClass			The EJB implementation class.  NOT the local or remove interface.
	 * @return						The named object.
	 * @see Context#lookup(String)
	 * @throws EJBLookupException	If an Exception was thrown during the lookup.
	 */
	@SuppressWarnings("unchecked")
	public static <T> T lookup(Class<T> managerClass) {
		try {
			return (T)getContext().lookup(APP_PREFIX + managerClass.getSimpleName() + LOCAL_SUFFIX);
		} catch(Throwable t) {
			// wrap an exceptions in an unchecked exception since there's really not much we can do about these
			throw new EJBLookupException(managerClass, t);
		}
	}
	
	/**
	 * Finds a manager specified by managerClass and implementationClass via named lookup.  Checks cacheVar first
	 * to see if the lookup has already been cached.  If not, sets the named object into cacheVar.
	 * @param managerClass			The local interface of the manager class
	 * @param implementationClass	The implementation class of managerClass
	 * @param cacheVar				A ThreadLocal variable used to cache manager lookups
	 * @return						The cached or resolved manager.
	 * @see #lookup(Class)
	 * @throws EJBLookupException 	If an Exception was thrown during the lookup.  This exception is unchecked.
	 */
	public static <T, K extends T> T get(Class<T> managerClass, Class<K> implementationClass, ThreadLocal<T> cacheVar) {
		T manager = cacheVar.get();
		
		if(manager == null) {
			manager = lookup(implementationClass);
			cacheVar.set(manager);
		}
		
		return manager;
	}
	
	
	private static final ThreadLocal<ConfigManager> configManager = new ThreadLocal<ConfigManager>();
	
	public static final ConfigManager getConfigManager() {
		return get(ConfigManager.class, ConfigManagerImpl.class, configManager);
	}

	public static final MailManager getMailManager() {
		return MailManagerFactory.defaultMailManager(ConfigContext.getCurrentContext());
	}
	
	
	private static final ThreadLocal<PopulatorLog> populatorLog = new ThreadLocal<PopulatorLog>();
	
	public static final PopulatorLog getPopulatorLog() {
		return get(PopulatorLog.class, PopulatorLogManager.class, populatorLog);
	}
	
	
	private static final ThreadLocal<SerialNumberCounter> serialNumberCounter = new ThreadLocal<SerialNumberCounter>();
	
	public static final SerialNumberCounter getSerialNumberCounter() {
		return get(SerialNumberCounter.class, SerialNumberCounterManager.class, serialNumberCounter);
	}
	
	
	private static final ThreadLocal<User> user = new ThreadLocal<User>();
	
	public static final User getUser() {
		return get(User.class, UserManager.class, user);
	}
	
	
	private static final ThreadLocal<LegacyProductType> productType = new ThreadLocal<LegacyProductType>();
	
	public static final LegacyProductType getProductType() {
		return get(LegacyProductType.class, LegacyProductTypeManager.class, productType);
	}
	
	
	private static final ThreadLocal<Option> option = new ThreadLocal<Option>();
	
	public static final Option getOption() {
		return get(Option.class, OptionManager.class, option);
	}
	
	
	private static final ThreadLocal<PersistenceManager> persistenceManager = new ThreadLocal<PersistenceManager>();
	
	public static final PersistenceManager getPersistenceManager() {
		return get(PersistenceManager.class, PersistenceManagerImpl.class, persistenceManager);
	}
	
	public static final PersistenceManager createPersistenceManager() {
		return lookup(PersistenceManagerImpl.class);
	}
	
	private static final ThreadLocal<ProductCodeMapping> productCodeMapping = new ThreadLocal<ProductCodeMapping>();
	
	public static final ProductCodeMapping getProductCodeMapping() {
		return get(ProductCodeMapping.class, ProductCodeMappingManager.class, productCodeMapping);
	}
	
	
	private static final ThreadLocal<ProofTestHandler> proofTestHandler = new ThreadLocal<ProofTestHandler>();
	
	public static final ProofTestHandler getProofTestHandler() {
		return get(ProofTestHandler.class, ProofTestHandlerImpl.class, proofTestHandler);
	}

	
	private static final ThreadLocal<LegacyProductSerial> productSerialManager = new ThreadLocal<LegacyProductSerial>();
	
	public static final LegacyProductSerial getProductSerialManager() {
		return get(LegacyProductSerial.class, LegacyProductSerialManager.class, productSerialManager);
	}
	
	
	private static final ThreadLocal<ServiceDTOBeanConverter> serviceDTOBeanConverter = new ThreadLocal<ServiceDTOBeanConverter>();
	
	public static final ServiceDTOBeanConverter getServiceDTOBeanConverter() {
		return get(ServiceDTOBeanConverter.class, ServiceDTOBeanConverterImpl.class, serviceDTOBeanConverter);
	}
	
	
	private static final ThreadLocal<AutoAttributeManager> autoAttributeManager = new ThreadLocal<AutoAttributeManager>();
	
	public static final AutoAttributeManager getAutoAttributeManager() {
		return get(AutoAttributeManager.class, AutoAttributeManagerImpl.class, autoAttributeManager);
	}

	
	private static final ThreadLocal<UnitOfMeasureManager> unitOfMeasureManager = new  ThreadLocal<UnitOfMeasureManager>();
	
	public static final UnitOfMeasureManager getUnitOfMeasureManager() {
		return get(UnitOfMeasureManager.class, UnitOfMeasureManagerImpl.class, unitOfMeasureManager);
	}
	
	
	private static final ThreadLocal<InspectionScheduleManager> inspectionScheduleManager = new ThreadLocal<InspectionScheduleManager>();
	
	public static final InspectionScheduleManager getInspectionScheduleManager() {
		return get(InspectionScheduleManager.class, InspectionScheduleManagerImpl.class, inspectionScheduleManager);
	}
	
	
	private static final ThreadLocal<InspectionManager> inspectionManager = new ThreadLocal<InspectionManager>();
	
	public static final InspectionManager getInspectionManager() {
		return get(InspectionManager.class, InspectionManagerImpl.class, inspectionManager);
	}
	
	
	private static final ThreadLocal<ProductManager> productManager = new ThreadLocal<ProductManager>();
	
	public static final ProductManager getProductManager() {
		return get(ProductManager.class, ProductManagerImpl.class, productManager);
	}
	
	
	private static final ThreadLocal<OrderManager> orderManager = new ThreadLocal<OrderManager>();
	
	public static final OrderManager getOrderManager() {
		return get(OrderManager.class, OrderManagerImpl.class, orderManager);
	}

	public static Notifier getDefaultNotifier() {
		return new EmailNotifier(getMailManager());
	}
}
