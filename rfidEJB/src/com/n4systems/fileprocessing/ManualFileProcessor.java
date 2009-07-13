package com.n4systems.fileprocessing;

import java.io.InputStream;

import com.n4systems.exceptions.FileProcessingException;
import com.n4systems.tools.FileDataContainer;

public class ManualFileProcessor extends FileProcessor {

	@Override
	protected void processFile( FileDataContainer fileDataContainer, InputStream file ) throws FileProcessingException {
		throw new FileProcessingException( "Can not process this type of file." );
	}

}
