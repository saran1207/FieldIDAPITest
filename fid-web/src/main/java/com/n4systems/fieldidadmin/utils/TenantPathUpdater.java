package com.n4systems.fieldidadmin.utils;

import java.io.File;
import java.io.IOException;

import org.apache.log4j.Logger;

import com.n4systems.model.Tenant;
import com.n4systems.reporting.PathHandler;

public class TenantPathUpdater {
	
	Logger logger = Logger.getLogger(TenantPathUpdater.class);
			
	public void renameTenantPaths(Tenant tenant, String newName) throws IOException {
		File [] files = {PathHandler.getEventAttachmentBaseFile(tenant),
		                 PathHandler.getEventProoftestBaseFile(tenant),
		                 PathHandler.getEventChartImageBaseFile(tenant),
		                 PathHandler.getEventSignatureBaseFile(tenant),
		                 PathHandler.getAssetAttachmentBaseFile(tenant),
		                 PathHandler.getConfDir(tenant),
		                 PathHandler.getTenantUserBaseFile(tenant)};

		for (File file: files) {
			if(file.exists()) {
				logger.info("Renaming tenant dir: " + file.getAbsolutePath());
				renameDirectory(file, newName);
			}			
		}
		
	}

	private void renameDirectory(File oldName, String newName) {
		oldName.renameTo(new File(oldName.getParent() + "/" + newName));
	}

}
