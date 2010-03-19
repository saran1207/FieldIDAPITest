package com.n4systems.importing;

import java.io.File;

import javax.ejb.Local;

import com.n4systems.exceptions.FileImportException;

@Local
public interface ImportManager {
	
	public static final int OBS_INSPECTION_TYPE_NAME = 0;
	public static final int OBS_SECTION_NAME = 1;
	public static final int OBS_CRITERIA_NAME = 2;
	public static final int OBS_TYPE = 3;
	public static final int OBS_TEXT = 4;
	public static final int OBS_LINE_PARTS = OBS_TEXT + 1;
	
	public static final String OBS_TYPE_REC = "R";
	public static final String OBS_TYPE_DEF = "D";
	
	public long importObservations(Long tenantId, File observationsFile) throws FileImportException;
	
}
