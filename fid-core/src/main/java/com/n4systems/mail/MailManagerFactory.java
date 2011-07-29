package com.n4systems.mail;

import com.n4systems.subscription.InvalidConfigurationException;
import com.n4systems.util.ConfigEntry;
import com.n4systems.util.ConfigurationProvider;

public class MailManagerFactory {
		
	public static MailManager defaultMailManager(ConfigurationProvider context) {
		String mailManagerName = context.getString(ConfigEntry.MAIL_MANAGER);
		try {
			return (MailManager) Class.forName(mailManagerName).newInstance();
		} catch (Exception e) {
			throw new InvalidConfigurationException("MailManager [" + mailManagerName + "] could not be found.", e);
		}
	}

}
