package com.n4systems.fieldidadmin.utils.importer;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import javax.naming.NamingException;

import org.ho.yaml.Yaml;

import com.n4systems.model.Tenant;
import com.n4systems.model.orgs.CustomerOrg;
import com.n4systems.model.orgs.FindOrCreateCustomerOrgHandler;
import com.n4systems.model.orgs.FindOrCreateExternalOrgHandler;
import com.n4systems.model.orgs.OrgSaver;
import com.n4systems.model.orgs.PrimaryOrg;
import com.n4systems.persistence.loaders.TenantFilteredListLoader;

public class EndUserImporter extends Importer {

	public static final String FILE_NAME_PREFIX = "endUsers";
	public static final String FAILURE_REASON = "failureReason";
	
	public EndUserImporter( File importerBaseDirectory, PrimaryOrg primaryOrg, boolean createMissingDivisions ) throws NamingException {
		super( importerBaseDirectory, primaryOrg, createMissingDivisions );
	}

	public static Collection<File> filesProcessing( Tenant tenant, File importerBaseDirectory ) {
		File processingDirectory = new File( processingDirectoryName( tenant, importerBaseDirectory ) );
		
		Collection<File> availableFiles = new ArrayList<File>();
		File[] files = processingDirectory.listFiles();
		if (files != null) {
			for (int i = 0; i < files.length; i++) {
				if (files[i].getName().startsWith(FILE_NAME_PREFIX)) {
					availableFiles.add(files[i]);
				}
			}
		}
		return availableFiles;
	}

	public static Collection<File> filesAvailableForProcessing(Tenant tenant, File importerBaseDirectory ) {
		File uploadDirectory = new File( uploadDirectoryName( tenant, importerBaseDirectory ) );
		
		Collection<File> availableFiles = new ArrayList<File>();
		File[] files = uploadDirectory.listFiles();
		if (files != null) {
			for (int i = 0; i < files.length; i++) {
				if (files[i].getName().startsWith(FILE_NAME_PREFIX)) {
					availableFiles.add(files[i]);
				}
			}
		}
		return availableFiles;
	}

	@SuppressWarnings("unchecked")
	public int processFile() {

		int processed = 0;
		try {
			Collection<Map> endUsers = (Collection<Map>) Yaml.load(currentFile);
			for (Map endUser : endUsers) {
				try {
					
					TenantFilteredListLoader<CustomerOrg> customerLoader = new TenantFilteredListLoader<CustomerOrg>(primaryOrg.getTenant(), CustomerOrg.class);
					FindOrCreateExternalOrgHandler<CustomerOrg, PrimaryOrg> customerSearcher = new FindOrCreateCustomerOrgHandler(customerLoader, new OrgSaver());

					customerSearcher.findOrCreate(primaryOrg, (String)endUser.get("endUserName"), (String)endUser.get("endUserId"));
					successes.add( endUser );
					
				} catch ( Exception e ) {
					StringWriter message = new StringWriter();
					e.printStackTrace(new PrintWriter(message));

					endUser.put(FAILURE_REASON, message.toString());
					exceptions.add(endUser);
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			System.out.println("woo");
		}
		return processed;
	}

}
