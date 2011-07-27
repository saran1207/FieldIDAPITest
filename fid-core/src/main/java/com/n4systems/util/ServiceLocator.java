package com.n4systems.util;

import java.util.Map;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.n4systems.ejb.AssetManager;
import com.n4systems.ejb.AutoAttributeManager;
import com.n4systems.ejb.ConfigManager;
import com.n4systems.ejb.EventManager;
import com.n4systems.ejb.EventScheduleManager;
import com.n4systems.ejb.OrderManager;
import com.n4systems.ejb.PersistenceManager;
import com.n4systems.ejb.ProofTestHandler;
import com.n4systems.ejb.legacy.LegacyAsset;
import com.n4systems.ejb.legacy.LegacyAssetType;
import com.n4systems.ejb.legacy.Option;
import com.n4systems.ejb.legacy.PopulatorLog;
import com.n4systems.ejb.legacy.SerialNumberCounter;
import com.n4systems.ejb.legacy.ServiceDTOBeanConverter;
import com.n4systems.ejb.legacy.UserManager;
import com.n4systems.ejb.legacy.wrapper.OptionEJBContainer;
import com.n4systems.ejb.legacy.wrapper.PopulatorLogEJBContainer;
import com.n4systems.ejb.legacy.wrapper.SerialNumberCounterEJBContainer;
import com.n4systems.ejb.wrapper.AutoAttributeManagerEJBContainer;
import com.n4systems.ejb.wrapper.ConfigManagerEJBContainer;
import com.n4systems.ejb.wrapper.EventManagerEJBContainer;
import com.n4systems.mail.MailManager;
import com.n4systems.mail.MailManagerFactory;
import com.n4systems.notifiers.EmailNotifier;
import com.n4systems.notifiers.Notifier;

public class ServiceLocator implements ApplicationContextAware {

	private static ApplicationContext applicationContext;

	
	// TODO : make all of these returned values spring objects. 
	//  Caveat : all spring beans should NOT be stateful..the previous implementation returned new instances but using getBean() 
	//   will just return an existing one. 
	//  currently, the app still uses legacy ejbContainers that handle their transactions themselves but in the future these methods should
	//  just return @Transaction marked implementations.  (see PersistenceManagerImpl class & PersistenceManager IF for example). 
	
	
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
	
	public static final UserManager getUser() {	
		return getBean(UserManager.class);		
	}

	public static final LegacyAssetType getAssetType() {
		return getBean(LegacyAssetType.class);
	}

	public static final Option getOption() {
		return new OptionEJBContainer();
	}

	public static final PersistenceManager getPersistenceManager() {		
		return getBean(PersistenceManager.class);
	}

	public static final ProofTestHandler getProofTestHandler() {
		return getBean(ProofTestHandler.class);
	}

	public static final LegacyAsset getLegacyAssetManager() {
		return getBean(LegacyAsset.class);
	}

	public static final ServiceDTOBeanConverter getServiceDTOBeanConverter() {
		return getBean(ServiceDTOBeanConverter.class);
	}

	public static final AutoAttributeManager getAutoAttributeManager() {
		return new AutoAttributeManagerEJBContainer();
	}

	public static final EventScheduleManager getEventScheduleManager() {
		return getBean(EventScheduleManager.class);
	}

	public static final EventManager getEventManager() {
		return new EventManagerEJBContainer();
	}

	public static final AssetManager getAssetManager() {
		return getBean(AssetManager.class);
	}

	public static final OrderManager getOrderManager() {
		return getBean(OrderManager.class);
	}

	public static Notifier getDefaultNotifier() {
		return new EmailNotifier(getMailManager());
	}

	private static <T> T getBean(Class<T> clazz) {
		Map<String, T> beans = applicationContext.getBeansOfType(clazz);		
		if (beans.size()==1)  { 
			return beans.values().iterator().next();
		} else { 
			throw new NoSuchBeanDefinitionException(clazz, "can't find bean instance of " + clazz.getSimpleName() + "  ("+beans.size()+")");			 
		}
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		ServiceLocator.applicationContext = applicationContext; 
	}
}
