package com.n4systems.util;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import com.n4systems.ejb.AutoAttributeManager;
import com.n4systems.ejb.ConfigManager;
import com.n4systems.ejb.InspectionManager;
import com.n4systems.ejb.InspectionScheduleManager;
import com.n4systems.ejb.OrderManager;
import com.n4systems.ejb.PersistenceManager;
import com.n4systems.ejb.ProductManager;
import com.n4systems.ejb.ProofTestHandler;
import com.n4systems.ejb.legacy.LegacyProductSerial;
import com.n4systems.ejb.legacy.LegacyProductType;
import com.n4systems.ejb.legacy.Option;
import com.n4systems.ejb.legacy.PopulatorLog;
import com.n4systems.ejb.legacy.ProductCodeMapping;
import com.n4systems.ejb.legacy.SerialNumberCounter;
import com.n4systems.ejb.legacy.ServiceDTOBeanConverter;
import com.n4systems.ejb.legacy.User;
import com.n4systems.ejb.legacy.wrapper.LegacyProductSerialEJBContainer;
import com.n4systems.ejb.legacy.wrapper.LegacyProductTypeEJBContainer;
import com.n4systems.ejb.legacy.wrapper.OptionEJBContainer;
import com.n4systems.ejb.legacy.wrapper.PopulatorLogEJBContainer;
import com.n4systems.ejb.legacy.wrapper.ProductCodeMappingEJBContainer;
import com.n4systems.ejb.legacy.wrapper.SerialNumberCounterEJBContainer;
import com.n4systems.ejb.legacy.wrapper.ServiceDTOBeanConverterEJBContainer;
import com.n4systems.ejb.legacy.wrapper.UserEJBContainer;
import com.n4systems.ejb.wrapper.AutoAttributeManagerEJBContainer;
import com.n4systems.ejb.wrapper.ConfigManagerEJBContainer;
import com.n4systems.ejb.wrapper.InspectionManagerEJBContainer;
import com.n4systems.ejb.wrapper.InspectionScheduleManagerEJBContainer;
import com.n4systems.ejb.wrapper.OrderManagerEJBContainer;
import com.n4systems.ejb.wrapper.PersistenceManagerEJBContainer;
import com.n4systems.ejb.wrapper.ProductManagerEJBContainer;
import com.n4systems.ejb.wrapper.ProofTestHandlerEJBContainer;
import com.n4systems.exceptions.EJBLookupException;
import com.n4systems.mail.MailManager;
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
	
	
	
	public static final ConfigManager getConfigManager() {
		return new ConfigManagerEJBContainer();
	}

	public static final MailManager getMailManager() {
		return MailManagerFactory.defaultMailManager(ConfigContext.getCurrentContext());
	}
	
	
	
	public static final PopulatorLog getPopulatorLog() {
		return new PopulatorLogEJBContainer();
	}
	
	
	
	public static final SerialNumberCounter getSerialNumberCounter() {
		return new SerialNumberCounterEJBContainer();
	}
	
	
	
	public static final User getUser() {
		return new UserEJBContainer();
	}
	
	
	
	public static final LegacyProductType getProductType() {
		return new LegacyProductTypeEJBContainer();
	}
	
	
	
	public static final Option getOption() {
		return new OptionEJBContainer();
	}
	
	
	public static final PersistenceManager getPersistenceManager() {
		return new PersistenceManagerEJBContainer();
	}
	
	
	
	
	public static final ProductCodeMapping getProductCodeMapping() {
		return new ProductCodeMappingEJBContainer();
	}
	
	
	
	public static final ProofTestHandler getProofTestHandler() {
		return new ProofTestHandlerEJBContainer();
	}

	
	
	public static final LegacyProductSerial getProductSerialManager() {
		return new LegacyProductSerialEJBContainer();
	}
	
	
	
	public static final ServiceDTOBeanConverter getServiceDTOBeanConverter() {
		return new ServiceDTOBeanConverterEJBContainer();
	}
	
	
	
	public static final AutoAttributeManager getAutoAttributeManager() {
		return new AutoAttributeManagerEJBContainer();
	}

	
	
	
	public static final InspectionScheduleManager getInspectionScheduleManager() {
		return new InspectionScheduleManagerEJBContainer();
	}
	
	
	
	public static final InspectionManager getInspectionManager() {
		return new InspectionManagerEJBContainer();
	}
	
	
	
	public static final ProductManager getProductManager() {
		return new ProductManagerEJBContainer();
	}
	
	
	
	public static final OrderManager getOrderManager() {
		return new OrderManagerEJBContainer();
	}

	public static Notifier getDefaultNotifier() {
		return new EmailNotifier(getMailManager());
	}
}
