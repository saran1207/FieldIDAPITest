package com.n4systems.fieldidadmin.utils.importer;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import javax.naming.NamingException;

import org.ho.yaml.Yaml;

import com.n4systems.ejb.CustomerManager;
import com.n4systems.model.TenantOrganization;
import com.n4systems.util.ServiceLocator;

public class EndUserImporter extends Importer {

	public static final String FILE_NAME_PREFIX = "endUsers";
	public static final String FAILURE_REASON = "failureReason";
	private CustomerManager endUserManager;
	
	public EndUserImporter( File importerBaseDirectory, TenantOrganization tenant, boolean createMissingDivisions ) throws NamingException {
		super( importerBaseDirectory, tenant, createMissingDivisions );
		
		this.endUserManager = ServiceLocator.getCustomerManager();
	}

	public static Collection<File> filesProcessing( TenantOrganization tenant, File importerBaseDirectory ) {
		File processingDirectory = new File( processingDirectoryName( tenant, importerBaseDirectory ) );
		
		Collection<File> availableFiles = new ArrayList<File>();
		File[] files = processingDirectory.listFiles();
		if( files != null ) {
			for (int i = 0; i < files.length; i++) {
				if( files[i].getName().startsWith( FILE_NAME_PREFIX ) ){ 
					availableFiles.add( files[i] );
				}
			}
		}
		return availableFiles;
	}
	public static Collection<File> filesAvailableForProcessing(TenantOrganization tenant, File importerBaseDirectory ) {
		File uploadDirectory = new File( uploadDirectoryName( tenant, importerBaseDirectory ) );
		
		Collection<File> availableFiles = new ArrayList<File>();
		File[] files = uploadDirectory.listFiles();
		if( files != null ) {
			for (int i = 0; i < files.length; i++) {
				if( files[i].getName().startsWith( FILE_NAME_PREFIX ) ){ 
					availableFiles.add( files[i] );
				}
			}
		}
		return availableFiles;
	}
	
	@SuppressWarnings("unchecked")
	public int processFile(  ) {
		
		int processed = 0;
		try {
			Collection<Map> endUsers = (Collection<Map>)Yaml.load( currentFile );
			for (Map endUser : endUsers) {
				try{
					endUserManager.findOrCreateCustomer((String)endUser.get("endUserName"), (String)endUser.get("endUserId"), tenant.getId(), null);
					successes.add( endUser );
				} catch ( Exception e ) {
					StringWriter message = new StringWriter();
					e.printStackTrace(  new PrintWriter( message ) ) ;
					
					endUser.put( FAILURE_REASON, message.toString() );
					exceptions.add( endUser );
					e.printStackTrace();
				}
			}
		} catch ( Exception e ) {
			System.out.println("woo");
		}
		return processed;
	}
	
}
