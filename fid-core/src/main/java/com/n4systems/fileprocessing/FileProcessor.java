package com.n4systems.fileprocessing;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;

import com.n4systems.exceptions.FileProcessingException;
import com.n4systems.tools.FileDataContainer;


abstract public class FileProcessor {
	
	public void processFile(FileDataContainer fileDataContainer) throws FileProcessingException {
		InputStream is = null;
		
		try {
			is = new ByteArrayInputStream(fileDataContainer.getFileData());
			processFile(fileDataContainer, is);
		} finally {
			IOUtils.closeQuietly(is);
		}
	}
	
	public FileDataContainer processFile(File proofTestFile) throws FileProcessingException {
		InputStream fileStream = null;
		FileDataContainer fileData;
		
		try {
			fileStream = new FileInputStream(proofTestFile);
			fileData = processFile(fileStream, proofTestFile.getName());
		} catch(IOException e) {
			throw new FileProcessingException("Failed to process Proof Test file [" + proofTestFile.getName() + "]", e);
		} finally {
			IOUtils.closeQuietly(fileStream);
		}
		
		return fileData;
	}
	
	public FileDataContainer processFile(byte[] proofTestData, String originalFileName) throws FileProcessingException {
		FileDataContainer fileData = new FileDataContainer();
		fileData.setFileName(originalFileName);
		fileData.setFileData(proofTestData);
		processFile(fileData);
		return fileData;
	}
	
	public FileDataContainer processFile(InputStream file, String originalFileName) throws FileProcessingException {
		FileDataContainer fileData = new FileDataContainer();
		fileData.setFileName(originalFileName);
		try {
			fileData.setFileData(IOUtils.toByteArray(file));
			processFile(fileData);
		} catch(IOException e) {
			throw new FileProcessingException("Failed to process Proof Test data [" + originalFileName + "]", e);
		}
			
		return fileData;
	}
	
	/**
	 * All file processors must implement this method which will parse the given file and return a chart from it
	 * in byte form.
	 * @param formFile
	 * @return
	 */
	abstract protected void processFile(FileDataContainer fileDataContainer, InputStream file) throws FileProcessingException;
	

}
