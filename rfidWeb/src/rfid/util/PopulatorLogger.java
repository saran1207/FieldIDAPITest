package rfid.util;

import rfid.ejb.entity.PopulatorLogBean;

import com.n4systems.ejb.legacy.PopulatorLog;
import com.n4systems.model.Tenant;
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
			Tenant tenant = ServiceLocator.getPersistenceManager().find(Tenant.class, tenantId);
			populatorLog.createPopulatorLog(new PopulatorLogBean(tenant, logMessage, logStatus, logType));
			
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
