package rfid.util;

import rfid.ejb.entity.PopulatorLogBean;
import rfid.ejb.session.PopulatorLog;

import com.n4systems.model.TenantOrganization;
import com.n4systems.util.ServiceLocator;

public class PopulatorLogger {
	private static PopulatorLogger instance = null;

	public static PopulatorLogger getInstance() {
		if (instance == null) {
			instance = new PopulatorLogger();
		}
		return instance;
	}

	public void logMessage(Long tenantId, String logMessage, PopulatorLog.logType logType, PopulatorLog.logStatus logStatus) {
		
		try {
			
			PopulatorLog populatorLog = ServiceLocator.getPopulatorLog();
			TenantOrganization tenant = ServiceLocator.getPersistenceManager().find(TenantOrganization.class, tenantId);
			populatorLog.createPopulatorLog(new PopulatorLogBean(tenant, logMessage, logStatus, logType));
			
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
