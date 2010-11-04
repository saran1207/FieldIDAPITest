package com.n4systems.util;

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
import com.n4systems.ejb.legacy.wrapper.LegacyAssetEJBContainer;
import com.n4systems.ejb.legacy.wrapper.LegacyAssetTypeEJBContainer;
import com.n4systems.ejb.legacy.wrapper.OptionEJBContainer;
import com.n4systems.ejb.legacy.wrapper.PopulatorLogEJBContainer;
import com.n4systems.ejb.legacy.wrapper.SerialNumberCounterEJBContainer;
import com.n4systems.ejb.legacy.wrapper.ServiceDTOBeanConverterEJBContainer;
import com.n4systems.ejb.legacy.wrapper.UserEJBContainer;
import com.n4systems.ejb.wrapper.AssetManagerEJBContainer;
import com.n4systems.ejb.wrapper.AutoAttributeManagerEJBContainer;
import com.n4systems.ejb.wrapper.ConfigManagerEJBContainer;
import com.n4systems.ejb.wrapper.EventManagerEJBContainer;
import com.n4systems.ejb.wrapper.EventScheduleManagerEJBContainer;
import com.n4systems.ejb.wrapper.OrderManagerEJBContainer;
import com.n4systems.ejb.wrapper.PersistenceManagerEJBContainer;
import com.n4systems.ejb.wrapper.ProofTestHandlerEJBContainer;
import com.n4systems.mail.MailManager;
import com.n4systems.mail.MailManagerFactory;
import com.n4systems.notifiers.EmailNotifier;
import com.n4systems.notifiers.Notifier;

public class ServiceLocator {

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
		return new UserEJBContainer();
	}

	public static final LegacyAssetType getProductType() {
		return new LegacyAssetTypeEJBContainer();
	}

	public static final Option getOption() {
		return new OptionEJBContainer();
	}

	public static final PersistenceManager getPersistenceManager() {
		return new PersistenceManagerEJBContainer();
	}

	public static final ProofTestHandler getProofTestHandler() {
		return new ProofTestHandlerEJBContainer();
	}

	public static final LegacyAsset getAssetManager() {
		return new LegacyAssetEJBContainer();
	}

	public static final ServiceDTOBeanConverter getServiceDTOBeanConverter() {
		return new ServiceDTOBeanConverterEJBContainer();
	}

	public static final AutoAttributeManager getAutoAttributeManager() {
		return new AutoAttributeManagerEJBContainer();
	}

	public static final EventScheduleManager getEventScheduleManager() {
		return new EventScheduleManagerEJBContainer();
	}

	public static final EventManager getEventManager() {
		return new EventManagerEJBContainer();
	}

	public static final AssetManager getProductManager() {
		return new AssetManagerEJBContainer();
	}

	public static final OrderManager getOrderManager() {
		return new OrderManagerEJBContainer();
	}

	public static Notifier getDefaultNotifier() {
		return new EmailNotifier(getMailManager());
	}
}
