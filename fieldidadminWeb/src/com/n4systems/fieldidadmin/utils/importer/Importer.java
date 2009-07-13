package com.n4systems.fieldidadmin.utils.importer;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Map;

import org.ho.yaml.Yaml;

import com.n4systems.model.TenantOrganization;

public abstract class Importer {

	public static final String TO_BE_PROCESSED_DIRECTORY = "new/";
	public static final String PROCESSING_DIRECTORY = "processing/";
	public static final String PROCESSED_DIRECTORY = "processed/";
	
	private static final int PROCESSING = 1;
	private static final int PROCESSED = 2;
	
	@SuppressWarnings("unchecked")
	protected Collection<Map> exceptions;
	@SuppressWarnings("unchecked")
	protected Collection<Map> successes;
	@SuppressWarnings("unchecked")
	protected Collection<Map> matches;
	
	protected File currentFile;
	protected File importerBaseDirectory;
	protected TenantOrganization tenant;
	protected Date startTime;
	protected SimpleDateFormat format; 
	protected boolean createMissingDivisions;
	
	public Importer( File importerBaseDirectory, TenantOrganization tenant, boolean createMissingDivisions ) {
		this.importerBaseDirectory = importerBaseDirectory;
		this.tenant = tenant;
		
		startTime = new Date();
		format = new SimpleDateFormat( "yyyy-MM-dd_HH:mm" );
		File processedFile = new File( constructFileName( PROCESSED, "")  );
		
		processedFile.mkdirs();
		this.createMissingDivisions = createMissingDivisions;
	}
	
	
	
	public static String uploadDirectoryName( TenantOrganization tenant, File importerBaseDirectory ) {
		return importerBaseDirectory.getAbsolutePath() + "/" + tenant.getName() + "/" + TO_BE_PROCESSED_DIRECTORY; 
	}
	
	public static String processingDirectoryName( TenantOrganization tenant, File importerBaseDirectory ) {
		return importerBaseDirectory.getAbsolutePath() + "/" + tenant.getName() + "/" + PROCESSING_DIRECTORY; 
	}
	
	protected String constructFileName( int type, String fileName ) {
		String filePath = importerBaseDirectory.getAbsolutePath() + "/" + tenant.getName();
		switch( type ) {
			case PROCESSING:
				filePath += "/" + PROCESSING_DIRECTORY;
				break;
			case PROCESSED:
				filePath += "/" + PROCESSED_DIRECTORY + "/" + format.format( startTime );
				break;
		}
		return filePath + "/" + fileName;
	}
	
	@SuppressWarnings("unchecked")
	public int processFile( File targetFile ) throws Exception {
		// move file for processing.
		exceptions = new ArrayList<Map>();
		successes = new ArrayList<Map>();
		matches = new ArrayList<Map>();
		File processingFile = new File( constructFileName( PROCESSING, targetFile.getName() ) );
		
		if( targetFile.renameTo( processingFile ) ) {
			currentFile = processingFile;
			
			// process the file
			processFile();
			
			File processedFile = new File( constructFileName( PROCESSED,  targetFile.getName() ) );
			
			if( !processingFile.renameTo( processedFile ) ) {
				//error moving file to processed but importing complete
			}

			currentFile = processedFile;
			outputResultFiles();
			// move files to processed
		} else {
			throw new Exception( "Cant move the file to processing" );
		}
		return 0;
	}
	
	
	protected abstract int processFile();
	
	protected void outputResultFiles() throws FileNotFoundException {
		
		Yaml.dump( successes, new File( currentFile.getAbsoluteFile() + ".successes.yml" ), false );
		Yaml.dump( exceptions, new File( currentFile.getAbsoluteFile() + ".exceptions.yml" ), false );
		Yaml.dump( matches, new File( currentFile.getAbsoluteFile() + ".matches.yml" ), false );
	}

}
