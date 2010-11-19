package com.n4systems.fieldidadmin.actions;


import com.n4systems.services.Initializer;
import com.n4systems.services.SetupDataLastModUpdateServiceInitializer;
import org.apache.log4j.Logger;



public class CacheControlAction extends AbstractAdminAction {
	private static final long serialVersionUID = 1L;
	private static Logger logger = Logger.getLogger(CacheControlAction.class);
	
	public String doShow() {
		return SUCCESS;
	}

	public String doReloadSetupDataLastModDatesCache() {
		reload(new SetupDataLastModUpdateServiceInitializer(), "Setup Data Mod Date Cache");
		return doShow();
	}

	private void reload(Initializer init, String name) {
		try {
			init.uninitialize();
			init.initialize();
			addActionMessage("Congrats you reloaded the " + name + "!!");
		} catch(Exception e) {
			addActionError(name + " Reload Failed: " + e.getMessage());
			logger.error("Failed to reaload cache: " + init.getClass().getSimpleName(), e);
		}
	}
	
}
