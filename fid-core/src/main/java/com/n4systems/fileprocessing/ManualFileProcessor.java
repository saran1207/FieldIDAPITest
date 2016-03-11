package com.n4systems.fileprocessing;

import com.n4systems.exceptions.FileProcessingException;
import com.n4systems.tools.FileDataContainer;

import java.io.InputStream;

public class ManualFileProcessor extends FileProcessor {

	@Override
	protected void processFile( FileDataContainer fileDataContainer, InputStream file ) throws FileProcessingException {
		throw new FileProcessingException( "Can not process this type of file." );
	}

}
